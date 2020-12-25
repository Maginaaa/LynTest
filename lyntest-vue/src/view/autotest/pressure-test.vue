<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="container">
    <el-card class="info">
      <el-form :inline="true" :model="param" class="demo-form-inline">
        <el-form-item>
          <el-button @click="dialog_visible = true" type="primary">Case选择</el-button>
        </el-form-item>
        <el-form-item label="线程数" class="label">
          <el-input-number v-model="param.threads" :min="1" :max="10"></el-input-number>
        </el-form-item>
        <el-form-item label="执行次数" class="label">
          <el-input-number v-model="param.times" :min="1" :max="5"></el-input-number>
        </el-form-item>
        <el-form-item class="label">
          <el-button type="success"
                     class="run-button"
                     @click="doExecute"
                     :disabled="!case_info.case_name || loading">
            执 行
            <i v-if="loading" class="el-icon-loading"></i>
            <i v-else class="el-icon-video-play"></i>
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="result">
      <div class="case-choose" v-show="case_info.case_name">
        <span class="title">{{case_info.case_name}}</span>
        <el-select v-model="report_id" placeholder="历史报告" size="small" @change="getReportDetail">
          <el-option v-for="id in report_list" :key="id" :value="id" :label="id"></el-option>
        </el-select>
      </div>
      <div class="case-info" v-if="loading" v-loading="loading"></div>
      <div class="case-info" v-if="report_id && !loading">
        <div class="statistical">
          <el-card class="ring-card">
            <div>
              <ve-ring :data="chartData"
                       :settings="chartSettings"
                       :extend="chartExtend" class="ring">
              </ve-ring>
            </div>
          </el-card>
          <el-card class="case-list">
            <div style="height: 260px">
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
        <div class="case-detail">
          <el-card class="table">
            <el-table :data="table_data">
              <el-table-column label="请求数">
                <template v-slot:default="scope">
                  <span>{{scope.row.pass_count + scope.row.error_count }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="average" label="平均"></el-table-column>
              <el-table-column prop="median" label="中位数"></el-table-column>
              <el-table-column prop="ninety" label="90%"></el-table-column>
              <el-table-column prop="ninety_five" label="95%"></el-table-column>
              <el-table-column prop="ninety_nine" label="99%"></el-table-column>
              <el-table-column prop="min" label="最小"></el-table-column>
              <el-table-column prop="max" label="最大"></el-table-column>
            </el-table>
          </el-card>
          <el-card class="detail">
            <div v-show="case_choose" style="height: 280px">
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
      </div>
    </el-card>
    <el-dialog :visible.sync="dialog_visible" :show-close="false" width="70%">
      <http-case-choose v-model="case_info" @dialogClose="dialogClose"></http-case-choose>
    </el-dialog>
  </div>
</template>

<script>
import Pressure from '@/model/autotest/pressure'
import HttpCaseChoose from '@/component/autotest/http-case-choose.vue'
import AssertionsDetail from '@/component/autotest/assertions-detail'
import VariableDetail from '@/component/autotest/variable-detail'
import ReadOnlyContent from '@/component/autotest/read-only-content'
import ResponseStatus from '@/component/autotest/response-status'

export default {
  name: 'batch-execute',
  components: {
    HttpCaseChoose,
    AssertionsDetail,
    VariableDetail,
    ReadOnlyContent,
    ResponseStatus
  },
  data() {
    return {
      websocket: null,
      loading: false,
      case_info: {},
      dialog_visible: false,
      param: {
        threads: 1,
        times: 1,
        case_id: null,
      },
      report_id: null,
      report_list: [],
      result_list: [],
      table_data: [],
      case_choose: false,
      case_detail: {},
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
    }
  },
  destroyed() {
    // 关闭链接
    if (this.websocket) {
      this.websocket.close()
    }
  },
  methods: {
    initWebSocket() {
      const uri = process.env.VUE_APP_SOCKET_URL
      this.websocket = new WebSocket(uri)
      this.websocket.onmessage = this.websocketOnMessage
      this.websocket.onopen = this.websocketOnOpen
      this.websocket.onerror = this.websocketOnError
      this.websocket.onclose = this.websocketClose
    },
    websocketOnOpen() {
      console.log('建立websocket连接')
    },
    websocketOnError() { // 连接建立失败重连
      this.initWebSocket()
    },
    async websocketOnMessage(e) { // 数据接收
      console.log(e.data)
      const [type, id] = e.data.split('_')
      if (type !== 'pressure') {
        return
      }
      if (Number(id) !== this.param.case_id) {
        return
      }
      this.websocket.close()
      this.loading = false
      await this.getReportList()
      await this.getReportDetail()
    },
    websocketSend(data) { // 数据发送
      this.websocket.send(data)
    },
    websocketClose(e) { // 关闭
      console.log('断开连接', e)
      this.loading = false
    },
    async doExecute() {
      this.loading = true
      this.initWebSocket()
      this.report_id = await Pressure.execute(this.param)
    },
    async dialogClose() {
      this.dialog_visible = false
      this.param.case_id = this.case_info.id
      await this.getReportList()
      await this.getReportDetail()
    },
    async getReportList() {
      const res = await Pressure.getReportIdList(this.param.case_id)
      this.report_list = res
      if (res.length === 0) {
        this.report_id = null
        return
      }
      const firstId = res[0]
      this.report_id = firstId
    },
    async getReportDetail() {
      if (this.report_id === null) {
        return
      }
      const res = await Pressure.getReportDetail(this.report_id)
      this.dataInit(res)
    },
    dataInit(res) {
      this.table_data = []
      const { pass_count, error_count, result } = res
      this.table_data.push(res)
      this.chartExtend.graphic[0].style.text = pass_count + error_count
      this.chartData.rows[0].count = pass_count
      this.chartData.rows[1].count = error_count
      this.result_list = result
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
  .container {
    padding: 6px;

    .info {
      height: 80px;

      .label {
        margin-left: 10px
      }

      .run-button {
        width: 100%;
        height: 40px;
        font-size: 120%;
      }
    }

    .result {
      min-height: 600px;
      margin-top: 6px;
      display: flex;
      flex-direction: column;

      .case-choose {
        display: flex;
        justify-content: space-between;
        margin-bottom: 5px;

        .title {
          color: $parent-title-color;
          font-size: 1.1em;
          font-weight: 500;
        }
      }

      .case-info {
        display: flex;

        .statistical {
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
            height: 300px;

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
          height: 600px;

          .table {
            height: 200px;
          }

          .detail {
            margin-top: 10px;
            height: 340px;

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
              height: 100%;
            }

            .response-text {
              margin-top: 10px;
              word-wrap: break-word;
              word-break: normal;
            }
          }

        }
      }

    }
  }

</style>
