<template>
  <a-modal
    title="增加配置"
    :visible="show"
    :width="800"
    :mask="false"
    :destroyOnClose="true"
    :footer="null"
    :maskClosable="false"
    @cancel="handleCancel"
  >
    <div>
      <a-spin :spinning="loading">
        <a-form
          :form="form"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 12 }"
          @submit="handleSubmit"
        >
          <a-form-item label="手机号">
            <a-input
              v-decorator="[
                'mobile',
                {
                  rules: [
                    {
                      required: true,
                      message: '输入手机号!',
                    },
                  ],
                  initialValue: defaultConfig.mobile,
                },
              ]"
              placeholder="手机号"
            />
          </a-form-item>
          <a-form-item label="类型">
            <a-radio-group
              v-decorator="['type', { initialValue: defaultConfig.type + '' }]"
            >
              <a-radio-button value="0"> 短信 </a-radio-button>
              <a-radio-button value="1"> 语音 </a-radio-button>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="启用关键字过滤">
            <a-switch
              v-decorator="[
                'enableFilter',
                {
                  valuePropName: 'checked',
                  initialValue: defaultConfig.enableFilter,
                },
              ]"
            />
          </a-form-item>
          <a-form-item label="关键字">
            <a-textarea
              rows="3"
              placeholder="多个关键字用英文逗号分隔，示例：topic,consumer"
              v-decorator="['keys', { initialValue: defaultConfig.keys }]"
            />
          </a-form-item>
          <a-form-item label="关键字条件">
            <a-radio-group
              v-decorator="[
                'filterType',
                { initialValue: defaultConfig.filterType },
              ]"
            >
              <a-radio-button value="OR"> 匹配任一关键字 </a-radio-button>
              <a-radio-button value="AND"> 匹配全部关键字 </a-radio-button>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="启用配置">
            <a-switch
              v-decorator="[
                'enable',
                {
                  valuePropName: 'checked',
                  initialValue: defaultConfig.enable,
                },
              ]"
            />
          </a-form-item>
          <a-form-item label="备注">
            <a-textarea
              rows="2"
              placeholder="建议设置一下备注，记录下是谁配置的，对哪些告警等"
              v-decorator="['remark', { initialValue: defaultConfig.remark }]"
            />
          </a-form-item>
          <a-form-item label="模板">
            <a-textarea
              rows="6"
              placeholder="告警模板，示例如下（这句话删除，短信下面的签名【xxx】一定要有）：
【xxx告警】：
告警名称：${alertname}
摘要：${summary}
描述：${description}
开始时间：${startsAt}
结束时间：${endsAt}
其它标签：...
"
              v-decorator="[
                'template',
                {
                  initialValue: defaultConfig.template,
                  rules: [
                    {
                      required: true,
                      message: '输入告警模板!',
                    },
                  ],
                },
              ]"
            />
          </a-form-item>
          <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
            <a-button type="primary" html-type="submit"> 提交 </a-button>
            <a-button type="danger" class="test-btn" @click="testConfig">
              测试连接
            </a-button>
          </a-form-item>
        </a-form>
      </a-spin>
    </div>
  </a-modal>
</template>

<script>
import request from "@/utils/request";
import { SmsAlarmConfig } from "@/utils/api";
import notification from "ant-design-vue/lib/notification";

export default {
  name: "AddSmsAlarmConfig",
  components: {},

  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultConfig: {},
    closeCallback: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      loading: false,
      data: [],
      show: this.visible,
      form: this.$form.createForm(this, { name: "add-config-form" }),
    };
  },
  watch: {
    visible(v) {
      this.show = v;
    },
  },
  methods: {
    getAlarmConfigList() {
      this.loading = true;
      request({
        url: SmsAlarmConfig.select.url,
        method: SmsAlarmConfig.select.method,
      }).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.data = res.data;
        } else {
          notification.error({
            message: "error",
            description: res.msg,
          });
        }
      });
    },
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true;
          const api = this.defaultConfig.id
            ? SmsAlarmConfig.update
            : SmsAlarmConfig.add;
          if (this.defaultConfig.id) {
            values.id = this.defaultConfig.id;
          }
          request({
            url: api.url,
            method: api.method,
            data: values,
          }).then((res) => {
            this.loading = false;
            if (res.code == 0) {
              this.$message.success(res.msg);
              this.$emit(this.closeCallback, { refresh: true });
            } else {
              notification.error({
                message: "error",
                description: res.msg,
              });
            }
          });
        }
      });
    },
    testConfig(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true;
          const api = SmsAlarmConfig.test;
          request({
            url: api.url,
            method: api.method,
            data: values,
          }).then((res) => {
            this.loading = false;
            if (res.code == 0) {
              this.$message.success(res.msg);
            } else {
              notification.error({
                message: "error",
                description: res.msg,
              });
            }
          });
        }
      });
    },
    handleCancel() {
      this.data = [];
      this.$emit(this.closeCallback, { refresh: false });
    },
  },
};
</script>

<style scoped>
.test-btn {
  float: right;
}
</style>
