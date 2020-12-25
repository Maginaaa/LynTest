<template>
  <div class="div">
    <span v-if="title" class="menu-title">{{'[' + title + ']'}}</span>
    <i v-else class="el-icon-files menu-icon"></i>
    <el-select filterable slot="prepend" v-model="value" @change="changeData" class="project_menu"
               size="small">
      <el-option v-for="(item,index) in data" :key="index" :label="item.name" :value="index"/>
    </el-select>
  </div>
</template>

<script>
export default {
  name: 'SelectMenu',
  props: {
    data: {
      type: Array
    },
    currentData: {
      type: Object
    },
    title: {
      type: String
    }
  },
  data() {
    return {
      value: ''
    }
  },
  watch: {
    currentData(data) {
      if (data !== undefined && data !== null) {
        this.value = data.name
      }
    }
  },
  methods: {
    changeData(index) {
      this.$emit('dataChange', this.data[index])
    }
  }
}
</script>

<style lang="scss" scoped>
  .div {
    display: flex;
    justify-content: center;
  }

  .menu-title {
    color: darkgrey;
    margin-right: 10px;
    white-space: nowrap;
    height:30px;
    line-height:30px;
  }

  .menu-icon {
    color: $theme;
    font-size: 1.5em;
    margin-right: 10px;
  }
</style>
