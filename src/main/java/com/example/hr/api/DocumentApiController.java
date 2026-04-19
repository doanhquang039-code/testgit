package com.example.hr.api;

import com.example.hr.dto.EmployeeDocumentDTO;
import com.example.hr.models.EmployeeDocument;
import com.example.hr.models.User;
import com.example.hr.repository.UserRepository;
import com.example.hr.service.EmployeeDocumentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentApiController {

    private final EmployeeDocumentService documentService;
    private final UserRepository userRepository;

    public DocumentApiController(EmployeeDocumentService documentService,
                                   UserRepository userRepository) {
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EmployeeDocument>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(documentService.getDocumentsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDocument> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @GetMapping("/unverified")
    public ResponseEntity<List<EmployeeDocument>> getUnverified() {
        return ResponseEntity.ok(documentService.getUnverifiedDocuments());
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<EmployeeDocument>> getExpiring() {
        return ResponseEntity.ok(documentService.getExpiringSoonDocuments());
    }

    @GetMapping("/expired")
    public ResponseEntity<List<EmployeeDocument>> getExpired() {
        return ResponseEntity.ok(documentService.getExpiredDocuments());
    }

    @PostMapping
    public ResponseEntity<EmployeeDocument> create(@Valid @RequestBody EmployeeDocumentDTO dto) {
        return ResponseEntity.ok(documentService.createDocumentMetadata(dto));
    }

    @PostMapping("/upload")
    public ResponseEntity<EmployeeDocument> upload(@Valid @ModelAttribute EmployeeDocumentDTO dto,
                                                      @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(documentService.uploadDocument(dto, file));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDocument> update(@PathVariable Integer id,
                                                      @RequestBody EmployeeDocumentDTO dto) {
        return ResponseEntity.ok(documentService.updateDocument(id, dto));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<EmployeeDocument> verify(@PathVariable Integer id, Principal principal) {
        User verifier = userRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(documentService.verifyDocument(id, verifier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/type")
    public ResponseEntity<Map<?, ?>> statsByType() {
        return ResponseEntity.ok(documentService.countDocumentsByType());
    }
}
