<template>
  <div class="container">
    <div class="title">Status:</div>
    <div :class="getStatusCss(statusCode)">{{statusCode}} {{message}}</div>
    <div class="title">Time:</div>
    <div :class="getTimeCss(responseTime)">{{responseTime}} ms</div>
  </div>
</template>

<script>
export default {
  name: 'response-status',
  props: {
    statusCode: {
      type: Number
    },
    responseTime: {
      type: Number
    },
    message: {
      type: String
    }
  },
  data() {
    return {
    }
  },
  methods: {
    getStatusCss(val) {
      if (!val) {
        return
      }
      if (String(val).startsWith('2')) {
        return 'success'
      }
      if (String(val).startsWith('3')) {
        return 'warning'
      }
      return 'error'
    },
    getTimeCss(val) {
      if (!val) {
        return
      }
      if (val <= 2000) {
        return 'success'
      }
      if (val > 2000 && val <= 5000) {
        return 'warning'
      }
      return 'error'
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  align-items: center;

  .title {
    font-size: 12px;
    color: #909399;
    margin-right: 10px;
  }

  .success {
    display: flex;
    font-size: 12px;
    margin-right: 10px;
    color: #0ec23b;
  }

  .warning {
    display: flex;
    font-size: 12px;
    margin-right: 10px;
    color: #E6A23C;
  }

  .error {
    display: flex;
    font-size: 12px;
    margin-right: 10px;
    color: #c20215;
  }
}
</style>
