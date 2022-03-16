package com.example.third_party.upload.impl;


import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectRequest;
import com.example.third_party.upload.uploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class uploadUtilsImpl implements uploadUtils {
    @Autowired
    OSS ossclient;

    @Value("${spring.cloud.alicloud.access-key}")
    String accessKeyId;

    @Value("${spring.cloud.alicloud.accessKeySecret}")
    String accessKeySecret;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    String endpoint;



    @Override
    public void putObject(String FileName, InputStream file) throws FileNotFoundException {
        String bucket="kyara-yangmall";

        ossclient.putObject(bucket,FileName,file);
    }

    @Override
    public void putObject(String FileName, String filePath) {
        String buckName="kyara-yangmall";
        PutObjectRequest putObjectRequest=new PutObjectRequest(buckName,FileName,new File(filePath));
        ossclient.putObject(putObjectRequest);

    }

    /**
     * 服务端直传，必须返回几个字段 accessId、host、policy、signature、expire、dir
     * @param dir
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public Map<String,String> uploadByPlolicy(String dir) throws UnsupportedEncodingException {
        String bucket="kyara-yangmall";
        String host="https://"+bucket+"."+endpoint;
        Map<String,String> mapp=new HashMap<>();
        long expireTime = 30;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = ossclient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes("utf-8");
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossclient.calculatePostSignature(postPolicy);

        mapp.put("ossaccessKeyId",accessKeyId);
        mapp.put("policy",encodedPolicy);
        mapp.put("signature",postSignature);
        mapp.put("dir",dir);
        mapp.put("host",host);
        mapp.put("key",String.valueOf(expireEndTime/1000));
        return mapp;
    }
}
