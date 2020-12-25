<template>

  <el-dialog :title="type === 'edit' ? '重命名' : '添加'"
             :visible.sync="dialogFormVisible"
             :before-close="close"
             width="30%">

    <el-row type="flex" justify="center">
      <el-col :span="18">
        <el-form :model="form" ref="nodeForm" @submit.native.prevent>
          <el-form-item
            label="模块名称"
            :label-width="formLabelWidth"
            prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="saveNode" @keydown.enter.native.prevent>保 存</el-button>
    </span>


  </el-dialog>

</template>

<script>
import Node from '@/model/track/node'

export default {
  data() {
    return {
      name: 'node-edit',
      form: {
        name: '',
      },
      type: '',
      node: {},
      nodeIds: [],
      formLabelWidth: '80px',
      dialogFormVisible: false,
    }
  },
  props: {
    treeNodes: {
      type: Array
    },
    currentProject: {
      type: Object
    }
  },
  methods: {
    open(type, data, nodeIds) {
      this.type = type
      this.node = data
      if (type === 'edit') {
        this.form.name = this.node.name
      }
      this.nodeIds = nodeIds
      this.dialogFormVisible = true
    },
    async saveNode() {
      const param = {}
      param.name = this.form.name.trim()
      param.label = this.form.name
      param.project_id = this.currentProject.id
      let res
      if (this.type === 'add') {
        param.level = 1
        if (this.node) {
          // 非根节点
          param.parent_id = this.node.id
          param.level = this.node.level + 1
        }
        res = await Node.createNode(param)
      } else if (this.type === 'edit') {
        param.id = this.node.id
        param.level = this.node.level
        param.nodeIds = this.nodeIds
        res = await Node.updateNode(param)
      }
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.$emit('refresh')
        this.close()
      } else {
        this.$message.error('服务端异常')
      }
    },
    close() {
      this.form.name = ''
      this.dialogFormVisible = false
    }
  }
}
</script>

<style scoped>

</style>
