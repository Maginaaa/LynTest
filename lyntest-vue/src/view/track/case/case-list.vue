<template>
  <div class="container">
    <div class="header">
      <div class="header-left">
        <el-button type="primary" @click="createReview">创建评审 <i class="el-icon-circle-plus-outline
"></i></el-button>
      </div>
      <div class="header-right">
        <lyn-search v-model="search.name" placeholder="名称筛选" @change="searchReview"></lyn-search>
        <el-pagination background
                       @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="search.page"
                       :page-size="search.count"
                       :page-sizes="[10, 20, 50, 100]"
                       :total="total" layout="total, sizes, prev, pager, next"/>
      </div>
    </div>
    <el-table class="adjust-table" :data="review_list" style="width: 100%">
      <el-table-column prop="name" label="名称" width="250" show-overflow-tooltip/>
      <el-table-column prop="description" label="描述" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-tooltip effect="dark" placement="top-start" v-if="scope.row.description">
            <div slot="content">{{scope.row.description}}</div>
            <l-icon name="read" color="#409EFF" height="1.6em" width="1.6em"></l-icon>
          </el-tooltip>
          <l-icon name="read" color="#ccc" height="1.6em" width="1.6em" v-else></l-icon>
        </template>
      </el-table-column>
      <el-table-column prop="creator" label="发起人"/>
      <el-table-column
              sortable
              prop="create_time"
              label="创建时间"
              show-overflow-tooltip>
      </el-table-column>
      <el-table-column
              sortable
              prop="end_time"
              label="截止时间"
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
    <el-drawer
            :visible.sync="drawer_visible"
            :with-header="false"  size="40%">
      <review-edit v-if="drawer_visible" :review-info="review_info" :operation-type="operation_type" @drawer-close="drawerClose"></review-edit>
    </el-drawer>
  </div>
</template>

<script>
import LynSearch from '@/component/lyn/lyn-search'
import ReviewEdit from '@/component/track/review-edit'
import Review from '@/model/track/review'

export default {
  name: 'review-list',
  components: {
    LynSearch,
    ReviewEdit
  },
  data() {
    return {
      drawer_visible: false,
      search: {
        name: '',
        count: 10,
        page: 1,
      },
      total: 0,
      operation_type: null,
      review_list: [],
      review_info: {},
      default_review_info: {
        name: '',
        description: '',
      },
    }
  },
  methods: {
    async searchReview() {
      const res = await Review.searchReviewList(this.search)
      this.review_list = res.items
      this.total = res.total
    },
    createReview() {
      this.drawer_visible = true
      this.operation_type = 1
      this.review_info = { ...this.default_review_info }
    },
    handleEdit(row) {
      this.drawer_visible = true
      this.operation_type = 2
      this.review_info = { ...row }
    },
    async handleDelete(id) {
      this.$confirm('确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(
          async () => {
            const res = await Review.deleteReview(id)
            console.log(res)
            if (res.code < window.MAX_SUCCESS_CODE) {
              this.$message.success(res.message)
              this.searchReview()
            } else {
              this.$message.error('删除功能异常')
            }
          }
        )
    },
    async saveReview() {
      const res = this.operation_type === 1 ? await Review.createReview(this.review_info) : await Review.updateReview(this.review_info)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(res.message)
        this.drawer_visible = false
        this.searchReview()
      } else {
        this.$message.error('服务端异常')
      }
    },
    handleSizeChange(val) {
      this.search.count = val
      this.searchReview()
    },
    handleCurrentChange(val) {
      this.search.page = val
      this.searchReview()
    },
    drawerClose() {
      this.drawer_visible = false
      this.searchReview()
      // this.$refs.review_info.resetFields()
    },
  },
  created() {
    this.searchReview()
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
