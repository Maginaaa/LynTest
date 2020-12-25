<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <!--筛选框-->
    <el-card style="margin-bottom: 5px;margin-top: 5px">
      <el-input v-model="search.case_name"
                placeholder="接口名称"
                style="width: 150px"
                @change="searchCaseList" />
      <el-select v-model="search.category"
                 placeholder="分 类"
                 style="width: 150px;margin-left: 10px"
                 @change="searchCaseList"
                 clearable
                 filterable>
        <el-option v-for="item in category_list"
                   :key="item.category_name"
                   :label="item.category_name"
                   :value="item.category_name"/>
      </el-select>
      <el-select v-model="search.creator_code"
                 placeholder="创建人"
                 style="width: 150px;margin-left: 10px"
                 @change="searchCaseList"
                 clearable
                 filterable>
        <el-option v-for="item in creator_list"
                   :key="item.creator_code"
                   :label="item.creator_name"
                   :value="item.creator_code">
          <span style="float: left">{{ item.creator_name }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.creator_code }}</span>
        </el-option>
      </el-select>
    </el-card>
    <!--form表单-->
    <el-card>
      <!--操作栏-->
      <el-row>
        <el-button type="primary" size="small" @click="createCase">新 增</el-button>
        <el-button type="primary" size="small" @click="confirmDeleteCase">删 除</el-button>
        <el-pagination background
                       @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="search.page"
                       :page-size="search.count"
                       :page-sizes="[10, 20, 50, 100]"
                       :total="total" layout="total, sizes, prev, pager, next" style="float: right">
        </el-pagination>
        <el-dialog :visible.sync="table.deleteConfirmDialogVisible" width="400px">
          <span>确认删除？</span>
          <span slot="footer" class="dialog-footer">
              <el-button @click="table.deleteConfirmDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="deleteCase">确 定</el-button>
            </span>
        </el-dialog>
      </el-row>
      <br/>
      <!--case数据表单-->
      <el-row>
        <el-table ref="multipleTable" :data="table_data" tooltip-effect="dark" @selection-change="tableSelectionChange" stripe>
          <el-table-column type="selection" width="35" fixed/>
          <el-table-column prop="case_name" label="接口名称" min-width="200" fixed></el-table-column>
          <el-table-column prop="api_method" min-width="100" label="请求方式">
            <template v-slot:default="scope">
              <div :class="scope.row.api_method">{{scope.row.api_method}}</div>
            </template>
          </el-table-column>
          <el-table-column label="接口路径" prop="api_url" :show-overflow-tooltip="true" min-width="440"></el-table-column>
          <el-table-column prop="category" label="分 类" min-width="100" sortable/>
          <el-table-column prop="creator_name" label="创建人" width="120"/>
          <el-table-column label="备注">
            <template v-slot:default="scope">
              <el-tooltip effect="dark" placement="top-start" v-if="scope.row.description">
                <div slot="content">{{scope.row.description}}</div>
                <l-icon name="read" color="#409EFF" height="1.6em" width="1.6em"></l-icon>
              </el-tooltip>
              <l-icon name="read" color="#ccc" height="1.6em" width="1.6em" v-else></l-icon>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            fixed="right"
            label="操作">
            <template v-slot:default="scope">
              <el-tooltip content="编辑"
                          placement="bottom"
                          :enterable="false"
                          effect="dark">
                <el-button
                  type="primary"
                  icon="el-icon-edit"
                  circle size="mini"
                  @click="handleEdit(scope.row)"></el-button>
              </el-tooltip>
              <el-tooltip content="复制"
                          placement="bottom"
                          :enterable="false"
                          effect="dark">
                <el-button
                  type="warning"
                  icon="el-icon-document-copy"
                  circle size="mini"
                  @click="handleCopy(scope.row)"></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </el-card>
    <!--case编辑弹出层-->
    <el-drawer :visible.sync="drawer_visible" :with-header="false" size="70%">
      <http-case-edit v-if="drawer_visible" :case-info="case_info" :operation-type="operation_type" @drawer-close="drawerClose"></http-case-edit>
    </el-drawer>
  </div>
</template>

<script>
import HttpCase from '@/model/autotest/http-case'
import HttpCaseEdit from '@/component/autotest/http-case-edit'
import Category from '@/model/autotest/category'

export default {
  components: {
    HttpCaseEdit,
  },
  data() {
    return {
      operation_type: 0,
      search: {
        case_name: '',
        category: '',
        creator_code: '',
        page: 1,
        count: 10,
      },
      creator_list: [],
      category_list: [],
      edit_dialog_visible: false,
      case_info: {},
      total: null,
      table: {
        deleteConfirmDialogVisible: false,
        multipleSelection: [],
        ids: [],
      },
      table_data: [],
      drawer_visible: false,
    }
  },
  mounted() {
    this.getCategoryList()
    this.getCreatorList()
    this.searchCaseList()
  },
  methods: {
    // 获取分类列表
    async getCategoryList() {
      const res = await Category.getCategoryList()
      this.category_list = res
    },
    // 获取创建人列表
    async getCreatorList() {
      const res = await HttpCase.getCreatorList()
      this.creator_list = res
    },
    // 搜索符合条件的所有testCase
    async searchCaseList() {
      const res = await HttpCase.searchCaseList(this.search)
      this.table_data = res.items
      this.total = res.total
    },
    // 创建case
    createCase() {
      this.case_info = { ...this.default_case_info }
      this.drawer_visible = true
      this.operation_type = 1
    },
    async saveCase() {
      const res = await HttpCase.createCase(this.case_info)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(`${res.message}`)
        this.searchCaseList()
      } else {
        this.$message.error(res.message)
      }
    },
    confirmDeleteCase() {
      if (this.table.multipleSelection.length > 0) {
        this.table.deleteConfirmDialogVisible = true
      } else {
        this.$message.warning('请选择至少一条case！')
      }
    },
    async deleteCase() {
      this.table.ids = []
      this.table.multipleSelection.forEach(e => {
        this.table.ids.push(e.id)
      })
      const res = await HttpCase.deleteCase(this.table.ids)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(`${res.message}`)
        this.table.deleteConfirmDialogVisible = false
        this.searchCaseList()
      } else {
        this.$message.error(res.message)
      }
    },
    handleSizeChange(val) {
      this.search.count = val
      this.searchCaseList()
    },
    handleCurrentChange(val) {
      this.search.page = val
      this.searchCaseList()
    },
    // 选中的case
    tableSelectionChange(value) {
      this.table.multipleSelection = value
    },
    handleEdit(row) {
      this.drawer_visible = true
      this.operation_type = 2
      this.case_info = { ...row }
    },
    handleCopy(row) {
      this.drawer_visible = true
      this.operation_type = 3
      this.case_info = { ...row }
    },
    drawerClose() {
      this.drawer_visible = false
      this.searchCaseList()
    }
  },
}
</script>

<style scoped>
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
</style>
