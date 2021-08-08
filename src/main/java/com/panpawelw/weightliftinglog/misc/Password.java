package com.panpawelw.weightliftinglog.misc;

public class Password {
  public static boolean passwordIsOK(String string) {
    int length = string.length();
    return length >= 4 && length <= 32;
  }
}
