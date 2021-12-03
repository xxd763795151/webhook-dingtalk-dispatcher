package com.xuxd.dispatcher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuxd.dispatcher.beans.ResponseData;
import com.xuxd.dispatcher.beans.dto.AlarmConfigDTO;
import com.xuxd.dispatcher.dao.AlarmConfigMapper;
import com.xuxd.dispatcher.service.AlarmConfigService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 16:46:14
 **/
@Service
public class AlarmConfigServiceImpl implements AlarmConfigService {

    private final AlarmConfigMapper alarmConfigMapper;

    public AlarmConfigServiceImpl(ObjectProvider<AlarmConfigMapper> alarmConfigMapper) {
        this.alarmConfigMapper = alarmConfigMapper.getIfAvailable();
    }

    @Override public ResponseData selectList() {
        return ResponseData.create().data(alarmConfigMapper.selectList(new QueryWrapper<>())).success();
    }

    @Override public ResponseData addAlarmConfig(AlarmConfigDTO dto) {
        alarmConfigMapper.insert(dto.toDO());
        return ResponseData.create().success();
    }

    @Override public ResponseData deleteAlarmConfig(AlarmConfigDTO dto) {
        alarmConfigMapper.deleteById(dto.getId());
        return ResponseData.create().success();
    }

    @Override public ResponseData updateAlarmConfig(AlarmConfigDTO dto) {
        alarmConfigMapper.updateById(dto.toDO());
        return ResponseData.create().success();
    }
}
