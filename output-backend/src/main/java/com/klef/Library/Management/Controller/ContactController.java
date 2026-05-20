package com.klef.Library.Management.Controller;

import com.klef.Library.Management.DTO.ApiResponse;
import com.klef.Library.Management.Modelclass.ContactModel;
import com.klef.Library.Management.Service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Handles: POST /api/contact   — submit a support ticket
 *          GET  /api/contact   — admin views all tickets
 */
@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /** POST /api/contact */
    @PostMapping
    public ResponseEntity<?> submit(@RequestBody ContactModel ticket) {
        try {
            ContactModel saved = contactService.submit(ticket);
            return ResponseEntity.status(201).body(ApiResponse.ok(
                Map.of("id", saved.getId(), "message", "Ticket submitted successfully.")));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error submitting ticket."));
        }
    }

    /** GET /api/contact (admin) */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(ApiResponse.ok(contactService.findAll()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error fetching tickets."));
        }
    }
}
