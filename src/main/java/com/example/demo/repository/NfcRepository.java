package com.example.demo.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class NfcRepository {


    private final JdbcTemplate jdbcTemplate;


    public NfcRepository(JdbcTemplate jdbcTemplate){

        this.jdbcTemplate = jdbcTemplate;

    }



    // ==========================
    // CHECK UID
    // ==========================
    public boolean existsUid(String uid){


        String sql = """
                SELECT COUNT(*)
                FROM nfc_tag
                WHERE uid = ?
                """;


        Integer count =
                jdbcTemplate.queryForObject(
                        sql,
                        Integer.class,
                        uid
                );


        return count != null && count > 0;

    }





    // ==========================
    // BIND GIFT TO NFC
    // ==========================
    public void bindGift(
            String uid,
            Long giftId
    ){


        if(existsUid(uid)){


            jdbcTemplate.update(

                    """
                    UPDATE nfc_tag
                    SET gift_id = ?
                    WHERE uid = ?
                    """,

                    giftId,
                    uid

            );


        }
        else{


            jdbcTemplate.update(

                    """
                    INSERT INTO nfc_tag(uid,gift_id)
                    VALUES(?,?)
                    """,

                    uid,
                    giftId

            );

        }

    }





    // ==========================
    // GET GIFT ID BY UID
    // ==========================
    public Long findGiftIdByUid(String uid){


        String sql = """
                SELECT gift_id
                FROM nfc_tag
                WHERE uid = ?
                """;


        List<Long> result =
                jdbcTemplate.query(

                        sql,

                        (rs,rowNum) ->
                                rs.getLong("gift_id"),

                        uid

                );


        if(result.isEmpty()){

            return null;

        }


        return result.get(0);

    }


}