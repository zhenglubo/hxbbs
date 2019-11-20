package com.lixin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 异常码
 * @author: zhenglubo
 * @create: 2019-10-22 11:09
 **/

@AllArgsConstructor
@Getter
public enum  ErrorCode {

    OK(200,"ok");

    private int code;
    private String message;
}
