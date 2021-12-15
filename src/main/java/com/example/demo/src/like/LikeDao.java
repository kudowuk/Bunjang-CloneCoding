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
        String getLikeQuery = "SELECT L.likeIdx, (SELECT PI.imgUrl FROM ProductImg PI INNER JOIN Product P on PI.productIdx = P.productIdx ORDER BY PI.productImgIdx LIMIT 1) AS imgUrl,\n" +
                "       P.safePayment, L.status AS likeStatus, P.status AS productStatus, P.productName, P.prices, U.storeName, L.createdAt\n" +
                "INNER JOIN Users U on P.userIdx = U.userIdx\n" +
                "INNER JOIN Likes L on P.productIdx = L.productIdx\n" +
                "WHERE L.userIdx = ? AND L.status = 'Y';";
        int getLikesByUserIdxParams = userIdx;

        return this.jdbcTemplate.query(getLikeQuery,
                (rs, rowNum) -> new GetLikeRes(
                        rs.getInt("likeIdx"),
                        rs.getString("imgUrl"),
                        rs.getString("safePayment"),
                        rs.getString("productName"),
                        rs.getString("likeStatus"),
                        rs.getString("purchaseStatus"),
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



}
