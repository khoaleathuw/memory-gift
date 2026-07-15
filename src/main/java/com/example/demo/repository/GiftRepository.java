package com.example.demo.repository;

import com.example.demo.entity.GiftImage;
import com.example.demo.entity.Gift;
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



    // =========================================
    // INSERT GIFT
    // =========================================
    public Long insertGift(Gift gift) {

        String sql = """
                INSERT INTO gift(title, message, voice_url)
                VALUES(?, ?, ?)
                """;


        KeyHolder keyHolder = new GeneratedKeyHolder();


        jdbcTemplate.update(connection -> {

            PreparedStatement ps =
                    connection.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS
                    );


            ps.setString(1, gift.getTitle());

            ps.setString(2, gift.getMessage());

            ps.setString(3, gift.getVoiceUrl());



            return ps;

        }, keyHolder);



        return keyHolder.getKey().longValue();
    }





    // =========================================
    // UPDATE GIFT
    // =========================================
    public void updateGift(Gift gift) {


        String sql = """
                UPDATE gift
                SET
                    title = ?,
                    message = ?,
                    voice_url = ?
                WHERE id = ?
                """;


        jdbcTemplate.update(
                sql,

                gift.getTitle(),

                gift.getMessage(),

                gift.getVoiceUrl(),

                gift.getId()
        );

    }





    // =========================================
    // INSERT IMAGE
    // =========================================
    public void insertImage(Long giftId, String imageUrl) {


        String sql = """
                INSERT INTO gift_image(gift_id, image_url)
                VALUES(?, ?)
                """;


        jdbcTemplate.update(
                sql,
                giftId,
                imageUrl
        );

    }





    // =========================================
    // FIND GIFT BY ID
    // =========================================
    public Gift findById(Long id) {


        String sql = """
                SELECT
                    id,
                    title,
                    message,
                    voice_url
                FROM gift
                WHERE id = ?
                """;



        List<Gift> list = jdbcTemplate.query(

                sql,

                (rs, rowNum) -> {


                    Gift gift = new Gift();


                    gift.setId(
                            rs.getLong("id")
                    );


                    gift.setTitle(
                            rs.getString("title")
                    );


                    gift.setMessage(
                            rs.getString("message")
                    );


                    gift.setVoiceUrl(
                            rs.getString("voice_url")
                    );



                    return gift;

                },

                id
        );



        if(list.isEmpty()){
            return null;
        }



        Gift gift = list.get(0);



        gift.setImages(
                findImagesByGiftId(id)
        );



        return gift;

    }





    // =========================================
    // FIND IMAGES
    // =========================================
    public List<GiftImage> findImagesByGiftId(Long giftId) {


        String sql = """
                SELECT
                    id,
                    gift_id,
                    image_url
                FROM gift_image
                WHERE gift_id = ?
                ORDER BY id
                """;



        return jdbcTemplate.query(

                sql,


                (rs,rowNum)->{


                    GiftImage image = new GiftImage();


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





    // =========================================
    // DELETE IMAGE
    // =========================================
    public void deleteImageByUrl(
            Long giftId,
            String imageUrl
    ){


        String sql = """
                DELETE FROM gift_image
                WHERE gift_id = ?
                AND image_url = ?
                """;


        jdbcTemplate.update(
                sql,
                giftId,
                imageUrl
        );

    }





    // =========================================
    // DELETE GIFT
    // =========================================
    public void deleteGift(Long id) {


        jdbcTemplate.update(
                """
                DELETE FROM gift_image
                WHERE gift_id = ?
                """,
                id
        );



        jdbcTemplate.update(
                """
                DELETE FROM gift
                WHERE id = ?
                """,
                id
        );

    }

}