package com.hackaton.alicecity.repository;

import com.hackaton.alicecity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User,String> {
}
