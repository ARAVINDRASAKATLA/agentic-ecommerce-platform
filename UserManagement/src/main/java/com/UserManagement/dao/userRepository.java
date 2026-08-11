package com.UserManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.UserManagement.Model.users;

@Repository
public interface userRepository extends JpaRepository<users, Integer> {


}
