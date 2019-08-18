package pl.pjm77.weightliftinglog.models;

/**
 * This class is used when changing user's password
 *
 * OldPassword - user's current password
 * ConfirmOldPassword - confirmation of current password
 * NewPassword - user's new password
 * ConfirmNewPassword - confirmation of new password
 */
public class ChangePassword {

    private String OldPassword;
    private String ConfirmOldPassword;
    private String NewPassword;
    private String ConfirmNewPassword;

    public ChangePassword() {}

    public ChangePassword(String oldPassword, String confirmOldPassword, String newPassword,
                          String confirmNewPassword) {
        OldPassword = oldPassword;
        ConfirmOldPassword = confirmOldPassword;
        NewPassword = newPassword;
        ConfirmNewPassword = confirmNewPassword;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }

    public String getConfirmOldPassword() {
        return ConfirmOldPassword;
    }

    public void setConfirmOldPassword(String confirmOldPassword) {
        ConfirmOldPassword = confirmOldPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return ConfirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        ConfirmNewPassword = confirmNewPassword;
    }
}