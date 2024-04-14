package com.fpetranzan.security.repositories;

import com.fpetranzan.security.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

	@Query("""
		SELECT t
		FROM Token t
		JOIN User u ON t.user.id = u.id
		WHERE u.id = :userId AND t.expired = false AND t.revoked = false
		""")
	List<Token> findAllValidTokensByUser(Integer userId);

	Optional<Token> findByToken(String token);

}
