package com.wizeline;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Methods {
  // hardcoded, has to be imported as environment variable
  private static final String SECRET_KEY = "my2w7wjd7yXF64FIADfJxNs1oupTGAuW";
  private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

  public static String hashTextWithSHA512(String input) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-512");
    digest.reset();
    digest.update(input.getBytes(StandardCharsets.UTF_8));

    return String.format("%0128x", new BigInteger(1, digest.digest()));
  }

  public static String generateToken(String userRole) {
    return JWT.create()
            .withClaim("role", userRole)
            .sign(algorithm);
  }

  public static String accessData(String authorization) {
    return "test";
  }
}

