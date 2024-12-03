package com.example.vrcSecurityAssignment.repository;


import com.example.vrcSecurityAssignment.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {
    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);
}
