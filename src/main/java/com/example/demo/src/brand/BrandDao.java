package com.example.demo.src.brand;

import com.example.demo.src.brand.model.GetBrandRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BrandDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); }

    // GET 전체 브랜드 리스트 조회 API
    public List<GetBrandRes> getBrands(int userIdx) {
        String getBrandQuery = "SELECT B.brandIdx, B.brandKorean, B.brandEnglish, B.imgUrl,\n" +
                "       (SELECT COUNT(followIdx) FROM Follow F WHERE B.brandIdx = F.brandIdx) cntFollowers,\n" +
                "       (SELECT F2.status FROM Follow F2 WHERE B.brandIdx = F2.brandIdx AND F2.userIdx = ?) statusFollow\n" +
                "FROM Brand B\n" +
                "LEFT JOIN Follow F on B.brandIdx = F.brandIdx\n" +
                "WHERE B.status = 'Y';";
        int getBrandsByUserIdxParams = userIdx;

        return this.jdbcTemplate.query(getBrandQuery,
                (rs, rowNum) -> new GetBrandRes(
                        rs.getInt("brandIdx"),
                        rs.getString("brandKorean"),
                        rs.getString("brandEnglish"),
                        rs.getString("imgUrl"),
                        rs.getInt("cntFollowers"),
                        rs.getString("statusFollow")),
                getBrandsByUserIdxParams);
    }

}
