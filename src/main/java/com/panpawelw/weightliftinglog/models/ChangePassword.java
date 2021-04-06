package com.panpawelw.weightliftinglog.models;

/**
 * This class is used when changing user's password
 * <p>
 * oldPassword - user's current password
 * confirmOldPassword - confirmation of current password
 * newPassword - user's new password
 * confirmNewPassword - confirmation of new password
 */
public class ChangePassword {

  private String oldPassword;
  private String confirmOldPassword;
  private String newPassword;
  private String confirmNewPassword;

  public ChangePassword() {
  }

  public ChangePassword(String oldPassword, String confirmOldPassword, String newPassword,
      String confirmNewPassword) {
    this.oldPassword = oldPassword;
    this.confirmOldPassword = confirmOldPassword;
    this.newPassword = newPassword;
    this.confirmNewPassword = confirmNewPassword;
  }

  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public String getConfirmOldPassword() {
    return confirmOldPassword;
  }

  public void setConfirmOldPassword(String confirmOldPassword) {
    this.confirmOldPassword = confirmOldPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getConfirmNewPassword() {
    return confirmNewPassword;
  }

  public void setConfirmNewPassword(String confirmNewPassword) {
    this.confirmNewPassword = confirmNewPassword;
  }
}