package com.xuxd.dispatcher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.xuxd.dispatcher.beans.DingResponse;
import com.xuxd.dispatcher.beans.ResponseData;
import com.xuxd.dispatcher.beans.dto.AlarmConfigDTO;
import com.xuxd.dispatcher.common.DispatcherExecutor;
import com.xuxd.dispatcher.common.FilterType;
import com.xuxd.dispatcher.dao.AlarmConfigMapper;
import com.xuxd.dispatcher.service.AlarmConfigService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 16:46:14
 **/
@Slf4j
@Service
public class AlarmConfigServiceImpl implements AlarmConfigService {

    private final AlarmConfigMapper alarmConfigMapper;

    private final DispatcherExecutor dispatcherExecutor;

    private Gson gson = new Gson();

    public AlarmConfigServiceImpl(ObjectProvider<AlarmConfigMapper> alarmConfigMapper,
        ObjectProvider<DispatcherExecutor> executorObjectProvider) {
        this.alarmConfigMapper = alarmConfigMapper.getIfAvailable();
        this.dispatcherExecutor = executorObjectProvider.getIfAvailable();
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

    @Override public ResponseData testAlarmConfig(AlarmConfigDTO dto) {

        log.info("test alarm config: {}", dto);
        Map<String, Object> args = new HashMap<>();
        String body = formatBody("Dispatcher alarm test, keys: " + dto.getKeys());
        DingResponse response = dispatcherExecutor.execute(args, body, dto.getUrl(), dto.getSecret(), dto.isEnableFilter(), FilterType.valueOf(dto.getFilterType()), dto.getKeys());
        log.info("test response: {}", response);
        ResponseData responseData = ResponseData.create();
        responseData.setCode(response.getErrcode());
        responseData.setMsg(response.getErrmsg());
        return responseData;
    }

    private String formatBody(String body) {
        Map<String, Object> map = new HashMap<>();
        map.put("msgtype", "markdown");
        Map<String, Object> content = new HashMap<>();
        content.put("title", "Dispatcher Alarm Test, Ignore");
        content.put("text", "### Message(ignore)\\\n**" + body + "**");
        map.put("markdown", content);
        return gson.toJson(map);
    }
}
