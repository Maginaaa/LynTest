<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div v-loading="result.loading">
    <el-input placeholder="模块搜索" v-model="filterText" size="small">
      <template v-if="type === 'edit'" v-slot:append>
        <el-button :disabled="disabled" icon="el-icon-folder-add" @click="openEditNodeDialog('add')"></el-button>
      </template>
    </el-input>

    <el-tree
      class="node-tree"
      :data="treeNodes"
      :default-expanded-keys="expandedNode"
      node-key="id"
      @node-drag-end="handleDragEnd"
      @node-expand="nodeExpand"
      @node-collapse="nodeCollapse"
      :filter-node-method="filterNode"
      :expand-on-click-node="false"
      highlight-current
      :draggable="draggable"
      :props="defaultProps"
      ref="tree">
      <template v-slot:default="{node,data}">
        <span class="custom-tree-node father" @click="handleNodeSelect(node)">
          <span class="node-icon">
            <i class="el-icon-folder"></i>
          </span>
          <!--如果没修改过字段，这里对应后端应该使用node.name-->
          <span class="node-title">{{ node.label }}</span>

          <span v-if="type === 'edit' && !disabled" class="node-operate child">
            <el-tooltip
              class="item"
              effect="dark"
              :open-delay="200"
              content="重命名"
              placement="top">
              <i @click.stop="openEditNodeDialog('edit', data)" class="el-icon-edit"></i>
            </el-tooltip>
            <el-tooltip
              class="item"
              effect="dark"
              :open-delay="200"
              content="添加子模块"
              placement="top">
              <i @click.stop="openEditNodeDialog('add', data)" class="el-icon-circle-plus-outline"></i>
            </el-tooltip>
            <el-tooltip class="item" effect="dark"
                        :open-delay="200" content="删除" placement="top">
              <i @click.stop="remove(node, data)" class="el-icon-delete"></i>
            </el-tooltip>
          </span>
        </span>
      </template>
    </el-tree>
    <node-edit ref="nodeEdit" :current-project="currentProject" :tree-nodes="treeNodes" @refresh="refreshNode" />
    <el-dialog :visible.sync="confirmDeleteDialog" width="30%">
      <span>删除模块同时,其子模块及测试用例会全被删除，是否继续删除？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="confirmDeleteDialog = false">取 消</el-button>
        <el-button type="primary" @click="confirmRemove">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import NodeEdit from './node-edit'
import Node from '@/model/track/node'

export default {
  name: 'node-tree',
  components: { NodeEdit },
  data() {
    return {
      result: {},
      expandedNode: [],
      filterText: '',
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      disabled: false,
      list: [],
      confirmDeleteDialog: false,
      removeNodeIds: [],
    }
  },
  props: {
    type: {
      type: String,
      default: 'view'
    },
    treeNodes: {
      type: Array
    },
    selectNode: {
      type: Object
    },
    draggable: {
      type: Boolean,
      default: true
    },
    currentProject: {
      type: Object
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  methods: {
    async handleDragEnd(draggingNode, dropNode, dropType) {
      if (dropType === 'none' || dropType === undefined) {
        return
      }
      const param = this.buildParam(draggingNode, dropNode, dropType)

      this.list = []
      this.getNodeTree(this.treeNodes, draggingNode.data.id, this.list)
      const res = await Node.dragNode(param)
      if (res.code < window.MAX_SUCCESS_CODE) {
        await Node.nodePosition(this.list)
        this.$emit('refresh')
      } else {
        this.$message.error('服务端异常')
      }
    },
    buildParam(draggingNode, dropNode, dropType) {
      let param
      if (dropNode.level === 1 && dropType !== 'inner') {
        param = draggingNode.data
      } else {
        this.treeNodes.some(node => {
          param = this.findTreeByNodeId(node, dropNode.data.id)
          return param
        })
      }
      return param
    },
    getNodeTree(nodes, id, list) {
      if (!nodes) {
        return
      }
      for (let i = 0; i < nodes.length; i++) {
        if (nodes[i].id === id) {
          list[0] = i - 1 >= 0 ? nodes[i - 1].id : 0
          list[1] = nodes[i].id
          list[2] = i + 1 < nodes.length ? nodes[i + 1].id : 0
          return
        }
        if (nodes[i].children) {
          this.getNodeTree(nodes[i].children, id, list)
        }
      }
    },
    refreshTable() {
      this.$emit('refreshTable')
    },
    findTreeByNodeId(rootNode, nodeId) {
      if (rootNode.id === nodeId) {
        return rootNode
      }
      if (rootNode.children) {
        for (let i = 0; i < rootNode.children.length; i++) {
          if (this.findTreeByNodeId(rootNode.children[i], nodeId)) {
            return rootNode
          }
        }
      }
    },
    remove(node) {
      const nodeIds = []
      this.getChildNodeId(node.data, nodeIds)
      this.removeNodeIds = nodeIds
      this.confirmDeleteDialog = true
    },
    async confirmRemove() {
      const res = await Node.deleteNodes(this.removeNodeIds)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.$emit('refresh')
        this.confirmDeleteDialog = false
      } else {
        this.$message.error('服务端异常')
      }
    },
    handleNodeSelect(node) {
      const nodeIds = []
      const pNodes = []
      this.getChildNodeId(node.data, nodeIds)
      this.getParentNodes(node, pNodes)
      this.$emit('nodeSelectEvent', nodeIds, pNodes)
      this.$emit('update:selectNode', node)
    },
    getChildNodeId(rootNode, nodeIds) {
      // 递归获取所有子节点ID
      nodeIds.push(rootNode.id)
      if (rootNode.children) {
        for (let i = 0; i < rootNode.children.length; i++) {
          this.getChildNodeId(rootNode.children[i], nodeIds)
        }
      }
    },
    getParentNodes(rootNode, pNodes) {
      if (rootNode.parent && rootNode.parent.id !== 0) {
        this.getParentNodes(rootNode.parent, pNodes)
      }
      if (rootNode.data.name && rootNode.data.name !== '') {
        pNodes.push(rootNode.data)
      }
    },
    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },
    openEditNodeDialog(type, data) {
      const nodeIds = []
      if (type === 'edit') {
        this.getChildNodeId(data, nodeIds)
      }
      this.$refs.nodeEdit.open(type, data, nodeIds)
    },
    refreshNode() {
      this.$emit('refresh')
    },
    nodeExpand(data) {
      if (data.id) {
        this.expandedNode.push(data.id)
      }
    },
    nodeCollapse(data) {
      if (data.id) {
        this.expandedNode.splice(this.expandedNode.indexOf(data.id), 1)
      }
    }
  }
}
</script>

<style scoped>

  .custom-tree-node {
    flex: 1 1 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
    width: 100%;
  }

  .node-tree {
    margin-top: 15px;
  }

  .father .child {
    display: none;
  }

  .father:hover .child {
    display: block;
  }

  .node-title {
    width: 0;
    text-overflow: ellipsis;
    white-space: nowrap;
    flex: 1 1 auto;
    padding: 0 5px;
    overflow: hidden;
  }

  .node-operate > i {
    color: #409eff;
    margin: 0 5px;
  }
</style>
