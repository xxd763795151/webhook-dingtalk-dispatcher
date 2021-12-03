package com.xuxd.dispatcher.beans.dto;

import com.xuxd.dispatcher.beans.dos.AlarmConfigDO;
import lombok.Data;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 16:44:34
 **/
@Data
public class AlarmConfigDTO {

    private Long id;

    private String url;

    private String secret;

    private String keys;

    private String remark;

    public AlarmConfigDO toDO() {
        AlarmConfigDO configDO = new AlarmConfigDO();
        configDO.setId(id);
        configDO.setUrl(url);
        configDO.setKeys(keys);
        configDO.setSecret(secret);

        return configDO;
    }
}
