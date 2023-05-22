package com.xuxd.dispatcher.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuxd.dispatcher.beans.dos.SmsAlarmConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 16:40:48
 **/
@Mapper
public interface SmsAlarmConfigMapper extends BaseMapper<SmsAlarmConfigDO> {
}
