package com.example.demo.src.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseProvider {

    private final PurchaseDao purchaseDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PurchaseProvider(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

}
