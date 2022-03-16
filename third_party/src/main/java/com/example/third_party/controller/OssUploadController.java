package com.example.third_party.controller;



import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.example.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("thirdParty/service/upload")
public class OssUploadController {
    @Autowired
    com.example.third_party.upload.uploadUtils uploadUtils;

    @RequestMapping(value="/file",method = RequestMethod.POST)
    public R upload(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName) throws IOException {
        byte [] byreArr = file.getBytes();
        //转换文件格式
        InputStream inputStream=new ByteArrayInputStream(byreArr);
        uploadUtils.putObject(fileName,inputStream);
        return R.ok();
    }

    @RequestMapping(value="/FilePath",method=RequestMethod.POST)
    public R upload(@RequestParam("file") String filePath)
    {
        String [] context=filePath.split("/");
        String finalPath="用户名/"+context[context.length-1];
        uploadUtils.putObject(finalPath,filePath);
        return R.ok();
    }

    @RequestMapping(value="/policy",method=RequestMethod.GET)
    public Map<String,String> uploadByPolicy(@RequestParam("t") String userName) throws UnsupportedEncodingException {
        Map<String,String> res=uploadUtils.uploadByPlolicy(userName+"/");
        return res;
    }

}
