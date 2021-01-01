<template>
  <div v-if="report_loading" class="loading-container">
    <el-progress class="progress" type="circle" :percentage="percentage" :width="160" :stroke-width="16"></el-progress>
  </div>
  <div v-else class="container" >
    <div class="case-info">
      <el-card class="ring-card">
        <ve-ring :data="chartData"
                 :settings="chartSettings"
                 :extend="chartExtend" class="ring">
        </ve-ring>
      </el-card>
      <el-card class="case-list">
        <el-popover placement="top-start"
                    trigger="hover"
                    content="报错接口前置">
          <el-switch  slot="reference" class="sort-switch" v-model="sort_switch" active-color="#13ce66" inactive-color="#ff4949" @change="dataSort"></el-switch>
        </el-popover>
        <div style="height: 360px">
          <vue-scroll>
            <div v-for="item in result_list"
                 :class="{'case-title-normal': !item.is_choose, 'case-title-choose':item.is_choose}"
                 :key="item.index"
                 @click="getCaseDetail(item)">
              <i class="el-icon-success icon" v-if="item.expected_is_pass" style="color: #00C292"></i>
              <i class="el-icon-error icon" v-else-if="!item.expected_is_pass" style="color: #E46A76"></i>
              <span class="title">
              {{item.case_name}}
              </span>
            </div>
          </vue-scroll>
        </div>
      </el-card>
    </div>
    <el-card class="case-detail">
      <div v-show="case_choose" style="height: 660px">
        <vue-scroll>
          <div class="header">
            <div class="title">
              {{case_detail.case_name}}
            </div>
            <response-status v-if="case_detail.status_code"
                             :status-code="case_detail.status_code"
                             :message="case_detail.message"
                             :response-time="case_detail.response_time"/>
          </div>
          <assertions-detail :result="case_detail.expected_res"
                             class="table"
                             v-if="case_choose && case_detail.expected_res.length !== 0">
          </assertions-detail>
          <variable-detail :result="case_detail.variable_list"
                           class="table"
                           v-if="case_choose && case_detail.variable_list.length !== 0">
          </variable-detail>
          <read-only-content title="URL" :content="case_detail.url"></read-only-content>
          <read-only-content title="Request" :content="case_detail.request_text"></read-only-content>
          <read-only-content title="Response" :content="case_detail.response_text"></read-only-content>
        </vue-scroll>
      </div>
    </el-card>
  </div>
</template>

<script>
import CollectionReport from '@/model/autotest/collection-report'
import AssertionsDetail from '@/component/autotest/assertions-detail'
import VariableDetail from '@/component/autotest/variable-detail'
import ReadOnlyContent from '@/component/autotest/read-only-content'
import ResponseStatus from '@/component/autotest/response-status'

export default {
  components: {
    AssertionsDetail,
    ReadOnlyContent,
    ResponseStatus,
    VariableDetail,
  },
  data() {
    return {
      report_id: null,
      collection_id: null,
      report_loading: true,
      total_count: 0,
      percentage: 0,
      sort_switch: false,
      base_result_list: [],
      result_list: [],
      case_detail: {},
      case_choose: false,
      chartSettings: {
        radius: [65, 45],
        hoverAnimation: false,
        label: { show: false }
      },
      chartExtend: {
        graphic: [{
          type: 'text',
          left: 'center',
          top: '45%',
          style: {
            text: 0,
            textAlign: 'center',
            fill: '#000',
            fontSize: 28,
          }
        }, {
          type: 'text',
          left: 'center',
          top: '55%',
          style: {
            text: '请求',
            textAlign: 'center',
            fill: '#999999',
            fontSize: 16,
          }
        }],
        color: ['#00C292', '#E46A76']
      },
      chartData: {
        columns: ['type', 'count'],
        rows: [
          { type: '成功', count: 0 },
          { type: '失败', count: 0 }
        ]
      },
      result_css: {
        status_code_type: 'success',
        time_type: 'success',
      },
    }
  },
  watch: {
    total_count(val) {
      this.chartExtend.graphic[0].style.text = val
    },
    $route() {
      this.pageInit()
    }
  },
  mounted() {
    this.pageInit()
  },
  destroyed() {
    if (this.websocket) {
      this.websocket.close()
    }
  },
  methods: {
    async pageInit() {
      this.report_id = this.$route.params.id
      const [collection_id] = this.report_id.split('_')
      this.collection_id = collection_id
      if (!await this.getReportDetail()) {
        this.initWebSocket()
      }
    },
    async getReportDetail() {
      const res = await CollectionReport.getReportDetail(this.report_id)
      if (!res) {
        return false
      }
      this.report_loading = false
      this.dataInit(res)
      return true
    },
    dataInit(res) {
      const { pass_count, error_count, result } = res
      this.total_count = pass_count + error_count
      this.chartData.rows[0].count = pass_count
      this.chartData.rows[1].count = error_count
      this.base_result_list = result.slice()
      this.result_list = result.slice()
      this.dataSort()
    },
    dataSort() {
      if (this.sort_switch) {
        this.result_list = this.base_result_list.slice()
      } else {
        this.result_list.sort((a, b) => a.expected_is_pass - b.expected_is_pass)
      }
    },
    initWebSocket() {
      const uri = process.env.VUE_APP_SOCKET_URL
      console.log(uri)
      this.websocket = new WebSocket(uri)
      this.websocket.onmessage = this.websocketOnMessage
      this.websocket.onopen = this.websocketOnOpen
      this.websocket.onerror = this.websocketOnError
      this.websocket.onclose = this.websocketClose
    },
    websocketOnOpen() {
      console.log('建立连接')
    },
    websocketOnError() { // 连接建立失败重连
      this.initWebSocket()
    },
    async websocketOnMessage(e) {
      const [type, collection_id, percentage] = e.data.split('_')
      if (type !== 'collection') {
        return
      }
      if (collection_id !== this.collection_id) {
        return
      }
      this.percentage = Number(percentage)
      if (Number(percentage) === 100) {
        this.websocket.close()
        await new Promise(resolve => {
          setTimeout(() => resolve('done!'), 1000)
        })
        await this.getReportDetail()
      }
    },
    websocketSend(data) { // 数据发送
      this.websocket.send(data)
    },
    websocketClose(e) { // 关闭
      console.log('断开连接', e)
    },
    getCaseDetail(caseInfo) {
      this.case_choose = true
      this.result_list.forEach(e => {
        e.is_choose = false
      })
      caseInfo.is_choose = true
      this.case_detail = caseInfo
    }
  }
}
</script>

<style lang="scss" scoped>

  .loading-container {
    width: 100%;
    height: 600px;

    .progress {
      top: 40%;
      left: 50%;
      transform: translate(-50%,-50%);
    }
  }

  .container {
    display: flex;
    padding: 10px 10px;

    .case-info {
      display: flex;
      flex-direction: column;
      width: 40%;

      .ring-card {
        height: 240px;

        .ring {
          transform: translateY(-90px);
        }
      }

      .case-list {
        margin-top: 10px;
        height: 440px;

        .sort-switch {
          padding-bottom: 10px;
        }

        .case-title {
          height: 30px;
          width: 98%;
          border:1px solid #b3d8ff;
          margin-bottom: 6px;
          display: flex;
          align-items: center;

          .icon {
            margin-left: 10px;
          }

          .title {
            margin-left: 10px;
          }
        }

        .case-title-normal {
          @extend .case-title;
          color: #409EFF;
          background: #ecf5ff;
        }

        .case-title-normal:hover {
          border: 1px solid #409EFF;
        }

        .case-title-choose{
          @extend .case-title;
          color: #ecf5ff;
          background: #409EFF;
        }
      }
    }

    .case-detail {
      margin-left: 10px;
      width: 60%;
      height: 690px;

      .header {
        display: flex;
        align-items: flex-end;
        justify-content: space-between;

        .title {
          color: $parent-title-color;
          font-size: 20px;
          font-weight: 500;
        }
      }

      .table {
        margin-top: 10px;
      }

      .response-text {
        margin-top: 10px;
        word-wrap: break-word;
        word-break: normal;
      }

    }

  }
</style>
