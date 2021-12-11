package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); }

    public GetProductRes getProduct(int productIdx) {
        String getProductQuery = "SELECT P.productIdx, P.prices, P.productName, A.areaName, P.conditions, P.freeShipping, P.negotiable, P.changes, P.quantity, P.content, S.subcategoryName, U.storeName\n" +
                "FROM Product P\n" +
                "INNER JOIN Subcategory S on P.subcategoryIdx = S.subcategoryIdx\n" +
                "INNER JOIN Area A on A.areaIdx = P.areaIdx\n" +
                "INNER JOIN Users U on P.userIdx = U.userIdx\n" +
                "WHERE P.productIdx = ?;";
        int getProductParams = productIdx;

        List<GetProductRes> result = new ArrayList<>();

        ProductVo product = this.jdbcTemplate.queryForObject(getProductQuery,
                (rs, rowNum) -> new ProductVo(
                        rs.getInt("productIdx"),
                        rs.getInt("prices"),
                        rs.getString("productName"),
                        rs.getString("areaName"),
                        rs.getString("conditions"),
                        rs.getString("freeShipping"),
                        rs.getString("negotiable"),
                        rs.getString("changes"),
                        rs.getInt("quantity"),
                        rs.getString("content"),
                        rs.getString("subcategoryName"),
                        rs.getString("storeName")),
                getProductParams);

        String imgSql = "SELECT PI.productImgIdx, PI.productIdx, PI.imgUrl " +
                "FROM ProductImg PI " +
                "INNER JOIN Product P on P.productIdx = PI.productIdx " +
                "WHERE PI.productIdx = ? AND P.status ='Y' AND PI.status = 'Y'";

        List<ProductImg> imgList = this.jdbcTemplate.query(imgSql,
                (rs, rowNum) -> new ProductImg(
                        rs.getInt("productImgIdx"),
                        rs.getInt("productIdx"),
                        rs.getString("imgUrl")),
                product.getProductIdx());

        String tagSql = "SELECT PT.productTagIdx, PT.productIdx, PT.tagName " +
                "FROM ProductTag PT " +
                "INNER JOIN Product P on P.productIdx = PT.productIdx " +
                "WHERE PT.productIdx =? AND P.status = 'Y' AND PT.status = 'Y'";

        List<ProductTag> tagList = this.jdbcTemplate.query(tagSql,
                (rs, rowNum) -> new ProductTag(
                        rs.getInt("productTagIdx"),
                        rs.getInt("productIdx"),
                        rs.getString("tagName")),
                product.getProductIdx());

        GetProductRes getProductRes = new GetProductRes(product.getProductIdx(), product.getPrices(), product.getProductName(), product.getAreaName(), product.getConditions(), product.getFreeShipping(), product.getNegotiable(), product.getChanges(), product.getQuantity(), product.getContent(), product.getSubcategoryName(), product.getStoreName(), imgList, tagList);

        result.add(getProductRes);

        return getProductRes;
    }


    // POST 상품 등록 API
    public int createProduct(int userIdx, PostProductReq postProductReq){
        String createProductQuery = "insert into Product (userIdx, productName, subcategoryIdx, content, prices, freeShipping, negotiable, areaIdx, quantity, conditions, changes) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createProductParams = new Object[]{userIdx, postProductReq.getProductName(), postProductReq.getSubcategoryIdx(), postProductReq.getContent(), postProductReq.getPrices(), postProductReq.getFreeShipping(), postProductReq.getNegotiable(), postProductReq.getAreaIdx(), postProductReq.getQuantity(), postProductReq.getConditions(), postProductReq.getChanges()};
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    public int createProductImg(int productIdx, PostProductImgReq postProductImgReq){
        String createProductImgQuery = "insert into ProductImg (productIdx, imgUrl) VALUES (?,?)";
        Object[] createProductImgParams = new Object[]{productIdx, postProductImgReq.getImgUrl()};
        this.jdbcTemplate.update(createProductImgQuery, createProductImgParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    public int createProductTag(int productIdx, PostProductTagReq postProductTagReq){
        String createProductTagQuery = "insert into ProductTag (productIdx, tagName) VALUES (?,?)";
        Object[] createProductTagParams = new Object[]{productIdx, postProductTagReq.getTagName()};
        this.jdbcTemplate.update(createProductTagQuery, createProductTagParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    // PATCH 카트 담기 수정 API
    public int modifyProduct(PatchProductReq patchProductReq){
        String modifyProductQuery = "update Product set productName = ?, subcategoryIdx = ?, content = ?, prices = ?, freeShipping = ?, areaIdx = ?, quantity = ?, conditions = ?, changes = ? where productIdx = ? AND userIdx = ?";
        Object[] modifyProductParams = new Object[]{patchProductReq.getProductName(), patchProductReq.getSubcategoryIdx(), patchProductReq.getContent(), patchProductReq.getPrices(), patchProductReq.getFreeShipping(), patchProductReq.getAreaIdx(), patchProductReq.getQuantity(), patchProductReq.getConditions(), patchProductReq.getChanges(), patchProductReq.getUserIdx(), patchProductReq.getProductIdx()};

        return this.jdbcTemplate.update(modifyProductQuery,modifyProductParams);
    }

}
