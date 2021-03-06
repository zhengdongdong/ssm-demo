# ssm-demo

## 说明
### 包名配置:
> spring-mvc.xml 

```
<context:component-scan base-package="自动扫描Controller所在包名"/>
```

> spring-mybatis.xml 

```
<context:componet-scan base-package="自动扫描位置"/>
```

```
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="Dao所在包名"/>
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
</bean>
```

### config文件配置
> spring-mybatis.xml

```
<bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:config/jdbc.properties"/>
</bean>
```

```
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!-- 自动扫描mapping.xml文件 -->
    <property name="mapperLocations" value="classpath:mappers/*.xml"></property>
</bean>
```

> web.xml

```
<context-param>
    <param-name>log4jConfigLocation</param-name>
    <param-value>classpath:config/log4j.properties</param-value>
</context-param>
```

```
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:config/spring-mybatis.xml</param-value>
</context-param>
```

```
<servlet>
    <servlet-name>SpringMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:config/spring-mvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
    <async-supported>true</async-supported>
</servlet>
```

### 注入

> UserServiceImpl.java

```
@Service("userService")  // 将此类作为spring的一个bean, 标记为 userService
public class UserServiceImpl implements UserService {
    @Resource // 注入Dao, (mybatis和spring结合IUserDao和mappers/UserDao.xml自动生成注入)
    private IUserDao userDao;
    ...
}
```

> UserController.java

```
@Controller
@RequestMapping("/user")
public class UserController {
    @Resource(name = "userService")  // 注入Service(如未标明, 自动扫描匹配)
    IUserService service;
    ...
}
```