package com.panpawelw.weightliftinglog.services;

import com.panpawelw.weightliftinglog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import com.panpawelw.weightliftinglog.models.SecureUserDetails;
import com.panpawelw.weightliftinglog.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  /**
   * Finds user in database by email
   *
   * @param email - username
   * @return user object or null if nothing found
   */
  public User findUserByEmail(String email) {
    Optional<User> user = userRepository.findUserByEmail(email);
    return user.orElse(null);
  }

  /**
   * Saves user to database
   *
   * @param user - user object
   */
  public Long saveUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
  return userRepository.saveAndFlush(user).getId();
  }

  /**
   * Saves user to database without modifying password
   *
   * @param user - user object
   */
  public Long saveUserWithoutModifyingPassword(User user) {
    return userRepository.saveAndFlush(user).getId();
  }

  /**
   * Deletes user from database
   *
   * @param id - id of user to be deleted
   */
  public void deleteUserById(long id) {
    userRepository.deleteById(id);
  }

  /**
   * Finds all users in database by activated boolean
   * @param activated - activated
   * @return list of users
   */
  public List<User> findAllByActivated(boolean activated) {
    return userRepository.findAllByActivated(activated);
  }

  /**
   * Counts all records in user table
   * @return count
   */
  public Long count() {
    return userRepository.count();
  }

  /**
   * Automatic user login
   *
   * @param request  - HttpServletRequest passed from controller
   * @param username - username
   * @param password - password
   */
  public void autoLogin(HttpServletRequest request, String username, String password) {
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(username, password);
    request.getSession();
    token.setDetails(new WebAuthenticationDetails(request));
    Authentication authenticatedUser = authenticationManager.authenticate(token);
    SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
  }

  /**
   * Logs current user out
   *
   * @param request  - HttpServletRequest passed from controller
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
   *
   * @param password - new password
   */
  public void changeCurrentUserPassword(String password) {
    User user = findUserByEmail(getLoggedInUsersEmail());
    user.setPassword(password);
    saveUser(user);
  }

  /**
   * Checks a string against currently logged in user's password.
   *
   * @param password password that is supposed to be checked
   * @return true for match, false for mismatch
   */
  public boolean passwordsDontMatch(String password) {
    return !passwordEncoder.matches(password, getLoggedInUserPassword());
  }

  /**
   * Provides the name of the user who is currently logged in, or a string
   * representation of principal object if it's anything else than instance of UserDetail.
   *
   * @return string value with name of the user or string representation of principal object
   */
  public String getLoggedInUserName() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      return ((UserDetails) principal).getUsername();
    } else {
      return principal.toString();
    }
  }

  /**
   * Provides the email of the user who is currently logged in, or a string
   * representation of principal object if it's anything else than instance of UserDetail.
   *
   * @return string value with email of the user or string representation of principal object
   */
  public String getLoggedInUsersEmail() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      return ((SecureUserDetails) principal).getEmail();
    } else {
      return principal.toString();
    }
  }

  /**
   * Check currently logged in user for admin rights
   *
   * @return true if user has admin rights, false if not
   */
  public boolean checkLoggedInUserForAdminRights() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Collection<? extends GrantedAuthority> authorities;
    if (principal instanceof UserDetails) {
      authorities = ((UserDetails) principal).getAuthorities();
      for (GrantedAuthority grantedAuthority : authorities) {
        if (("ROLE_ADMIN").equals(grantedAuthority.getAuthority())) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Provides the password of the user who is currently logged in, or null if
   * it's anything else than instance of UserDetail.
   *
   * @return string value with password of the currently logged in user or null
   */
  public String getLoggedInUserPassword() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      return ((UserDetails) principal).getPassword();
    } else {
      return null;
    }
  }
}