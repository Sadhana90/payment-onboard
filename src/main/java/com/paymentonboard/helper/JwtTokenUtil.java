package com.paymentonboard.helper;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenUtil {

	public static final long JWT_TOKEN_VALIDITY = (long) 30 * 60 * 1000;

	@Value("${jwt.privateKeyPath}")
	String jwtPrivateKeyPath;

	@Value("${jwt.publicKeyPath}")
	String jwtPublicKeyPath;

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		PublicKey pubkey = null;
		try {
			String key = Files.readString(Path.of(jwtPublicKeyPath), Charset.defaultCharset());

			String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "").replaceAll(System.lineSeparator(), "")
					.replace("-----END PUBLIC KEY-----", "").replace("\n", "");

			byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
			pubkey = keyFactory.generatePublic(keySpec);
		} catch (Exception ex) {
			log.error("Error while generating token: {}", ex.getMessage());
			throw new BadRequestException(ErrorCode.AUTH102.getCode(), ErrorCode.AUTH102.getMsg());
		}
		return Jwts.parser().setSigningKey(pubkey).parseClaimsJws(token).getBody();

	}

	// check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// generate token for user
	public String generateToken(String subject) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, subject);
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		PrivateKey privatekey = null;
		try {
			String key = Files.readString(Path.of(jwtPrivateKeyPath), Charset.defaultCharset());
			String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "").replaceAll(System.lineSeparator(), "")
					.replace("-----END PRIVATE KEY-----", "").replace("\n", "");
			byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			privatekey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(encoded));
		} catch (Exception e) {
			log.error("Error while generating token: {}", e.getMessage());
			throw new BadRequestException(ErrorCode.AUTH102.getCode(), ErrorCode.AUTH102.getMsg());
		}
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (JWT_TOKEN_VALIDITY)))
				.signWith(SignatureAlgorithm.RS512, privatekey).compact();

	}

	// validate token
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public String getLoggedInUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((AuditUser) authentication.getPrincipal()).getUsername();
	}

}
