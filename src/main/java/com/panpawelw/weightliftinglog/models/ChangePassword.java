package com.panpawelw.weightliftinglog.models;

/**
 * This class is used when changing user's password
 * <p>
 * oldPassword - user's current password
 * oldConfirmPassword - confirmation of current password
 * newPassword - user's new password
 * newConfirmPassword - confirmation of new password
 */
public class ChangePassword {

  private String oldPassword;
  private String oldConfirmPassword;
  private String newPassword;
  private String newConfirmPassword;

  public ChangePassword() {
  }

  public ChangePassword(String oldPassword, String oldConfirmPassword, String newPassword,
                        String newConfirmPassword) {
    this.oldPassword = oldPassword;
    this.oldConfirmPassword = oldConfirmPassword;
    this.newPassword = newPassword;
    this.newConfirmPassword = newConfirmPassword;
  }

  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public String getOldConfirmPassword() {
    return oldConfirmPassword;
  }

  public void setOldConfirmPassword(String oldConfirmPassword) {
    this.oldConfirmPassword = oldConfirmPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getNewConfirmPassword() {
    return newConfirmPassword;
  }

  public void setNewConfirmPassword(String newConfirmPassword) {
    this.newConfirmPassword = newConfirmPassword;
  }
}