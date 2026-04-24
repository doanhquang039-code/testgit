package com.example.hr.service;

import com.example.hr.enums.UserStatus;
import com.example.hr.models.User;
import com.example.hr.repository.DepartmentRepository;
import com.example.hr.repository.JobPositionRepository;
import com.example.hr.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final JobPositionRepository positionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    public UserService(
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            JobPositionRepository positionRepository,
            PasswordEncoder passwordEncoder,
            CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
    }

    @CacheEvict(value = "users", allEntries = true)
    public void registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "'active'")
    public List<User> getActiveUsers() {
        return userRepository.findByStatus(UserStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    }

    @Transactional(readOnly = true)
    public List<User> findAdminUsers(String keyword, Integer deptId, String role, String sortBy) {
        List<User> users = getActiveUsers().stream()
                .filter(user -> matchesKeyword(user, keyword))
                .filter(user -> matchesDepartment(user, deptId))
                .filter(user -> matchesRole(user, role))
                .toList();

        return users.stream()
                .sorted(resolveComparator(sortBy))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<User> searchUsers(String keyword, Sort sort) {
        if (keyword != null && !keyword.isBlank()) {
            return userRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, sort);
        }
        return userRepository.findAll(sort);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User saveAdminUser(User user,
                              MultipartFile file,
                              Integer departmentId,
                              Integer positionId,
                              String phoneNumber,
                              String gender,
                              String dateOfBirth,
                              String address,
                              String employeeCode,
                              String hireDate) throws IOException {
        User existing = user.getId() != null ? getUserById(user.getId()) : null;

        applyRelations(user, departmentId, positionId);
        applyEditableFields(user, phoneNumber, gender, dateOfBirth, address, employeeCode, hireDate, existing);
        handleProfileImage(user, file, existing);
        handlePassword(user, existing);
        touchAuditFields(user, existing == null);

        return userRepository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void softDeleteUser(Integer id) {
        User user = getUserById(id);
        user.setStatus(UserStatus.INACTIVE);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private void applyRelations(User user, Integer departmentId, Integer positionId) {
        if (departmentId != null) {
            departmentRepository.findById(departmentId).ifPresent(user::setDepartment);
        } else {
            user.setDepartment(null);
        }

        if (positionId != null) {
            positionRepository.findById(positionId).ifPresent(user::setPosition);
        } else {
            user.setPosition(null);
        }
    }

    private void applyEditableFields(User user,
                                     String phoneNumber,
                                     String gender,
                                     String dateOfBirth,
                                     String address,
                                     String employeeCode,
                                     String hireDate,
                                     User existing) {
        user.setPhoneNumber(phoneNumber);
        user.setGender(gender);
        user.setAddress(address);
        user.setEmployeeCode(employeeCode != null && !employeeCode.isBlank() ? employeeCode : null);
        user.setDateOfBirth(parseDate(dateOfBirth, existing != null ? existing.getDateOfBirth() : null));
        user.setHireDate(parseDate(hireDate, existing != null ? existing.getHireDate() : null));

        if (existing != null && user.getCreatedAt() == null) {
            user.setCreatedAt(existing.getCreatedAt());
        }
        if (existing != null && user.getStatus() == null) {
            user.setStatus(existing.getStatus());
        }
    }

    private void handleProfileImage(User user, MultipartFile file, User existing) throws IOException {
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                Map<?, ?> result = cloudinaryService.uploadAvatar(file, "hr_avatars");
                user.setProfileImage(result.get("secure_url").toString());
                return;
            }
        }

        if (existing != null) {
            user.setProfileImage(existing.getProfileImage());
        }
    }

    private void handlePassword(User user, User existing) {
        if (existing == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return;
        }

        String rawPwd = user.getPassword();
        if (rawPwd != null && !rawPwd.isBlank()) {
            user.setPassword(passwordEncoder.encode(rawPwd));
        } else {
            user.setPassword(existing.getPassword());
        }
    }

    private void touchAuditFields(User user, boolean creating) {
        LocalDateTime now = LocalDateTime.now();
        if (creating && user.getCreatedAt() == null) {
            user.setCreatedAt(now);
        }
        user.setUpdatedAt(now);
    }

    private boolean matchesKeyword(User user, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        String kw = keyword.toLowerCase();
        return user.getFullName().toLowerCase().contains(kw)
                || (user.getEmail() != null && user.getEmail().toLowerCase().contains(kw))
                || (user.getEmployeeCode() != null && user.getEmployeeCode().toLowerCase().contains(kw));
    }

    private boolean matchesDepartment(User user, Integer deptId) {
        return deptId == null || (user.getDepartment() != null && user.getDepartment().getId().equals(deptId));
    }

    private boolean matchesRole(User user, String role) {
        return role == null || role.isBlank() || (user.getRole() != null && user.getRole().name().equals(role));
    }

    private Comparator<User> resolveComparator(String sortBy) {
        if ("createdAt".equals(sortBy)) {
            return Comparator.comparing(User::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()));
        }
        if ("department".equals(sortBy)) {
            return Comparator.comparing(
                    user -> user.getDepartment() != null ? user.getDepartment().getDepartmentName() : "",
                    String.CASE_INSENSITIVE_ORDER
            );
        }
        return Comparator.comparing(User::getFullName, String.CASE_INSENSITIVE_ORDER);
    }

    private LocalDate parseDate(String rawDate, LocalDate fallback) {
        if (rawDate == null || rawDate.isBlank()) {
            return fallback;
        }
        try {
            return LocalDate.parse(rawDate);
        } catch (Exception ignored) {
            return fallback;
        }
    }
}
