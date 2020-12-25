<template>
  <div class="mock-container">
    <div class="mock-header">
      <div class="header-left">
        <el-button type="primary" @click="createMockApi">新 增</el-button>
      </div>
      <div class="header-right">
        <lyn-search v-model="search.path" placeholder="输入请求地址" @change="searchMockApiList"></lyn-search>
        <el-pagination background
                       @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="search.page"
                       :page-size="search.count"
                       :page-sizes="[10, 20, 50, 100]"
                       :total="total" layout="total, sizes, prev, pager, next"/>
      </div>
    </div>
    <el-table :data="mock_list" stripe>
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column prop="method" min-width="100" label="请求方式">
        <template slot-scope="scope">
          <div :class="scope.row.method">{{scope.row.method}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="备注"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            plain
            @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            plain
            @click="handleDelete(scope.row.id)">删除</el-button>
          <el-button
            size="mini"
            type="info"
            plain
            @click="handleCopy(scope.row.path)">复制路径</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-drawer
      title="我是标题"
      :visible.sync="drawer_visible"
      :with-header="drawer_title"
      size="60%"
      >
      <div class="mock_case" v-if="drawer_visible">
        <div class="base_info">
          <el-input style="width: 70%" placeholder="请输入URL地址" v-model="mock_info.path">
            <el-select style="width: 90px" v-model="mock_info.method" slot="prepend">
              <el-option v-for="item in method_list" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-input>
          <el-button type="primary" @click="saveMockApi">Save <i class="el-icon-download"/></el-button>
        </div>
        <el-tabs v-model="req_active_tab" type="border-card" style="margin-top: 10px;">
          <el-tab-pane label="请求体" name="request_body">
              <codemirror v-model="mock_info.request_body"  :options="codemirror_options" ></codemirror>
          </el-tab-pane>
          <el-tab-pane label="描述" name="description">
            <div style="height: 300px">
              <el-input type="textarea" :rows="14" placeholder="请输入描述" v-model="mock_info.description" resize="none"></el-input>
            </div>
          </el-tab-pane>
        </el-tabs>

        <el-tabs v-model="res_active_tab" type="border-card" style="margin-top: 10px;">
          <el-tab-pane label="响应头" name="response_header">
            <div style="height: 300px">
              测试响应头
            </div>
          </el-tab-pane>
          <el-tab-pane label="响应体" name="response_body">
            <codemirror v-model="mock_info.response_body"  :options="codemirror_options"></codemirror>
          </el-tab-pane>
          <el-tab-pane label="HTTP状态码" name="code">

            <div style="height: 300px">
              <el-input v-model="mock_info.response_code"></el-input>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

    </el-drawer>
  </div>
</template>

<script>
import { codemirror } from 'vue-codemirror'
import Mock from '@/model/mock'
import LynSearch from '@/component/lyn/lyn-search'
// require css
/* eslint-disable */
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/theme/cobalt.css'

export default {
  components: {
    codemirror,
    LynSearch
  },
  data() {
    return {
      search: {
        path: '',
        count: 10,
        page: 1,
      },
      total: 0,
      operation_type: 0,
      mock_list: [],
      mock_info: {},
      default_mock_info: {
        path: '',
        method: 'POST',
        description: '',
      },
      drawer_visible: false,
      drawer_title: false,
      method_list: ['POST', 'GET', 'PUT', 'DELETE'],
      req_active_tab: 'request_body',
      res_active_tab: 'response_body',
      codemirror_options: {
        tabSize: 4,
        mode: 'text/javascript',
        theme: 'cobalt',
        lineNumbers: true,
        line: true,
      },
    }
  },
  mounted() {
    this.searchMockApiList()
  },
  methods: {
    async searchMockApiList() {
      const res = await Mock.searchMockList(this.search)
      this.mock_list = res.items
      this.total = res.total
    },
    handleSizeChange(val) {
      this.search.count = val
      this.searchMockApiList()
    },
    handleCurrentChange(val) {
      this.search.page = val
      this.searchMockApiList()
    },
    createMockApi() {
      this.drawer_visible = true
      this.operation_type = 1
      this.mock_info = { ...this.default_mock_info }
    },
    async saveMockApi() {
      const res = this.operation_type === 1 ? await Mock.createMock(this.mock_info) : await Mock.updateMock(this.mock_info)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.drawer_visible = false
        this.searchMockApiList()
      } else {
        this.$message.error('服务端异常')
      }
    },
    handleEdit(row) {
      this.drawer_visible = true
      this.operation_type = 2
      this.mock_info = { ...row }
    },
    async handleDelete(id) {
      const res = await Mock.deleteMock(id)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.searchMockApiList()
      } else {
        this.$message.error('删除功能异常')
      }
    },
    handleCopy(value) {
      const input = document.createElement('input')
      input.value = `${process.env.VUE_APP_BASE_URL}/mock/dynamic${value}`
      document.body.appendChild(input)
      input.select()
      document.execCommand('Copy')
      document.body.removeChild(input)
      this.$message.success('已复制路径至剪贴板')
    }
  }
}
</script>

<style lang="scss" scoped>
  .mock-container {
    padding: 0 30px;

    .mock-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 10px;
      margin-bottom: 10px;

      .header-left {
        float: left;

        .name {
          width: 200px;
          margin-right: 10px;
        }
      }

      .header-right {
        float: right;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
    }

    .GET {
      color: #00C292;
      font-weight: 500;
    }
    .POST {
      color: #409EFF;
      font-weight: 500;
    }
    .PUT {
      color: #E6A23C;
      font-weight: 500;
    }
    .DELETE {
      color: #E46A76;
      font-weight: 500;
    }

    .mock_case {
      display: flex;
      flex-direction: column;
      margin: 20px;

      .base_info {
        display: flex;
        justify-content: space-between;
      }

      .tabs {
        height: 300px;
      }
    }
  }
</style>
