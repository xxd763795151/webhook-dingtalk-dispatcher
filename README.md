# webhook-dingtalk-dispatcher
## 介绍

prometheus + alert-manager + webhook-dingtalk 向钉钉告警，如果想要不同的告警发给不同的钉钉群机器人，
配置太麻烦了，这里开发个可视化工具，很容易的配置，可以针对告警信息里包含的关键字发给不同的群助手。

打个比方，监控mq的告警，不同的项目用的是不同的topic，各自项目只想接收自己topic的监控告警信息，就可以通过
这个很容易配置。

## 安装
将安装包下载下来并解压开，配置文件：config/application.yml
```shell
# 启动
sh bin/start.sh
# 停止
sh bin/shutdown.sh
```

## 使用方式
1. 安装并配置prometheus: https://prometheus.io/docs/prometheus/latest/installation/
2. 安装并配置alert manager: https://prometheus.io/docs/alerting/latest/configuration/
3. 安装prometheus-webhook-dingtalk: https://github.com/timonwong/prometheus-webhook-dingtalk  
4. 安装并启动webhook-dingtalk-dispathcer，就是这个工具  

前面这4个都安装完成后，修改prometheus-webhook-dingtalk的配置：  
```yaml
targets:
  webhook1:
    url: http://localhost:7006/dispatcher?access_token=1234567890
```
将本来指向钉钉webhook的地址，设置为webhook-dingtalk-dispatcher的地址，最后的access_token就是webhook-dingtalk-dispatcher的配置文件里配置的access-token属性
```yaml
# 设置一个access-token，算是增加一点安全性
access-token: 1234567890
```