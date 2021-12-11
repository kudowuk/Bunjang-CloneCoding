package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.src.address.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class AddressProvider {

    private final AddressDao addressDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AddressProvider(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    public List<GetAddressRes> getAddresses(int userIdx) throws BaseException {
        try{
            List<GetAddressRes> getAddressRes = addressDao.getAddresses(userIdx);
            return getAddressRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetAddressRes getAddress(int userIdx, int addressIdx) throws BaseException {
        try {
            GetAddressRes getAddressRes = addressDao.getAddress(userIdx, addressIdx);
            return getAddressRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
