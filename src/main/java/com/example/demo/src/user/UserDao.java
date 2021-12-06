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
                        rs.getString("storeName"),
                        rs.getFloat("avgScore"),
                        rs.getInt("cntLikes"),
                        rs.getInt("cntReviews"),
                        rs.getInt("cntFollowers"),
                        rs.getInt("cntFollowings")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "SELECT userIdx, storeName,\n" +
                "       (SELECT AVG(R.score)\n" +
                "       FROM Review R\n" +
                "       INNER JOIN Purchase Pu on R.userIdx = Pu.userIdx\n" +
                "       INNER JOIN Product Pr on Users.userIdx = Pr.userIdx\n" +
                "       INNER JOIN Pr on Pr.productIdx = R.productIdx\n" +
                "       WHERE Users.userIdx = ? ) AS avgScore\n" +
                "FROM Users\n" +
                "INNER JOIN Likes L on Users.userIdx = L.userIdx\n" +
                "\n" +
                "WHERE userIdx = ;";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("storeName"),
                        rs.getFloat("avgScore"),
                        rs.getInt("cntLikes"),
                        rs.getInt("cntReviews"),
                        rs.getInt("cntFollowers"),
                        rs.getInt("cntFollowings")),
                getUserParams);
    }


    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into Users (email, password, storeName, phone, birthDate, userType) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getStoreName(), postUserReq.getPhone(), postUserReq.getBirthDate(), postUserReq.getUserType()};

        this.jdbcTemplate.update(createUserQuery, createUserParams);

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

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update Users set storeName = ?, password = ?, birthDate = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getStoreName(), patchUserReq.getBirthDate(), patchUserReq.getPassword(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, email, password, storeName, birthDate, userType from Users where email = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("storeName"),
                        rs.getDate("birthDate"),
                        rs.getString("password")
                ),
                getPwdParams
                );

    }



}
