<template>
  <el-card class="edit">
    <div class="info-row">
      <div class="info-cols">
        <div class="category-col">
          <div class="title">分类名称:</div>
          <el-select v-model="case_info.category" style="width: 120px">
            <el-option v-for="item in category_list"
                       :key="item.category_name"
                       :label="item.category_name"
                       :value="item.category_name"/>
          </el-select>
        </div>
        <div class="name-col">
          <div class="title">接口名称:</div>
          <el-input v-model="case_info.case_name" style="width: calc(100% - 80px)"></el-input>
        </div>
      </div>
      <div>
        <el-button type="success" @click="caseExecute">请求 <i class="el-icon-s-promotion"></i></el-button>
        <el-button type="primary" @click="saveCase">{{operationType === 2? '更新': '保存'}} <i class="el-icon-download"></i></el-button>
      </div>
    </div>
    <div class="info-row">
      <div class="title">接口地址:</div>
      <el-input style="width: calc(100% - 80px)" v-model="case_info.api_url">
        <el-select style="width: 90px" v-model="case_info.api_method" slot="prepend">
          <el-option v-for="item in config.method_list" :key="item" :label="item" :value="item"></el-option>
        </el-select>
      </el-input>
    </div>
    <el-tabs type="border-card" class="request">
      <el-tab-pane label="请求体">
        <el-radio-group v-model="case_info.body_type" style="margin-bottom: 10px">
          <el-radio :label="1">json</el-radio>
          <el-radio :label="2">form</el-radio>
        </el-radio-group>
        <el-input type="textarea"
                  :autosize="{ minRows: 5, maxRows: 5}"
                  class="request-body"
                  v-model="case_info.body_value" resize="none"/>
      </el-tab-pane>
      <el-tab-pane label="请求头">
        <div class="request-param">
          <vue-scroll>
            <div v-for="(item,index) in case_info.header_form" :key="index" class="kv-row">
              <el-row type="flex" :gutter="20" justify="space-between" align="middle">
                <el-col class="kv-checkbox">
                  <el-checkbox v-if="!eleIsDisable(index, case_info.header_form)" v-model="item.enable" />
                </el-col>
                <el-col>
                  <el-input v-model="item.header_key" size="small"
                            placeholder="键"
                            @change="headerChange" show-word-limit/>
                </el-col>
                <el-col>
                  <el-input v-model="item.header_value" size="small"
                            @change="headerChange"
                            placeholder="值" show-word-limit/>
                </el-col>
                <el-col class="kv-delete">
                  <el-button size="mini" class="el-icon-delete-solid"
                             circle
                             @click="deleteRow(index, case_info.header_form)"
                             :disabled="eleIsDisable(index, case_info.header_form)"/>
                </el-col>
              </el-row>
            </div>
          </vue-scroll>
        </div>
      </el-tab-pane>
      <el-tab-pane label="变量">
        <div class="request-param">
          <vue-scroll>
            <div v-for="(item,index) in case_info.variable_list" :key="index" class="kv-row">
              <el-row type="flex" :gutter="20" justify="space-between" align="middle">
                <el-col class="kv-checkbox">
                  <el-checkbox v-if="!eleIsDisable(index, case_info.variable_list)" v-model="item.enable" />
                </el-col>
                <el-col class="kv-select">
                  <el-select v-model="item.extract_method" placeholder="请选择取值方式" size="small" @change="variableChange">
                    <el-option v-for="item in config.extract_type_list" :key="item.value" :label="item.label" :value="item.value"/>
                  </el-select>
                </el-col>
                <el-col>
                  <el-input size="small" v-model="item.extract_rule" placeholder="提取路径" @change="variableChange"></el-input>
                </el-col>
                <el-col>
                  <el-tooltip class="item" effect="dark"
                              content="为保持变量名规范，请输入大写、下划线，类似于VARIABLE_TYPE,引用时使用${VARIABLE_TYPE}"
                              :enterable="false"
                              placement="top-end">
                    <el-input size="small" v-model="item.variable_name" placeholder="变量名" @change="variableChange"></el-input>
                  </el-tooltip>
                </el-col>
                <el-col class="kv-delete">
                  <el-button size="mini" class="el-icon-delete-solid"
                             circle
                             @click="deleteRow(index, case_info.variable_list)"
                             :disabled="eleIsDisable(index, case_info.variable_list)"/>
                </el-col>
              </el-row>
            </div>
          </vue-scroll>
        </div>
      </el-tab-pane>
      <el-tab-pane label="断言">
        <div class="request-param">
          <vue-scroll>
            <div v-for="(item,index) in case_info.expected_list" :key="index" class="kv-row">
              <el-row type="flex" :gutter="20" justify="space-between" align="middle">
                <el-col class="kv-checkbox">
                  <el-checkbox v-if="!eleIsDisable(index, case_info.expected_list)" v-model="item.enable" />
                </el-col>
                <el-col class="kv-select">
                  <el-select v-model="item.extract_method" placeholder="请选择取值方式" size="small" @change="expectedChange">
                    <el-option v-for="item in config.extract_type_list" :key="item.value" :label="item.label" :value="item.value"/>
                  </el-select>
                </el-col>
                <el-col>
                  <el-input size="small" v-model="item.extract_rule" placeholder="提取路径/规则" @change="expectedChange"></el-input>
                </el-col>
                <el-col class="kv-select">
                  <el-select v-model="item.compare_type" placeholder="校验方式" size="small" @change="expectedChange">
                    <el-option v-for="type in config.expected_type_list" :key="type.value" :label="type.label" :value="type.value"/>
                  </el-select>
                </el-col>
                <el-col>
                  <el-input size="small" v-model="item.expected_value" placeholder="预期值" @change="expectedChange" :disabled="item.compare_type === 3"></el-input>
                </el-col>
                <el-col class="kv-delete">
                  <el-button size="mini" class="el-icon-delete-solid"
                             circle
                             @click="deleteRow(index, case_info.expected_list)"
                             :disabled="eleIsDisable(index, case_info.expected_list)"/>
                </el-col>
              </el-row>
            </div>
          </vue-scroll>
        </div>
      </el-tab-pane>
      <el-tab-pane label="描述">
        <el-input type="textarea"
                  :autosize="{ minRows: 6, maxRows: 6}"
                  class="request-body"
                  v-model="case_info.description" resize="none"/>
      </el-tab-pane>
    </el-tabs>
    <div class="response">
      <div class="response-header">
        <div class="response-title">Response</div>
        <div class="response-info-list" v-show="has_result">
          <el-popover
            placement="left"
            width="450"
            trigger="hover" v-if="result_is_json">
            <assertions-detail :result="result.expected_res"></assertions-detail>
            <i slot="reference" class="el-icon-view" style="transform: translateX(-10px)"></i>
          </el-popover>
          <response-status :status-code="result.status_code" :message="result.message" :response-time="result.response_time"/>
        </div>
      </div>
      <div v-if="!has_result" style="height: calc(100% - 40px)">
          <div class="response-default-body">
            <l-icon name="file" color="#ccc" height="3.2em" width="3.2em"></l-icon>
            <div class="default-content">Hit Send to get a response</div>
          </div>
      </div>
      <div v-else class="response-body">
        <div class="response-body-path" @click="copyPath" v-if="result_json_path">
          {{result_json_path}} <i class="el-icon-copy-document"></i>
        </div>
        <vue-scroll>
          <div class="body-text-is-json" v-if="result_is_json">
            <vue-json-pretty
              path="$"
              :data="result.response_text"
              @click="resultTextClick">
            </vue-json-pretty>
          </div>
          <div v-else class="body-text-not-json">
            {{result.response_text}}
          </div>
        </vue-scroll>
      </div>
    </div>
  </el-card>
</template>

<script>
import VueJsonPretty from 'vue-json-pretty'
import 'vue-json-pretty/lib/styles.css'
import Category from '@/model/autotest/category'
import HttpCase from '@/model/autotest/http-case'
import AssertionsDetail from '@/component/autotest/assertions-detail'
import ResponseStatus from '@/component/autotest/response-status'

export default {
  props: {
    operationType: {
      type: Number
    },
    caseInfo: {
      type: Object
    }
  },
  components: {
    VueJsonPretty,
    AssertionsDetail,
    ResponseStatus,
  },
  data() {
    return {
      case_info: {},
      category_list: [],
      default_case_info: {
        category: '',
        case_name: '',
        api_url: 'http://',
        api_port: null,
        api_method: 'POST',
        api_path: '',
        description: '',
        header_form: [{
          enable: true,
        }],
        body_type: 1,
        body_value: null,
        variable_list: [{
          enable: true,
        }],
        expected_list: [{
          enable: true,
        }],
      },
      config: {
        method_list: ['POST', 'GET', 'PUT', 'DELETE'],
        extract_type_list: [{
          label: 'JsonPath提取',
          value: 1,
        }, {
          label: '正则表达式提取',
          value: 2,
        }],
        expected_type_list: [{
          label: 'equals',
          value: 1,
        }, {
          label: 'contains',
          value: 2,
        }, {
          label: 'not empty or null',
          value: 3,
        }]
      },
      has_result: false, // 是否进行了请求
      result: {},
      result_css: {
        status_code_type: 'success',
        time_type: 'success',
      },
      result_is_json: false, // 返回body是否为json
      result_json_path: '',
    }
  },
  mounted() {
    this.getCaseInfo()
    this.getCategoryInfo()
  },
  methods: {
    async getCategoryInfo() {
      const res = await Category.getCategoryList()
      this.category_list = res
    },
    getCaseInfo() {
      this.case_info = this.operationType === 1 ? { ...this.default_case_info } : { ...this.caseInfo }
    },
    async caseExecute() {
      const res = await HttpCase.execute(this.case_info)
      this.result = res
      // 如果返回结果为json，则进行格式美化，并展示copy按钮；不为json则直接显示文本内容
      if (this.isJson(res.response_text)) {
        this.result.response_text = JSON.parse(res.response_text)
        this.result_is_json = true
      }
      this.has_result = true
    },
    async saveCase() {
      const res = this.operationType === 2 ? await HttpCase.updateCase(this.case_info) : await HttpCase.createCase(this.case_info)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(`${res.message}`)
        this.$emit('drawer-close')
      } else {
        this.$message.error('服务端异常')
      }
    },
    eleIsDisable(index, data) {
      return data.length - 1 === index
    },
    deleteRow(index, data) {
      data.splice(index, 1)
    },
    headerChange() {
      let isNeedCreate = true
      this.case_info.header_form.forEach(item => {
        if (!item.header_key && !item.header_value) {
          // 没有空行，需要创建空行
          isNeedCreate = false
        }
      })
      if (isNeedCreate) {
        this.case_info.header_form.push({ enable: true, header_key: '', header_value: '' })
      }
    },
    variableChange() {
      let isNeedCreate = true
      this.case_info.variable_list.forEach(item => {
        if (!item.extract_method && !item.extract_rule && !item.variable_name) {
          // 没有空行，需要创建空行
          isNeedCreate = false
        }
      })
      if (isNeedCreate) {
        this.case_info.variable_list.push({ enable: true, extract_method: '', extract_rule: '', variable_name: '' })
      }
    },
    expectedChange() {
      let isNeedCreate = true
      this.case_info.expected_list.forEach(item => {
        if (!item.extract_method && !item.extract_rule && !item.compare_type && !item.expected_value) {
          // 没有空行，需要创建空行
          isNeedCreate = false
        }
      })
      if (isNeedCreate) {
        this.case_info.expected_list.push({ enable: true, extract_method: '', extract_rule: '', compare_type: '', expected_value: '' })
      }
    },
    // 点击json的key，获取jsonpath和data
    resultTextClick(path) {
      this.result_json_path = path
    },
    // 复制jsonPath
    copyPath() {
      const input = document.createElement('input')
      input.value = this.result_json_path
      document.body.appendChild(input)
      input.select()
      document.execCommand('Copy')
      document.body.removeChild(input)
      this.$message.success('复制成功')
    },
    isJson(str) {
      if (typeof str !== 'string') {
        return false
      }
      try {
        const obj = JSON.parse(str)
        return !!(typeof obj === 'object' && obj)
      } catch (e) {
        return false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .edit {
    padding: 5px;

    .info-row {
      margin-bottom: 20px;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .info-cols {
        display: flex;
        align-items: center;
      }

      .title {
        color: #333333;
        font-weight: 500;
        font-size: 14px;
        width: 80px;
      }

      .category-col {
        display: flex;
        align-items: center;
        margin-right: 20px;
      }

      .name-col {
        display: flex;
        align-items: center;
        width: 400px;
      }

      .url-col {
        display: flex;
        align-items: center;
      }
    }

    .request {
      height: 210px;

      .request-param {
        height: 140px;
        transform: translateY(-10px);

        .kv-row {
          margin-top: 10px;

          .kv-checkbox {
            width: 20px;
            margin-right: 10px;
          }

          .kv-delete {
            width: 60px;
            margin-right: 10px;
          }

          .kv-select {
            width: 500px;
          }
        }
      }

      .request-body {
        width: 100%;
        height: 100%;
      }

    }
    .response {
      height: 360px;
      background-color: #FFFFFF;
      border: 1px solid #dfe2e8;
      box-shadow: 1px 3px 5px #DCDFE6;

      .response-header {
        background-color: #F6F7FA;
        height: 40px;
        display: flex;
        justify-content:space-between;
        align-items: center;
        border-bottom: 1px solid #dfe2e8;

        .response-title {
          font-size: 15px;
          color: #909399;
          margin-left: 10px;
        }

        .response-info-list {
          display: flex;
          align-items: center;
        }
      }

      .response-default-body {
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        .default-content {
          color: #909399;
          margin-top: 10px;
        }
      }

      .response-body {
        height: calc(100% - 40px);
        position: relative;

        .response-body-path {
          position: absolute;
          width: auto;
          top: 10px;
          right: 20px;
          color: #909399;
          z-index: 999;
        }

        .body-text-is-json {
          word-wrap: break-word;
          margin: 10px 15px 10px 10px;
        }

        .body-text-not-json {
          word-wrap: break-word;
          margin: 10px 15px 10px 10px;
          color: #909399;
        }
      }
    }
  }
</style>
