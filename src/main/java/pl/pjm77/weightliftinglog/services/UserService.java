package pl.pjm77.weightliftinglog.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    public void saveUser(User user) throws DataIntegrityViolationException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Logs current user out
     * @param request - HttpServletRequest passed from controller
     * @param response - HttpServletResponse passed from controller
     * @return null if logout succesful, Authentication object if failure
     */
    public Authentication logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    /**
     * Sets a new password for user who is currently logged in and saves it to database
     * @param password - new password
     */
    public void changeCurrentUserPassword(String password) {
        User user = findUserByName(getLoggedInUserName());
        user.setPassword(password);
        saveUser(user);
    }

    /**
     * Checks a string against currently logged in user's password.
     * @param password password that is supposed to be checked
     * @return true for match, false for mismatch
     */
    public static boolean passwordsDontMatch(String password) {
        return !BCrypt.checkpw(password, getLoggedInUserPassword());
    }

    /**
     * Provides the name of the user who is currently logged in, or a string
     * representation of principal object if it's anything else than instance of UserDetail.
     * @return string value with name of the user or string representation of principal object
     */
    public static String getLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName;
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    /**
     * Check currently logged in user for admin rights
     * @return true if user has admin rights, false if not
     */
    public static boolean checkLoggedInUserForAdminRights() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> authorities;
        if (principal instanceof UserDetails) {
            authorities = ((UserDetails) principal).getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                if (("ROLE_ADMIN").equals(grantedAuthority.getAuthority())) {return true;}
            }
        }
        return false;
    }

    /**
     * Provides the password of the user who is currently logged in, or null if
     * it's anything else than instance of UserDetail.
     * @return string value with password of the currently logged in user or null
     */
    private static String getLoggedInUserPassword() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userPassword;
        if (principal instanceof UserDetails) {
            userPassword = ((UserDetails) principal).getPassword();
        } else {
            userPassword = null;
        }
        return userPassword;
    }
}