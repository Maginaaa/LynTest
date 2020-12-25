<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <el-table
      :data="result"
      stripe>
      <el-table-column prop="expected_value" label="预期值"></el-table-column>
      <el-table-column prop="compare_value" label="断言方式"></el-table-column>
      <el-table-column prop="actual_value" label="实际取值" show-overflow-tooltip></el-table-column>
      <el-table-column prop="extract_value" label="结果">
        <template v-slot:default="scope">
          <el-tag :type="scope.row.extract_value === '通过' ? 'success' : 'danger'"
            disable-transitions>{{scope.row.extract_value}}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

export default {
  name: 'assertions-detail',
  props: {
    result: {
      type: Array
    }
  },
  watch: {
    result() {
      this.getResult()
    }
  },
  mounted() {
    this.getResult()
  },
  data() {
    return {
      compare_list: {
        1: '相等',
        2: '包含',
        3: '不为空或null'
      }
    }
  },
  methods: {
    getResult() {
      this.result.forEach(e => {
        e.compare_value = this.compare_list[e.compare_type]
        e.extract_value = e.extract_success ? '通过' : '失败'
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
