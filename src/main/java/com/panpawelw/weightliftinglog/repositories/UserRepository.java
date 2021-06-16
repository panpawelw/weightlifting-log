package com.panpawelw.weightliftinglog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.panpawelw.weightliftinglog.models.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByName(String name);

  Optional<User> findUserByEmail(String email);

  List<User> findAllByActivated(boolean activated);

  long deleteById(long id);
}