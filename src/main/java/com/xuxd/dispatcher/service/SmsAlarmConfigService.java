package com.xuxd.dispatcher.service;

import com.xuxd.dispatcher.beans.ResponseData;
import com.xuxd.dispatcher.beans.dto.SmsAlarmConfigDTO;

/**
 * @author: xuxd
 * @date: 2023/5/22 13:55
 **/
public interface SmsAlarmConfigService {
    ResponseData selectList();

    ResponseData addAlarmConfig(SmsAlarmConfigDTO dto);

    ResponseData deleteAlarmConfig(SmsAlarmConfigDTO dto);

    ResponseData updateAlarmConfig(SmsAlarmConfigDTO dto);

    ResponseData testAlarmConfig(SmsAlarmConfigDTO dto);
}
