package com.amn.repository;

import com.amn.dto.UserDTO;
import com.amn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT new com.amn.dto.UserDTO(u.id, u.fullName, u.email, u.role) FROM User u")
    List<UserDTO> findAllUserData();


}
