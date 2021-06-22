package com.njbandou.web.vo;

import lombok.Data;

/**
 * Created by CANONYANG on 2018/6/11.
 */
@Data
public class Result {
    private Integer code;

    private String msg;

    private Object data;

    private Integer count;

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", count=" + count +
                '}';
    }
}
