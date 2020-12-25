<template>
  <div class="container">
    <div class="header">
      <div class="header-left">
        <el-button type="primary" @click="createProject">创建项目 <i class="el-icon-circle-plus-outline
"></i></el-button>
      </div>
      <div class="header-right">
        <lyn-search v-model="search.name" placeholder="项目名筛选" @change="searchProject"></lyn-search>
        <el-pagination background
                       @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="search.page"
                       :page-size="search.count"
                       :page-sizes="[10, 20, 50, 100]"
                       :total="total" layout="total, sizes, prev, pager, next"/>
      </div>
    </div>
    <el-table class="adjust-table" :data="project_list" style="width: 100%">
      <el-table-column prop="name" label="项目名" width="250" show-overflow-tooltip/>
      <el-table-column prop="description" label="描述" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-tooltip effect="dark" placement="top-start" v-if="scope.row.description">
            <div slot="content">{{scope.row.description}}</div>
            <l-icon name="read" color="#409EFF" height="1.6em" width="1.6em"></l-icon>
          </el-tooltip>
          <l-icon name="read" color="#ccc" height="1.6em" width="1.6em" v-else></l-icon>
        </template>
      </el-table-column>
      <el-table-column
        sortable
        prop="create_time"
        label="创建时间"
        show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        sortable
        prop="update_time"
        label="更新时间"
        show-overflow-tooltip>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
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
    <el-dialog :close-on-click-modal="false" :visible.sync="dialog_visible" destroy-on-close @close="closeEdit" width="400px">
      <el-form :model="project_info" ref="project_info" label-position="right" label-width="80px" size="small">
        <el-form-item label="名称" prop="name">
          <el-input v-model="project_info.name" autocomplete="off" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input :autosize="{ minRows: 3, maxRows: 5}" type="textarea" v-model="project_info.description" style="width: 240px"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveProject">保 存</el-button>
        <el-button @click="closeEdit">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import LynSearch from '@/component/lyn/lyn-search'
import Project from '@/model/track/project'

export default {
  name: 'project',
  components: {
    LynSearch,
  },
  data() {
    return {
      dialog_visible: false,
      search: {
        name: '',
        count: 10,
        page: 1,
      },
      total: 0,
      operation_type: null,
      project_list: [],
      project_info: {},
      default_project_info: {
        name: '',
        description: '',
      },
      popover_visible: false,
    }
  },
  methods: {
    async searchProject() {
      const res = await Project.searchProjectList(this.search)
      this.project_list = res.items
      this.total = res.total
    },
    createProject() {
      this.dialog_visible = true
      this.operation_type = 1
      this.project_info = { ...this.default_project_info }
    },
    handleEdit(row) {
      this.dialog_visible = true
      this.operation_type = 2
      this.project_info = { ...row }
    },
    async handleDelete(id) {
      this.$confirm('删除该项目同时，该项目下所有测试资源将同步删除，确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(
          async () => {
            const res = await Project.deleteProject(id)
            console.log(res)
            if (res.code < window.MAX_SUCCESS_CODE) {
              this.$message.success(res.message)
              this.searchProject()
            } else {
              this.$message.error('删除功能异常')
            }
          }
        )
    },
    async saveProject() {
      const res = this.operation_type === 1 ? await Project.createProject(this.project_info) : await Project.updateProject(this.project_info)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.dialog_visible = false
        this.searchProject()
      } else {
        this.$message.error('服务端异常')
      }
    },
    handleSizeChange(val) {
      this.search.count = val
      this.searchProject()
    },
    handleCurrentChange(val) {
      this.search.page = val
      this.searchProject()
    },
    closeEdit() {
      this.dialog_visible = false
      this.$refs.project_info.resetFields()
    },
  },
  mounted() {
    this.searchProject()
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
      margin-top: 10px;
      margin-bottom: 10px;

      .header-left {
        float: left;
      }

      .header-right {
        float: right;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
    }
  }
</style>
