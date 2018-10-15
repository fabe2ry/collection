### 前端html转vue记录

##### 页面跳转不同

* 在html使用`window.location.href="./register"`跳转界面
* 在vue中使用`window.location.href="./register"`，界面链接会变成`http://localhost:8888/register#/login`，而使用`this.$router.push('/register')`，界面链接才是正确的`http://localhost:8888/#/register`，能够跳转到正确的component

