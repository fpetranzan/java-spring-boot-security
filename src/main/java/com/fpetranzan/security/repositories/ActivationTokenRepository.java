package com.fpetranzan.security.repositories;

import com.fpetranzan.security.models.token.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Integer> {

	Optional<ActivationToken> findByToken(String token);

}
