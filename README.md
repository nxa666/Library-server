# 智慧图书管理系统 — 后端服务

基于 Spring Boot 2.7 构建的 RESTful API 服务，提供图书、用户、分类、借阅等全功能管理。

---

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 17 | 运行环境 |
| Spring Boot | 2.7.18 | Web 框架 |
| MyBatis-Plus | 3.5.5 | ORM / 分页 |
| MySQL | 8.0 | 数据库 |
| Redis | - | 缓存 |
| Knife4j | 4.4.0 | Swagger API 文档 |
| Lombok | - | 代码简化 |
| JWT | 自实现 (HmacSHA256) | 身份认证 |

---

## 项目结构

```
src/main/java/com/library/
├── LibraryApplication.java          # 启动类
│
├── common/                          # 公共模块
│   ├── config/
│   │   ├── CorsConfig.java          # 跨域配置
│   │   ├── SecurityConfig.java      # BCrypt 密码编码器
│   │   ├── OpenApiConfig.java       # Knife4j 接口文档配置
│   │   └── WebMvcConfig.java        # 拦截器注册 + 静态资源映射
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java  # 全局异常处理
│   │   └── BusinessException.java       # 业务异常
│   ├── interceptor/
│   │   └── JwtInterceptor.java        # JWT 认证拦截器
│   └── result/
│       └── Result.java              # 统一返回结构 {code, message, data}
│
├── controller/                      # 控制器层
│   ├── AuthController.java          # 登录 / 注册 / 获取用户信息
│   ├── UserController.java          # 用户管理（CRUD + 密码重置）
│   ├── BookController.java          # 图书管理（分页搜索 + CRUD）
│   ├── CategoryController.java      # 分类管理（树形结构 + CRUD）
│   ├── BorrowController.java        # 借阅管理（借书 / 还书 / 续借）
│   ├── UploadController.java        # 文件上传（封面）
│   └── TestController.java          # 测试接口
│
├── service/                         # 服务接口
│   └── impl/                        # 服务实现
│       ├── AuthServiceImpl.java
│       ├── UserServiceImpl.java
│       ├── BookServiceImpl.java
│       ├── CategoryServiceImpl.java
│       ├── BorrowServiceImpl.java
│       └── UploadServiceImpl.java
│
├── entity/                          # 实体类（对应数据库表）
│   ├── User.java                    # sys_user — 用户表
│   ├── Book.java                    # book — 图书表
│   ├── BookCategory.java            # book_category — 分类表
│   └── Borrow.java                  # borrow_record — 借阅记录表
│
├── dto/                             # 数据传输对象（接口入参）
│   ├── LoginDTO.java
│   ├── RegisterDTO.java
│   ├── UserDTO.java
│   ├── BookDTO.java
│   ├── CategoryDTO.java
│   └── BorrowDTO.java
│
├── vo/                              # 视图对象（接口返回值）
│   ├── LoginVO.java
│   ├── UserInfoVO.java
│   ├── UserVO.java
│   ├── BookVO.java
│   ├── CategoryVO.java
│   └── BorrowVO.java
│
├── mapper/                          # MyBatis-Plus Mapper
│   ├── UserMapper.java
│   ├── BookMapper.java
│   ├── BookCategoryMapper.java
│   └── BorrowMapper.java
│
└── utils/
    └── JwtUtils.java                # JWT 工具类
```

---

## API 接口

### 统一返回格式

```json
{ "code": 200, "message": "success", "data": { ... } }
```

### 认证模块（无需登录）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 用户登录 |
| POST | `/api/auth/register` | 用户注册 |
| GET  | `/api/auth/info` | 获取当前用户信息 |

### 用户管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/users` | 分页查询用户列表 |
| GET | `/api/users/{id}` | 查询用户详情 |
| POST | `/api/users` | 新增用户 |
| PUT | `/api/users/{id}` | 编辑用户 |
| DELETE | `/api/users/{id}` | 删除用户 |
| PUT | `/api/users/{id}/reset-password` | 重置密码为 123456 |

### 图书管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/books` | 分页查询图书（支持 title/author/isbn 搜索） |
| GET | `/api/books/{id}` | 查询图书详情 |
| POST | `/api/books` | 新增图书 |
| PUT | `/api/books/{id}` | 编辑图书 |
| DELETE | `/api/books/{id}` | 删除图书 |

### 分类管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/categories` | 查询所有分类（扁平列表） |
| GET | `/api/categories/tree` | 查询分类树 |
| GET | `/api/categories/{id}` | 查询分类详情 |
| POST | `/api/categories` | 新增分类 |
| PUT | `/api/categories/{id}` | 编辑分类 |
| DELETE | `/api/categories/{id}` | 删除分类 |

### 借阅管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/borrows` | 分页查询借阅记录 |
| GET | `/api/borrows/{id}` | 查询借阅详情 |
| POST | `/api/borrows` | 借书 |
| PUT | `/api/borrows/{id}/return` | 还书 |
| PUT | `/api/borrows/{id}/renew` | 续借 |

### 文件上传

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/upload/book-cover` | 上传图书封面 |

---

## 数据库表

### sys_user — 用户表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键自增 |
| username | VARCHAR(50) | 用户名（唯一） |
| password | VARCHAR(255) | 密码（BCrypt） |
| real_name | VARCHAR(50) | 姓名 |
| student_no | VARCHAR(30) | 学号 |
| email | VARCHAR(100) | 邮箱 |
| phone | VARCHAR(20) | 手机 |
| avatar | VARCHAR(255) | 头像 URL |
| role | VARCHAR(20) | READER / ADMIN |
| status | TINYINT | 状态 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### book — 图书表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键自增 |
| isbn | VARCHAR(50) | ISBN |
| title | VARCHAR(200) | 书名 |
| author | VARCHAR(100) | 作者 |
| publisher | VARCHAR(100) | 出版社 |
| category_id | BIGINT | 分类 ID |
| cover_url | VARCHAR(500) | 封面 URL |
| description | TEXT | 简介 |
| keywords | VARCHAR(500) | 关键词 |
| language | VARCHAR(50) | 语言 |
| page_count | INT | 页数 |
| publish_date | DATE | 出版日期 |
| location | VARCHAR(100) | 书架位置 |
| total_count | INT | 总数量 |
| available_count | INT | 可借数量 |
| borrow_count | INT | 借阅次数 |
| rating | DECIMAL(3,2) | 评分 |
| status | TINYINT | 状态 |

### book_category — 分类表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键自增 |
| category_name | VARCHAR(100) | 分类名称 |
| parent_id | BIGINT | 父分类 ID（0 为根） |
| sort_order | INT | 排序 |

### borrow_record — 借阅记录表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键自增 |
| user_id | BIGINT | 用户 ID |
| book_id | BIGINT | 图书 ID |
| borrow_time | DATETIME | 借阅时间 |
| due_time | DATETIME | 应归还时间 |
| return_time | DATETIME | 实际归还时间 |
| status | VARCHAR(20) | BORROWED / RETURNED / OVERDUE |
| renew_count | INT | 续借次数 |
| remark | VARCHAR(500) | 备注 |

---

## 快速开始

### 1. 环境准备

- JDK 17+
- MySQL 8.0+
- Redis（可选）

### 2. 初始化数据库

```sql
CREATE DATABASE library_db DEFAULT CHARSET utf8mb4;
```

在 `library_db` 中执行项目提供的 SQL 脚本，创建表并插入初始数据。

### 3. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库和 Redis 连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library_db
    username: root
    password: your_password
```

### 4. 启动项目

```bash
mvn spring-boot:run
```

服务默认运行在 `http://localhost:8080`。

### 5. 访问接口文档

浏览器打开 Knife4j 文档：

```
http://localhost:8080/doc.html
```

---

## 核心业务流程

### 借书流程

1. 校验 `book.available_count > 0`，不足则拒绝
2. 创建借阅记录（status = BORROWED，due_time = 当前 + 30 天）
3. `book.available_count - 1`，`book.borrow_count + 1`

### 还书流程

1. 校验 status 为 BORROWED
2. 更新 status = RETURNED，记录 return_time
3. `book.available_count + 1`

### 续借流程

1. 校验 status 为 BORROWED
2. due_time 延长 30 天
3. renew_count + 1

### 鉴权方式

所有 `/api/**` 接口（除 login / register / test 外）需要 JWT Token。

请求头格式：`Authorization: Bearer <token>`

Token 由登录接口返回，有效期 24 小时，使用 HmacSHA256 签名。

---

## 异常处理

全局异常处理器 `GlobalExceptionHandler` 统一处理：

| 异常类型 | 返回码 | 说明 |
|----------|--------|------|
| BusinessException | 自定义 | 业务异常 |
| BindException | 400 | 参数校验失败 |
| RuntimeException | 500 | 运行时异常 |
| Exception | 500 | 其他未知异常 |
