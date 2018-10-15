import Vue from 'vue'
import Router from 'vue-router'
import login from '@/components/login'
import register from '@/components/register'
import logout from '@/components/logout'
import hall from '@/components/hall'
import test from '@/components/test'

import add from '@/components/inside/add'
import select from '@/components/inside/select'
import vlog from '@/components/inside/vlog'
import importExcel from '@/components/inside/importExcel'
import exportExcel from '@/components/inside/exportExcel'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: 'login'
    }, {
      path: '/login',
      component: login
    }, {
        path: '/register',
        component: register
    }, {
        path: '/logout',
        component: logout
    }, {
        path: '/hall',
        component: hall,
        children: [
            {
                path: '/',
                // component: test
                // TODO:路径问题
                redirect: 'select'
            }, {
                path: '/hall/add',
                component: add
            }, {
                path: '/hall/select',
                component: select
            }, {
                path: '/hall/vlog',
                component: vlog
            }, {
                path: '/hall/import',
                component: importExcel
            }, {
                path: '/hall/export',
                component: exportExcel
            }
        ]
    }, {
        path: '/test',
        component: add,
    }
  ]
})
