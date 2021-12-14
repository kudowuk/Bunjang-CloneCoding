package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.src.category.model.GetSubcategoryRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class CategoryProvider {

    private final CategoryDao categoryDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CategoryProvider(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    public List<GetCategoryRes> getCategories() throws BaseException {

        try{
            List<GetCategoryRes> getCategoryRes = categoryDao.getCategories();
            return getCategoryRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // GET 서브카테고리 해당 상품 조회 API
    public GetSubcategoryRes getSubcategory(int userIdx, int subcategoryIdx) throws BaseException {

        try{
            GetSubcategoryRes getSubcategoryRes = categoryDao.getSubcategory(userIdx, subcategoryIdx);
            return getSubcategoryRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
