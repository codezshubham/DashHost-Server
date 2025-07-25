package com.WebHost.Manager.Repository;

import com.WebHost.Manager.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.clients WHERE u.email = :email")
    Optional<User> findByEmailWithClients(@Param("email") String email);

}

