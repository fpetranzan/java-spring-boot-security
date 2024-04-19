package com.fpetranzan.security.repositories;

import com.fpetranzan.security.models.token.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Integer> {

	@Query("""
		SELECT t
		FROM ActivationToken t
		JOIN User u ON t.user.id = u.id
		WHERE u.id = :userId AND t.token = :token AND t.validatedAt IS NULL
		""")
	Optional<ActivationToken> findByUserAndToken(Integer userId, String token);

}
