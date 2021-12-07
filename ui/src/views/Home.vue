<template>
  <div class="content">
    <a-spin :spinning="loading">
      <div class="operation-row-button">
        <a-button type="primary" @click="openAddAlarmConfigDialog"
          >新增</a-button
        >
      </div>
      <div>
        <hr />
      </div>
      <div>
        <a-table
          :columns="columns"
          :data-source="data"
          :rowKey="(record) => record.id"
          bordered
        >
          <div slot="enableFilter" slot-scope="enableFilter">
            <span v-if="enableFilter"
              ><a-icon
                type="check-circle"
                theme="twoTone"
                two-tone-color="#52c41a"
            /></span>
            <span v-else
              ><a-icon type="close-circle" theme="twoTone" two-tone-color="red"
            /></span>
          </div>
          <div slot="enable" slot-scope="enable">
            <span v-if="enable"
              ><a-icon
                type="check-circle"
                theme="twoTone"
                two-tone-color="#52c41a"
            /></span>
            <span v-else
              ><a-icon type="close-circle" theme="twoTone" two-tone-color="red"
            /></span>
          </div>
          <div slot="operation" slot-scope="record" v-show="!record.internal">
            <a-button
              size="small"
              href="javascript:;"
              class="operation-btn"
              @click="openUpdateConfigDialog(record)"
              >编辑
            </a-button>
            <a-popconfirm
              title="删除当前配置？"
              ok-text="确认"
              cancel-text="取消"
              @confirm="deleteConfig(record)"
            >
              <a-button size="small" href="javascript:;" class="operation-btn"
                >删除
              </a-button>
            </a-popconfirm>
          </div>
        </a-table>
      </div>
      <AddAlarmConfig
        :visible="showAddAlarmConfigDialog"
        close-callback="closeAddAlarmConfigDialog"
        :default-config="{ enable: true, enableFilter: true }"
        @closeAddAlarmConfigDialog="closeAddAlarmConfigDialog"
      ></AddAlarmConfig>
      <AddAlarmConfig
        :visible="showUpdateAlarmConfigDialog"
        :default-config="selectRecord"
        close-callback="closeUpdateConfigDialog"
        @closeUpdateConfigDialog="closeUpdateConfigDialog"
      ></AddAlarmConfig>
    </a-spin>
  </div>
</template>

<script>
import request from "@/utils/request";
import { AlarmConfig } from "@/utils/api";
import notification from "ant-design-vue/lib/notification";
import AddAlarmConfig from "./AddAlarmConfig";
export default {
  name: "Home",
  components: { AddAlarmConfig },

  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      loading: false,
      columns: columns,
      data: [],
      showAddAlarmConfigDialog: false,
      showUpdateAlarmConfigDialog: false,
      selectRecord: {},
    };
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
    deleteConfig(record) {
      this.loading = true;
      request({
        url: AlarmConfig.delete.url,
        method: AlarmConfig.delete.method,
        data: record,
      }).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.getAlarmConfigList();
        } else {
          notification.error({
            message: "error",
            description: res.msg,
          });
        }
      });
    },
    openUpdateConfigDialog(record) {
      this.selectRecord = record;
      this.showUpdateAlarmConfigDialog = true;
    },
    closeUpdateConfigDialog(res) {
      this.showUpdateAlarmConfigDialog = false;
      if (res.refresh) {
        this.getAlarmConfigList();
      }
    },
    openAddAlarmConfigDialog() {
      this.showAddAlarmConfigDialog = true;
    },
    closeAddAlarmConfigDialog(res) {
      this.showAddAlarmConfigDialog = false;
      if (res.refresh) {
        this.getAlarmConfigList();
      }
    },
  },
  created() {
    this.getAlarmConfigList();
  },
};

const columns = [
  // {
  //   title: "ID",
  //   dataIndex: "id",
  //   key: "id",
  //   width: 80,
  // },
  {
    title: "钉钉链接",
    dataIndex: "url",
    key: "url",
    ellipsis: true,
  },
  {
    title: "加签",
    dataIndex: "secret",
    key: "secret",
    ellipsis: true,
  },
  {
    title: "关键字",
    dataIndex: "keys",
    key: "keys",
    ellipsis: true,
  },
  {
    title: "关键字过滤",
    dataIndex: "enableFilter",
    key: "enableFilter",
    scopedSlots: { customRender: "enableFilter" },
  },
  {
    title: "启用配置",
    dataIndex: "enable",
    key: "enable",
    scopedSlots: { customRender: "enable" },
  },
  {
    title: "备注",
    dataIndex: "remark",
    key: "remark",
    ellipsis: true,
  },
  {
    title: "操作",
    key: "operation",
    scopedSlots: { customRender: "operation" },
    width: 200,
  },
];
</script>

<style scoped>
.content {
  width: 100%;
  height: 100%;
}

.operation-row-button {
  height: 4%;
  text-align: left;
  margin-bottom: 8px;
}

.operation-btn {
  margin-right: 5%;
}
</style>
