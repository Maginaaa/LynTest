const apiTest = {
  route: null,
  name: null,
  title: '接口自动化',
  type: 'folder',
  icon: 'iconfont icon-api',
  order: null,
  inNav: true,
  children: [
    {
      name: 'manage',
      title: 'Case管理',
      type: 'view',
      icon: 'iconfont icon-case',
      route: '/autotest/http',
      filePath: 'view/autotest/http-case-list.vue',
      inNav: true,
    }, {
      name: 'pressure',
      title: '压力测试',
      type: 'view',
      icon: 'iconfont icon-pressure',
      route: '/autotest/pressure',
      filePath: 'view/autotest/pressure-test.vue',
      inNav: true,
    }, {
      name: 'distributed-pressure',
      title: '分布式压测',
      type: 'view',
      icon: 'iconfont icon-pressure',
      route: '/autotest/distributed-pressure',
      filePath: 'view/autotest/distributed-pressure.vue',
      inNav: true,
    }, {
      name: 'collection',
      title: '集合管理',
      type: 'view',
      icon: 'el-icon-files',
      inNav: true,
      route: '/autotest/collection-list',
      filePath: 'view/autotest/collection-list.vue',
    }, {
      name: 'collection-detail',
      title: '集合详情',
      type: 'view',
      icon: 'iconfont icon-collection',
      inNav: false,
      route: '/autotest/collection-detail/:id',
      filePath: 'view/autotest/collection-detail.vue'
    }, {
      name: 'report-list',
      title: '测试报告',
      type: 'view',
      icon: 'iconfont icon-file-search',
      inNav: true,
      route: '/autotest/report-list',
      filePath: 'view/autotest/report-list.vue'
    }, {
      name: 'report-detail',
      title: '测试报告详情',
      type: 'view',
      icon: 'iconfont icon-file-search',
      inNav: false,
      route: '/autotest/report-detail/:id',
      filePath: 'view/autotest/report-detail.vue'
    }, {
      name: 'config',
      title: '配置',
      type: 'folder',
      inNav: true,
      children: [{
        name: 'category',
        title: '分类配置',
        type: 'view',
        icon: 'iconfont icon-setting',
        route: '/autotest/config/category',
        filePath: 'view/autotest/category-config.vue',
        inNav: true,
      }, {
        name: 'variable',
        title: '变量配置',
        type: 'view',
        icon: 'iconfont icon-database',
        route: '/autotest/config/variable',
        filePath: 'view/autotest/variable-config.vue',
        inNav: true,
      }]
    }
  ],
}

export default apiTest
