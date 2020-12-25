<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="container">
    <div class="header">
      <el-button type="primary" @click="createCategory">新 增</el-button>
    </div>
    <el-table :data="category_list" stripe>
      <el-table-column prop="category_name" label="分类"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
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
      <el-form :model="category_info" ref="categoryForm" label-width="80px">
        <el-form-item prop="category_name" label="分类名">
          <el-input v-model="category_info.category_name" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="描述">
          <el-input v-model="category_info.description" style="width: 240px"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveCategory">保 存</el-button>
        <el-button @click="closeEdit">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>


<script>
import Category from '@/model/autotest/category'

export default {
  data() {
    return {
      operation_type: 1,
      category_list: [],
      category_info: {
        category_name: '',
        description: '',
      },
      dialog_visible: false
    }
  },
  mounted() {
    this.getCategoryList()
  },
  methods: {
    // 获取分类列表
    async getCategoryList() {
      const res = await Category.getCategoryList()
      this.category_list = res
    },
    createCategory() {
      this.dialog_visible = true
      this.operation_type = 1
    },
    async saveCategory() {
      const res = this.operation_type === 1 ? await Category.createCategory(this.category_info) : await Category.updateCategory(this.category_info)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.dialog_visible = false
        this.getCategoryList()
      } else {
        this.$message.error('服务端异常')
      }
    },
    handleEdit(row) {
      this.dialog_visible = true
      this.operation_type = 2
      this.category_info = { ...row }
    },
    async handleDelete(id) {
      const res = await Category.deleteCategory(id)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.getCategoryList()
      } else {
        this.$message.error('删除功能异常')
      }
    },
    closeEdit() {
      this.dialog_visible = false
      this.$refs.categoryForm.resetFields()
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
