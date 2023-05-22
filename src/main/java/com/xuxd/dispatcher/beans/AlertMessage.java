package com.xuxd.dispatcher.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * {
 * "receiver": "web\\.hook",
 * "status": "firing",
 * "alerts": [
 * {
 * "status": "firing",
 * "labels": {
 * "alertname": "kafka节点挂了",
 * "env": "测试",
 * "severity": "warning"
 * },
 * "annotations": {
 * "description": "测试 集群的节点挂了，当前可用节点：1",
 * "summary": "测试 集群的节点挂了"
 * },
 * "startsAt": "2023-05-22T02:42:36.112Z",
 * "endsAt": "0001-01-01T00:00:00Z",
 * "generatorURL": "http://xiaodonggg:9090/graph?g0.expr=count+by+%28env%29+%28kafka_server_replicamanager_leadercount%7Benv%3D%22%E6%B5%8B%E8%AF%95%22%7D%29+%3C+3\u0026g0.tab=1",
 * "fingerprint": "c7d265afa2fdc79c"
 * }
 * ],
 * "groupLabels": {
 * "alertname": "kafka节点挂了"
 * },
 * "commonLabels": {
 * "alertname": "kafka节点挂了",
 * "env": "测试",
 * "severity": "warning"
 * },
 * "commonAnnotations": {
 * "description": "测试 集群的节点挂了，当前可用节点：1",
 * "summary": "测试 集群的节点挂了"
 * },
 * "externalURL": "http://xiaodonggg:9093",
 * "version": "4",
 * "groupKey": "{}:{alertname=\"kafka节点挂了\"}",
 * "truncatedAlerts": 0
 * }
 *
 * @author: xuxd
 * @date: 2023/5/22 10:50
 **/
@NoArgsConstructor
@Data
public class AlertMessage {

    private String receiver;
    private String status;
    private List<AlertsDTO> alerts;
    private Map<String, Object> groupLabels;
    private Map<String, Object> commonLabels;
    private Map<String, Object> commonAnnotations;
    private String externalURL;
    private String version;
    private String groupKey;
    private Integer truncatedAlerts;

    @NoArgsConstructor
    @Data
    public static class AlertsDTO {
        private String status;
        private Map<String, Object> labels;
        private Map<String, Object> annotations;
        private String startsAt;
        private String endsAt;
        private String generatorURL;
        private String fingerprint;
    }
}

