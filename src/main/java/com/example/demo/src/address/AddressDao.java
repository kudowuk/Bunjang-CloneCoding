package com.example.demo.src.address;

import com.example.demo.src.address.model.GetAddressRes;
import com.example.demo.src.address.model.PatchAddressReq;
import com.example.demo.src.address.model.PostAddressReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AddressDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource); }

    public List<GetAddressRes> getAddresses(int userIdx) {
        String getAddressQuery = "SELECT A.addressIdx, A.recipient, A.phone, A.latitude, A.longitude, A.roadName, A.detailedAddress, A.requestMsg, A.status\n" +
                "FROM Address A\n" +
                "LEFT JOIN Users U on A.userIdx = U.userIdx\n" +
                "WHERE U.userIdx = ? AND A.status = 'Y' OR A.status = 'M';";
        int getAddressesByUserIdxParams = userIdx;
        return this.jdbcTemplate.query(getAddressQuery,
                (rs, rowNum) -> new GetAddressRes(
                        rs.getInt("addressIdx"),
                        rs.getString("recipient"),
                        rs.getString("phone"),
                        rs.getString("latitude"),
                        rs.getString("longitude"),
                        rs.getString("roadName"),
                        rs.getString("detailedAddress"),
                        rs.getString("requestMsg"),
                        rs.getString("status")),
                getAddressesByUserIdxParams);
    }

    // 유저 특정 주소 조회 API
    public GetAddressRes getAddress(int userIdx, int addressIdx) {
        String getAddressQuery = "SELECT A.addressIdx, A.recipient, A.phone, A.latitude, A.longitude, A.roadName, A.detailedAddress, A.requestMsg, A.status\n" +
                "FROM Address A\n" +
                "LEFT JOIN Users U on A.userIdx = U.userIdx\n" +
                "WHERE A.addressIdx = ? AND U.userIdx = ? AND (A.status = 'Y' OR A.status = 'M');";
        int getAddressByAddressIdxParams = addressIdx;
        int getAddressByUserIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(getAddressQuery,
                (rs, rowNum) -> new GetAddressRes(
                        rs.getInt("addressIdx"),
                        rs.getString("recipient"),
                        rs.getString("phone"),
                        rs.getString("latitude"),
                        rs.getString("longitude"),
                        rs.getString("roadName"),
                        rs.getString("detailedAddress"),
                        rs.getString("requestMsg"),
                        rs.getString("status")),
                getAddressByAddressIdxParams, getAddressByUserIdxParams);
    }

    // 유저 주소 등록 API
    public int createAddress(int userIdx, PostAddressReq postAddressReq){
        String createAddressQuery = "insert into Address (userIdx, recipient, phone, latitude, longitude, roadName, detailedAddress, requestMsg, status) VALUES (?,?,?,?,?,?,?,?,?)";
        Object[] createAddressParams = new Object[]{userIdx, postAddressReq.getRecipient(), postAddressReq.getPhone(), postAddressReq.getLatitude(), postAddressReq.getLongitude(), postAddressReq.getRoadName(), postAddressReq.getDetailedAddress(), postAddressReq.getRequestMsg(), postAddressReq.getStatus() };
        this.jdbcTemplate.update(createAddressQuery, createAddressParams);

        String lastInsertAdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertAdQuery,int.class);
    }

    // PATCH 주소 수정 API
    public int modifyAddress(PatchAddressReq patchAddressReq){
        String modifyAddressQuery = "update Address set recipient = ?, phone = ?, latitude = ?, longitude = ?, roadName = ?, detailedAddress = ?, requestMsg = ?, status = ? where addressIdx = ? AND userIdx = ?";
        Object[] modifyAddressParams = new Object[]{patchAddressReq.getRecipient(), patchAddressReq.getPhone(), patchAddressReq.getLatitude(), patchAddressReq.getLongitude(), patchAddressReq.getRoadName(), patchAddressReq.getDetailedAddress(), patchAddressReq.getRequestMsg(), patchAddressReq.getStatus(), patchAddressReq.getUserIdx(), patchAddressReq.getAddressIdx()};

        return this.jdbcTemplate.update(modifyAddressQuery,modifyAddressParams);
    }


}
