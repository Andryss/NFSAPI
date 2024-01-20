package ru.andryss.nfsapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.andryss.nfsapi.model.Token;
import ru.andryss.nfsapi.repository.TokenRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isValid(String token) {
        return tokenRepository.existsById(token);
    }

    public String generate() {
        Token newToken = new Token();
        newToken.setToken(UUID.randomUUID().toString());
        tokenRepository.save(newToken);
        return newToken.getToken();
    }
}
