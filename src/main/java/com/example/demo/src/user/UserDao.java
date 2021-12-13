package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from Users where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("profiles"),
                        rs.getString("storeName"),
                        rs.getString("storeAddress"),
                        rs.getString("contactableTime"),
                        rs.getString("storeIntro"),
                        rs.getString("tradePolicy"),
                        rs.getString("flag")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "SELECT S.userIdx, S.profiles, U.storeName, S.storeAddress, S.contactableTime, S.storeIntro, S.tradePolicy, S.flag\n" +
                "FROM Store S\n" +
                "LEFT JOIN Users U on S.userIdx = U.userIdx\n" +
                "WHERE U.userIdx = ?;";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("profiles"),
                        rs.getString("storeName"),
                        rs.getString("storeAddress"),
                        rs.getString("contactableTime"),
                        rs.getString("storeIntro"),
                        rs.getString("tradePolicy"),
                        rs.getString("flag")),
                getUserParams);
    }


    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into Users (email, password, storeName, phone, birthDate, userType) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getStoreName(), postUserReq.getPhone(), postUserReq.getBirthDate(), postUserReq.getUserType()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int createStore(int userIdx){
        String createStoreQuery = "insert into Store (userIdx) VALUES (?)";
        Object[] createStoreParams = new Object[]{userIdx};
        this.jdbcTemplate.update(createStoreQuery, createStoreParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }


    // 이메일 중복 체크
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from Users where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    // 상점명 중복체크
    public int checkStoreName(String storeName){
        String checkStoreNameQuery = "select exists(select storeName from Users where storeName = ?)";
        String checkStoreNameParams = storeName;
        return this.jdbcTemplate.queryForObject(checkStoreNameQuery,
                int.class,
                checkStoreNameParams);

    }

//    public int modifyStoreName(PatchUserReq patchUserReq){
//        String modifyUserNameQuery = "UPDATE Users SET storeName  WHERE userIdx = ?";
//        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserIdx(), patchUserReq.getStoreName()};
//
//        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
//    }

    public int modifyStoreInfo(int userIdx, Store store){
        String modifyStoreInfoQuery = "UPDATE Store SET profiles = ?, storeAddress = ?, contactableTime = ?, storeIntro = ?, tradePolicy = ?, flag = ? WHERE userIdx = ?";
        Object[] modifyStoreInfoParams = new Object[]{store.getProfiles(), store.getStoreAddress(), store.getContactableTime(), store.getStoreIntro(), store.getTradePolicy(), store.getFlag(), userIdx};
        this.jdbcTemplate.update(modifyStoreInfoQuery,modifyStoreInfoParams);

        String modifyStoreNameQuery = "UPDATE Users SET storeName = ? WHERE userIdx = ?";
        Object[] modifyStoreNameParams = new Object[]{store.getStoreName(), userIdx};
        return this.jdbcTemplate.update(modifyStoreNameQuery,modifyStoreNameParams);
    }



    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, email, password, storeName, birthDate, userType from Users where email = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("storeName"),
                        rs.getString("birthDate"),
                        rs.getString("password")
                ),
                getPwdParams
                );

    }

    // 유저 유무 확인
    public int checkUserIdx(int userIdx){
        String checkUserIdxQuery = "select exists(select userIdx from Users where userIdx = ?)";
        int checkUserIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserIdxQuery, int.class, checkUserIdxParams);
    }

    // 유저 탈퇴 확인
    public int checkStatusUserIdx(int userIdx){
        String checkUserIdxQuery = "select exists(select userIdx from Users where userIdx = ? AND Users.status = 'N')";
        int checkUserIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserIdxQuery, int.class, checkUserIdxParams);
    }



}
