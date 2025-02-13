package com.fpetranzan.security.services;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TwoFactorAuthenticationService {

	public String genereteNewSecret() {
		return new DefaultSecretGenerator().generate();
	}

	public String genereteQrCodeImageUrl(String secret) {
		QrData data = new QrData.Builder()
			.label("spring-boot-jwt-security")
			.secret(secret)
			.issuer("JWT - Two Factor")
			.algorithm(HashingAlgorithm.SHA256)
			.digits(6)
			.period(30)
			.build();

		QrGenerator generator = new ZxingPngQrGenerator();
		byte[] imageData = new byte[0];

		try {
			imageData = generator.generate(data);
		} catch (QrGenerationException e) {
			log.error("Error while generating QrCode");
		}

		return Utils.getDataUriForImage(imageData, generator.getImageMimeType());
	}

	public boolean isOtpValid(String secret, String code) {
		TimeProvider timeProvider = new SystemTimeProvider();
		CodeGenerator codeGenerator = new DefaultCodeGenerator();
		CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

		return verifier.isValidCode(secret, code);
	}

	public boolean isOtpNotValid(String secret, String code) {
		return !isOtpValid(secret, code);
	}
}
