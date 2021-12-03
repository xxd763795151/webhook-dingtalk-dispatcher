package com.xuxd.dispatcher.beans;

import lombok.Data;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 14:02:48
 **/
@Data
public class DingResponse {

    private int errcode = 0;

    private String errmsg = "ok";

    public static DingResponse def() {
        return new DingResponse();
    }
}
