<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="container">
    <div class="header">
      <div class="title">{{collection_info.collection_name}} <i class="el-icon-edit" @click="collection_info_dialog_visible = true"></i></div>
      <el-button type="primary" size="small" icon="el-icon-setting" style="float: right" @click="config_dialog_visible = true"/>
    </div>
    <div class="body">
      <el-card class="pocket-card">
        <el-button type="success"
                   class="run-button"
                   @click="doExecute"
                   :disabled="pocket_list.length === 1 && current_choose_case_id_list.length === 0">
          执 行
          <i v-if="execute_button_loading" class="el-icon-loading"></i>
          <i v-else class="el-icon-video-play"></i>
        </el-button>
        <div class="tree-list">
          <vue-scroll>
            <el-tree :data="pocket_list" :props="defaultProps"
                     @node-click="handleNodeClick"
                     @node-drag-end="handleDragEnd"
                     :allow-drop="allowDrop"
                     :default-expanded-keys="default_expanded_key"
                     node-key="id"
                     draggable
                     highlight-current
                     accordion
                     ref="tree"
                     style="margin-top: 10px">
              <template v-slot:default="{node,data}">
            <span class="custom-tree-node father">
              <span v-if="node.level === 1" class="node-icon">
                <i class="el-icon-folder"></i>
              </span>
              <!--如果没修改过字段，这里对应后端应该使用node.name-->
              <span class="node-title">{{ node.label }}</span>
              <span v-if="node.level === 1" class="node-operate child">
                <el-tooltip
                  class="item"
                  effect="dark"
                  :open-delay="200"
                  content="新增模块"
                  placement="top">
                  <i @click.stop="createNewPocket" class="el-icon-circle-plus-outline"></i>
                </el-tooltip>
                <el-tooltip
                  class="item"
                  effect="dark"
                  :open-delay="200"
                  content="重命名"
                  placement="top">
                  <i @click.stop="updatePocket(data)" class="el-icon-edit"></i>
                </el-tooltip>
                <el-tooltip class="item" effect="dark"
                            :open-delay="200" content="删除" placement="top">
                  <i @click.stop="deletePocket(data.id)" class="el-icon-delete"></i>
                </el-tooltip>
              </span>
              <span v-if="node.level === 2" class="node-operate child">
                <el-tooltip class="item" effect="dark"
                            :open-delay="200" content="删除" placement="top">
                  <i @click.stop="deleteCase(data)" class="el-icon-delete"></i>
                </el-tooltip>
              </span>
            </span>
              </template>
            </el-tree>
          </vue-scroll>
        </div>

      </el-card>
      <el-card class="case-manage-card">
        <el-row>
          <el-input v-model="search.case_name"
                    placeholder="接口名称"
                    class="row-name"
                    size="small"
                    @change="refreshTableInfo" />
          <el-select v-model="search.category"
                     placeholder="分 类"
                     class="row-category"
                     @change="refreshTableInfo"
                     clearable
                     size="small"
                     filterable>
            <el-option v-for="item in category_list"
                       :key="item.category_name"
                       :label="item.category_name"
                       :value="item.category_name"/>
          </el-select>
          <el-select v-model="search.creator_code"
                     placeholder="创建人"
                     class="row-creator"
                     @change="refreshTableInfo"
                     clearable
                     size="small"
                     filterable>
            <el-option v-for="item in creator_list"
                       :key="item.creator_code"
                       :label="item.creator_name"
                       :value="item.creator_code">
              <span class="creator-name">{{ item.creator_name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.creator_code }}</span>
            </el-option>
          </el-select>
          <el-pagination background
                         small
                         @size-change="handleSizeChange"
                         @current-change="handleCurrentChange"
                         :current-page="search.page"
                         :page-size="search.count"
                         :page-sizes="[10, 20, 50, 100]"
                         :total="total" layout="sizes, prev, pager, next" style="float: right">
          </el-pagination>
        </el-row>
        <div class="case-table">
          <vue-scroll>
            <el-table ref="multipleTable"
                      :data="case_list"
                      tooltip-effect="dark"
                      @select-all="selectAll"
                      @select="rowSelect"
                      stripe>
              <el-table-column type="selection" width="35" fixed/>
              <el-table-column prop="case_name" label="接口名称" min-width="200" fixed></el-table-column>
              <el-table-column label="备注" fixed>
                <template v-slot:default="scope">
                  <el-tooltip effect="dark" placement="top-start" v-if="scope.row.description">
                    <div slot="content">{{scope.row.description}}</div>
                    <l-icon name="read" color="#409EFF" height="1.6em" width="1.6em"></l-icon>
                  </el-tooltip>
                  <l-icon name="read" color="#ccc" height="1.6em" width="1.6em" v-else></l-icon>
                </template>
              </el-table-column>
              <el-table-column prop="api_method" min-width="100" label="请求方式">
                <template v-slot:default="scope">
                  <div :class="scope.row.api_method">{{scope.row.api_method}}</div>
                </template>
              </el-table-column>
              <el-table-column label="接口路径" prop="api_url" :show-overflow-tooltip="true" min-width="440"></el-table-column>
              <el-table-column prop="category" label="分 类" min-width="100" sortable/>
              <el-table-column prop="creator_name" label="创建人" width="120"/>
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
                </template>
              </el-table-column>
            </el-table>
          </vue-scroll>
        </div>
      </el-card>
    </div>
    <el-dialog :visible.sync="collection_info_dialog_visible" width="400px" :close-on-click-modal="false">
      <el-form :model="collection_info" ref="collection_info" label-width="80px">
        <el-form-item prop="collection_name" label="集合名">
          <el-input v-model="collection_info.collection_name" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="描述">
          <el-input v-model="collection_info.description" style="width: 240px"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateCollection">保 存</el-button>
        <el-button @click="closeCollectionEdit">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="config_dialog_visible">
      <el-form label-width="80px">
        <el-form-item label="定时任务">
          <el-switch v-model="collection_info.timing_switch" active-color="#13ce66"></el-switch>
          <el-input placeholder="请输入cron表达式"
                    v-show="collection_info.timing_switch"
                    v-model="collection_info.cron"
                    clearable
                    style="width: 240px;margin-left: 20px">
            <el-button type="primary"
                       icon="el-icon-time"
                       slot="append"
                       @click="timing_dialog_visible = true"
                       v-if="collection_info.timing_switch"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="微信推送">
          <el-radio-group v-model="collection_info.push_type" @change="pushListChange">
            <el-radio v-for="item in push_type_list" :key="item.label" :label="item.label">{{item.value}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="推送列表">
          <el-select v-model="collection_info.push_list" multiple filterable :disabled="push_list_visible" placeholder="请选择" style="width: 200px">
            <el-option v-for="item in tester_list" :key="item.creator_code" :label="item.creator_name" :value="item.creator_code"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateCollection">保 存</el-button>
        <el-button @click="config_dialog_visible = false">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="pocket_dialog_visible" destroy-on-close width="400px">
      <el-form label-width="80px">
        <el-form-item label="模块名">
          <el-input v-model="pocket_info.pocket_name"
                    placeholder="请输入名称" style="width: 240px"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="pocketSave">保 存</el-button>
        <el-button @click="pocket_dialog_visible = false">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="timing_dialog_visible"
               destroy-on-close
               width="800px"
               :show-close="false"
               :close-on-click-modal="false">
      <lyn-cron v-model="collection_info.cron"></lyn-cron>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="timing_dialog_visible = false">确 定</el-button>
      </span>
    </el-dialog>
    <el-drawer :visible.sync="drawer_visible" :with-header="false" size="70%">
      <http-case-edit v-if="drawer_visible" :case-info="case_info" operation-type="2" @drawer-close="drawerClose"></http-case-edit>
    </el-drawer>
  </div>
</template>

<script>
import LynCron from '@/component/lyn/lyn-cron'
import Collection from '@/model/autotest/collection'
import HttpCase from '@/model/autotest/http-case'
import CollectionReport from '@/model/autotest/collection-report'
import Category from '@/model/autotest/category'
import HttpCaseEdit from '@/component/autotest/http-case-edit'

export default {
  components: {
    LynCron,
    HttpCaseEdit
  },
  data() {
    return {
      execute_button_loading: false,
      collection_id: null,
      current_pocket_id: null,
      collection_info: {},
      push_type_list: [{
        label: 1,
        value: '不推送'
      }, {
        label: 2,
        value: '失败时推送'
      }, {
        label: 3,
        value: '总是推送'
      }],
      push_list_visible: false,
      collection_info_dialog_visible: false,
      config_dialog_visible: false,
      pocket_dialog_visible: false,
      timing_dialog_visible: false,
      drawer_visible: false,
      pocket_operation_type: 0,
      search: {
        count: 10,
        page: 1,
        case_name: '',
        category: '',
        creator_code: '',
      },
      total: 0,
      creator_list: [],
      category_list: [],
      case_list: [],
      case_info: {},
      pocket_list: [],
      // 默认展开的pocketId
      default_expanded_key: [],
      pocket_info: {
        collection_id: null,
        pocket_name: ''
      },
      // 当前选中pocket内，所有case的id的列表
      current_choose_case_id_list: [],
      tester_list: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  mounted() {
    this.getCategoryList()
    this.getCreatorList()
    this.pageInit()
  },
  watch: {
    $route() {
      this.pageInit()
    }
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
    async pageInit() {
      this.collection_id = this.$route.params.id
      this.getCollectionDetail()
      await this.getPocketList()
      await this.pocketDefaultExpanded()
      await this.setPocketCaseIdList()
      await this.refreshTableInfo()
      await this.getPushList()
    },
    // 获取推送人列表（测试人员）
    async getPushList() {
      this.tester_list = await HttpCase.getCreatorList()
    },
    // 获取集合基础信息详情（不包含case信息）
    async getCollectionDetail() {
      this.collection_info = await Collection.getCollectionDetail(this.collection_id)
      this.push_list_visible = this.collection_info.push_type === 1
    },
    // 获取pocket列表
    async getPocketList() {
      this.pocket_list = await Collection.getPocketInfo(this.collection_id)
    },
    // 初始化时，默认展开第一个pocket
    pocketDefaultExpanded() {
      this.current_pocket_id = this.pocket_list[0].id
      this.default_expanded_key = [this.pocket_list[0].id]
    },
    // 获取当前选中pocket的caseList的id集合
    async setPocketCaseIdList() {
      this.current_choose_case_id_list = []
      this.pocket_list.forEach(pocket => {
        if (pocket.id !== this.current_pocket_id) {
          return false
        }
        pocket.children.forEach(e => {
          this.current_choose_case_id_list.push(e.business_id)
        })
      })
    },
    // 刷新table的数据与选中状态
    async refreshTableInfo() {
      await this.searchCaseList()
      await this.refreshCaseChooseType()
    },
    // 获取caseList
    async searchCaseList() {
      const res = await HttpCase.searchCaseList(this.search)
      this.case_list = res.items
      this.total = res.total
    },
    // 对caseList中的row的选中状态进行渲染
    async refreshCaseChooseType() {
      this.case_list.forEach(row => {
        if (this.current_choose_case_id_list.indexOf(row.id) > -1) {
          this.$refs.multipleTable.toggleRowSelection(row, true)
        }
      })
    },
    handleEdit(row) {
      this.drawer_visible = true
      this.case_info = { ...row }
    },
    async updateCollection() {
      const res = await Collection.updateCollection(this.collection_info)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.collection_info_dialog_visible = false
        this.config_dialog_visible = false
      } else {
        this.$message.error('服务端异常')
      }
    },
    closeCollectionEdit() {
      this.collection_info_dialog_visible = false
      this.$refs.collection_info.resetFields()
    },
    pushListChange(val) {
      this.push_list_visible = val === 1
    },
    async handleNodeClick(data, node) {
      if (node.level === 2) {
        return
      }
      const id = node.level === 1 ? data.id : node.parent.data.id
      this.current_pocket_id = id
      this.default_expanded_key = [id]
      this.setPocketCaseIdList()
      this.getPocketList()
      await this.refreshTableInfo()
    },
    async handleDragEnd(draggingNode, dropNode, dropType) {
      if (dropType === 'none' || dropType === undefined) {
        return
      }
      const list = [draggingNode.level]
      let nodes = null
      if (draggingNode.level === 1) {
        nodes = dropNode.parent.data
      } else if (draggingNode.level === 2) {
        nodes = dropNode.parent.data.children
      }
      this.getNodeTree(nodes, draggingNode.data.id, list)
      await Collection.nodePosition(list)
    },
    getNodeTree(nodes, id, list) {
      if (!nodes) {
        return
      }
      for (let i = 0; i < nodes.length; i++) {
        if (nodes[i].id === id) {
          list[1] = i - 1 >= 0 ? nodes[i - 1].id : 0
          list[2] = nodes[i].id
          list[3] = i + 1 < nodes.length ? nodes[i + 1].id : 0
          return
        }
      }
    },
    allowDrop(draggingNode, dropNode, type) {
      return draggingNode.level === dropNode.level && type !== 'inner'
    },
    createNewPocket() {
      this.pocket_dialog_visible = true
      this.pocket_operation_type = 1
      this.pocket_info.pocket_name = ''
      this.pocket_info.collection_id = this.collection_id
    },
    updatePocket(data) {
      this.pocket_dialog_visible = true
      this.pocket_operation_type = 2
      this.pocket_info = {
        id: data.id,
        pocket_name: data.name
      }
    },
    async deletePocket(id) {
      if (this.pocket_list.length <= 1) {
        this.$message.error('至少保留一个模块')
        return
      }
      this.$confirm('确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(
          async () => {
            const res = await Collection.deletePocket(id)
            if (res.code < window.MAX_SUCCESS_CODE) {
              this.$message.success(res.message)
              this.pageInit()
            } else {
              this.$message.error('删除功能异常')
            }
          }
        )
    },
    async pocketSave() {
      let res
      if (this.pocket_operation_type === 2) {
        res = await Collection.updatePocket(this.pocket_info)
        if (res.code < window.MAX_SUCCESS_CODE) {
          this.$message.success(res.message)
          this.pocket_dialog_visible = false
          this.getPocketList()
        } else {
          this.$message.error('服务端异常')
        }
      } else if (this.pocket_operation_type === 1) {
        res = await Collection.createPocket(this.pocket_info)
        if (res.code < window.MAX_SUCCESS_CODE) {
          this.pocket_dialog_visible = false
          this.getPocketList()
          const id = Number(res.message)
          this.default_expanded_key = [id]
          this.current_pocket_id = id
          this.current_choose_case_id_list = []
          await this.refreshTableInfo()
        } else {
          this.$message.error('服务端异常')
        }
      }
    },
    async selectAll(val) {
      const list = []
      if (val.length === 0) {
        // 取消全选
        this.case_list.forEach(async e => {
          const param = {
            pocket_id: this.current_pocket_id,
            case_id: e.id,
            case_name: e.case_name,
          }
          list.push(param)
          this.current_choose_case_id_list.splice(this.current_choose_case_id_list.indexOf(e.id), 1)
        })
        await Collection.batchDeleteCase(list)
      } else {
        // 全选
        val.forEach(async e => {
          if (!this.current_choose_case_id_list.includes(e.id)) {
            const param = {
              pocket_id: this.current_pocket_id,
              case_id: e.id,
              case_name: e.case_name,
            }
            list.push(param)
            this.current_choose_case_id_list.push(e.id)
          }
        })
        await Collection.batchInsertCase(list)
      }
      this.getPocketList()
      this.default_expanded_key = [this.current_pocket_id]
    },
    async rowSelect(selection, row) {
      // false为反选，true为选中
      let chooseType = false
      const { id, case_name } = row
      selection.forEach(r => {
        chooseType = r.id === id
      })
      const param = {
        pocket_id: this.current_pocket_id,
        case_id: id,
        case_name,
      }
      if (chooseType) {
        await Collection.insertCaseToPocket(param)
        this.current_choose_case_id_list.push(id)
      } else {
        await Collection.deleteCaseFromPocket(param)
        this.current_choose_case_id_list.splice(this.current_choose_case_id_list.indexOf(id), 1)
      }
      this.getPocketList()
    },
    async deleteCase(data) {
      this.current_choose_case_id_list.splice(this.current_choose_case_id_list.indexOf(data.business_id), 1)
      const param = {
        pocket_id: this.current_pocket_id,
        case_id: data.business_id,
      }
      await Collection.deleteCaseFromPocket(param)
      await this.getPocketList()
      await this.refreshTableInfo()
    },
    handleSizeChange(val) {
      this.search.count = val
      this.refreshTableInfo()
    },
    handleCurrentChange(val) {
      this.search.page = val
      this.refreshTableInfo()
    },
    async doExecute() {
      this.execute_button_loading = true
      const reportId = await CollectionReport.execute(this.collection_id)
      await new Promise(resolve => {
        setTimeout(() => resolve('down'), 1000)
      })
      this.execute_button_loading = false
      this.$router.push(`/autotest/report-detail/${reportId}`)
    },
    drawerClose() {
      this.drawer_visible = false
      this.searchCaseList()
    }
  },
}
</script>

<style lang="scss" scoped>
  //noinspection CssUnknownTarget
  @import "@/static/css/common.scss";
  .body {
    display: flex;

    .pocket-card {
      width: 22%;
      height: 600px;
      position:relative;

      .run-button {
        width: 100%;
        height: 40px;
        font-size: 120%;
      }

      .tree-list {
        height: 520px;

        .custom-tree-node {
          flex: 1 1 auto;
          display: flex;
          align-items: center;
          justify-content: space-between;
          font-size: 14px;
          padding-right: 8px;
          width: 100%;

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

          .children-content {
            width: 150px;
            overflow:hidden;
            text-overflow:ellipsis;
            white-space:nowrap;
          }

          .delete-icon {
            display: none;
          }
        }

        .father .child {
          display: none;
        }

        .father:hover .child {
          display: block;
        }

        .custom-tree-node:hover .delete-icon{
          display: block;
        }
      }


    }

    .case-manage-card {
      width: 78%;
      height: 600px;
      margin-left: 5px;

      .row-name {
        width: 120px
      }

      .row-category {
        width: 100px;
        margin-left: 10px
      }

      .row-creator {
        width: 120px;
        margin-left: 10px;

        .creator-name {
          float: left;
        }
        .creator-code {
          float: right;
          color: #8492a6;
          font-size: 13px;
        }
      }

      .case-table {
        height: 520px;
        margin-top: 10px;
        width: 100%;

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
      }
    }
  }

  .el-tree /deep/ .is-expanded {
    color: #333333;
    font-weight: bold
  }


</style>
