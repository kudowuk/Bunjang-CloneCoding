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
        String getFollowQuery = "SELECT F.followIdx, (SELECT ST.profiles FROM Store ST WHERE F.userIdx = ST.userIdx) profiles ,\n" +
                "       (SELECT U2.storeName FROM Users U2 WHERE F.userIdx = U2.userIdx) storeName,\n" +
                "       (SELECT COUNT(P.productIdx) FROM Product P WHERE F.userIdx = P.userIdx) cntProducts,\n" +
                "       (SELECT COUNT(F2.followIdx) FROM Follow F2 WHERE F.userIdx = F2.followingUserIdx) cntFollowers\n" +
                "FROM Follow F\n" +
                "WHERE F.followingUserIdx = ? AND F.status = 'Y' ORDER BY F.createdAt DESC;";
        int getFollowsByUserIdxParams = userIdx;

        return this.jdbcTemplate.query(getFollowQuery,
                (rs, rowNum) -> new GetFollowerRes(
                        rs.getInt("followIdx"),
                        rs.getString("profiles"),
                        rs.getString("storeName"),
                        rs.getInt("cntProducts"),
                        rs.getInt("cntFollowers")),
                getFollowsByUserIdxParams);
    }


    // GET 팔로잉 리스트 조회 API
    public List<GetFollowingRes> getFollowings(int userIdx) {
        String getFollowingQuery = "SELECT F.followIdx, (SELECT ST.profiles FROM Store ST WHERE F.followingUserIdx = ST.userIdx) profiles,\n" +
                "       (SELECT U2.storeName FROM Users U2 WHERE F.followingUserIdx = U2.userIdx) storeName,\n" +
                "       (SELECT COUNT(P.productIdx) FROM Product P WHERE F.followingUserIdx = P.userIdx) cntProducts,\n" +
                "       (SELECT COUNT(F2.followIdx) FROM Follow F2 WHERE F2.followingUserIdx = F.followingUserIdx) cntFollowers\n" +
                "FROM Follow F\n" +
                "WHERE F.userIdx = ? AND F.status = 'Y' ORDER BY F.createdAt DESC;";
        int getFollowsByFollowingUserIdxParams = userIdx;

        List<GetFollowingRes> result = new ArrayList<>();

        List<FollowingVo> followingList = this.jdbcTemplate.query(getFollowingQuery,
                (rs, rowNum) -> new FollowingVo(
                        rs.getInt("followIdx"),
                        rs.getString("profiles"),
                        rs.getString("storeName"),
                        rs.getInt("cntProducts"),
                        rs.getInt("cntFollowers")),
                        getFollowsByFollowingUserIdxParams);

        for (FollowingVo followingVo : followingList) {
            String subcategorySql = "SELECT (SELECT imgUrl FROM ProductImg PI INNER JOIN Product on PI.productIdx = P.productIdx WHERE PI.status = 'Y' ORDER BY PI.productImgIdx LIMIT 1 ) imgUrl, (SELECT P.prices LIMIT 1) prices\n" +
                    "FROM Product P\n" +
                    "INNER JOIN Follow F on P.userIdx = F.followingUserIdx\n" +
                    "WHERE F.followIdx = ? ORDER BY P.createdAt DESC LIMIT 3;";

            List<ProductVo> productList = this.jdbcTemplate.query(subcategorySql,
                    (rs, rowNum) -> new ProductVo(
                            rs.getString("imgUrl"),
                            rs.getInt("prices")),
                    followingVo.getFollowIdx());

            GetFollowingRes getFollowingRes = new GetFollowingRes(followingVo.getFollowIdx(), followingVo.getProfiles(), followingVo.getStoreName(), followingVo.getCntProducts(), followingVo.getCntFollowers(), productList);

            result.add(getFollowingRes);
        }

        return result;
    }


    // POST  등록 API
    public int createFollow(int userIdx, PostFollowReq postFollowReq){
        String createFollowQuery = "INSERT INTO Follow (userIdx, brandIdx, followingUserIdx) VALUES (?,?,?)";
        Object[] createFollowParams = new Object[]{userIdx, postFollowReq.getBrandIdx(), postFollowReq.getFollowingUserIdx()};
        this.jdbcTemplate.update(createFollowQuery, createFollowParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    // PATCH 즐겨찾기 수정 API
    public int modifyFollow(PatchFollowReq patchFollowReq){
        String modifyFollowQuery = "update Follow set status = ? where userIdx = ? AND followIdx = ?";
        Object[] modifyFollowParams = new Object[]{patchFollowReq.getStatus(), patchFollowReq.getUserIdx(), patchFollowReq.getFollowIdx()};

        return this.jdbcTemplate.update(modifyFollowQuery,modifyFollowParams);
    }

    // 브랜드 유무확인
    public int checkBrandIdx(int brandIdx){
        String checkBrandQuery = "select exists(select brandIdx from Brand where brandIdx = ?)";
        int checkBrandParams = brandIdx;
        return this.jdbcTemplate.queryForObject(checkBrandQuery, int.class, checkBrandParams);

    }
    // 브랜드 활성화 확인
    public int checkStatusBrandIdx(int brandIdx){
        String checkBrandIdxQuery = "select exists(select brandIdx from Brand where brandIdx = ? AND Brand.status = 'N')";
        int checkBrandIdxParams = brandIdx;
        return this.jdbcTemplate.queryForObject(checkBrandIdxQuery, int.class, checkBrandIdxParams);
    }

    // 팔로잉 하려는 유저인덱스 유무확인
    public int checkFollowingUserIdx(int followingUserIdx){
        String checkFollowQuery = "select exists(select followingUserIdx from Follow where followingUserIdx = ?)";
        int checkFollowParams = followingUserIdx;
        return this.jdbcTemplate.queryForObject(checkFollowQuery, int.class, checkFollowParams);

    }
    // 팔로잉 하려는 유저인덱스 활성화 확인
    public int checkStatusFollowingUserIdx(int followingUserIdx){
        String checkFollowIdxQuery = "select exists(select followingUserIdx from Follow where followingUserIdx = ? AND Follow.status = 'N')";
        int checkFollowIdxParams = followingUserIdx;
        return this.jdbcTemplate.queryForObject(checkFollowIdxQuery, int.class, checkFollowIdxParams);
    }


}
