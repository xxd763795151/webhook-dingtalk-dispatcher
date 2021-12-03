# webhook-dingtalk-dispatcher

prometheus + alert-manager + webhook-dingtalk 向钉钉告警，如果想要不同的告警发给不同的钉钉群机器人，
配置太麻烦了，这里开发个可视化工具，很容易的配置，可以针对告警信息里包含的关键字发给不同的群助手。

打个比方，监控mq的告警，不同的项目用的是不同的topic，各自项目只想接收自己topic的监控告警信息，就可以通过
这个很容易配置。