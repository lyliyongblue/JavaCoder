package com.yong.spring.data.repository;

import com.yong.spring.data.domain.User;
import com.yong.spring.data.repository.custom.CustomizedUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomizedUserRepository {
    Page<User> queryByCreatedTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    long countByAgeBetweenAndFirstName(Integer start, Integer end, String firstName);

    long removeByFirstName(String firstName);
}
