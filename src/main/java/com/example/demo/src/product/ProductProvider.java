package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.src.product.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;


@Service
public class ProductProvider {

    private final ProductDao productDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao) {
        this.productDao = productDao;
    }


    public List<GetMainRes> getMains(int userIdx) throws BaseException {
        try{
            List<GetMainRes> getMainRes = productDao.getMains(userIdx);
            return getMainRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



    // GET 특정 상품 조회 API
    public GetProductRes getProduct(int userIdx, int productIdx) throws BaseException {
        try{
            GetProductRes getProductRes = productDao.getProduct(userIdx, productIdx);
            return getProductRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
