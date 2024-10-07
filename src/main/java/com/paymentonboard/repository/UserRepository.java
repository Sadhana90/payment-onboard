package com.paymentonboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.paymentonboard.entity.User;

public interface UserRepository extends JpaRepository<User, String> {


}
