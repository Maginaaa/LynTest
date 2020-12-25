<template>
  <div class="lin-search">
    <el-input size="medium" :placeholder="placeholder" clearable v-model="keyword" class="input-with-select"
              @change="handleChange">
      <el-button slot="append" icon="el-icon-search"></el-button>
    </el-input>
  </div>
</template>

<script>

export default {
  props: {
    placeholder: {
      type: String,
      default: '请输入内容',
    }
  },
  data() {
    return {
      keyword: '',
    }
  },
  methods: {
    clear() {
      this.keyword = ''
    },
    async handleChange(change) {
      this.$emit('input', change)
      this.$emit('change', change)
    }
  },
}
</script>

<style lang="scss" scoped>
  .lin-search /deep/ .el-input-group__append {
    border-top-right-radius: 20px;
    border-bottom-right-radius: 20px;
    padding: 0 8px;
    color: #ffffff;
    border: 1px solid $theme;
    .el-icon-search {
      font-size: 18px;
    }
  }
  .lin-search /deep/ .el-input__inner {
    border-top-left-radius: 20px;
    border-bottom-left-radius: 20px;
    border-right: none;
    width: 150px;
    transition: all 0.2s linear;

    &:focus {
      width: 250px;
      transition: all 0.3s linear;
    }
  }
</style>
