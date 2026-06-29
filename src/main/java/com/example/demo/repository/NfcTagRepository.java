package com.example.demo.repository;

import com.example.demo.entity.NfcTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NfcTagRepository extends JpaRepository<NfcTag, Long> {

    Optional<NfcTag> findByUid(String uid);

}