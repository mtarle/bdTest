package com.studentcloud.dbaccess.repo.auth;

import com.studentcloud.dbaccess.entities.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
