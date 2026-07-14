package com.example.demo.repository;

import com.example.demo.entity.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {

    // JOIN FETCH tường minh: đảm bảo "images" được load managed đúng cách
    // trong CÙNG một query/transaction, tránh mọi rủi ro liên quan tới cách
    // EAGER mặc định của Hibernate tách thành 2 query riêng (1 cho gift,
    // 1 cho collection con) rồi kết hợp lại - vốn là nguồn gốc phổ biến
    // của các bug orphanRemoval xóa nhầm dữ liệu khi merge/save lại entity.
    @Query("SELECT DISTINCT g FROM Gift g LEFT JOIN FETCH g.images WHERE g.id = :id")
    Optional<Gift> findByIdWithImages(@Param("id") Long id);
}