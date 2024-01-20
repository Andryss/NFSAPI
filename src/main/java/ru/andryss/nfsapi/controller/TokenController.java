package ru.andryss.nfsapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.andryss.nfsapi.service.TokenService;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Controller
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/api/token")
    private ResponseEntity<byte[]> generate() {
        String token = tokenService.generate();
        return ResponseEntity.ok(token.getBytes(US_ASCII));
    }
}
