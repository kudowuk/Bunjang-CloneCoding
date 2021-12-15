package com.example.demo.src.category;

import com.example.demo.src.category.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); }

    public List<GetCategoryRes> getCategories() {
        String getCategoryQuery = "SELECT C.categoryIdx, C.categoryName\n" +
                "FROM Category C\n" +
                "WHERE C.status = 'Y';";

        List<GetCategoryRes> result = new ArrayList<>();

        List<CategoryVo> categoryList = this.jdbcTemplate.query(getCategoryQuery,
                (rs, rowNum) -> new CategoryVo(
                        rs.getInt("categoryIdx"),
                        rs.getString("categoryName")));

        for (CategoryVo categoryVo : categoryList) {
            String subcategorySql = "SELECT S.subcategoryIdx, S.subcategoryName, S.imgUrl\n" +
                    "FROM Subcategory S\n" +
                    "INNER JOIN Category C on S.categoryIdx = C.categoryIdx\n" +
                    "WHERE C.categoryIdx = ? AND C.status = 'Y' AND S.status = 'Y';";

            List<SubcategoryVo> subcategoryList = this.jdbcTemplate.query(subcategorySql,
                    (rs, rowNum) -> new SubcategoryVo(
                            rs.getInt("subcategoryIdx"),
                            rs.getString("subcategoryName"),
                            rs.getString("imgUrl")),
                    categoryVo.getCategoryIdx());

            GetCategoryRes getCategoryRes = new GetCategoryRes(categoryVo.getCategoryIdx(), categoryVo.getCategoryName(), subcategoryList);

            result.add(getCategoryRes);

        }

        return result;
    }



    // GET 하위 범주 상품 조회API
    public GetSubcategoryRes getSubcategory(int userIdx, int subcategoryIdx) {
        String getSubcategoryQuery = "SELECT S.subcategoryIdx, S.subcategoryName, S.imgUrl\n" +
                "FROM Subcategory S\n" +
                "INNER JOIN Category C on S.categoryIdx = C.categoryIdx\n"+
                "WHERE subcategoryIdx = ?;";
        int getSubcategoryParams = subcategoryIdx;
        int getUserParams = userIdx;

        List<GetSubcategoryRes> result = new ArrayList<>();

        SubcategoryVo subcategory = this.jdbcTemplate.queryForObject(getSubcategoryQuery,
                (rs, rowNum) -> new SubcategoryVo(
                        rs.getInt("subcategoryIdx"),
                        rs.getString("subcategoryName"),
                        rs.getString("imgUrl")),
                getSubcategoryParams);

        String goodsSql = "SELECT P.productIdx, P.prices, P.productName, A.areaName,\n" +
                "       CASE WHEN TIMESTAMPDIFF(SECOND, P.createdAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(SECOND, P.createdAt, NOW()), '초 전') WHEN TIMESTAMPDIFF(MINUTE, P.createdAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, P.createdAt, NOW()), '분 전')\n" +
                "           WHEN TIMESTAMPDIFF(HOUR, P.createdAt, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, P.createdAt, NOW()), '시간 전')\n" +
                "           WHEN TIMESTAMPDIFF(DAY, P.createdAt, NOW()) < 31 THEN CONCAT(TIMESTAMPDIFF(DAY, P.createdAt, NOW()), '일 전')\n" +
                "           WHEN TIMESTAMPDIFF(MONTH, P.createdAt, NOW()) < 12 THEN CONCAT(TIMESTAMPDIFF(MONTH, P.createdAt, NOW()), '달 전')\n" +
                "           ELSE CONCAT(YEAR(TIMEDIFF(NOW(),P.createdAt)), '년 전') END createdAt,\n" +
                "       P.safePayment,\n" +
                "       (SELECT count(L.likeIdx) FROM Likes L WHERE P.productIdx = L.productIdx) cntLikes,\n" +
                "       (SELECT imgUrl FROM ProductImg PI INNER JOIN Product on PI.productIdx = P.productIdx ORDER BY PI.productImgIdx LIMIT 1) imgUrl,\n" +
                "       (SELECT L2.status FROM Likes L2 WHERE P.productIdx = L2.productIdx AND L2.userIdx = ?) statusLike\n" +
                "FROM Product P\n" +
                "LEFT JOIN Area A on P.areaIdx = A.areaIdx\n" +
                "INNER JOIN Subcategory S on P.subcategoryIdx = S.subcategoryIdx\n" +
                "WHERE P.subcategoryIdx = ? AND P.status = 'ACTIVE';";

        List<Goods> goodsList = this.jdbcTemplate.query(goodsSql,
                (rs, rowNum) -> new Goods(
                        rs.getInt("productIdx"),
                        rs.getString("imgUrl"),
                        rs.getInt("prices"),
                        rs.getString("productName"),
                        rs.getString("areaName"),
                        rs.getString("createdAt"),
                        rs.getString("safePayment"),
                        rs.getInt("cntLikes"),
                        rs.getString("statusLike")),
                getUserParams ,subcategory.getSubcategoryIdx());


        GetSubcategoryRes getSubcategoryRes = new GetSubcategoryRes(subcategory.getSubcategoryIdx(), subcategory.getSubcategoryName(), subcategory.getImgUrl(), goodsList);

        result.add(getSubcategoryRes);

        return getSubcategoryRes;
    }



}
