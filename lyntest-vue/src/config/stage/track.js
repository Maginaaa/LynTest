const trackRouter = {
  route: null,
  name: null,
  title: '测试用例',
  type: 'folder', // 类型: folder, tab, view
  icon: 'iconfont el-icon-s-unfold',
  filePath: 'view/track/', // 文件路径
  order: null,
  inNav: true,
  children: [
    {
      title: '项目',
      type: 'view',
      name: 'project',
      route: '/track/project',
      filePath: 'view/track/project-list.vue',
      inNav: true,
      icon: 'iconfont el-icon-collection',
    }, {
      title: '测试用例',
      type: 'view',
      name: 'case',
      route: '/track/case',
      filePath: 'view/track/case/case-list.vue',
      inNav: true,
      icon: 'iconfont el-icon-s-unfold',
    }, {
      title: '用例评审',
      type: 'view',
      name: 'review-list',
      route: '/track/review-list',
      filePath: 'view/track/review/review-list',
      inNav: true,
      icon: 'el-icon-user',
    }
  ],
}

export default trackRouter
