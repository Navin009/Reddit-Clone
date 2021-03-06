package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUsername(String shreya);
}
