package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(rollbackFor = BaseException.class)
public class ProductService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;

    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider) {
        this.productDao = productDao;
        this.productProvider = productProvider;
    }

    // POST 카트 담기 등록 API
    public PostProductRes createProduct(int userIdx, PostProductReq postProductReq) throws BaseException {

        try {
            // Insert Product 상품 추가하기
            int productIdx = productDao.createProduct(userIdx, postProductReq);

            // Insert ProductImg 상품 이미지 추가하기
            for (PostProductImgReq postProductImgReq : postProductReq.getImgList()){
                productDao.createProductImg(productIdx, postProductImgReq);
            }

            // Insert ProductTag 상품 태그 추가하기
            for (PostProductTagReq postProductTagReq : postProductReq.getTagList()){
                productDao.createProductTag(productIdx, postProductTagReq);
            }

            PostProductRes postProductRes = new PostProductRes(productIdx);
            return postProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // PATCH 카트 담기 수정 API
    public void modifyProduct(PatchProductReq patchProductReq) throws BaseException {
        try {
            int result = productDao.modifyProduct(patchProductReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
