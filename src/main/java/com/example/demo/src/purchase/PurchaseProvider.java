package com.example.demo.src.purchase;

import com.example.demo.config.BaseException;
import com.example.demo.src.purchase.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PurchaseProvider {

    private final PurchaseDao purchaseDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PurchaseProvider(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    // GET 구매 내역 조회 API
    public List<GetPurchaseRes> getPurchased(int userIdx) throws BaseException {
        try {
            List<GetPurchaseRes> getPurchaseRes = purchaseDao.getPurchased(userIdx);
            return getPurchaseRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // GET 판매 내역 조회 API
    public List<GetPurchaseRes> getSold(int userIdx) throws BaseException {
        try {
            List<GetPurchaseRes> getPurchaseRes = purchaseDao.getSold(userIdx);
            return getPurchaseRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
