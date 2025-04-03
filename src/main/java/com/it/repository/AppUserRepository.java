package com.it.repository;

import com.it.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // check if user exist By username
    boolean existsByUsername(String username);

    // check if user exist By email
    boolean existsByEmail(String email);

    // find user with username
    Optional<AppUser> findByUsername(String username);

    // check using find by password method
    boolean findByPassword(String password);
}