package com.tt.sharedutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zxu on 5/20/16.
 */
public class StringUtil {
  public static boolean isEmpty(String str) {
    return str == null || str.trim().equals("");
  }

  public static boolean emailPattern(String email) {
    Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    Matcher m = p.matcher(email);
    return m.matches();
  }
}
