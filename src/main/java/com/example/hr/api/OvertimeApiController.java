package com.example.hr.api;

import com.example.hr.dto.OvertimeRequestDTO;
import com.example.hr.models.OvertimeRequest;
import com.example.hr.models.User;
import com.example.hr.service.OvertimeService;
import com.example.hr.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/overtime")
public class OvertimeApiController {

    private final OvertimeService overtimeService;
    private final UserRepository userRepository;

    public OvertimeApiController(OvertimeService overtimeService, UserRepository userRepository) {
        this.overtimeService = overtimeService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<OvertimeRequest>> getAll() {
        return ResponseEntity.ok(overtimeService.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OvertimeRequest> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(overtimeService.getRequestById(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<OvertimeRequest>> getPending() {
        return ResponseEntity.ok(overtimeService.getPendingRequests());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OvertimeRequest>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(overtimeService.getRequestsByUser(userId));
    }

    @PostMapping
    public ResponseEntity<OvertimeRequest> submit(@Valid @RequestBody OvertimeRequestDTO dto) {
        return ResponseEntity.ok(overtimeService.submitRequest(dto));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<OvertimeRequest> approve(@PathVariable Integer id, Principal principal) {
        User approver = userRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(overtimeService.approveRequest(id, approver));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<OvertimeRequest> reject(@PathVariable Integer id,
                                                    @RequestBody Map<String, String> body,
                                                    Principal principal) {
        User approver = userRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(overtimeService.rejectRequest(id, approver, body.get("reason")));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OvertimeRequest> cancel(@PathVariable Integer id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(overtimeService.cancelRequest(id, user.getId()));
    }

    @GetMapping("/stats/department")
    public ResponseEntity<Map<String, BigDecimal>> statsByDepartment(@RequestParam int year) {
        return ResponseEntity.ok(overtimeService.getOvertimeByDepartment(year));
    }
}
