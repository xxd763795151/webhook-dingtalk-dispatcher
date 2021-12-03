<template>
  <div class="content">
    <a-spin :spinning="loading">
      <div class="operation-row-button">
        <a-button type="primary">新增</a-button>
      </div>
      <div>
        <hr />
      </div>
      <div>
        <a-table :columns="columns" :data-source="data" bordered>
          <div slot="operation" slot-scope="record" v-show="!record.internal">
            <a-button size="small" href="javascript:;" class="operation-btn"
              >编辑
            </a-button>
            <a-button size="small" href="javascript:;" class="operation-btn"
              >删除
            </a-button>
          </div>
        </a-table>
      </div>
    </a-spin>
  </div>
</template>

<script>
import request from "@/utils/request";
import { AlarmConfig } from "@/utils/api";
import notification from "ant-design-vue/lib/notification";
export default {
  name: "Home",
  components: {},

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
  },
  created() {
    this.getAlarmConfigList();
  },
};

const columns = [
  {
    title: "ID",
    dataIndex: "id",
    key: "id",
    width: 80,
  },
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
