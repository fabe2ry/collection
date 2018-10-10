### springboot项目搭建

#### 接入mybatis

TODO

#### 使用aop实现日志记录

TODO

#### 多环境配置

##### 配置文件格式

`application-{profile}.properties`

##### 通常情况

- `application-dev.properties`：开发环境
- `application-test.properties`：测试环境

##### 设置当前生产环境

`application.properties`设置`spring.profiles.active={profile}`来设置生产环境

##### 打包成为jar包（需要maven插件`spring-boot-maven-plugin`）

* mvn clean package
* mvn clean package -Dmaven.test.skip=true # 排除测试代码后进行打包

##### 多模块打包
TODO

##### 通过jar包启动springboot

* java -jar xxx.jar
* java -jar xxx.jar --spring.profiles.active=dev # 动态设置生产环境

#### 配置h2数据库

##### 添加依赖

`pox.xml`

```xml
<!-- https://mvnrepository.com/artifact/com.h2database/h2 -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>1.4.197</version>
    <scope>test</scope>
</dependency>
```

##### springboot设置

在`application-{profile}.properties`中添加

```xml
spring.datasource.url=jdbc:h2:mem:test # mem表述数据存储在内存
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=
spring.datasource.password=
```

##### 数据库初始化设置

* 新建文件`resources/db/schema.sql`用于对数据库的结构进行操作
* 新建文件`resources/db/data.sql`用于对数据库的数据进行操作
* 在`application-{profile}.properties`中添加

```xml
spring.datasource.schema=classpath:db/schema.sql
spring.datasource.data=classpath:db/data.sql
```

##### h2 web consloe

```xml
spring.h2.console.enabled=true # 是否开启h2 cosloe
spring.h2.console.path=/h2-console # 设置h2 consloe的url路径
spring.h2.console.settings.web-allow-others=true # 允许远程访问
```



#### 接入swagger2

##### 添加pom依赖

```xml
<dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-swagger2</artifactId>
   <version>2.9.2</version>
</dependency>

<dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-swagger-ui</artifactId>
   <version>2.9.2</version>
</dependency>
```

##### 书写配置类

```java
@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("your package"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("title")
                .description("description")
                .termsOfServiceUrl("url")
                .contact("author")
                .version("1.0")
                .build();
    }

}
```

##### 在controller的方法上注解

* `@ApiOperation(value="value", notes="note")`
*  `@ApiImplicitParam(name = "name", value = "value", required = true, dataType = "User")`
* ```java 
@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
})
  ```

##### 访问链接

`http://localhost:8080/swagger-ui.html`