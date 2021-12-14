package com.example.demo.src.brand;

import com.example.demo.config.BaseException;
import com.example.demo.src.brand.BrandDao;
import com.example.demo.src.brand.model.GetBrandRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class BrandProvider {

    private final BrandDao brandDao;

    @Autowired
    public BrandProvider(BrandDao brandDao) {
        this.brandDao = brandDao;
    }

    public List<GetBrandRes> getBrands(int userIdx) throws BaseException {
        try{
            List<GetBrandRes> getBrandRes = brandDao.getBrands(userIdx);
            return getBrandRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
