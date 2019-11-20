package com.lixin.demo;

/**
 * @description: 单列设计模式
 * @author: zhenglubo
 * @create: 2019-11-12 09:56
 **/

public class SignModel {

    private static  SignModel signModel=null;
    private SignModel(){}
    public static SignModel getInstance(){
        if(signModel==null){
            synchronized (SignModel.class){
                if(signModel==null){
                    signModel = new SignModel();
                }
            }
        }
        return signModel;
    }
}
