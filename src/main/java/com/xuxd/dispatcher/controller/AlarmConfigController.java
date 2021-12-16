package com.xuxd.dispatcher.controller;

import com.xuxd.dispatcher.beans.dto.AlarmConfigDTO;
import com.xuxd.dispatcher.service.AlarmConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 16:57:13
 **/
@RestController
@RequestMapping("/alarm/config")
public class AlarmConfigController {

    @Autowired
    private AlarmConfigService alarmConfigService;

    @GetMapping
    public Object select() {
        return alarmConfigService.selectList();
    }

    @PostMapping
    public Object add(@RequestBody AlarmConfigDTO dto) {
        return alarmConfigService.addAlarmConfig(dto);
    }

    @DeleteMapping
    public Object delete(@RequestBody AlarmConfigDTO dto) {
        return alarmConfigService.deleteAlarmConfig(dto);
    }

    @PutMapping
    public Object update(@RequestBody AlarmConfigDTO dto) {
        return alarmConfigService.updateAlarmConfig(dto);
    }

    @PostMapping("/test")
    public Object test(@RequestBody AlarmConfigDTO dto) {
        return alarmConfigService.testAlarmConfig(dto);
    }
}
