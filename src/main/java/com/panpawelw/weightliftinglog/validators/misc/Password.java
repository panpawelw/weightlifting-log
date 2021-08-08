package com.panpawelw.weightliftinglog.validators.misc;

public class Password {
  /**
   * Checks whether given password meets the length criteria.
   *
   * @param string  - password
   * @return boolean
   */
  public static boolean passwordIsOK(String string) {
    int length = string.length();
    return length >= 4 && length <= 32;
  }
}
