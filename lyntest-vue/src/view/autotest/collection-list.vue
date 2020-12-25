<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="collection">
    <div class="collection-header">
      <div class="header-left">
        <el-button type="primary" @click="dialog_visible = true">新 增</el-button>
      </div>
      <div class="header-right">
        <lyn-search placeholder="集合名"
                    v-model="search.collection_name"
                    style="margin-right: 20px"
                    @change="searchCollectionList"/>
        <el-pagination background
                       @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="search.page"
                       :page-size="search.count"
                       :page-sizes="[10, 20, 50, 100]"
                       :total="total" layout="total, sizes, prev, pager, next" style="float: right"/>
      </div>
    </div>
    <el-table :data="collection_list" stripe>
      <el-table-column prop="collection_name" label="集合名"></el-table-column>
      <el-table-column prop="creator_name" label="创建人"></el-table-column>
      <el-table-column label="定时任务">
        <template v-slot:default="scope">
          <el-tooltip effect="dark" placement="top-start" v-if="scope.row.timing_switch">
            <div slot="content">{{scope.row.cron}}</div>
            <l-icon name="time-circle" color="#409EFF" height="1.6em" width="1.6em"></l-icon>
          </el-tooltip>
          <l-icon name="time-circle" color="#ccc" height="1.6em" width="1.6em" v-else></l-icon>
        </template>
      </el-table-column>
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
        width="100"
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
              @click="handleEdit(scope.row.id)"></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="dialog_visible" width="400px" @closed="closeEdit">
      <el-form :model="collection" ref="collection" label-width="80px">
        <el-form-item prop="collection_name" label="集合名">
          <el-input v-model="collection.collection_name" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="描述">
          <el-input v-model="collection.description" style="width: 240px"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveCollection">保 存</el-button>
        <el-button @click="closeEdit">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>


<script>
import LynSearch from '@/component/lyn/lyn-search'
import Collection from '@/model/autotest/collection'

export default {
  components: {
    LynSearch,
  },
  data() {
    return {
      search: {
        page: 1,
        count: 10,
        collection_name: '',
        creator_code: '',
      },
      total: 0,
      collection_list: [],
      dialog_visible: false,
      collection: {
        collection_name: '',
        description: '',
      },
    }
  },
  mounted() {
    this.searchCollectionList()
  },
  methods: {
    async searchCollectionList() {
      const res = await Collection.searchCollectionList(this.search)
      this.collection_list = res.items
      this.total = res.total
    },
    handleSizeChange(val) {
      this.search.count = val
      this.searchCollectionList()
    },
    handleCurrentChange(val) {
      this.search.page = val
      this.searchCollectionList()
    },
    handleEdit(id) {
      this.$router.push(`/autotest/collection-detail/${id}`)
    },
    async saveCollection() {
      const res = await Collection.createCollection(this.collection)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$router.push(`/autotest/collection-detail/${res.message}`)
      } else {
        this.$message.error('服务端异常')
      }
    },
    closeEdit() {
      this.dialog_visible = false
      this.$refs.collection.resetFields()
    }
  },
}
</script>

<style lang="scss" scoped>
  .collection {
    padding: 0 30px;

    .collection-header {
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
