JStarCraft AI
==========

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

*****

**JStarCraft AI是一个机器学习的轻量级框架.遵循Apache 2.0协议.**

在学术界,绝大多数研究人员使用的编程语言是Python.
在工业界,绝大多数开发人员使用的编程语言是Java.
作为在学术界与工业界从事机器学习研发的相关人员之间的桥梁.

|作者|洪钊桦|
|---|---
|E-mail|110399057@qq.com, jstarcraft@gmail.com

*****

## JStarCraft AI架构

JStarCraft AI框架各个模块之间的关系:
![ai](https://github.com/HongZhaoHua/jstarcraft-reference/blob/master/ai/JStarCraft%E4%BA%BA%E5%B7%A5%E6%99%BA%E8%83%BD%E6%A1%86%E6%9E%B6%E7%BB%84%E4%BB%B6%E5%9B%BE.png "JStarCraft AI架构")

*****

## JStarCraft AI特性
* [1.数据(data)](https://github.com/HongZhaoHua/jstarcraft-ai-1.0/wiki/%E6%95%B0%E6%8D%AE)
    * 属性与特征
        * 连续
        * 离散
    * 模块与实例
    * 选择,排序与切割
* 2.环境(environment)
    * 串行计算
    * 并行计算
    * CPU计算
    * GPU计算
* 3.数学(math)
    * 算法(algorithm)
        * 分解
        * 概率
        * 相似度
        * 损失函数
    * [数据结构(structure)](https://github.com/HongZhaoHua/jstarcraft-ai/wiki/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84)
        * 标量
        * 向量
        * 矩阵
        * 张量
        * 单元
        * 表单
* 4.调制解调(modem)
* [5.模型(model)](https://github.com/HongZhaoHua/jstarcraft-ai-1.0/wiki/%E6%A8%A1%E5%9E%8B)
    * 线性模型(linear)
    * 近邻模型(nearest neighbor)
    * 矩阵分解模型(matrix factorization)
    * 神经网络模型(neutral network)
        * 计算图
            * 节点
            * 层
        * 正向传播与反向传播
        * 激活函数
        * 梯度更新
    * 概率图模型(probabilistic graphical)
    * 规则模型(rule)
    * 支持向量机模型(support vector machine)
    * 树模型(tree)
* 6.优化(optimization)
    * 梯度下降法(gradient descent)
        * 批量梯度下降
        * 随机梯度下降
    * 牛顿法和拟牛顿法(newton method/quasi newton method)
    * 共轭梯度法(conjugate gradient)
    * [试探法(heuristics)](https://github.com/HongZhaoHua/jstarcraft-ai-1.0/wiki/%E8%AF%95%E6%8E%A2%E6%B3%95)
        * 模拟退火算法
        * 遗传算法
        * 蚁群算法
        * 粒子群算法
* [7.有监督学习(supervised)](https://github.com/HongZhaoHua/jstarcraft-ai-1.0/wiki/%E6%9C%89%E7%9B%91%E7%9D%A3%E5%AD%A6%E4%B9%A0)
    * 分类
    * 回归
* [8.无监督学习(unsupervised)](https://github.com/HongZhaoHua/jstarcraft-ai-1.0/wiki/%E6%97%A0%E7%9B%91%E7%9D%A3%E5%AD%A6%E4%B9%A0)
    * 聚类
    * 关联
* 9.工具(utility)

*****

## JStarCraft AI教程

* 1.设置依赖
    * [Maven依赖](#Maven依赖)
    * [Gradle依赖](#Gradle依赖)
* 2.配置环境
    * [设置CPU环境](#设置CPU环境)
    * [设置GPU环境](#设置GPU环境)
    * [使用环境上下文](#使用环境上下文)
* 3.使用数据
    * [数据表示](#数据表示)
    * [数据转换](#数据转换)
        * ARFF
        * CSV
        * JSON
        * HQL
        * SQL
    * [数据处理](#数据处理)
        * 选择
        * 排序
        * 切割

#### Maven依赖

```maven
<dependency>
    <groupId>com.jstarcraft</groupId>
    <artifactId>ai</artifactId>
    <version>1.0</version>
</dependency>
```

#### Gradle依赖

```gradle
compile group: 'com.jstarcraft', name: 'ai', version: '1.0'
```

#### 设置CPU环境

```maven
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-native-platform</artifactId>
    <version>1.0.0-beta3</version>
</dependency>
```

#### 设置GPU环境

* CUDA 9.0

```maven
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-cuda-9.0-platform</artifactId>
    <version>1.0.0-beta3</version>
</dependency>
```

* CUDA 9.1

```maven
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-cuda-9.1-platform</artifactId>
    <version>1.0.0-beta3</version>
</dependency>
```

* CUDA 9.2

```maven
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-cuda-9.2-platform</artifactId>
    <version>1.0.0-beta3</version>
</dependency>
```

* CUDA 10.0

```maven
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-cuda-10.0-platform</artifactId>
    <version>1.0.0-beta3</version>
</dependency>
```

* CUDA 10.1

```maven
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-cuda-10.1-platform</artifactId>
    <version>1.0.0-beta3</version>
</dependency>
```

#### 使用环境上下文

```java
// 获取默认环境上下文
EnvironmentContext context = EnvironmentContext.getContext();
// 在环境上下文中执行任务
Future<?> task = context.doTask(() - > {
    int dimension = 10;
    MathMatrix leftMatrix = getRandomMatrix(dimension);
    MathMatrix rightMatrix = getRandomMatrix(dimension);
    MathMatrix dataMatrix = getZeroMatrix(dimension);
    dataMatrix.dotProduct(leftMatrix, false, rightMatrix, true, MathCalculator.PARALLEL);
});
```

#### 数据表示

* 未处理的形式(转换前)

| 用户(User) | 旧手机类型(Item) | 新手机类型(Item) | 评分(Score) |
| :----: | :----: | :----: | :----: |
| Google Fan | Android | Android | 3 |
| Google Fan | Android | IOS | 1 |
| Google Fan | IOS | Android | 5 |
| Apple Fan | IOS | IOS | 3 |
| Apple Fan | Android | IOS | 5 |
| Apple Fan | IOS | Android | 1 |

* 已处理的形式(转换后)

| 定性(User) | 定性(Item) | 定性(Item) | 定量(Score) |
| :----: | :----: | :----: | :----: |
| 0 | 0 | 0 | 3 |
| 0 | 0 | 1 | 1 |
| 0 | 1 | 0 | 5 |
| 1 | 1 | 1 | 3 |
| 1 | 0 | 1 | 5 |
| 1 | 1 | 0 | 1 |

#### 数据转换

**数据转换器**(DataConverter)负责各种各样的格式转换为JStarCraft AI框架能够处理的**数据模块**(DataModule).

JStarCraft AI框架各个转换器与其它系统之间的关系: 

![converter](https://github.com/HongZhaoHua/jstarcraft-reference/blob/master/ai/%E8%BD%AC%E6%8D%A2%E5%99%A8%E7%B1%BB%E5%9B%BE.png "转换器")

* 定义数据属性

```java
// 定性属性
Map<String, Class<?>> qualityDifinitions = new HashMap<>();
qualityDifinitions.put("user", String.class);
qualityDifinitions.put("item", String.class);

// 定量属性
Map<String, Class<?>> quantityDifinitions = new HashMap<>();
quantityDifinitions.put("score", float.class);
DataSpace space = new DataSpace(qualityDifinitions, quantityDifinitions);
```

* 定义数据模块

```java
TreeMap<Integer, String> configuration = new TreeMap<>();
configuration.put(1, "user");
configuration.put(3, "item");
configuration.put(4, "score");
DataModule module = space.makeDenseModule("module", configuration, 1000);
```

**JStarCraft AI框架兼容的格式**

* ARFF

```java
// ARFF转换器
ArffConverter converter = new ArffConverter(space.getQualityAttributes(), space.getQuantityAttributes());

// 获取流
File file = new File(this.getClass().getResource("module.arff").toURI());
InputStream stream = new FileInputStream(file);

// 转换数据
int count = converter.convert(module, stream, null, null, null);
```

* CSV

```java
// CSV转换器
CsvConverter converter = new CsvConverter(',', space.getQualityAttributes(), space.getQuantityAttributes());

// 获取流
File file = new File(this.getClass().getResource("module.csv").toURI());
InputStream stream = new FileInputStream(file);

// 转换数据
int count = converter.convert(module, stream, null, null, null);
```

* JSON

```java
// JSON转换器
JsonConverter converter = new JsonConverter(space.getQualityAttributes(), space.getQuantityAttributes());

// 获取流
File file = new File(this.getClass().getResource("module.json").toURI());
InputStream stream = new FileInputStream(file);

// 转换数据
int count = converter.convert(module, stream, null, null, null);
```

* HQL

```java
// HQL转换器
QueryConverter converter = new QueryConverter(space.getQualityAttributes(), space.getQuantityAttributes());

// 获取游标
String selectDataHql = "select data.user, data.leftItem, data.rightItem, data.score from MockData data";
Session session = sessionFactory.openSession();
Query query = session.createQuery(selectDataHql);
ScrollableResults iterator = query.scroll();

// 转换数据
int count = converter.convert(module, iterator, null, null, null);
session.close();
```

* SQL

```java
// SQL转换器
QueryConverter converter = new QueryConverter(space.getQualityAttributes(), space.getQuantityAttributes());

// 获取游标
String selectDataSql = "select user, leftItem, rightItem, score from MockData";
Session session = sessionFactory.openSession();
Query query = session.createQuery(selectDataSql);
ScrollableResults iterator = query.scroll();

// 转换数据
int count = converter.convert(module, iterator, null, null, null);
session.close();
```

#### 数据处理

* 选择

```java

```

* 排序

```java

```

* 切割

```java

```
