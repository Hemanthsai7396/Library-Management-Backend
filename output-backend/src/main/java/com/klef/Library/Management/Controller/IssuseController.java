package com.klef.Library.Management.Controller;

import com.klef.Library.Management.DTO.ApiResponse;
import com.klef.Library.Management.DTO.BorrowRequestDTO;
import com.klef.Library.Management.Modelclass.IssueModel;
import com.klef.Library.Management.Modelclass.SignupModel;
import com.klef.Library.Management.Service.IssueService;
import com.klef.Library.Management.Service.Signupservice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Handles: POST   /api/requests              (user — submit borrow request)
 *          GET    /api/requests/my           (user — view own requests)
 *          GET    /api/requests              (admin — view all)
 *          PATCH  /api/requests/{id}/approve (admin)
 *          PATCH  /api/requests/{id}/reject  (admin)
 *          PATCH  /api/requests/{id}/collected (admin)
 *          PATCH  /api/requests/{id}/returned  (admin)
 */
@RestController
@RequestMapping("/api/requests")
public class IssuseController {

    private final IssueService issueService;
    private final Signupservice signupService;

    public IssuseController(IssueService issueService, Signupservice signupService) {
        this.issueService = issueService;
        this.signupService = signupService;
    }

    /** POST /api/requests */
    @PostMapping
    public ResponseEntity<?> submit(@RequestBody Map<String, String> body,
                                    Authentication auth) {
        try {
            String userId   = (String) auth.getPrincipal();
            String bookId   = body.get("bookId");
            String userNote = body.get("userNote");

            SignupModel user = signupService.findById(userId);
            IssueModel  req  = issueService.submitRequest(user, bookId, userNote);
            return ResponseEntity.status(201).body(ApiResponse.ok(BorrowRequestDTO.from(req)));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error submitting request."));
        }
    }

    /** GET /api/requests/my */
    @GetMapping("/my")
    public ResponseEntity<?> getMyRequests(Authentication auth) {
        try {
            String userId = (String) auth.getPrincipal();
            SignupModel user = signupService.findById(userId);
            List<BorrowRequestDTO> dtos = issueService.getMyRequests(user)
                    .stream().map(BorrowRequestDTO::from).toList();
            return ResponseEntity.ok(ApiResponse.ok(dtos));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error fetching requests."));
        }
    }

    /** GET /api/requests?status= (admin) */
    @GetMapping
    public ResponseEntity<?> getAllRequests(
            @RequestParam(required = false) String status) {
        try {
            List<BorrowRequestDTO> dtos = issueService.getAllRequests(status)
                    .stream().map(BorrowRequestDTO::from).toList();
            return ResponseEntity.ok(ApiResponse.ok(dtos));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error fetching requests."));
        }
    }

    /** PATCH /api/requests/{id}/approve (admin) */
    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable String id,
                                     @RequestBody(required = false) Map<String, String> body) {
        try {
            String note = (body != null) ? body.getOrDefault("adminNote", "Approved") : "Approved";
            IssueModel req = issueService.approve(id, note);
            return ResponseEntity.ok(ApiResponse.ok(BorrowRequestDTO.from(req)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error approving request."));
        }
    }

    /** PATCH /api/requests/{id}/reject (admin) */
    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable String id,
                                    @RequestBody Map<String, String> body) {
        try {
            String note = body.getOrDefault("adminNote", "");
            IssueModel req = issueService.reject(id, note);
            return ResponseEntity.ok(ApiResponse.ok(BorrowRequestDTO.from(req)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error rejecting request."));
        }
    }

    /** PATCH /api/requests/{id}/collected (admin) */
    @PatchMapping("/{id}/collected")
    public ResponseEntity<?> markCollected(@PathVariable String id) {
        try {
            IssueModel req = issueService.markCollected(id);
            return ResponseEntity.ok(ApiResponse.ok(BorrowRequestDTO.from(req)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error marking as collected."));
        }
    }

    /** PATCH /api/requests/{id}/returned (admin) */
    @PatchMapping("/{id}/returned")
    public ResponseEntity<?> markReturned(@PathVariable String id) {
        try {
            IssueModel req = issueService.markReturned(id);
            return ResponseEntity.ok(ApiResponse.ok(BorrowRequestDTO.from(req)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error marking as returned."));
        }
    }
}
