package com.tt.sharedutils;


import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Created by zhengguo on 6/16/16.
 */
public class MailUtil {
  public static void sendEmail(String from, String pass, String to, String subject, String content) throws EmailException {
      Email email = new SimpleEmail();
      email.setMsg(content);
      email.setHostName("smtp.qq.com");
      email.setSmtpPort(465);
      email.setAuthentication(from, pass);
      email.setSSLOnConnect(true);
      email.setFrom(from);
      email.addTo(to);
      email.setSubject(subject);
      email.send();
  }
}
