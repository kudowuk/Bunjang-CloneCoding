package com.example.demo.src.recent;

import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.src.recent.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RecentDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); }

    // GET 최근 본 상품 조회 API
    public List<GetLookedRes> getLooked(int userIdx) {
        String getLookedQuery = "SELECT L.lookedIdx, P2.status, P2.productName, P2.prices,\n" +
                "       (SELECT PI.imgUrl FROM ProductImg PI JOIN Product on PI.productIdx = P2.productIdx WHERE PI.status = 'Y' ORDER BY PI.productImgIdx LIMIT 1) imgUrl,\n" +
                "       CASE WHEN TIMESTAMPDIFF(SECOND, P2.createdAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(SECOND, P2.createdAt, NOW()), '초 전') WHEN TIMESTAMPDIFF(MINUTE, P2.createdAt, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, P2.createdAt, NOW()), '분 전')\n" +
                "        WHEN TIMESTAMPDIFF(HOUR, P2.createdAt, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, P2.createdAt, NOW()), '시간 전')\n" +
                "        WHEN TIMESTAMPDIFF(DAY, P2.createdAt, NOW()) < 31 THEN CONCAT(TIMESTAMPDIFF(DAY, P2.createdAt, NOW()), '일 전')\n" +
                "        WHEN TIMESTAMPDIFF(MONTH, P2.createdAt, NOW()) < 12 THEN CONCAT(TIMESTAMPDIFF(MONTH, P2.createdAt, NOW()), '달 전')\n" +
                "        ELSE CONCAT(YEAR(TIMEDIFF(NOW(),P2.createdAt)), '년 전') END createdAt\n" +

                "FROM Looked L\n" +
                "    JOIN Product P2 on L.productIdx = P2.productIdx\n" +
                "WHERE L.userIdx = ? AND L.status = 'Y';";
        int getLookedByUserIdxParams = userIdx;

        return this.jdbcTemplate.query(getLookedQuery,
                (rs, rowNum) -> new GetLookedRes(
                        rs.getInt("lookedIdx"),
                        rs.getString("imgUrl"),
                        rs.getString("status"),
                        rs.getString("productName"),
                        rs.getInt("prices"),
                        rs.getString("createdAt")),
                getLookedByUserIdxParams);
    }

    // GET 최근 검색어 조회 API
    public List<GetSearchedRes> getSearched(int userIdx) {
        String getSearchedQuery = "SELECT S.searchedIdx, S.word\n" +
                "FROM Searched S\n" +
                "INNER JOIN Users U on S.userIdx = U.userIdx\n" +
                "WHERE U.userIdx = ? AND S.status = 'Y' ORDER BY S.createdAt DESC;";
        int getSearchedByUserIdxParams = userIdx;

        return this.jdbcTemplate.query(getSearchedQuery,
                (rs, rowNum) -> new GetSearchedRes(
                        rs.getInt("SearchedIdx"),
                        rs.getString("word")),
                getSearchedByUserIdxParams);

    }

    // POST 최근 본 상품 등록 API
    public int createLooked(int userIdx, PostLookedReq postLookedReq){
        String createLookedQuery = "insert into Looked (userIdx, productIdx) VALUES (?,?)";
        Object[] createLookedParams = new Object[]{userIdx, postLookedReq.getProductIdx()};
        this.jdbcTemplate.update(createLookedQuery, createLookedParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    // POST 최근 검색어 등록 API
    public int createSearched(int userIdx, PostSearchedReq postSearchedReq){
        String createSearchedQuery = "insert into Searched (userIdx, word) VALUES (?,?)";
        Object[] createSearchedParams = new Object[]{userIdx, postSearchedReq.getWord()};
        this.jdbcTemplate.update(createSearchedQuery, createSearchedParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    // DELETE 최근 본 상품 삭제 API
    public int deleteLooked(int userIdx, int lookedIdx){
        String deleteLookedQuery = "update Looked set status = 'N' where userIdx = ? and lookedIdx = ?";
        Object[] deleteLookedParams = new Object[]{userIdx, lookedIdx};
        return this.jdbcTemplate.update(deleteLookedQuery, deleteLookedParams);
    }

    // DELETE 최근 검색어 삭제 API
    public int deleteSearched(int userIdx, int searchedIdx){
        String deleteSearchedQuery = "update Searched set status = 'N' where userIdx = ? and searchedIdx = ?";
        Object[] deleteSearchedParams = new Object[]{userIdx, searchedIdx};
        return this.jdbcTemplate.update(deleteSearchedQuery, deleteSearchedParams);

    }

    // 이미 본상품에 들어가있는지 확인
    public int checkDuplicateProductIdx(int productIdx){
        String checkDuplicateProductIdxQuery = "select exists(select productIdx from looked where productIdx = ? AND looked.status = 'Y')";
        int checkDuplicateProductIdxParams = productIdx;
        return this.jdbcTemplate.queryForObject(checkDuplicateProductIdxQuery, int.class, checkDuplicateProductIdxParams);
    }

    // 이미 검색어에 들어가있는지 확인
    public int checkDuplicateWord(int userIdx, String word){
        String checkDuplicateWordQuery = "select exists(select word from Searched where userIdx = ? AND word = ? AND Searched.status = 'Y')";
        Object[] checkDuplicateWordParams = new Object[]{userIdx, word};
        return this.jdbcTemplate.queryForObject(checkDuplicateWordQuery, int.class, checkDuplicateWordParams);
    }

    // 최근 본 상품 인덱스 유무 확인
    public int checkLookedIdx(int lookedIdx){
        String checkLookedIdxQuery = "select exists(select lookedIdx from Looked where lookedIdx = ?)";
        int checkLookedIdxParams = lookedIdx;
        return this.jdbcTemplate.queryForObject(checkLookedIdxQuery, int.class, checkLookedIdxParams);
    }
    // 최근 본 상품에 이미 삭제가 되어있는지 확인(상태가 'N')
    public int checkStatusLookedIdx(int lookedIdx){
        String checkLookedIdxQuery = "select exists(select lookedIdx from Looked where lookedIdx = ? AND Looked.status = 'N')";
        int checkLookedIdxParams = lookedIdx;
        return this.jdbcTemplate.queryForObject(checkLookedIdxQuery, int.class, checkLookedIdxParams);
    }

    // 최근 검색어 인덱스 유무 확인
    public int checkSearchedIdx(int searched){
        String checkSearchedIdxQuery = "select exists(select searchedIdx from Searched where searchedIdx = ?)";
        int checkSearchedIdxParams = searched;
        return this.jdbcTemplate.queryForObject(checkSearchedIdxQuery, int.class, checkSearchedIdxParams);
    }
    // 최근 검색어에 이미 삭제가 되어있는지 확인(상태가 'N')
    public int checkStatusSearchedIdx(int searched){
        String checkSearchedIdxQuery = "select exists(select searched from Searched where searched = ? AND Searched.status = 'N')";
        int checkSearchedIdxParams = searched;
        return this.jdbcTemplate.queryForObject(checkSearchedIdxQuery, int.class, checkSearchedIdxParams);
    }

}
