package com.example.demo.src.follow;

import com.example.demo.src.follow.model.*;
import com.example.demo.src.follow.model.PatchFollowReq;
import com.example.demo.src.follow.model.PostFollowReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FollowDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); }

    // GET 팔로워 리스트 조회 API
    public List<GetFollowerRes> getFollowers(int userIdx) {
        String getFollowQuery = "SELECT L.followIdx, (SELECT PI.imgUrl FROM ProductImg PI INNER JOIN Product P on PI.productIdx = P.productIdx ORDER BY PI.productImgIdx LIMIT 1) AS imgUrl,\n" +
                "       P.safePayment, L.status AS followStatus, P.status AS productStatus, P.productName, P.prices, U.storeName, L.createdAt\n" +
                "INNER JOIN Users U on P.userIdx = U.userIdx\n" +
                "INNER JOIN Follows L on P.productIdx = L.productIdx\n" +
                "WHERE L.userIdx = ? AND L.status = 'Y';";
        int getFollowsByUserIdxParams = userIdx;

        return this.jdbcTemplate.query(getFollowQuery,
                (rs, rowNum) -> new GetFollowerRes(
                        rs.getInt("followIdx"),
                        rs.getString("imgUrl"),
                        rs.getString("safePayment"),
                        rs.getInt("prices"),
                        rs.getInt("prices")),
                getFollowsByUserIdxParams);
    }


    // GET 팔로잉 리스트 조회 API
    public List<GetFollowingRes> getFollowings(int followingUserIdx) {
        String getFollowingQuery = "SELECT C.categoryIdx, C.categoryName\n" +
                "FROM Following C\n" +
                "WHERE C.status = 'Y';";
        int getFollowsByFollowingUserIdxParams = followingUserIdx;

        List<GetFollowingRes> result = new ArrayList<>();

        List<FollowingVo> followingList = this.jdbcTemplate.query(getFollowingQuery,
                (rs, rowNum) -> new FollowingVo(
                        rs.getInt("followIdx"),
                        rs.getString("profile"),
                        rs.getString("storeName"),
                        rs.getInt("cntProducts"),
                        rs.getInt("cntFollowers")),
                        getFollowsByFollowingUserIdxParams);

        for (FollowingVo followingVo : followingList) {
            String subcategorySql = "SELECT S.subcategoryIdx, S.subcategoryName, S.imgUrl\n" +
                    "FROM Subcategory S\n" +
                    "INNER JOIN Following C on S.categoryIdx = C.categoryIdx\n" +
                    "WHERE C.categoryIdx = ? AND C.status = 'Y' AND S.status = 'Y';";

            List<ProductVo> productList = this.jdbcTemplate.query(subcategorySql,
                    (rs, rowNum) -> new ProductVo(
                            rs.getString("imgUrl"),
                            rs.getInt("prices")),
                    getFollowsByFollowingUserIdxParams);

            GetFollowingRes getFollowingRes = new GetFollowingRes(followingVo.getFollowIdx(), followingVo.getProfile(), followingVo.getStoreName(), followingVo.getCntProducts(), followingVo.getCntProducts(), productList);

            result.add(getFollowingRes);
        }

        return result;
    }


    // POST  등록 API
    public int createFollow(int userIdx, PostFollowReq postFollowReq){
        String createFollowQuery = "INSERT INTO Follows (userIdx, followingUserIdx, brandIdx) VALUES (?,?,?)";
        Object[] createFollowParams = new Object[]{userIdx, postFollowReq.getFollowingUserIdx(), postFollowReq.getBrandIdx() };
        this.jdbcTemplate.update(createFollowQuery, createFollowParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    // PATCH 즐겨찾기 수정 API
    public int modifyFollow(PatchFollowReq patchFollowReq){
        String modifyFollowQuery = "update Follows set status = ? where userIdx = ? AND followIdx = ?";
        Object[] modifyFollowParams = new Object[]{patchFollowReq.getStatus(), patchFollowReq.getUserIdx(), patchFollowReq.getFollowIdx()};

        return this.jdbcTemplate.update(modifyFollowQuery,modifyFollowParams);
    }


}
