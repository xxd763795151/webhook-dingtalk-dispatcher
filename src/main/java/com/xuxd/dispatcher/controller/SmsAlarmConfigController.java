package com.xuxd.dispatcher.controller;

import com.xuxd.dispatcher.beans.dto.SmsAlarmConfigDTO;
import com.xuxd.dispatcher.service.SmsAlarmConfigService;
import org.springframework.web.bind.annotation.*;

/**
 * @author: xuxd
 * @date: 2023/5/22 13:34
 **/
@RestController
@RequestMapping("/sms/alarm/config")
public class SmsAlarmConfigController {

    private final SmsAlarmConfigService alarmConfigService;

    public SmsAlarmConfigController(SmsAlarmConfigService alarmConfigService) {
        this.alarmConfigService = alarmConfigService;
    }

    @GetMapping
    public Object select() {
        return alarmConfigService.selectList();
    }

    @PostMapping
    public Object add(@RequestBody SmsAlarmConfigDTO dto) {
        return alarmConfigService.addAlarmConfig(dto);
    }

    @DeleteMapping
    public Object delete(@RequestBody SmsAlarmConfigDTO dto) {
        return alarmConfigService.deleteAlarmConfig(dto);
    }

    @PutMapping
    public Object update(@RequestBody SmsAlarmConfigDTO dto) {
        return alarmConfigService.updateAlarmConfig(dto);
    }

    @PostMapping("/test")
    public Object test(@RequestBody SmsAlarmConfigDTO dto) {
        return alarmConfigService.testAlarmConfig(dto);
    }
}
