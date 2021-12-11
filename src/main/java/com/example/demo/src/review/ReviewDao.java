package com.example.demo.src.review;

import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.*;
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

    public List<GetReviewRes> getReviews(int userIdx, int storeIdx) {
        String getReviewQuery = "SELECT A.reviewIdx, A.name, A.roadName, A.latitude, A.longitude, A.sections\n" +
                "FROM Review A\n" +
                "JOIN Users U on A.userIdx = U.userIdx\n" +
                "WHERE A.userIdx = ? AND A.status = 'Y' AND U.status='Y'";
        int getReviewsByUserIdxParams = userIdx;
        int getStoresByStoreIdxParams = storeIdx;
        return this.jdbcTemplate.query(getReviewQuery,
                (rs, rowNum) -> new GetReviewRes(
                        rs.getString("userName"),
                        rs.getInt("reviewScore"),
                        rs.getTimestamp("createdAt"),
                        rs.getString("menuName"),
                        rs.getString("reviewContent"),
                        rs.getString("reviewImg1"),
                        rs.getString("reviewImg2"),
                        rs.getString("reviewImg3"),
                        rs.getString("reviewImg4"),
                        rs.getString("reviewImg5"),
                        rs.getInt("cntHelp")),
                getReviewsByUserIdxParams, getStoresByStoreIdxParams);
    }

    // 유저 특정 주소 조회 API
    public GetReviewRes getReview(int userIdx, int reviewIdx) {
        String getReviewQuery = "SELECT A.reviewIdx, A.name, A.roadName, A.latitude, A.longitude, A.sections\n" +
                "FROM Review A\n" +
                "JOIN Users U on A.userIdx = U.userIdx\n" +
                "WHERE A.reviewIdx = ? AND A.userIdx = ? AND A.status = 'Y' AND U.status='Y';";
        int getReviewByReviewIdxParams = reviewIdx;
        int getReviewByUserIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(getReviewQuery,
                (rs, rowNum) -> new GetReviewRes(
                        rs.getString("userName"),
                        rs.getInt("reviewScore"),
                        rs.getTimestamp("createdAt"),
                        rs.getString("menuName"),
                        rs.getString("reviewContent"),
                        rs.getString("reviewImg1"),
                        rs.getString("reviewImg2"),
                        rs.getString("reviewImg3"),
                        rs.getString("reviewImg4"),
                        rs.getString("reviewImg5"),
                        rs.getInt("cntHelp")),
                getReviewByReviewIdxParams, getReviewByUserIdxParams);
    }

    // POST 리뷰 등록 API
    public int createReview(int userIdx, int orderIdx, PostReviewReq postReviewReq){
        String createReviewQuery = "insert into Review (userIdx, orderIdx, storeIdx, reviewScore, reviewContent, reviewImg1, reviewImg2, reviewImg3, reviewImg4, reviewImg5, deliveryLike) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createReviewParams = new Object[]{userIdx, orderIdx, postReviewReq.getStoreIdx(), postReviewReq.getReviewScore(), postReviewReq.getReviewContent(), postReviewReq.getReviewImg1(), postReviewReq.getReviewImg2(), postReviewReq.getReviewImg3(), postReviewReq.getReviewImg4(), postReviewReq.getReviewImg5(), postReviewReq.getDeliveryLike() };
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    // PATCH 리뷰 수정 API
    public int modifyReview(PatchReviewReq patchReviewReq){
        String modifyReviewQuery = "update Review set storeIdx = ?, reviewScore = ?, reviewContent = ?, reviewImg1 = ?, reviewImg2 = ?, reviewImg3 = ?, reviewImg4 = ?, reviewImg5 = ?, deliveryLike = ?, status = ? where userIdx = ? AND reviewIdx = ? AND orderIdx = ?";
        Object[] modifyReviewParams = new Object[]{patchReviewReq.getStoreIdx(), patchReviewReq.getReviewScore(), patchReviewReq.getReviewContent(), patchReviewReq.getReviewImg1(), patchReviewReq.getReviewImg2(), patchReviewReq.getReviewImg3(), patchReviewReq.getReviewImg4(), patchReviewReq.getReviewImg5(), patchReviewReq.getDeliveryLike(), patchReviewReq.getStatus(), patchReviewReq.getReviewIdx(), patchReviewReq.getOrderIdx(), patchReviewReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyReviewQuery,modifyReviewParams);
    }
}
