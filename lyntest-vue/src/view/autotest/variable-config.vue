<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="container">
    <div class="header">
      <el-button type="primary" @click="createVariable">新 增</el-button>
      <el-pagination background
                     @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="search.page"
                     :page-size="search.count"
                     :page-sizes="[10, 20, 50, 100]"
                     :total="total" layout="total, sizes, prev, pager, next"/>
    </div>
    <el-table :data="variable_list" stripe>
      <el-table-column prop="variable_name" label="变量名"></el-table-column>
      <el-table-column prop="variable_value" label="真实值" show-overflow-tooltip></el-table-column>
      <el-table-column prop="description" label="备注"></el-table-column>
      <el-table-column label="操作">
        <template v-slot:default="scope">
          <el-tooltip content="编辑"
                      placement="bottom"
                      :enterable="false"
                      effect="dark">
            <el-button
              size="small"
              type="primary"
              icon="el-icon-edit"
              circle
              @click="handleEdit(scope.row)"></el-button>
          </el-tooltip>
          <el-tooltip content="删除"
                      placement="bottom"
                      :enterable="false"
                      effect="dark">
            <el-button
              size="small"
              type="danger"
              icon="el-icon-delete"
              circle
              @click="handleDelete(scope.row.id)"></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="dialog_visible" width="400px" @closed="closeEdit">
      <el-form :model="variable_info" ref="variableForm" label-width="80px">
        <el-form-item prop="variable_name" label="变量名">
          <el-input v-model="variable_info.variable_name" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item prop="variable_value" label="真实值">
          <el-input v-model="variable_info.variable_value" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="描述">
          <el-input v-model="variable_info.description" style="width: 240px"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveVariable">保 存</el-button>
        <el-button @click="closeEdit">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Variable from '@/model/autotest/variable'

export default {
  data() {
    return {
      search: {
        count: 10,
        page: 1,
      },
      total: 0,
      operation_type: 1,
      variable_list: [],
      variable_info: {
        variable_name: '',
        variable_value: '',
        description: '',
      },
      dialog_visible: false
    }
  },
  mounted() {
    this.searchVariableList()
  },
  methods: {
    // 获取分类列表
    async searchVariableList() {
      const res = await Variable.getVariableList(this.search)
      this.variable_list = res.items
      this.total = res.total
    },
    handleSizeChange(val) {
      this.search.count = val
      this.searchVariableList()
    },
    handleCurrentChange(val) {
      this.search.page = val
      this.searchVariableList()
    },
    createVariable() {
      this.dialog_visible = true
      this.operation_type = 1
    },
    async saveVariable() {
      const res = this.operation_type === 1 ? await Variable.createVariable(this.variable_info) : await Variable.updateVariable(this.variable_info)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.dialog_visible = false
        this.searchVariableList()
      } else {
        this.$message.error('服务端异常')
      }
    },
    handleEdit(row) {
      this.dialog_visible = true
      this.operation_type = 2
      this.variable_info = { ...row }
    },
    async handleDelete(id) {
      const res = await Variable.deleteVariable(id)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.searchVariableList()
      } else {
        this.$message.error('删除功能异常')
      }
    },
    closeEdit() {
      this.dialog_visible = false
      this.$refs.variableForm.resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
  .container {
    padding: 0 30px;

    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 59px;
    }
  }
</style>
