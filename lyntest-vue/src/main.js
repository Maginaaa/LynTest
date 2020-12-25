import '@babel/polyfill'
import Vue from 'vue'
import ElementUI from 'element-ui'
import VueScroll from 'vuescroll'
import VCharts from 'v-charts'

import '@/config/global'
import '@/lin/mixin'
import '@/lin/filter'
import '@/lin/plugin'
import '@/lin/directive'

import CollapseTransition from 'element-ui/lib/transitions/collapse-transition'
import LinNotify from '@/component/notify'
import router from '@/router'
import store from '@/store'
import App from '@/app.vue'

import StickyTop from '@/component/base/sticky-top/sticky-top'
import LIcon from '@/component/base/icon/lin-icon'
import LinTable from '@/component/base/table/lin-table'
import SourceCode from '@/component/base/source-code/source-code'
import globalVariable from '@/config/global_variable'


import '@/assets/style/index.scss' // eslint-disable-line
import '@/assets/style/realize/element-variable.scss'
import 'element-ui/lib/theme-chalk/display.css'

Vue.config.productionTip = false
Vue.prototype.GLOBAL = globalVariable

Vue.use(ElementUI)
Vue.use(VCharts)
Vue.use(VueScroll, {
  ops: {
    vuescroll: {
      mode: 'native',
      sizeStrategy: 'percent',
    },
    scrollPanel: {
      easing: 'easeInQuad',
      speed: 800,
      // 禁用横向滚动
      scrollingX: false,
    },
    bar: {
      background: '#4c4d4d',
      keepShow: false,
      size: '10px',
      minSize: 0.2
    }
  }
})
Vue.use(LinNotify, {
  reconnection: true,
  reconnectionAttempts: 5,
  reconnectionDelay: 3000,
})

Vue.component(CollapseTransition.name, CollapseTransition)

// base 组件注册
Vue.component('sticky-top', StickyTop)
Vue.component('l-icon', LIcon)
Vue.component('source-code', SourceCode)
Vue.component('lin-table', LinTable)

/* eslint no-unused-vars: 0 */
const AppInstance = new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')

// 设置 App 实例
window.App = AppInstance
