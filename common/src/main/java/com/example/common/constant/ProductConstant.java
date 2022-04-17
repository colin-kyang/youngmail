package com.example.common.constant;

public class ProductConstant {

    /**
     * spu 对应商品的状态码
     */
    public enum SpuStatus{
        PUBLISHED(1,"上架"),UNPUBLISHED(0,"新建");
        private int code;
        private String msg;

        SpuStatus(int cd, String mg){
            this.code = cd;
            this.msg = mg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
