package com.example.demo.src.purchase;

import com.example.demo.config.BaseException;
import com.example.demo.src.purchase.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Repository
public class PurchaseDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); }

    // POST 리뷰 등록 API
    public int createPurchase(int userIdx, PostPurchaseReq postPurchaseReq){
        String createPurchaseQuery = "insert into Purchase (userIdx, productIdx, addressIdx, requestMsg, purchaseType, points) VALUES (?,?,?,?,?,?)";
        Object[] createPurchaseParams = new Object[]{userIdx, postPurchaseReq.getProductIdx(), postPurchaseReq.getAddressIdx(), postPurchaseReq.getRequestMsg(), postPurchaseReq.getPurchaseType(), postPurchaseReq.getPoints() };
        this.jdbcTemplate.update(createPurchaseQuery, createPurchaseParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }
}
