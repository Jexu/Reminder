package com.tt.sharedutils;

/**
 * Created by zxu on 5/20/16.
 */
public class StringUtil {
  public static boolean isEmpty(String str) {
    return str == null || str.trim().equals("");
  }
}
