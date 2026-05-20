package com.klef.Library.Management.Controller;

import com.klef.Library.Management.DTO.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * General health / utility endpoints.
 */
@RestController
@RequestMapping("/api")
public class LibraryController {

    /** GET /api/health */
    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        return ApiResponse.ok(Map.of("status", "ok"));
    }
}
