package com.xuxd.dispatcher.beans.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 16:39:08
 **/
@Data
@TableName("T_SMS_ALARM_CONFIG")
public class SmsAlarmConfigDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String mobile;

    private Integer type;

    private String keys;

    private String remark;

    private String updateTime;

    private Boolean enableFilter;

    private String filterType;

    private boolean enable;

    private String template;
}
