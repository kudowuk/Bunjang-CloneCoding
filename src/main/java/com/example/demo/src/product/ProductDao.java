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

    // GET 전체 주문 내역 조회 API
    public List<GetMainRes> getMains(int userIdx) {
        String getMainQuery = "SELECT P.productIdx, P.prices, P.productName, A.areaName,\n" +
                "       CASE WHEN TIMESTAMPDIFF(SECOND, P.createdAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(SECOND, P.createdAt, NOW()), '초 전')\n" +
                "           WHEN TIMESTAMPDIFF(MINUTE, P.createdAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, P.createdAt, NOW()), '분 전')\n" +
                "           WHEN TIMESTAMPDIFF(HOUR, P.createdAt, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, P.createdAt, NOW()), '시간 전')\n" +
                "           WHEN TIMESTAMPDIFF(DAY, P.createdAt, NOW()) < 31 THEN CONCAT(TIMESTAMPDIFF(DAY, P.createdAt, NOW()), '일 전')\n" +
                "           WHEN TIMESTAMPDIFF(MONTH, P.createdAt, NOW()) < 12 THEN CONCAT(TIMESTAMPDIFF(MONTH, P.createdAt, NOW()), '달 전')\n" +
                "           ELSE CONCAT(YEAR(TIMEDIFF(NOW(),P.createdAt)), '년 전') END createdAt,\n" +
                "       P.safePayment, P.conditions,\n" +
                "       (SELECT count(L.likeIdx) FROM Likes L WHERE P.productIdx = L.productIdx) cntLikes,\n" +
                "       P.freeShipping, P.negotiable, P.changes, P.quantity, P.content, S.subcategoryName, U.storeName,\n" +
                "       (SELECT COUNT(F.followIdx) FROM Follow F WHERE P.userIdx = F.followingUserIdx) cntFollowers,\n" +
                "       (SELECT AVG(score) FROM Review R JOIN Purchase P2 on R.purchaseIdx = P2.purchaseIdx WHERE P.productIdx = P2.productIdx) avgScores,\n" +
                "       (SELECT L2.status FROM Likes L2 WHERE P.productIdx = L2.productIdx AND L2.userIdx = ?) statusLike\n" +
                "FROM Product P\n" +
                "LEFT JOIN Area A on P.areaIdx = A.areaIdx\n" +
                "INNER JOIN Subcategory S on P.subcategoryIdx = S.subcategoryIdx\n" +
                "INNER JOIN Users U on P.userIdx = U.userIdx\n" +
                "WHERE P.status = 'ACTIVE' AND S.status = 'Y';";
        int getUserParams = userIdx;

        List<GetMainRes> result = new ArrayList<>();

        List<MainVo> productArray = this.jdbcTemplate.query(getMainQuery,
                (rs, rowNum) -> new MainVo(
                        rs.getInt("productIdx"),
                        rs.getInt("prices"),
                        rs.getString("productName"),
                        rs.getString("areaName"),
                        rs.getString("createdAt"),
                        rs.getString("safePayment"),
                        rs.getString("conditions"),
                        rs.getInt("cntLikes"),
                        rs.getString("freeShipping"),
                        rs.getString("negotiable"),
                        rs.getString("changes"),
                        rs.getInt("quantity"),
                        rs.getString("content"),
                        rs.getString("subcategoryName"),
                        rs.getString("storeName"),
                        rs.getInt("cntFollowers"),
                        rs.getFloat("avgScores"),
                        rs.getString("statusLike")),
                getUserParams);

        for (MainVo mainVO : productArray) {
            String imgSql = "SELECT PI.productImgIdx, PI.productIdx, PI.imgUrl " +
                    "FROM ProductImg PI " +
                    "INNER JOIN Product P on P.productIdx = PI.productIdx " +
                    "WHERE PI.productIdx = ? AND P.status ='ACTIVE' AND PI.status = 'Y'";

            List<ProductImg> imgList= this.jdbcTemplate.query(imgSql,
                    (rs, rowNum) -> new ProductImg(
                            rs.getInt("productImgIdx"),
                            rs.getInt("productIdx"),
                            rs.getString("imgUrl")),
                    mainVO.getProductIdx());

            String tagSql = "SELECT PT.productTagIdx, PT.productIdx, PT.tagName " +
                    "FROM ProductTag PT " +
                    "INNER JOIN Product P on P.productIdx = PT.productIdx " +
                    "WHERE PT.productIdx =? AND P.status = 'ACTIVE' AND PT.status = 'Y'";

            List<ProductTag> tagList = this.jdbcTemplate.query(tagSql,
                    (rs, rowNum) -> new ProductTag(
                            rs.getInt("productTagIdx"),
                            rs.getInt("productIdx"),
                            rs.getString("tagName")),
                    mainVO.getProductIdx());

            GetMainRes getMainRes = new GetMainRes(mainVO.getProductIdx(), mainVO.getPrices(), mainVO.getProductName(), mainVO.getAreaName(), mainVO.getCreatedAt(), mainVO.getSafePayment(), mainVO.getConditions(), mainVO.getCntLikes(), mainVO.getFreeShipping(), mainVO.getNegotiable(), mainVO.getChanges(), mainVO.getQuantity(), mainVO.getContent(), mainVO.getSubcategoryName(), mainVO.getStoreName(), mainVO.getCntFollowers(), mainVO.getAvgScores(), mainVO.getStatusLike(), imgList, tagList);

            result.add(getMainRes);

        }

        return result;

    }


    public GetProductRes getProduct(int userIdx, int productIdx) {
        String getProductQuery = "SELECT P.productIdx, P.prices, P.productName, A.areaName,\n" +
                "       CASE WHEN TIMESTAMPDIFF(SECOND, P.createdAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(SECOND, P.createdAt, NOW()), '초 전') WHEN TIMESTAMPDIFF(MINUTE, P.createdAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, P.createdAt, NOW()), '분 전')\n" +
                "           WHEN TIMESTAMPDIFF(HOUR, P.createdAt, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, P.createdAt, NOW()), '시간 전')\n" +
                "           WHEN TIMESTAMPDIFF(DAY, P.createdAt, NOW()) < 31 THEN CONCAT(TIMESTAMPDIFF(DAY, P.createdAt, NOW()), '일 전')\n" +
                "           WHEN TIMESTAMPDIFF(MONTH, P.createdAt, NOW()) < 12 THEN CONCAT(TIMESTAMPDIFF(MONTH, P.createdAt, NOW()), '달 전')\n" +
                "           ELSE CONCAT(YEAR(TIMEDIFF(NOW(),P.createdAt)), '년 전') END createdAt,\n" +
                "       P.safePayment,\n" +
                "       (SELECT count(L.likeIdx) FROM Likes L WHERE P.productIdx = L.productIdx) cntLikes,\n" +
                "       P.conditions, P.freeShipping, P.negotiable, P.changes, P.quantity, P.content, S.subcategoryName, U.storeName,\n" +
                "       (SELECT COUNT(F.followIdx) FROM Follow F WHERE P.userIdx = F.followingUserIdx) cntFollowers,\n" +
                "       (SELECT AVG(score) FROM Review R JOIN Purchase P2 on R.purchaseIdx = P2.purchaseIdx WHERE P.productIdx = P2.productIdx) avgScores,\n" +
                "       (SELECT L2.status FROM Likes L2 WHERE P.productIdx = L2.productIdx AND L2.userIdx = ?) statusLike\n" +
                "FROM Product P\n" +
                "LEFT JOIN Area A on P.areaIdx = A.areaIdx\n" +
                "INNER JOIN Subcategory S on P.subcategoryIdx = S.subcategoryIdx\n" +
                "INNER JOIN Users U on P.userIdx = U.userIdx\n" +
                "WHERE P.productIdx = ? AND S.status = 'Y' AND NOT P.status = 'INACTIVE';";
        int getUserParams = userIdx;
        int getProductParams = productIdx;

        // 리스트가 아닌 객체로 받아오기 아래 것 주석처리
        // List<GetProductRes> result = new ArrayList<>();
        
        ProductVo product = this.jdbcTemplate.queryForObject(getProductQuery,
                (rs, rowNum) -> new ProductVo(
                        rs.getInt("productIdx"),
                        rs.getInt("prices"),
                        rs.getString("productName"),
                        rs.getString("areaName"),
                        rs.getString("createdAt"),
                        rs.getString("safePayment"),
                        rs.getInt("cntLikes"),
                        rs.getString("conditions"),
                        rs.getString("freeShipping"),
                        rs.getString("negotiable"),
                        rs.getString("changes"),
                        rs.getInt("quantity"),
                        rs.getString("content"),
                        rs.getString("subcategoryName"),
                        rs.getString("storeName"),
                        rs.getInt("cntFollowers"),
                        rs.getFloat("avgScores"),
                        rs.getString("statusLike")),
                getUserParams, getProductParams);

        String imgSql = "SELECT PI.productImgIdx, PI.productIdx, PI.imgUrl " +
                "FROM ProductImg PI " +
                "INNER JOIN Product P on P.productIdx = PI.productIdx " +
                "WHERE PI.productIdx = ? AND PI.status = 'Y' AND NOT P.status = 'INACTIVE'";

        List<ProductImg> imgList = this.jdbcTemplate.query(imgSql,
                (rs, rowNum) -> new ProductImg(
                        rs.getInt("productImgIdx"),
                        rs.getInt("productIdx"),
                        rs.getString("imgUrl")),
                product.getProductIdx());

        String tagSql = "SELECT PT.productTagIdx, PT.productIdx, PT.tagName " +
                "FROM ProductTag PT " +
                "INNER JOIN Product P on P.productIdx = PT.productIdx " +
                "WHERE PT.productIdx =? AND PT.status = 'Y' AND NOT P.status = 'INACTIVE'";

        List<ProductTag> tagList = this.jdbcTemplate.query(tagSql,
                (rs, rowNum) -> new ProductTag(
                        rs.getInt("productTagIdx"),
                        rs.getInt("productIdx"),
                        rs.getString("tagName")),
                product.getProductIdx());

        GetProductRes getProductRes = new GetProductRes(product.getProductIdx(), product.getPrices(), product.getProductName(), product.getAreaName(), product.getCreatedAt(), product.getSafePayment(), product.getCntLikes(), product.getConditions(), product.getFreeShipping(), product.getNegotiable(), product.getChanges(), product.getQuantity(), product.getContent(), product.getSubcategoryName(), product.getStoreName(), product.getCntFollowers(), product.getAvgScores(), product.getStatusLike(), imgList, tagList);

        // 리스트가 아닌 객체로 받아오기 아래 것 주석처리
        // result.add(getProductRes);
        
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
    // POST 상품 이미지 등록 API
    public int createProductImg(int productIdx, PostProductImgReq postProductImgReq){
        String createProductImgQuery = "insert into ProductImg (productIdx, imgUrl) VALUES (?,?)";
        Object[] createProductImgParams = new Object[]{productIdx, postProductImgReq.getImgUrl()};
        this.jdbcTemplate.update(createProductImgQuery, createProductImgParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }
    // POST 상품 이미지 태그 API
    public int createProductTag(int productIdx, PostProductTagReq postProductTagReq){
        String createProductTagQuery = "insert into ProductTag (productIdx, tagName) VALUES (?,?)";
        Object[] createProductTagParams = new Object[]{productIdx, postProductTagReq.getTagName()};
        this.jdbcTemplate.update(createProductTagQuery, createProductTagParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    // PATCH 상품 수정 API
    public int modifyProduct(PatchProductReq patchProductReq){
        String modifyProductQuery = "update Product set productName = ?, subcategoryIdx = ?, content = ?, prices = ?, freeShipping = ?, areaIdx = ?, quantity = ?, conditions = ?, changes = ? where productIdx = ? AND userIdx = ?";
        Object[] modifyProductParams = new Object[]{patchProductReq.getProductName(), patchProductReq.getSubcategoryIdx(), patchProductReq.getContent(), patchProductReq.getPrices(), patchProductReq.getFreeShipping(), patchProductReq.getAreaIdx(), patchProductReq.getQuantity(), patchProductReq.getConditions(), patchProductReq.getChanges(), patchProductReq.getUserIdx(), patchProductReq.getProductIdx()};

        return this.jdbcTemplate.update(modifyProductQuery,modifyProductParams);
    }

    // 서브카테고리 유무 확인
    public int checkSubcategoryIdx(int subcategoryIdx){
        String checkSubcategoryIdxQuery = "select exists(select subcategoryIdx from Subcategory where subcategoryIdx = ?)";
        int checkSubcategoryIdxParams = subcategoryIdx;
        return this.jdbcTemplate.queryForObject(checkSubcategoryIdxQuery, int.class, checkSubcategoryIdxParams);
    }
    // 서브카테고리 활성화 확인
    public int checkStatusSubcategoryIdx(int subcategoryIdx){
        String checkSubcategoryIdxQuery = "select exists(select subcategoryIdx from Subcategory where subcategoryIdx = ? AND Subcategory.status = 'N')";
        int checkStatusSubcategoryIdxParams = subcategoryIdx;
        return this.jdbcTemplate.queryForObject(checkSubcategoryIdxQuery, int.class, checkStatusSubcategoryIdxParams);
    }

    // 거래지역 유무 확인
    public int checkAreaIdx(int areaIdx){
        String checkAreaIdxQuery = "select exists(select areaIdx from Area where areaIdx = ?)";
        int checkAreaIdxParams = areaIdx;
        return this.jdbcTemplate.queryForObject(checkAreaIdxQuery, int.class, checkAreaIdxParams);
    }
    // 거래지역 활성화 확인
    public int checkStatusAreaIdx(int areaIdx){
        String checkAreaIdxQuery = "select exists(select areaIdx from Area where areaIdx = ? AND Area.status = 'N')";
        int checkStatusAreaIdxParams = areaIdx;
        return this.jdbcTemplate.queryForObject(checkAreaIdxQuery, int.class, checkStatusAreaIdxParams);
    }

    // 상품 유무 확인
    public int checkProductIdx(int productIdx){
        String checkProductIdxQuery = "select exists(select productIdx from Product where productIdx = ?)";
        int checkProductIdxParams = productIdx;
        return this.jdbcTemplate.queryForObject(checkProductIdxQuery, int.class, checkProductIdxParams);
    }
    // 상품 활성화 확인
    public int checkStatusProductIdx(int productIdx){
        String checkProductIdxQuery = "select exists(select productIdx from Product where productIdx = ? AND Product.status = 'INACTIVE')";
        int checkStatusProductIdxParams = productIdx;
        return this.jdbcTemplate.queryForObject(checkProductIdxQuery, int.class, checkStatusProductIdxParams);
    }
    // 상품 판매완료 또는 예약됨 확인
    public int checkDoneProductIdx(int productIdx){
        String checkDoneProductIdxQuery = "select exists(select productIdx from Product where productIdx = ? AND Product.status = 'SOLD' || Product.status = 'BOOKED')";
        int checkStatusProductIdxParams = productIdx;
        return this.jdbcTemplate.queryForObject(checkDoneProductIdxQuery, int.class, checkStatusProductIdxParams);
    }



}
