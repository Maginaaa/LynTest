## LynTest

Springboot 版本 1.5.3

Vue 版本 2.9.6

数据平台功能介绍[查看这里](https://blog.csdn.net/lt326030434/article/details/102601308)


- 使用方式

目前已经将vue项目打成静态文件放入了Springboot中，所以按一下操作方式可以直接在本地启用项目

1. 启动数据库，执行sql文件内的dataplatform.sql文件，建立数据库所依赖的表
2. 修改application.yml中对应的数据库信息
3. 本地启动SpringBoot，然后访问 http://localhost:8070/dist/index.html/#/login, 预设的测试账号为admin,密码123456
