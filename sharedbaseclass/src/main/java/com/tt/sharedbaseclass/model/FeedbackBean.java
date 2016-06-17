package com.tt.sharedbaseclass.model;

/**
 * Created by zhengguo on 6/17/16.
 */
public class FeedbackBean {
  private String mComment;
  private String mUserMail;

  public FeedbackBean(String comment, String userMail) {
    this.mComment = comment;
    this.mUserMail = userMail;
  }

  public String getComment() {
    return mComment;
  }

  public void setComment(String comment) {
    this.mComment = comment;
  }

  public String getUserMail() {
    return mUserMail;
  }

  public void setUserMail(String userMail) {
    this.mUserMail = userMail;
  }
}
