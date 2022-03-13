package com.example.youngmall.product.controller;

import com.example.youngmall.product.upload.uploadUtils;
import io.renren.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("product/upload")
public class OssUploadController {
    @Autowired
    uploadUtils uploadUtils;

    @RequestMapping(value="/file",method = RequestMethod.POST)
    public R upload(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName) throws IOException {
        byte [] byreArr = file.getBytes();
        //转换文件格式
        InputStream inputStream=new ByteArrayInputStream(byreArr);
        uploadUtils.putObject(fileName,inputStream);
        return R.ok();
    }
}
