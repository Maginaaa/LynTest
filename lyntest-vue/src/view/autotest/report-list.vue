<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <el-pagination background
                   @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   :current-page="search.page"
                   :page-size="search.count"
                   :page-sizes="[10, 20, 50, 100]"
                   :total="total" layout="total, sizes, prev, pager, next" style="float: right;margin-top: 5px;margin-bottom: 5px"/>
    <el-table :data="report_list" stripe>
      <el-table-column prop="collection_name" label="集合名"></el-table-column>
      <el-table-column prop="execute_name" label="执行人"></el-table-column>
      <el-table-column prop="execute_time" label="执行时间"></el-table-column>
      <el-table-column label="测试结果">
        <template v-slot:default="scope">
          <el-tag type="success" v-if="scope.row.error_count === 0">通过</el-tag>
          <el-tag type="danger" v-else>未通过</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        width="100"
        align="center"
        fixed="right"
        label="操作">
        <template v-slot:default="scope">
          <el-button
            size="mini"
            style="margin:auto"
            type="primary"
            @click="checkDetail(scope.row.id)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import CollectionReport from '@/model/autotest/collection-report'

export default {
  data() {
    return {
      search: {
        page: 1,
        count: 10,
        collection_name: '',
      },
      total: 0,
      report_list: [],
    }
  },
  methods: {
    async getReportList() {
      const res = await CollectionReport.searchReportList(this.search)
      this.report_list = res.items
      this.total = res.total
    },
    handleSizeChange(val) {
      this.search.count = val
      this.getReportList()
    },
    handleCurrentChange(val) {
      this.search.page = val
      this.getReportList()
    },
    checkDetail(id) {
      this.$router.push(`/autotest/report-detail/${id}`)
    }
  },
  mounted() {
    this.getReportList()
  }
}
</script>

<style scoped>

</style>
