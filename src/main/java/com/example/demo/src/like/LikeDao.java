package com.example.demo.src.like;

import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.PatchLikeReq;
import com.example.demo.src.like.model.PostLikeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LikeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); }

    // GET 즐겨찾기 리스트 조회 API
    public List<GetLikeRes> getLikes(int userIdx) {
        String getLikeQuery = "SELECT L.likeIdx, (SELECT imgUrl FROM ProductImg PI INNER JOIN Product on PI.productIdx = P.productIdx ORDER BY PI.productImgIdx LIMIT 1) imgUrl,\n" +
                "       P.safePayment, L.status AS likeStatus, P.status AS productStatus, P.productName, P.prices, U.storeName,\n" +
                "       CASE WHEN TIMESTAMPDIFF(SECOND, P.createdAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(SECOND, P.createdAt, NOW()), '초 전') WHEN TIMESTAMPDIFF(MINUTE, P.createdAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, P.createdAt, NOW()), '분 전')\n" +
                "           WHEN TIMESTAMPDIFF(HOUR, P.createdAt, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, P.createdAt, NOW()), '시간 전')\n" +
                "           WHEN TIMESTAMPDIFF(DAY, P.createdAt, NOW()) < 31 THEN CONCAT(TIMESTAMPDIFF(DAY, P.createdAt, NOW()), '일 전')\n" +
                "           WHEN TIMESTAMPDIFF(MONTH, P.createdAt, NOW()) < 12 THEN CONCAT(TIMESTAMPDIFF(MONTH, P.createdAt, NOW()), '달 전')\n" +
                "           ELSE CONCAT(YEAR(TIMEDIFF(NOW(),P.createdAt)), '년 전') END createdAt\n" +
                "FROM Product P\n" +
                "INNER JOIN Users U on P.userIdx = U.userIdx\n" +
                "INNER JOIN Likes L on P.productIdx = L.productIdx\n" +
                "WHERE L.userIdx = ? AND L.status = 'Y';";
        int getLikesByUserIdxParams = userIdx;

        return this.jdbcTemplate.query(getLikeQuery,
                (rs, rowNum) -> new GetLikeRes(
                        rs.getInt("likeIdx"),
                        rs.getString("imgUrl"),
                        rs.getString("safePayment"),
                        rs.getString("likeStatus"),
                        rs.getString("productStatus"),
                        rs.getString("productName"),
                        rs.getInt("prices"),
                        rs.getString("storeName"),
                        rs.getString("createdAt")),
                getLikesByUserIdxParams);
    }

    // POST 즐겨찾기 등록 API
    public int createLike(int userIdx, PostLikeReq postLikeReq){
        String createLikeQuery = "INSERT INTO Likes (userIdx, productIdx) VALUES (?,?)";
        Object[] createLikeParams = new Object[]{userIdx, postLikeReq.getProductIdx() };
        this.jdbcTemplate.update(createLikeQuery, createLikeParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    // PATCH 즐겨찾기 수정 API
    public int modifyLike(PatchLikeReq patchLikeReq){
        String modifyLikeQuery = "update Likes set status = ? where userIdx = ? AND likeIdx = ?";
        Object[] modifyLikeParams = new Object[]{patchLikeReq.getStatus(), patchLikeReq.getUserIdx(), patchLikeReq.getLikeIdx()};

        return this.jdbcTemplate.update(modifyLikeQuery,modifyLikeParams);
    }

    // 유저 유무 확인
    public int checkLikeIdx(int likeIdx){
        String checkLikeIdxQuery = "select exists(select likeIdx from Likes where likeIdx = ?)";
        int checkLikeIdxParams = likeIdx;
        return this.jdbcTemplate.queryForObject(checkLikeIdxQuery, int.class, checkLikeIdxParams);
    }



}
