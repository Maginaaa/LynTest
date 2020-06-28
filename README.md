## LynTest

Springboot 版本 1.5.3

Vue 版本 2.9.6

LynTest功能介绍[查看这里](https://blog.csdn.net/lt326030434/article/details/102601308)

线上体验地址 [查看这里](http://106.54.183.225:8080/vue/dist/#/login)
账号 admin 密码123456

- 特色功能
1. 多case的参数依赖、函数助手等
2. 详细、精细的断言
3. 前端拖拽管理case顺序
4. 压力测试
5. 定时构建

- 使用方式

1. 启动数据库，执行sql文件内的dataplatform.sql文件，建立数据库所依赖的表
2. 修改application.yml中对应的数据库信息，本地启动SpringBoot
3. 访问代码中提供的prefile/dist/index.html,在地址中加入/login后缀即可(例如:path/lyntest/prefile/dist/index.html#/index)

如需部署至服务器使用，请微信联系: x59401x
