<template>
  <div class="container">
    <span v-if="title" class="title">{{title}}</span>
    <div class="content" aria-readonly="true">{{content}}</div>
  </div>
</template>

<script>
export default {
  name: 'read-only-content',
  props: {
    title: {
      type: String
    },
    content: {
      type: String
    }
  }
}
</script>

<style lang="scss" scoped>
  .container {
    margin-top: 8px;
    display: flex;
    flex-direction: column;

    .title {
      color: $parent-title-color;
      font-size: 1.1em;
      font-weight: 500;
    }

    .content {
      margin-top: 5px;
      color: #666666;
      word-wrap: break-word;
      word-break: normal;
    }
  }
</style>
