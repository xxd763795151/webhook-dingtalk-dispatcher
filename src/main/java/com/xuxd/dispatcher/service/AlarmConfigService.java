package com.xuxd.dispatcher.service;

import com.xuxd.dispatcher.beans.ResponseData;
import com.xuxd.dispatcher.beans.dto.AlarmConfigDTO;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 16:42:14
 **/
public interface AlarmConfigService {

    ResponseData selectList();

    ResponseData addAlarmConfig(AlarmConfigDTO dto);

    ResponseData deleteAlarmConfig(AlarmConfigDTO dto);

    ResponseData updateAlarmConfig(AlarmConfigDTO dto);
}
