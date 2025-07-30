package com.nexodigital.controller;

import com.nexodigital.dto.ContactFormDto;
import com.nexodigital.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = { "http://localhost:5500", "http://127.0.0.1:5500", "file://" })
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendContactEmail(@Valid @RequestBody ContactFormDto contactForm) {
        Map<String, Object> response = new HashMap<>();

        try {
            emailService.sendContactEmail(contactForm);

            response.put("success", true);
            response.put("message", "Email enviado correctamente");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al enviar el email: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "API funcionando correctamente");
        return ResponseEntity.ok(response);
    }
}