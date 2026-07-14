package com.example.demo.repository;

import com.example.demo.entity.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {


    Optional<Gift> findByUid(String uid);

}