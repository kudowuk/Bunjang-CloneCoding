package com.example.demo.src.purchase;

import com.example.demo.config.BaseException;

import com.example.demo.src.product.ProductDao;
import com.example.demo.src.purchase.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(rollbackFor = BaseException.class)
public class PurchaseService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PurchaseDao purchaseDao;
    private final PurchaseProvider purchaseProvider;
    private final ProductDao productDao;

    @Autowired
    public PurchaseService(PurchaseDao purchaseDao, PurchaseProvider purchaseProvider, ProductDao productDao) {
        this.purchaseDao = purchaseDao;
        this.purchaseProvider = purchaseProvider;
        this.productDao = productDao;
    }

    // POST 구매 등록
    public PostPurchaseRes createPurchase(int userIdx, PostPurchaseReq postPurchaseReq) throws BaseException {
        try {
            // 상품 유무 확인
            if(productDao.checkProductIdx(postPurchaseReq.getProductIdx()) == 0) {
                throw new BaseException(NOT_EXIST_PRODUCT);
            }
            // 상품 활성화 확인
            if(productDao.checkStatusProductIdx(postPurchaseReq.getProductIdx()) == 1) {
                throw new BaseException(INACTIVE_PRODUCT);
            }
            // 상품 판매완료 또는 예약됨 확인
            if(productDao.checkDoneProductIdx(postPurchaseReq.getProductIdx()) == 1) {
                throw new BaseException(SOLD_OR_BOOKED_PRODUCT);
            }

            int purchaseIdx = purchaseDao.createPurchase(userIdx, postPurchaseReq);

            return new PostPurchaseRes(purchaseIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
