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
          <a-form-item label="钉钉链接">
            <a-input
              v-decorator="[
                'url',
                {
                  rules: [
                    {
                      required: true,
                      message: '输入钉钉Webhook!',
                    },
                  ],
                  initialValue: defaultConfig.url,
                },
              ]"
              placeholder="钉钉助手Webhook"
            />
          </a-form-item>
          <a-form-item label="加签">
            <a-input
              v-decorator="['secret', { initialValue: defaultConfig.secret }]"
              placeholder="钉钉助手签名，可选，如果有请设置"
            />
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
              rows="4"
              placeholder="建议设置一下备注，记录下是谁配置的，对哪些告警等"
              v-decorator="['remark', { initialValue: defaultConfig.remark }]"
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
import { AlarmConfig } from "@/utils/api";
import notification from "ant-design-vue/lib/notification";
export default {
  name: "AddAlarmConfig",
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
        url: AlarmConfig.select.url,
        method: AlarmConfig.select.method,
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
            ? AlarmConfig.update
            : AlarmConfig.add;
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
          const api = AlarmConfig.test;
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
