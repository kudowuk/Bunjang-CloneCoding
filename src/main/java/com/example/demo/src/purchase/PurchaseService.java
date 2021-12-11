package com.example.demo.src.purchase;

import com.example.demo.config.BaseException;

import com.example.demo.src.purchase.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(rollbackFor = BaseException.class)
public class PurchaseService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PurchaseDao purchaseDao;
    private final PurchaseProvider purchaseProvider;

    @Autowired
    public PurchaseService(PurchaseDao purchaseDao, PurchaseProvider purchaseProvider) {
        this.purchaseDao = purchaseDao;
        this.purchaseProvider = purchaseProvider;
    }

    // POST 구매 등록
    public PostPurchaseRes createPurchase(int userIdx, PostPurchaseReq postPurchaseReq) throws BaseException {
        int purchaseIdx = purchaseDao.createPurchase(userIdx, postPurchaseReq);
        return new PostPurchaseRes(purchaseIdx);

        //        try {
//            int purchaseIdx = purchaseDao.createPurchase(userIdx, postPurchaseReq);
//            return new PostPurchaseRes(purchaseIdx);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
    }


}
