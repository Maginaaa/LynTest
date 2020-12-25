<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <el-card class="container">
    <el-row>
      <el-input v-model="search.case_name"
                placeholder="接口名称"
                class="row-name"
                size="small"
                @change="searchCaseList" />
      <el-select v-model="search.category"
                 placeholder="分 类"
                 class="row-category"
                 @change="searchCaseList"
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
                 @change="searchCaseList"
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
                  @row-click="rowClick"
                  stripe>
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
        </el-table>
      </vue-scroll>
    </div>
  </el-card>
</template>

<script>
import HttpCase from '@/model/autotest/http-case'

export default {
  name: 'http-case-chose',
  props: {
    value: {
      type: Object
    }
  },
  data() {
    return {
      search: {
        page: 1,
        count: 10,
        case_name: '',
        category: '',
        creator_code: '',
      },
      total: 0,
      category_list: [],
      creator_list: [],
      case_list: [],
    }
  },
  created() {
    this.searchCaseList()
    this.getCreatorList()
  },
  methods: {
    async searchCaseList() {
      const res = await HttpCase.searchCaseList(this.search)
      this.case_list = res.items
      this.total = res.total
    },
    // 获取创建人列表
    async getCreatorList() {
      const res = await HttpCase.getCreatorList()
      this.creator_list = res
    },
    handleSizeChange(val) {
      this.search.count = val
      this.searchCaseList()
    },
    handleCurrentChange(val) {
      this.search.page = val
      this.searchCaseList()
    },
    rowClick(row) {
      this.$emit('input', row)
      this.$emit('dialogClose')
    }
  }
}
</script>

<style lang="scss" scoped>
.container{
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
</style>
