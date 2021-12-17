package com.example.demo.src.review;

import com.example.demo.src.review.model.*;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PatchReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); }

    public List<GetReviewRes> getReviews(int userIdx) {
        String getReviewQuery = "SELECT R.reviewIdx, US.storeName, R.score, R.content, P.productName, R.imgUrl1, R.imgUrl2, R.imgUrl3, R.createdAt\n" +
                "FROM Review R\n" +
                "INNER JOIN Purchase PC on R.purchaseIdx = PC.purchaseIdx\n" +
                "INNER JOIN Users US on R.userIdx = US.userIdx\n" +
                "INNER JOIN Product P on PC.productIdx = P.productIdx\n" +
                "LEFT JOIN Users U on P.userIdx = U.userIdx\n" +
                "WHERE P.userIdx = ?;";
        int getReviewsByUserIdxParams = userIdx;
        return this.jdbcTemplate.query(getReviewQuery,
                (rs, rowNum) -> new GetReviewRes(
                        rs.getInt("reviewIdx"),
                        rs.getString("storeName"),
                        rs.getInt("score"),
                        rs.getString("content"),
                        rs.getString("productName"),
                        rs.getString("imgUrl1"),
                        rs.getString("imgUrl2"),
                        rs.getString("imgUrl3"),
                        rs.getTimestamp("createdAt")),
                getReviewsByUserIdxParams);
    }


    // POST 리뷰 등록 API
    public int createReview(int userIdx, int purchaseIdx, PostReviewReq postReviewReq){
        String createReviewQuery = "insert into Review (userIdx, purchaseIdx, score, content, imgUrl1, imgUrl2, imgUrl3) VALUES (?,?,?,?,?,?,?)";
        Object[] createReviewParams = new Object[]{userIdx, purchaseIdx, postReviewReq.getScore(), postReviewReq.getContent(), postReviewReq.getImgUrl1(), postReviewReq.getImgUrl2(), postReviewReq.getImgUrl3()};
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    // PATCH 리뷰 수정 API
    public int modifyReview(PatchReviewReq patchReviewReq){
        String modifyReviewQuery = "update Review set score = ?, content = ?, imgUrl1 = ?, imgUrl2 = ?, imgUrl3 = ?, status = ? where userIdx = ? AND reviewIdx = ? AND purchaseIdx = ?";
        Object[] modifyReviewParams = new Object[]{patchReviewReq.getScore(), patchReviewReq.getContent(), patchReviewReq.getImgUrl1(), patchReviewReq.getImgUrl2(), patchReviewReq.getImgUrl3(), patchReviewReq.getStatus(),patchReviewReq.getUserIdx(), patchReviewReq.getReviewIdx(),  patchReviewReq.getPurchaseIdx()};

        return this.jdbcTemplate.update(modifyReviewQuery,modifyReviewParams);
    }

    // 구매한 이력 유무 확인
    public int checkPurchaseIdx(int purchaseIdx, int userIdx){
        String checkPurchaseIdxQuery = "select exists(select purchaseIdx from Purchase where purchaseIdx = ? AND userIdx = ? )";
        int checkPurchaseIdxParams = purchaseIdx;
        int checkUserIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkPurchaseIdxQuery, int.class, checkPurchaseIdxParams, checkUserIdxParams);
    }
    // 구매한 이력 상태 확인
    public int checkStatusPurchaseIdx(int purchaseIdx, int userIdx){
        String checkPurchaseIdxQuery = "select exists(select purchaseIdx from Purchase where purchaseIdx = ? AND userIdx = ? AND Purchase.status = 'N')";
        int checkPurchaseIdxParams = purchaseIdx;
        int checkUserIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkPurchaseIdxQuery, int.class, checkPurchaseIdxParams, checkUserIdxParams);
    }

    // 거래내역 유무 확인
    public int checkAnyPurchaseIdx(int purchaseIdx){
        String checkPurchaseIdxQuery = "select exists(select purchaseIdx from Purchase where purchaseIdx = ?)";
        int checkPurchaseIdxParams = purchaseIdx;
        return this.jdbcTemplate.queryForObject(checkPurchaseIdxQuery, int.class, checkPurchaseIdxParams);
    }

    // 리뷰 유무 확인
    public int checkReviewIdx(int reviewIdx){
        String checkReviewIdxQuery = "select exists(select reviewIdx from Review where reviewIdx = ?)";
        int checkReviewIdxParams = reviewIdx;
        return this.jdbcTemplate.queryForObject(checkReviewIdxQuery, int.class, checkReviewIdxParams);
    }


}
