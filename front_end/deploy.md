### 部署vue前端记录

##### 如果之前有安装过，要卸载重新安装

- 输入`npm uninstall vue-cli -g`来进行全局的卸载
- 删除对于node文件夹下的文件，不然可能出现安装错误，文件夹路径在windows下通常是：`C:\Users\username\AppData\Roaming\npm\node_modules`对应的vue-cli文件夹

##### 安装vue-cli

* `npm install vue-cli -g`

##### 创建项目

* `vue init webpack front_end`
* 按照提示，进行对应的项目设置
* 进入项目文件夹，输入`npm install`来安装需要的模块
* npm run dev启动项目

在启动过程中，出现**webpack-dev-server 不是内部或外部命令，也不是可运行的程序**的报错，需要使用`npm install webpack-dev-server --save`来安装对应的插件

默认启动的端口为8080，为了和后端的项目不影响，设置端口为8888，打开`../config/index,js`,修改8080为8888，就可以了

##### 导入element-ui(参考官网的*快速上手*)

* 安装element-ui模块，`npm install element-ui -S`
* 在main.js中引入模块

```javascript
import ElementUI from ‘element-ui‘
import ‘element-ui/lib/theme-chalk/index.css‘
Vue.use(ElementUI)
```

这里可能会提示需要安装对应的css文件，按照提示按照就行了




