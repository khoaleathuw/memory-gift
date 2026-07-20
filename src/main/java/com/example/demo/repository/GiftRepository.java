package com.example.demo.repository;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftImage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GiftRepository {

    private final JdbcTemplate jdbcTemplate;

    public GiftRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // ==================================================
    // INSERT GIFT
    // ==================================================

    public Long insertGift(Gift gift) {

        if (gift == null) {
            throw new IllegalArgumentException(
                    "Gift không được để trống"
            );
        }

        if (gift.getUid() == null || gift.getUid().isBlank()) {
            throw new IllegalArgumentException(
                    "UID không được để trống"
            );
        }

        String sql = """
                INSERT INTO gift (
                    uid,
                    title,
                    theme,
                    message,
                    voice_url
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder =
                new GeneratedKeyHolder();

        int affectedRows = jdbcTemplate.update(
                connection -> {

                    PreparedStatement ps =
                            connection.prepareStatement(
                                    sql,
                                    Statement.RETURN_GENERATED_KEYS
                            );

                    ps.setString(1, gift.getUid());
                    ps.setString(2, gift.getTitle());
                    ps.setString(3, gift.getTheme());
                    ps.setString(4, gift.getMessage());
                    ps.setString(5, gift.getVoiceUrl());

                    return ps;
                },
                keyHolder
        );

        if (affectedRows == 0) {
            throw new RuntimeException(
                    "Không thể tạo Gift mới"
            );
        }

        Number generatedId =
                keyHolder.getKey();

        if (generatedId == null) {
            throw new RuntimeException(
                    "Không lấy được ID của Gift vừa tạo"
            );
        }

        return generatedId.longValue();
    }


    // ==================================================
    // UPDATE GIFT
    // ==================================================

    public void updateGift(Gift gift) {

        if (gift == null || gift.getId() == null) {
            throw new IllegalArgumentException(
                    "Gift hoặc Gift ID không hợp lệ"
            );
        }

        String sql = """
                UPDATE gift
                SET
                    uid = ?,
                    title = ?,
                    theme = ?,
                    message = ?,
                    voice_url = ?
                WHERE id = ?
                """;

        int affectedRows = jdbcTemplate.update(
                sql,
                gift.getUid(),
                gift.getTitle(),
                gift.getTheme(),
                gift.getMessage(),
                gift.getVoiceUrl(),
                gift.getId()
        );

        if (affectedRows == 0) {
            throw new RuntimeException(
                    "Không tìm thấy Gift ID: " + gift.getId()
            );
        }
    }


    // ==================================================
    // FIND GIFT BY UID
    // ==================================================

    public Gift findByUid(String uid) {

        if (uid == null || uid.isBlank()) {
            return null;
        }

        String sql = """
                SELECT
                    id,
                    uid,
                    title,
                    theme,
                    message,
                    voice_url
                FROM gift
                WHERE uid = ?
                """;

        List<Gift> gifts = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapGift(rs),
                uid
        );

        if (gifts.isEmpty()) {
            return null;
        }

        Gift gift = gifts.get(0);

        gift.setImages(
                findImagesByGiftId(gift.getId())
        );

        return gift;
    }


    // ==================================================
    // FIND GIFT BY ID
    // ==================================================

    public Gift findById(Long id) {

        if (id == null) {
            return null;
        }

        String sql = """
                SELECT
                    id,
                    uid,
                    title,
                    theme,
                    message,
                    voice_url
                FROM gift
                WHERE id = ?
                """;

        List<Gift> gifts = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapGift(rs),
                id
        );

        if (gifts.isEmpty()) {
            return null;
        }

        Gift gift = gifts.get(0);

        gift.setImages(
                findImagesByGiftId(gift.getId())
        );

        return gift;
    }


    // ==================================================
    // MAP RESULTSET TO GIFT
    // ==================================================

    private Gift mapGift(java.sql.ResultSet rs)
            throws java.sql.SQLException {

        Gift gift = new Gift();

        gift.setId(
                rs.getLong("id")
        );

        gift.setUid(
                rs.getString("uid")
        );

        gift.setTitle(
                rs.getString("title")
        );

        gift.setTheme(
                rs.getString("theme")
        );

        gift.setMessage(
                rs.getString("message")
        );

        gift.setVoiceUrl(
                rs.getString("voice_url")
        );

        return gift;
    }


    // ==================================================
    // INSERT IMAGE
    // ==================================================

    public Long insertImage(
            Long giftId,
            String imageUrl
    ) {

        if (giftId == null) {
            throw new IllegalArgumentException(
                    "Gift ID không được để trống"
            );
        }

        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException(
                    "Image URL không được để trống"
            );
        }

        String sql = """
                INSERT INTO gift_image (
                    gift_id,
                    image_url
                )
                VALUES (?, ?)
                """;

        KeyHolder keyHolder =
                new GeneratedKeyHolder();

        int affectedRows = jdbcTemplate.update(
                connection -> {

                    PreparedStatement ps =
                            connection.prepareStatement(
                                    sql,
                                    Statement.RETURN_GENERATED_KEYS
                            );

                    ps.setLong(1, giftId);
                    ps.setString(2, imageUrl);

                    return ps;
                },
                keyHolder
        );

        if (affectedRows == 0) {
            throw new RuntimeException(
                    "Không thể thêm ảnh vào Gift ID: " + giftId
            );
        }

        Number generatedId =
                keyHolder.getKey();

        return generatedId == null
                ? null
                : generatedId.longValue();
    }


    // ==================================================
    // FIND IMAGES BY GIFT ID
    // ==================================================

    public List<GiftImage> findImagesByGiftId(
            Long giftId
    ) {

        if (giftId == null) {
            return List.of();
        }

        String sql = """
                SELECT
                    id,
                    gift_id,
                    image_url
                FROM gift_image
                WHERE gift_id = ?
                ORDER BY id ASC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {

                    GiftImage image =
                            new GiftImage();

                    image.setId(
                            rs.getLong("id")
                    );

                    image.setGiftId(
                            rs.getLong("gift_id")
                    );

                    image.setImageUrl(
                            rs.getString("image_url")
                    );

                    return image;
                },
                giftId
        );
    }


    // ==================================================
    // DELETE IMAGE BY URL
    // ==================================================

    public int deleteImageByUrl(
            Long giftId,
            String imageUrl
    ) {

        if (giftId == null ||
                imageUrl == null ||
                imageUrl.isBlank()) {

            return 0;
        }

        String sql = """
                DELETE FROM gift_image
                WHERE gift_id = ?
                AND image_url = ?
                """;

        return jdbcTemplate.update(
                sql,
                giftId,
                imageUrl
        );
    }


    // ==================================================
    // DELETE ALL IMAGES BY GIFT ID
    // ==================================================

    public int deleteImagesByGiftId(
            Long giftId
    ) {

        if (giftId == null) {
            return 0;
        }

        String sql = """
                DELETE FROM gift_image
                WHERE gift_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                giftId
        );
    }


    // ==================================================
    // DELETE GIFT
    // ==================================================

    public void deleteGift(Long id) {

        if (id == null) {
            return;
        }

        deleteImagesByGiftId(id);

        jdbcTemplate.update(
                """
                DELETE FROM gift
                WHERE id = ?
                """,
                id
        );
    }
}