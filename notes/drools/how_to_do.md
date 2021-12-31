#kie的生命周期
1. 编写规则文件
2. 构建
3. 校验和测试
4. 使用和管理
5. 执行


 KieService是获取构建和运行的相关对象，常用于获取KieContainer

 KieContainer就是一个KieModule的容器
 KieModule类似于maven项目中pom.xml文件的定位，是存放所有定义好KieBase资源（若干的规则、流程、方法）的容器。比如说，定义kieBase名字，GAV（groupid，artifactId，version）
 KiBase是编译好的资源（包含rules（规则）, processes（流程）, functions（方法）, type models）
KieBuilder是针对含有资源的kieModule的构造器