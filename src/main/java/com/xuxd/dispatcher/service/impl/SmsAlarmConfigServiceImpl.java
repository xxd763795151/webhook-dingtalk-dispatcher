package com.xuxd.dispatcher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuxd.dispatcher.beans.AlertStatus;
import com.xuxd.dispatcher.beans.DingResponse;
import com.xuxd.dispatcher.beans.ResponseData;
import com.xuxd.dispatcher.beans.dto.SmsAlarmConfigDTO;
import com.xuxd.dispatcher.common.DispatcherExecutor;
import com.xuxd.dispatcher.common.FilterType;
import com.xuxd.dispatcher.dao.SmsAlarmConfigMapper;
import com.xuxd.dispatcher.service.SmsAlarmConfigService;
import com.xuxd.dispatcher.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: xuxd
 * @date: 2023/5/22 13:55
 **/
@Slf4j
@Service
public class SmsAlarmConfigServiceImpl implements SmsAlarmConfigService {

    private final SmsAlarmConfigMapper alarmConfigMapper;

    private final DispatcherExecutor dispatcherExecutor;

    public SmsAlarmConfigServiceImpl(SmsAlarmConfigMapper alarmConfigMapper, DispatcherExecutor dispatcherExecutor) {
        this.alarmConfigMapper = alarmConfigMapper;
        this.dispatcherExecutor = dispatcherExecutor;
    }

    @Override
    public ResponseData selectList() {
        return ResponseData.create().data(alarmConfigMapper.selectList(new QueryWrapper<>())).success();
    }

    @Override
    public ResponseData addAlarmConfig(SmsAlarmConfigDTO dto) {
        alarmConfigMapper.insert(dto.toDO());
        return ResponseData.create().success();
    }

    @Override
    public ResponseData deleteAlarmConfig(SmsAlarmConfigDTO dto) {
        alarmConfigMapper.deleteById(dto.getId());
        return ResponseData.create().success();
    }

    @Override
    public ResponseData updateAlarmConfig(SmsAlarmConfigDTO dto) {
        alarmConfigMapper.updateById(dto.toDO());
        return ResponseData.create().success();
    }

    @Override
    public ResponseData testAlarmConfig(SmsAlarmConfigDTO dto) {
        log.info("test alarm config: {}", dto);
        Map<String, Object> labels = new HashMap<>();
        labels.put("alertname", "告警测试");
        labels.put("summary", "测试配置");
        labels.put("status", ConvertUtil.statusOf(AlertStatus.FIRING));
        labels.put("description", "测试配置，告警内容请忽略");
        labels.put("startsAt", ConvertUtil.utc2Gmt8("2023-05-22T10:41:31.557Z"));
        labels.put("endsAt", ConvertUtil.utc2Gmt8("0001-01-01T00:00:00Z"));
        String messageBody = ConvertUtil.convert(dto.getTemplate(), labels);
        Set<String> set = Arrays.stream(dto.getMobile().split(",")).map(String::trim).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
        DingResponse response = dispatcherExecutor.executeFromAlert(new ArrayList<>(set), messageBody, dto.getType(),
                true, FilterType.OR, "测试");
        log.info("test response: {}", response);
        ResponseData responseData = ResponseData.create();
        responseData.setCode(response.getErrcode());
        responseData.setMsg(response.getErrmsg());
        return responseData;
    }
}
