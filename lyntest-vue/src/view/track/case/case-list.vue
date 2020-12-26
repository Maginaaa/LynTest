<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="body">
    <el-card class="project-node-card">
      <select-menu
              :data="projects"
              :current-data="currentProject"
              @dataChange="changeProject"/>
      <node-tree
              class="node-tree"
              @nodeSelectEvent="nodeChange"
              @refresh="refresh"
              :tree-nodes="treeNodes"
              :type="'edit'"
              :select-node.sync="selectNode"
              @refreshTable="refreshTable"
              :current-project="currentProject"
              draggable
              ref="nodeTree"/>
    </el-card>
    <el-card class="case-manage-card">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item>
          <a><i @click="bachHome" class="el-icon-s-home breadcrumb"></i></a>
        </el-breadcrumb-item>
        <el-breadcrumb-item v-for="node in selectParentNodes" :key="node.id">
          <a class="breadcrumb">{{node.name}}</a>
        </el-breadcrumb-item>
      </el-breadcrumb>
      <el-row class="title_row">
        <div class="header-left">
          <el-button @click="handleCreate" type="primary"
                     :disabled="selectNodeIds.length === 0"
                     class="create-button">创建用例</el-button>
        </div>
        <div class="header-right">
          <lyn-search v-model="search.case_name" placeholder="用例名筛选" @change="refreshTable"></lyn-search>
          <el-pagination background
                         @size-change="handleSizeChange"
                         @current-change="handleCurrentChange"
                         :current-page="search.page"
                         :page-size="search.count"
                         :page-sizes="[10, 20, 50, 100]"
                         :total="total" layout="total, sizes, prev, pager, next"/>
        </div>
      </el-row>
      <el-table :data="caseList" stripe class="case-table">
        <el-table-column prop="id" label="id" width="60" show-overflow-tooltip></el-table-column>
        <el-table-column prop="name" label="用例名称" show-overflow-tooltip width="120"></el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip width="60">
          <template v-slot:default="scope">
            <el-tooltip effect="dark" placement="top-start" v-if="scope.row.description">
              <div slot="content">{{scope.row.description}}</div>
              <l-icon name="read" color="#409EFF" height="1.6em" width="1.6em"></l-icon>
            </el-tooltip>
            <l-icon name="read" color="#ccc" height="1.6em" width="1.6em" v-else></l-icon>
          </template>
        </el-table-column>
        <el-table-column
                prop="priority"
                column-key="priority"
                label="优先级"
                show-overflow-tooltip
                :filters="priorityList"
                :filter-method="priorityFilter"
                filter-placement="bottom-end"
                width="120">
          <template v-slot:default="scope">
            <span>
              <el-tag :type="getPriorityStyle(scope.row.priority)" effect="dark">{{ scope.row.priority }}</el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column
                prop="type"
                label="类型"
                show-overflow-tooltip
                width="120">
        </el-table-column>

        <el-table-column
                prop="review_status"
                label="评审状态"
                :filters="reviewStatusList"
                :filter-method="reviewStatusFilter"
                filter-placement="bottom-end"
                width="120">
          <template v-slot:default="scope">
            <span>
              <el-tag v-if="scope.row.review_status === 'Prepare'" type="info">未评审</el-tag>
              <el-tag v-if="scope.row.review_status === 'Pass'" type="success">通过</el-tag>
              <el-tag v-if="scope.row.review_status === 'UnPass'" type="danger">未通过</el-tag>
            </span>
          </template>
        </el-table-column>

        <el-table-column
                prop="update_time"
                label="更新时间"
                show-overflow-tooltip>
        </el-table-column>
        <el-table-column fixed="right" width="160" label="操作">
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
            <el-tooltip content="复制"
                        placement="bottom"
                        :enterable="false"
                        effect="dark">
              <el-button
                      size="small"
                      type="warning"
                      icon="el-icon-document-copy"
                      circle
                      @click="handleCopy(scope.row)"></el-button>
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
    </el-card>

    <el-dialog :close-on-click-modal="false"
               :title="operationType === 1 ? '创建用例' : '用例编辑'"
               :visible.sync="editDialogVisible" width="65%">
      <el-form :model="caseInfo"  ref="caseInfo" label-width="80px">
        <!--用例基础信息-->
        <el-row>
          <el-col :span="17" :offset="1">
            <el-form-item
                    label="用例名称"
                    prop="name">
              <el-input class="case-name" placeholder="请输入用例名称" v-model="caseInfo.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="10" :offset="1">
            <el-form-item label="用例类型"  prop="type">
              <el-select v-model="caseInfo.type" clearable>
                <el-option v-for="item in typeList" :label="item" :value="item" :key="item.index"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用例等级"  prop="priority">
              <el-select v-model="caseInfo.priority" clearable>
                <el-option v-for="item in priorityList" :label="item.test" :value="item.value" :key="item.index"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!--测试步骤-->
        <el-row style="margin-bottom: 10px">
          <el-col :offset="1">
            <div class="el-form-item__label">操作步骤:</div>
          </el-col>
        </el-row>
        <el-row type="flex">
          <el-col :span="22" :offset="1">
            <el-table
                    :data="caseInfo.steps"
                    class="tb-edit"
                    border
                    size="mini"
                    highlight-current-row>
              <el-table-column label="编码" prop="num" min-width="10%"></el-table-column>
              <el-table-column label="步骤描述" prop="description" min-width="35%">
                <template v-slot:default="scope">
                  <el-input
                          class="table-edit-input"
                          size="mini"
                          type="textarea"
                          :autosize="{ minRows: 1, maxRows: 6}"
                          :rows="2"
                          v-model="scope.row.desc"
                          placeholder="请输入内容"
                          clearable/>
                </template>
              </el-table-column>
              <el-table-column label="预期结果" prop="result" min-width="35%">
                <template v-slot:default="scope">
                  <el-input
                          class="table-edit-input"
                          size="mini"
                          type="textarea"
                          :autosize="{ minRows: 1, maxRows: 6}"
                          :rows="2"
                          v-model="scope.row.result"
                          placeholder="请输入内容"
                          clearable/>
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="20%">
                <template v-slot:default="scope">
                  <el-button
                          type="primary"
                          icon="el-icon-plus"
                          circle size="mini"
                          @click="handleAddStep(scope.$index, scope.row)"></el-button>
                  <el-button
                          icon="el-icon-document-copy"
                          type="success"
                          circle size="mini"
                          @click="handleCopyStep(scope.$index, scope.row)"></el-button>
                  <el-button
                          type="danger"
                          icon="el-icon-delete"
                          circle size="mini"
                          @click="handleDeleteStep(scope.$index, scope.row)"
                          :disabled="scope.$index === 0 && caseInfo.steps.length <= 1"></el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>

        <!--备注-->
        <el-row style="margin-top: 15px;margin-bottom: 10px">
          <el-col :offset="1">
            <div class="el-form-item__label">备注:</div>
          </el-col>
        </el-row>
        <el-row type="flex">
          <el-col :span="22" :offset="1">
            <el-form-item prop="description" label-width="0">
              <el-input v-model="caseInfo.description"
                        :autosize="{ minRows: 3, maxRows: 5}"
                        type="textarea"
                        :rows="3"
                        placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveCase">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import LynSearch from '@/component/lyn/lyn-search'
import SelectMenu from '@/component/common/select-menu'
import NodeTree from '@/component/common/node-tree'
import Project from '@/model/track/project'
import Node from '@/model/track/node'
import TestCase from '@/model/track/case'

export default {
  name: 'case-list',
  components: {
    SelectMenu,
    NodeTree,
    LynSearch
  },
  data() {
    return {
      search: {
        case_name: '',
        count: 10,
        page: 1,
      },
      total: 0,
      result: {},
      projects: [],
      currentProject: null,
      treeNodes: [],
      selectNodeIds: [],
      selectParentNodes: [],
      selectNode: {},
      caseList: [],
      operationType: null,
      editDialogVisible: false,
      caseInfo: {},
      defaultCaseInfo: {
        name: '',
        type: '',
        priority: '',
        steps: [{ num: 1, result: '', steps: '', description: '' }]
      },
      typeList: ['功能测试', '性能测试', '接口测试', '冒烟测试', '回归测试', '其它'],
      priorityList: [{ text: 'P1', value: 'P1' },
        { text: 'P2', value: 'P2' },
        { text: 'P3', value: 'P3' },
        { text: 'P4', value: 'P4' }],
      reviewStatusList: [{ text: '未评审', value: 'Prepare' },
        { text: '通过', value: 'Pass' },
        { text: '未通过', value: 'UnPass' }],
    }
  },
  mounted() {
    this.pageInit()
  },
  methods: {
    async pageInit() {
      await this.getProjects()
      await this.getNodeTree()
      await this.refreshTable()
    },
    async getProjects() {
      const res = await Project.getNewProject(20)
      this.projects = res.items
      this.setCurrentProject(res.items[0])
    },
    async getNodeTree() {
      this.treeNodes = await Node.getNodeByProjectId(this.currentProject.id)
    },
    changeProject(project) {
      this.setCurrentProject(project)
      this.refreshTable()
    },
    nodeChange(nodeIds, pNodes) {
      this.selectNodeIds = nodeIds
      this.selectParentNodes = pNodes
      this.refreshTable()
    },
    bachHome() {
      this.selectNodeIds = []
      this.selectParentNodes = []
      this.selectNode = {}
      this.refreshTable()
    },
    handleCreate() {
      this.editDialogVisible = true
      this.caseInfo = { ...this.defaultCaseInfo }
      this.operationType = 1
    },
    handleEdit(row) {
      this.editDialogVisible = true
      this.caseInfo = { ...row }
      this.operationType = 2
    },
    handleCopy(row) {
      this.editDialogVisible = true
      this.caseInfo = { ...row }
      this.operationType = 1
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(
          async () => {
            const res = await TestCase.deleteCase(id)
            if (res.code < window.MAX_SUCCESS_CODE) {
              this.$message.success(res.message)
              this.refreshTable()
            } else {
              this.$message.error('删除功能异常')
            }
          }
        )
    },
    handleSizeChange(val) {
      this.search.count = val
      this.refreshTable()
    },
    handleCurrentChange(val) {
      this.search.page = val
      this.refreshTable()
    },
    priorityFilter(value, row) {
      return row.priority === value
    },
    getPriorityStyle(val) {
      if (val === 'P1' || val === 'P2') {
        return 'danger'
      }
      if (val === 'P3') {
        return 'warning'
      }
      if (val === 'P4') {
        return 'success'
      }
    },
    reviewStatusFilter(value, row) {
      return row.review_status === value
    },
    async refreshTable() {
      this.search.node_ids = this.selectNodeIds
      this.search.project_id = this.currentProject.id
      const res = await TestCase.getCaseList(this.search)
      res.items.forEach(e => {
        e.steps = JSON.parse(e.steps)
      })
      this.caseList = res.items
      this.total = res.total
    },
    setCurrentProject(project) {
      if (project) {
        this.currentProject = project
      }
      this.refresh()
    },
    refresh() {
      this.selectNodeIds = []
      this.selectParentNodes = []
      this.selectNode = {}
      this.getNodeTree()
    },
    handleAddStep(index, data) {
      const step = {}
      step.num = data.num + 1
      step.desc = ''
      step.result = ''
      this.caseInfo.steps.forEach(s => {
        if (s.num > data.num) {
          s.num++
        }
      })
      this.caseInfo.steps.splice(index + 1, 0, step)
    },
    handleCopyStep(index, data) {
      const step = {}
      step.num = data.num + 1
      step.desc = data.desc
      step.result = data.result
      this.caseInfo.steps.forEach(s => {
        if (s.num > data.num) {
          s.num++
        }
      })
      this.caseInfo.steps.splice(index + 1, 0, step)
    },
    handleDeleteStep(index, data) {
      this.caseInfo.steps.splice(index, 1)
      this.caseInfo.steps.forEach(step => {
        if (step.num > data.num) {
          step.num--
        }
      })
    },
    async saveCase() {
      const param = { ...this.caseInfo }
      const nodeId = this.selectNodeIds[0]
      param.steps = JSON.stringify(this.caseInfo.steps)
      if (!param.node_id) {
        param.node_id = nodeId
      }
      param.project_id = this.currentProject.id
      const res = this.operationType === 1 ? await TestCase.createCase(param) : await TestCase.updateCase(param)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.editDialogVisible = false
        this.refreshTable()
      } else {
        this.$message.error('服务端异常')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .body {
    display: flex;
    margin-top: 5px;

    .project-node-card {
      width: 22%;
      height: 700px;
      margin-left: 5px;
    }

    .case-manage-card {
      width: 78%;
      height: 700px;
      margin-left: 5px;
      margin-right: 5px;

      .breadcrumb {
        font-size: 1.6em
      }

      .title_row {
        margin-top: 10px;

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

      .case-table {
        margin-top: 10px;
      }
    }
  }
</style>
