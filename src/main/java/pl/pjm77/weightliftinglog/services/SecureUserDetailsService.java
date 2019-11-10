package pl.pjm77.weightliftinglog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.pjm77.weightliftinglog.models.SecureUserDetails;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.repositories.UserRepository;

import java.util.Optional;

@Service
public class SecureUserDetailsService implements UserDetailsService {

    private final UserRepository secureUserRepository;

    @Autowired
    public SecureUserDetailsService(UserRepository secureUserRepository) {
        this.secureUserRepository = secureUserRepository;
    }

    /**
     * Gets Spring Security UserDetails object of user with given name
     * @param name - user name
     * @return UserDetails object
     * @throws UsernameNotFoundException - thrown when user with give name is not found in database
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = secureUserRepository.findUserByName(name);
        if (user.isPresent()) {
            return new SecureUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException("User name not found!");
        }
    }
}