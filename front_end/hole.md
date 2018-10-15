### 记录过程中的一些坑

#### axios全局使用

axios并没有install 方法，所以是不能使用vue.use()方法的。 所以每个使用axios都需要import一下，这样太麻烦了

##### 解决方法

##### 1.结合 vue-axios使用

```javascript
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueAxios,axios);

// 通过this.axios进行操作
this.axios.get('api')...(省略)
```

##### 2.axios 改写为 Vue 的原型属性

```javascript
import axios from 'axios'
// ajax可以改成你希望的名称，比如$http,后面操作对应
Vue.prototype.$ajax= axios

// 通过this.$ajax进行操作
this.$ajax.get('api')...(省略)
```

##### 3.结合 Vuex的action

```javascript
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

const store = new Vuex.Store({
  // 定义状态
  state: {
  },
  actions: {
    // 封装一个 ajax 方法
    login (context) {
      axios({
        method: 'post',
        url: '/user',
        data: context.state.user
      })
    }
  }
})

export default store
```

之后在其他文件

```javascript
 this.$store.dispatch('login')
```

