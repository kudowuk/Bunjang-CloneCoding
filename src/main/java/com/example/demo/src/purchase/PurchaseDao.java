package com.example.demo.src.purchase;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.model.GetFollowerRes;
import com.example.demo.src.purchase.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Repository
public class PurchaseDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); }

    // GET 구매 내역 조회 API
    public List<GetPurchaseRes> getPurchased(int userIdx) {
        String getPurchasQuery = "SELECT F.followIdx, (SELECT ST.profiles FROM Store ST WHERE F.userIdx = ST.userIdx) profiles ,\n" +
                "       (SELECT U2.storeName FROM Users U2 WHERE F.userIdx = U2.userIdx) storeName,\n" +
                "       (SELECT COUNT(P.productIdx) FROM Product P WHERE F.userIdx = P.userIdx) cntProducts,\n" +
                "       (SELECT COUNT(F2.followIdx) FROM Follow F2 WHERE F.userIdx = F2.followingUserIdx) cntFollowers\n" +
                "FROM Follow F\n" +
                "WHERE F.followingUserIdx = ? AND F.status = 'Y' ORDER BY F.createdAt DESC;";
        int getPurchasByUserIdxParams = userIdx;

        return this.jdbcTemplate.query(getPurchasQuery,
                (rs, rowNum) -> new GetPurchaseRes(
                        rs.getInt("purchaseIdx"),
                        rs.getString("imgUrl"),
                        rs.getString("status"),
                        rs.getString("productName"),
                        rs.getInt("prices"),
                        rs.getString("storeName"),
                        rs.getString("createdAt")),
                getPurchasByUserIdxParams);
    }

    // GET 판매 내역 조회 API
    public List<GetPurchaseRes> getSold(int userIdx) {
        String getPurchasQuery = "SELECT F.followIdx, (SELECT ST.profiles FROM Store ST WHERE F.userIdx = ST.userIdx) profiles ,\n" +
                "       (SELECT U2.storeName FROM Users U2 WHERE F.userIdx = U2.userIdx) storeName,\n" +
                "       (SELECT COUNT(P.productIdx) FROM Product P WHERE F.userIdx = P.userIdx) cntProducts,\n" +
                "       (SELECT COUNT(F2.followIdx) FROM Follow F2 WHERE F.userIdx = F2.followingUserIdx) cntFollowers\n" +
                "FROM Follow F\n" +
                "WHERE F.followingUserIdx = ? AND F.status = 'Y' ORDER BY F.createdAt DESC;";
        int getPurchasByUserIdxParams = userIdx;

        return this.jdbcTemplate.query(getPurchasQuery,
                (rs, rowNum) -> new GetPurchaseRes(
                        rs.getInt("purchaseIdx"),
                        rs.getString("imgUrl"),
                        rs.getString("status"),
                        rs.getString("productName"),
                        rs.getInt("prices"),
                        rs.getString("storeName"),
                        rs.getString("createdAt")),
                getPurchasByUserIdxParams);
    }










    // POST 리뷰 등록 API
    public int createPurchase(int userIdx, PostPurchaseReq postPurchaseReq){
        String createPurchaseQuery = "insert into Purchase (userIdx, productIdx, addressIdx, requestMsg, purchaseType, points) VALUES (?,?,?,?,?,?)";
        Object[] createPurchaseParams = new Object[]{userIdx, postPurchaseReq.getProductIdx(), postPurchaseReq.getAddressIdx(), postPurchaseReq.getRequestMsg(), postPurchaseReq.getPurchaseType(), postPurchaseReq.getPoints() };
        this.jdbcTemplate.update(createPurchaseQuery, createPurchaseParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }
}
