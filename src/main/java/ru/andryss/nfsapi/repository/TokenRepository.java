package ru.andryss.nfsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andryss.nfsapi.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
}
