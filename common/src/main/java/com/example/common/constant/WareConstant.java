package com.example.common.constant;

/**
 * 库存常量
 */
public class WareConstant {

    /**
     * 采购单状态码
     */
    public enum PurchaseStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),RECEIVED(2,"已领取"),FINISH(3,"已分配"),HASERROR(4,"未分配");
        private int code;
        private String msg;

        PurchaseStatusEnum(int code, String msg){
            this.code = code;
            this.msg  = msg;
        }

        public int getCode(){
            return this.code;
        }

        public String getMsg(){
            return this.msg;
        }
    }

    /**
     * 采购需求状态码
     */
    public enum PurchaseDetailedStatusEnum{
        //状态[0新建，1已分配，2正在采购，3已完成，4采购失败]
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),BUYING(2,"正在采购"),DONE(3,"已完成"),FAIL(4,"采购失败");
        private int code;
        private String msg;

        PurchaseDetailedStatusEnum(int code, String msg){
            this.code = code;
            this.msg  = msg;
        }

        public int getCode(){
            return this.code;
        }

        public String getMsg(){
            return this.msg;
        }
    }

}
