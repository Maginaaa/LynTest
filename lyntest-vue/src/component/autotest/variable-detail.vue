<template>
  <div>
    <el-table
      :data="result"
      stripe>
      <el-table-column prop="variable_name" label="保存变量名"></el-table-column>
      <el-table-column prop="actual_res" label="变量值" show-overflow-tooltip></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'variable-detail',
  props: {
    result: {
      type: Array
    }
  },
  data() {
    return {
    }
  },
}
</script>

<style scoped>

</style>
