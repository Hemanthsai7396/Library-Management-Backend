package com.klef.Library.Management.Controller;
import com.klef.Library.Management.DTO.*;
import com.klef.Library.Management.Modelclass.SignupModel;
import com.klef.Library.Management.Security.JwtUtil;
import com.klef.Library.Management.Service.Signupservice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class SignupController {
    private final Signupservice signupService;
    private final JwtUtil jwtUtil;

    // ── Hardcoded admin credentials ──────────────────────────────────────
    private static final String ADMIN_EMAIL    = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "7396414766";
    // ─────────────────────────────────────────────────────────────────────

    public SignupController(Signupservice signupService, JwtUtil jwtUtil) {
        this.signupService = signupService;
        this.jwtUtil = jwtUtil;
    }

    /** POST /api/auth/register */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            SignupModel user = signupService.register(req);
            UserDTO dto = toDTO(user);
            return ResponseEntity.status(201).body(ApiResponse.ok(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error during registration."));
        }
    }

    /** POST /api/auth/login */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {

            // ── ADMIN SHORTCUT (fixed credentials, bypasses DB lookup) ────
            if (ADMIN_EMAIL.equalsIgnoreCase(req.getEmail())
                    && ADMIN_PASSWORD.equals(req.getPassword())) {

                // Build a synthetic SignupModel so the rest of the flow is uniform
                SignupModel adminUser = new SignupModel();
                adminUser.setId("ADMIN-001");
                adminUser.setName("Admin");
                adminUser.setEmail(ADMIN_EMAIL);
                adminUser.setPhone("");
                adminUser.setMemberId("ADMIN");
                adminUser.setRole("ADMIN");
                adminUser.setIsActive(true);

                String token = jwtUtil.generateToken(adminUser.getId(), adminUser.getRole());
                LoginResponse payload = new LoginResponse(token, toDTO(adminUser));
                return ResponseEntity.ok(ApiResponse.ok(payload));
            }
            // ─────────────────────────────────────────────────────────────

            // ── All existing logic below is UNCHANGED ─────────────────────
            SignupModel user = signupService.findByEmail(req.getEmail());
            if (!user.getIsActive()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.fail("Invalid credentials or inactive account."));
            }
            if (!signupService.checkPassword(req.getPassword(), user.getPasswordHash())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.fail("Invalid credentials."));
            }
            String token = jwtUtil.generateToken(user.getId(), user.getRole());
            LoginResponse payload = new LoginResponse(token, toDTO(user));
            return ResponseEntity.ok(ApiResponse.ok(payload));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("Invalid credentials."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error during login."));
        }
    }

    /** GET /api/auth/me — requires valid JWT */
    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication auth) {
        try {
            String userId = (String) auth.getPrincipal();
            SignupModel user = signupService.findById(userId);
            return ResponseEntity.ok(ApiResponse.ok(toDTO(user)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error."));
        }
    }

    private UserDTO toDTO(SignupModel u) {
        return new UserDTO(u.getId(), u.getName(), u.getEmail(),
                           u.getPhone(), u.getMemberId(), u.getRole(), u.getIsActive());
    }
}