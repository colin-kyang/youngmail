package com.example.third_party.upload;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;


public interface uploadUtils {
    public void putObject(String FileName, InputStream file) throws FileNotFoundException;

    public void putObject(String FileName,String filePath);

    public Map<String,String> uploadByPlolicy(String dir) throws UnsupportedEncodingException;
}
