package com.xuxd.dispatcher.beans.dto;

import com.xuxd.dispatcher.beans.dos.SmsAlarmConfigDO;
import lombok.Data;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 16:44:34
 **/
@Data
public class SmsAlarmConfigDTO {

    private Long id;

    private Integer type;

    private String mobile;

    private String keys;

    private String remark;

    private Boolean enableFilter;

    private Boolean enable;

    private String filterType;

    private String template;

    public SmsAlarmConfigDO toDO() {
        SmsAlarmConfigDO configDO = new SmsAlarmConfigDO();
        configDO.setId(id);
        configDO.setMobile(mobile);
        configDO.setKeys(keys);
        configDO.setRemark(remark);
        configDO.setEnable(enable);
        configDO.setEnableFilter(enableFilter);
        configDO.setFilterType(filterType);
        configDO.setType(type);
        configDO.setTemplate(template);

        return configDO;
    }

    public static enum Type {
        SMS(0), VOICE(1);
        private int code;

        Type(int code) {
            this.code = code;
        }
    }
}
