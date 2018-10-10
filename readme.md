### 项目通过idea部署

#### 使用h2数据库

* 将项目导入idea后，在`collection\sample\src\main\resources\application.properties`文件中，设置`spring.profiles.active=test`
* 启动项目，使用idea的`Built-in Webserver`启动前端界面，或者输入`http://localhost:63342/collection/sample/static/login.html`访问（端口取决于idea设置的built-in server端口）
* 默认登陆密码：alice/alice

#### 使用本地mysql数据库

* 通过本地mysql数据库，需要先新建数据库，数据库的sql文件放在`/sql`文件夹下

* 修改配置文件`collection\sample\src\main\resources\application-dev.properties`将

  ```xml
  spring.datasource.url=jdbc:mysql://localhost:3306/your database?useUnicode=true&characterEncoding=utf-8&useSSL=false
  spring.datasource.username=your username
  spring.datasource.password=your password
  ```

* 修改配置文件在`collection\sample\src\main\resources\application.properties`文件中，设置`spring.profiles.active=dev`

* 启动项目，使用idea的`Built-in Webserver`启动前端界面，或者输入`http://localhost:63342/collection/sample/static/login.html`访问（端口取决于idea设置的built-in server端口）

* 默认登陆密码：alice/alice

### see also

* 项目打包，进入最外层的pox.xml文件所在位置，输入`mvn clean package`
* 通过jar启动项目`java -jar sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=test`,使用h2数据库
* 项目前端界面建议使用idea的build-in web部署，这是由于cors如果要允许cookie，设置时候不能使用*来允许，所有代码中只指定了idea的build-in web，即`http://localhost:63342/`



