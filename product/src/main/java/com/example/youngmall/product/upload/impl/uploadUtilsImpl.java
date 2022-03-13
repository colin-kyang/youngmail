package com.example.youngmall.product.upload.impl;

import com.aliyun.oss.OSS;
import com.example.youngmall.product.upload.uploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class uploadUtilsImpl implements uploadUtils {
    @Autowired
    OSS ossclient;

    @Override
    public void putObject(String FileName, InputStream file) throws FileNotFoundException {
        ossclient.putObject("kyara-yangmall",FileName,file);
    }
}
