package com.example.youngmall.product.upload;

import java.io.FileNotFoundException;
import java.io.InputStream;


public interface uploadUtils {
    public void putObject(String FileName, InputStream file) throws FileNotFoundException;
}
