<template>
  <el-card class="container">
    <div class="header">
      <div class="title">{{operationType === 1 ? '创建评审' : '编辑'}}</div>
      <div>
        <el-button type="primary" @click="saveReview">保存 <i class="el-icon-download"></i></el-button>
      </div>
    </div>
    <el-form ref="review_info" :model="review_info" label-width="80px" class="review_info">
      <el-form-item label="名称">
        <el-input v-model="review_info.name" class="width"></el-input>
      </el-form-item>
      <el-form-item label="包含项目">
        <el-select v-model="review_info.project" multiple class="width">
          <el-option
            v-for="item in project_list"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="评审人">
        <el-select v-model="review_info.reviewer" multiple class="width">
          <el-option
            v-for="item in user_list"
            :key="item.id"
            :label="item.tester_name"
            :value="item.tester_code">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="截止时间">
        <el-date-picker
          v-model="review_info.end_time"
          type="datetime"
          placeholder="选择日期时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          class="width">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="review_info.description" type="textarea"
                  :autosize="{ minRows: 3, maxRows: 5}"
                  :rows="2"
                  class="width"></el-input>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import Review from '@/model/track/review'
import Project from '@/model/track/project'
import HttpCase from '@/model/autotest/http-case'

export default {
  name: 'review_edit',
  props: {
    operationType: {
      type: Number
    },
    reviewInfo: {
      type: Object
    }
  },
  data() {
    return {
      review_info: {},
      default_review_info: {},
      project_list: [],
      user_list: [],
    }
  },
  created() {
    this.getReviewInfo()
    this.getProjectList()
    this.getUserList()
  },
  methods: {
    getReviewInfo() {
      this.review_info = this.operationType === 1 ? { ...this.default_review_info } : { ...this.reviewInfo }
    },
    async saveReview() {
      const res = this.operationType === 2 ? await Review.updateReview(this.review_info) : await Review.createReview(this.review_info)
      if (res.code < window.MAX_SUCCESS_CODE) {
        this.$message.success(`${res.message}`)
        this.$emit('drawer-close')
      } else {
        this.$message.error('服务端异常')
      }
    },
    async getProjectList() {
      const res = await Project.getAllProkect()
      this.project_list = res
    },
    async getUserList() {
      const res = await HttpCase.getCreatorList()
      this.creator_list = res
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 5px;
  height: 100%;

  .header {
    display: flex;
    justify-content: space-between;

    .title {
      color: $parent-title-color;
      font-size: 1.1em;
      font-weight: 500;
    }

  }

  .review_info {
    margin-top: 16px;

    .width {
      width: 360px;
    }
  }
}
</style>
