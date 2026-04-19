package com.example.hr.service;

import com.example.hr.dto.AssetAssignmentDTO;
import com.example.hr.dto.CompanyAssetDTO;
import com.example.hr.enums.AssetStatus;
import com.example.hr.exception.BusinessValidationException;
import com.example.hr.exception.DuplicateResourceException;
import com.example.hr.exception.ResourceNotFoundException;
import com.example.hr.models.AssetAssignment;
import com.example.hr.models.CompanyAsset;
import com.example.hr.models.User;
import com.example.hr.repository.AssetAssignmentRepository;
import com.example.hr.repository.CompanyAssetRepository;
import com.example.hr.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service quản lý tài sản công ty.
 * Bao gồm: CRUD assets, assign/return, depreciation, overdue tracking.
 */
@Service
@Transactional
public class AssetManagementService {

    private static final Logger log = LoggerFactory.getLogger(AssetManagementService.class);
    private static final int DEFAULT_USEFUL_LIFE_YEARS = 3;

    private final CompanyAssetRepository assetRepository;
    private final AssetAssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public AssetManagementService(CompanyAssetRepository assetRepository,
                                    AssetAssignmentRepository assignmentRepository,
                                    UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }

    // ===================== COMPANY ASSETS =====================

    /**
     * Lấy tất cả tài sản.
     */
    @Transactional(readOnly = true)
    public List<CompanyAsset> getAllAssets() {
        return assetRepository.findAll();
    }

    /**
     * Lấy tài sản theo ID.
     */
    @Transactional(readOnly = true)
    public CompanyAsset getAssetById(Integer id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tài sản", id));
    }

    /**
     * Lấy tài sản sẵn sàng.
     */
    @Transactional(readOnly = true)
    public List<CompanyAsset> getAvailableAssets() {
        return assetRepository.findAvailableAssets();
    }

    /**
     * Tìm kiếm tài sản.
     */
    @Transactional(readOnly = true)
    public List<CompanyAsset> searchAssets(String keyword) {
        return assetRepository.searchByKeyword(keyword);
    }

    /**
     * Tạo tài sản mới.
     */
    public CompanyAsset createAsset(CompanyAssetDTO dto) {
        // Check unique asset code
        if (dto.getAssetCode() != null && assetRepository.existsByAssetCode(dto.getAssetCode())) {
            throw new DuplicateResourceException("Tài sản", "mã", dto.getAssetCode());
        }

        CompanyAsset asset = new CompanyAsset();
        asset.setAssetName(dto.getAssetName());
        asset.setAssetCode(dto.getAssetCode());
        asset.setCategory(dto.getCategory());
        asset.setSerialNumber(dto.getSerialNumber());
        asset.setDescription(dto.getDescription());
        asset.setPurchaseDate(dto.getPurchaseDate());
        asset.setPurchasePrice(dto.getPurchasePrice() != null ? dto.getPurchasePrice() : BigDecimal.ZERO);
        asset.setCurrentValue(dto.getPurchasePrice() != null ? dto.getPurchasePrice() : BigDecimal.ZERO);
        asset.setLocation(dto.getLocation());
        asset.setWarrantyExpiry(dto.getWarrantyExpiry());
        asset.setStatus(AssetStatus.AVAILABLE);
        asset.setCreatedAt(LocalDateTime.now());

        log.info("Asset created: code={}, name={}", dto.getAssetCode(), dto.getAssetName());
        return assetRepository.save(asset);
    }

    /**
     * Cập nhật tài sản.
     */
    public CompanyAsset updateAsset(Integer id, CompanyAssetDTO dto) {
        CompanyAsset asset = getAssetById(id);

        if (dto.getAssetName() != null) asset.setAssetName(dto.getAssetName());
        if (dto.getCategory() != null) asset.setCategory(dto.getCategory());
        if (dto.getSerialNumber() != null) asset.setSerialNumber(dto.getSerialNumber());
        if (dto.getDescription() != null) asset.setDescription(dto.getDescription());
        if (dto.getLocation() != null) asset.setLocation(dto.getLocation());
        if (dto.getWarrantyExpiry() != null) asset.setWarrantyExpiry(dto.getWarrantyExpiry());

        return assetRepository.save(asset);
    }

    /**
     * Thanh lý tài sản.
     */
    public CompanyAsset retireAsset(Integer id) {
        CompanyAsset asset = getAssetById(id);

        // Check if currently assigned
        if (assignmentRepository.existsByAssetIdAndActualReturnIsNull(id)) {
            throw new BusinessValidationException("Không thể thanh lý tài sản đang được sử dụng");
        }

        asset.setStatus(AssetStatus.RETIRED);
        asset.setCurrentValue(BigDecimal.ZERO);
        log.info("Asset retired: code={}, name={}", asset.getAssetCode(), asset.getAssetName());
        return assetRepository.save(asset);
    }

    /**
     * Cập nhật khấu hao cho tất cả tài sản.
     */
    public int updateAllDepreciations() {
        List<CompanyAsset> assets = assetRepository.findAll().stream()
                .filter(a -> a.getStatus() != AssetStatus.RETIRED)
                .toList();

        int count = 0;
        for (CompanyAsset asset : assets) {
            BigDecimal oldValue = asset.getCurrentValue();
            asset.updateCurrentValue(DEFAULT_USEFUL_LIFE_YEARS);
            if (oldValue.compareTo(asset.getCurrentValue()) != 0) {
                assetRepository.save(asset);
                count++;
            }
        }
        log.info("Updated depreciation for {} assets", count);
        return count;
    }

    /**
     * Xóa tài sản.
     */
    public void deleteAsset(Integer id) {
        CompanyAsset asset = getAssetById(id);
        if (asset.getStatus() == AssetStatus.ASSIGNED) {
            throw new BusinessValidationException("Không thể xóa tài sản đang được sử dụng");
        }
        assetRepository.delete(asset);
    }

    // ===================== ASSET ASSIGNMENTS =====================

    /**
     * Giao tài sản cho nhân viên.
     */
    public AssetAssignment assignAsset(AssetAssignmentDTO dto) {
        CompanyAsset asset = getAssetById(dto.getAssetId());
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên", dto.getUserId()));

        // Check asset available
        if (!asset.isAvailableForAssignment()) {
            throw new BusinessValidationException("Tài sản không sẵn sàng. Trạng thái: " + asset.getStatus().getDisplayName());
        }

        User assignedBy = null;
        if (dto.getAssignedById() != null) {
            assignedBy = userRepository.findById(dto.getAssignedById()).orElse(null);
        }

        AssetAssignment assignment = new AssetAssignment();
        assignment.setAsset(asset);
        assignment.setUser(user);
        assignment.setAssignedDate(dto.getAssignedDate() != null ? dto.getAssignedDate() : LocalDate.now());
        assignment.setExpectedReturn(dto.getExpectedReturn());
        assignment.setAssignedBy(assignedBy);
        assignment.setConditionOnAssign(dto.getConditionOnAssign() != null ? dto.getConditionOnAssign() : "GOOD");
        assignment.setNotes(dto.getNotes());
        assignment.setCreatedAt(LocalDateTime.now());

        // Update asset status
        asset.markAssigned();
        assetRepository.save(asset);

        log.info("Asset assigned: asset={} -> user={}", asset.getAssetCode(), user.getUsername());
        return assignmentRepository.save(assignment);
    }

    /**
     * Trả tài sản.
     */
    public AssetAssignment returnAsset(Integer assignmentId, String condition) {
        AssetAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment", assignmentId));

        if (assignment.isReturned()) {
            throw new BusinessValidationException("Tài sản đã được trả");
        }

        assignment.returnAsset(condition != null ? condition : "GOOD");

        // Asset status already updated by returnAsset()
        assetRepository.save(assignment.getAsset());

        log.info("Asset returned: asset={} from user={}",
                assignment.getAsset().getAssetCode(), assignment.getUser().getUsername());
        return assignmentRepository.save(assignment);
    }

    /**
     * Lấy assignments hiện tại của nhân viên.
     */
    @Transactional(readOnly = true)
    public List<AssetAssignment> getCurrentAssignmentsByUser(Integer userId) {
        return assignmentRepository.findCurrentAssignmentsByUser(userId);
    }

    /**
     * Lấy tất cả assignments đang active.
     */
    @Transactional(readOnly = true)
    public List<AssetAssignment> getAllActiveAssignments() {
        return assignmentRepository.findAllActiveAssignments();
    }

    /**
     * Lấy assignments quá hạn.
     */
    @Transactional(readOnly = true)
    public List<AssetAssignment> getOverdueAssignments() {
        return assignmentRepository.findOverdueAssignments(LocalDate.now());
    }

    // ===================== ANALYTICS =====================

    /**
     * Tổng giá trị tài sản.
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalAssetValue() {
        return assetRepository.sumTotalAssetValue();
    }

    /**
     * Thống kê tài sản theo danh mục.
     */
    @Transactional(readOnly = true)
    public Map<String, Long> countByCategory() {
        return assetRepository.countByCategory().stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
    }

    /**
     * Thống kê tài sản theo trạng thái.
     */
    @Transactional(readOnly = true)
    public Map<AssetStatus, Long> countByStatus() {
        return assetRepository.countByStatus().stream()
                .collect(Collectors.toMap(
                        row -> (AssetStatus) row[0],
                        row -> (Long) row[1]
                ));
    }

    /**
     * Giá trị tài sản theo danh mục.
     */
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getValueByCategory() {
        return assetRepository.sumValueByCategory().stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (BigDecimal) row[1]
                ));
    }

    /**
     * Tài sản sắp hết bảo hành.
     */
    @Transactional(readOnly = true)
    public List<CompanyAsset> getWarrantyExpiringSoon() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(30);
        return assetRepository.findWarrantyExpiringSoon(start, end);
    }
}
