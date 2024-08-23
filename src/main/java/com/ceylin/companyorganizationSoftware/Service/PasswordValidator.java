package com.ceylin.companyorganizationSoftware.Service;

import java.util.regex.Pattern;

public class PasswordValidator {
  private static final String PASSWORD_PATTERN =
          "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$.!\\-+])[A-Za-z\\d@$.!\\-+]{8,32}$";

  private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

  public static boolean isValid(String password) {
    return pattern.matcher(password).matches();
  }
}
