package com.klef.Library.Management.Service;

import com.klef.Library.Management.DTO.*;
import com.klef.Library.Management.Modelclass.SignupModel;
import com.klef.Library.Management.Repository.signupRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Signupservice {

    private final signupRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public Signupservice(signupRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public SignupModel register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        SignupModel user = new SignupModel();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setMemberId(req.getMemberId());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole() != null ? req.getRole() : "user");
        user.setIsActive(true);

        return userRepo.save(user);
    }

    public SignupModel findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    public SignupModel findById(String id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    public boolean checkPassword(String raw, String hash) {
        return passwordEncoder.matches(raw, hash);
    }
}
