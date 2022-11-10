package com.aminov.corporativelibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aminov.corporativelibrary.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

}