package pl.pjm77.weightliftinglog.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.repositories.UserRepository;

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
     *
     * @param password
     */
    public void changeCurrentUserPassword(String password) {
        User user = findUserByName(UserService.getLoggedInUserName());
        user.setPassword(password);
        saveUser(user);
    }

    /**
     * Checks a string against currently logged in user's password.
     * @param password password that is supposed to be checked
     * @return boolean
     */
    public boolean checkCurrentUserPassword(String password) {
        return BCrypt.checkpw(password, getLoggedInUserPassword());
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