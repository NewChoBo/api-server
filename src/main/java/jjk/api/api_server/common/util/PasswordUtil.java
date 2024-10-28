package jjk.api.api_server.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

  private PasswordUtil() {
  }

  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public static String encodePassword(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }
}