// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

Vue.config.productionTip = false

// 导入element-ui
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI)

// 导入axios
import axios from 'axios'
Vue.prototype.$http=axios

// 导入jquery
import $ from 'jquery'

// 引入bootstraop
import  'bootstrap/dist/js/bootstrap.min.js'
import  'bootstrap/dist/css/bootstrap.min.css'

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
