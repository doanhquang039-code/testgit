package com.example.hr.api;

import com.example.hr.dto.AssetAssignmentDTO;
import com.example.hr.dto.CompanyAssetDTO;
import com.example.hr.models.AssetAssignment;
import com.example.hr.models.CompanyAsset;
import com.example.hr.service.AssetManagementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
public class AssetApiController {

    private final AssetManagementService assetService;

    public AssetApiController(AssetManagementService assetService) {
        this.assetService = assetService;
    }

    // --- Assets ---

    @GetMapping
    public ResponseEntity<List<CompanyAsset>> getAll() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyAsset> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<CompanyAsset>> getAvailable() {
        return ResponseEntity.ok(assetService.getAvailableAssets());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CompanyAsset>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(assetService.searchAssets(keyword));
    }

    @PostMapping
    public ResponseEntity<CompanyAsset> create(@Valid @RequestBody CompanyAssetDTO dto) {
        return ResponseEntity.ok(assetService.createAsset(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyAsset> update(@PathVariable Integer id,
                                                 @RequestBody CompanyAssetDTO dto) {
        return ResponseEntity.ok(assetService.updateAsset(id, dto));
    }

    @PatchMapping("/{id}/retire")
    public ResponseEntity<CompanyAsset> retire(@PathVariable Integer id) {
        return ResponseEntity.ok(assetService.retireAsset(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    // --- Assignments ---

    @PostMapping("/assign")
    public ResponseEntity<AssetAssignment> assign(@Valid @RequestBody AssetAssignmentDTO dto) {
        return ResponseEntity.ok(assetService.assignAsset(dto));
    }

    @PatchMapping("/assignment/{id}/return")
    public ResponseEntity<AssetAssignment> returnAsset(@PathVariable Integer id,
                                                         @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(assetService.returnAsset(id, body.get("condition")));
    }

    @GetMapping("/assignments/active")
    public ResponseEntity<List<AssetAssignment>> getActiveAssignments() {
        return ResponseEntity.ok(assetService.getAllActiveAssignments());
    }

    @GetMapping("/assignments/overdue")
    public ResponseEntity<List<AssetAssignment>> getOverdue() {
        return ResponseEntity.ok(assetService.getOverdueAssignments());
    }

    @GetMapping("/assignments/user/{userId}")
    public ResponseEntity<List<AssetAssignment>> getUserAssignments(@PathVariable Integer userId) {
        return ResponseEntity.ok(assetService.getCurrentAssignmentsByUser(userId));
    }

    // --- Analytics ---

    @GetMapping("/total-value")
    public ResponseEntity<BigDecimal> getTotalValue() {
        return ResponseEntity.ok(assetService.getTotalAssetValue());
    }

    @GetMapping("/stats/category")
    public ResponseEntity<Map<String, Long>> statsByCategory() {
        return ResponseEntity.ok(assetService.countByCategory());
    }

    @GetMapping("/stats/value-by-category")
    public ResponseEntity<Map<String, BigDecimal>> valueByCategory() {
        return ResponseEntity.ok(assetService.getValueByCategory());
    }

    @GetMapping("/warranty-expiring")
    public ResponseEntity<List<CompanyAsset>> warrantyExpiring() {
        return ResponseEntity.ok(assetService.getWarrantyExpiringSoon());
    }
}
