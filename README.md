<h1 align="center">LynTest</h1>
<h3 align="center">接口自动化测试平台</h3>

<p align="center">

  <a href="https://spring.io/" rel="nofollow">
  <img src="https://img.shields.io/badge/spring%20boot-2.2.5.RELEASE-green" alt="spring boot" data-canonical-src="https://img.shields.io/badge/spring%20boot-2.2.5.RELEASE-green" style="max-width:100%;">
  </a>
  
  <a href="https://mybatis.plus/" rel="nofollow">
  <img src="https://img.shields.io/badge/mybatis--plus-3.3.0-yellow" alt="mybatis-plus" data-canonical-src="https://img.shields.io/badge/mybatis--plus-3.3.0-yellow" style="max-width:100%;">
  </a>
  
  <a href="https://cn.vuejs.org/" rel="nofollow">
  <img src="https://img.shields.io/badge/Vue-2.6.0-green" alt="Vue" data-canonical-src="https://img.shields.io/badge/Vue-2.6.0-green" style="max-width:100%;">
  </a>
  
</p>

#### 项目介绍
本项目支持单接口调试、并发测试，支持批量测试、定时执行、报告在线展示及推送。支持变量传递、函数助手等功能。所有批量执行、压测功能等均为异步执行。
为支持企业级的应用落地，新版本支持较为复杂的权限管理系统，并内置大量可拓展功能，例如：自定义入参校验、okhttp请求拦截、redis键空间通知、七牛等第三方接入、企业微信推送等。

平台技术栈为 **SpringBoot + Vue**，前后端分离实现。为方便部署，数据库简化为仅使用MySQL，当然平台也内置了Redis操作类，可根据需要让同学们进行快速的二次迁移。

线上体验地址： [http://81.68.103.109](http://81.68.103.109)

github地址： [https://github.com/Maginaaa/LynTest](https://github.com/Maginaaa/LynTest)

这里默认填写了测试账号，同学们自己部署后才可通过root账户使用"用户及权限管理模块"

#### 部分页面展示
Case管理
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201225152100256.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x0MzI2MDMwNDM0,size_16,color_FFFFFF,t_70)
集合详情
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201225152214250.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x0MzI2MDMwNDM0,size_16,color_FFFFFF,t_70)
测试报告
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201225152300442.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x0MzI2MDMwNDM0,size_16,color_FFFFFF,t_70)
分组及权限管理
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201225152530248.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x0MzI2MDMwNDM0,size_16,color_FFFFFF,t_70)
#### 项目启动
**后端**：
1. 数据库初始化：启动MySQL数据库，执行根目录下的`lyntest.sql`文件
2. 更改`lyntest/lyntest-server/src/main/resources/application-dev.yml` 内的配置文件
3. `mvn install`
4. 直接启动SpringBoot

**前端**：
1. 安装 `Node.js`（version：8.11.0+）
2. 安装依赖包: `cd lyntest/lyntest-vue && npm install`  
3. 运行项目： `npm run serve`

#### 后续功能：
1. 测试用例管理平台
2. 测试用例用自动化测试的打通
3. 测试用例集成xmind

#### 帮助文档
使用文档和代码解读会后续在[《简单随风的CSDN博客》](https://jiandansuifeng.blog.csdn.net/)上进行陆续更新。
更多问题欢迎直接微信联系

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201225163715553.png)
