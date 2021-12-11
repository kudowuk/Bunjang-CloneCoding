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
    public GetSubcategoryRes getSubcategory(int subcategoryIdx) {
        String getSubcategoryQuery = "SELECT S.subcategoryIdx, S.subcategoryName, S.imgUrl\n" +
                "FROM Subcategory S\n" +
                "INNER JOIN Category C on S.categoryIdx = C.categoryIdx\n" +
                "WHERE subcategoryIdx = ?;";
        int getSubcategoryParams = subcategoryIdx;
//        int getSubcategoryByUserIdxParams = userIdx;

        List<GetSubcategoryRes> result = new ArrayList<>();

        SubcategoryVo subcategory = this.jdbcTemplate.queryForObject(getSubcategoryQuery,
                (rs, rowNum) -> new SubcategoryVo(
                        rs.getInt("subcategoryIdx"),
                        rs.getString("subcategoryName"),
                        rs.getString("imgUrl")),
                getSubcategoryParams);

        String goodsSql = "SELECT P.productIdx, P.prices, P.productName, PI2.imgUrl\n" +
                "FROM Product P, (SELECT PI.imgUrl FROM ProductImg PI INNER JOIN Product on PI.productIdx = P.productIdx INNER JOIN Subcategory S on P.subcategoryIdx = S.subcategoryIdx WHERE S.subcategoryIdx = ? AND PI.status = 'Y' ORDER BY PI.productImgIdx LIMIT 1) img\n" +
                "INNER JOIN ProductImg PI2 on P12.productIdx = P.productIdx\n" +
                "WHERE P.subcategoryIdx = ? AND P.status = 'Y' ORDER BY P.updatedAt;";

        List<Goods> goodsList = this.jdbcTemplate.query(goodsSql,
                (rs, rowNum) -> new Goods(
                        rs.getInt("productIdx"),
                        rs.getString("imgUrl"),
                        rs.getInt("prices"),
                        rs.getString("productName")),
                subcategory.getSubcategoryIdx());

        GetSubcategoryRes getSubcategoryRes = new GetSubcategoryRes(subcategory.getSubcategoryIdx(), subcategory.getSubcategoryName(), subcategory.getImgUrl(), goodsList);

        result.add(getSubcategoryRes);

        return getSubcategoryRes;
    }


}
