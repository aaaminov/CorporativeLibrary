package com.aminov.corporativelibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aminov.corporativelibrary.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
}