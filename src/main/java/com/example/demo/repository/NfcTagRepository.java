package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.NfcTag;

@Repository
public interface NfcTagRepository extends JpaRepository<NfcTag, Integer> {

    Optional<NfcTag> findByUid(String uid);

}