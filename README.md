<div align="center">

**智能 AI 面试官平台** - 基于大语言模型的简历分析、模拟面试和 RAG 知识库系统

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.3-blue?logo=react)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.6-blue?logo=typescript)](https://www.typescriptlang.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-pgvector-336791?logo=postgresql)](https://www.postgresql.org/)

</div>


---

## 项目介绍

InterviewGuide 是一个集成了简历分析、模拟面试（文字 + 语音）、面试安排、知识库管理和多模型配置的智能面试辅助平台。系统利用大语言模型（LLM）、向量数据库、Redis Stream 异步任务和实时语音技术，为求职者、HR 和培训机构提供智能化的简历评估、面试练习、知识库问答和面试日程管理能力。

## 技术栈

### 后端技术

| 技术                  | 版本  | 说明                          |
| --------------------- | ----- | ----------------------------- |
| Spring Boot           | 4.0.1 | 应用框架                      |
| Java                  | 21    | 开发语言（虚拟线程）          |
| Spring AI             | 2.0.0-M4 | AI 集成框架、OpenAI 兼容模型接入 |
| Spring AI Agent Utils | 0.7.0 | Skill 资源加载、Advisor 能力扩展 |
| PostgreSQL + pgvector | 14+   | 关系数据库 + 向量存储（Compose 默认 PG16） |
| Redis + Redisson      | 6+ / 4.0.0 | 缓存 + 消息队列（Stream） |
| Apache Tika           | 2.9.2 | 文档解析                      |
| iText 8               | 8.0.5 | PDF 导出                      |
| MapStruct             | 1.6.3 | 对象映射                      |
| SpringDoc OpenAPI     | 3.0.2 | API 接口文档                  |
| DashScope SDK         | 2.22.7 | 语音识别/合成（Qwen3 ASR/TTS）|
| AWS S3 SDK            | 2.29.51 | S3 兼容对象存储（MinIO/RustFS）|
| WebSocket             | -     | 语音面试实时双向通信          |
| Gradle                | 8.14  | 构建工具                      |

### 前端技术

| 技术              | 版本  | 说明           |
| ----------------- | ----- | -------------- |
| React             | 18.3  | UI 框架        |
| TypeScript        | 5.6   | 开发语言       |
| Vite              | 5.4   | 构建工具       |
| Tailwind CSS      | 4.1   | 样式框架       |
| React Router      | 7.11  | 路由管理       |
| Framer Motion     | 12.23 | 动画库         |
| Recharts          | 3.6   | 图表库         |
| Lucide React      | 0.468 | 图标库         |
| React Big Calendar| 1.19  | 面试日历组件   |
| React Virtuoso    | 4.18  | RAG 聊天虚拟列表 |
| pnpm              | 10.26 | 前端包管理器   |

## 功能特性

### 简历管理模块

- **多格式解析**：支持 PDF、DOCX、DOC、TXT 等多种简历格式。
- **异步处理流**：基于 Redis Stream 实现异步简历分析，支持实时查看处理进度（待分析/分析中/已完成/失败）。
- **稳定性保障**：内置分析失败自动重试机制（最多 3 次）与基于内容哈希的重复检测。
- **分析报告导出**：支持将 AI 分析结果一键导出为结构化的 PDF 简历分析报告。

### 模拟面试模块

- **Skill 驱动出题**：内置 10+ 面试方向（Java 后端、阿里/字节/腾讯专项、前端、Python、算法、系统设计、测开、AI Agent 等），每个方向由 `SKILL.md` 定义考察范围、难度分布和参考知识库。
- **历史题目去重**：出题时自动排除已有会话中问过的题目，避免重复考察。
- **面试阶段时长联动**：总时长滑块拖动后，各阶段（自我介绍、技术考察、项目深挖、反问环节）按时比自动分配。
- **智能追问流**：支持配置多轮智能追问（默认 1 条），模拟多轮问答场景。
- **统一评估架构**：文字面试和语音面试共用同一套评估引擎（分批评估 + 结构化输出 + 二次汇总 + 降级兜底），评估结果可对比。
- **报告一键导出**：支持异步生成并导出详细的 PDF 模拟面试评估报告。
- **面试中心入口**：面试中心页整合文字面试和语音面试入口，支持继续面试和重新面试。

### 面试安排模块

- **邀请解析**：规则 + AI 双引擎，支持飞书/腾讯会议/Zoom 格式，自动提取公司、岗位、时间、会议链接
- **日历管理**：日/周/月视图 + 拖拽调整 + 列表视图
- **状态流转**：定时任务自动过期，手动标记待面试/已完成/已取消
- **面试提醒**：可配置提醒，避免错过面试

### 语音面试模块

实时语音对话面试，WebSocket + 千问3 语音模型（ASR/TTS/LLM 统一 API Key）：

- **实时流式对话**：句子级并发 TTS，边生成边合成边播放，首包延迟 200ms
- **服务端 VAD**：自动断句，实时字幕（含中间结果）
- **回声防护 + 手动提交**：避免 AI 语音被误录入
- **多轮上下文记忆 + 暂停/恢复**：超时自动暂停
- **Micrometer 埋点**：TTS/ASR 延迟、会话时长等指标

### 知识库管理模块

- **文档智能处理**：支持 PDF、DOCX、Markdown 等多种格式文档的自动上传、分块与异步向量化。
- **RAG 检索增强**：集成 pgvector，通过查询改写、相似度阈值和 TopK 策略提升 AI 问答的准确性与专业度。
- **流式响应交互**：基于 SSE（Server-Sent Events）技术实现打字机式流式响应。
- **智能问答对话**：支持会话管理、置顶、多知识库关联、Markdown 展示和虚拟列表渲染。
- **知识库运维**：支持分类管理、下载、重新向量化、搜索和统计信息展示。

### 多模型与系统设置模块

- **多 Provider 管理**：内置 DashScope、LM Studio、Kimi、DeepSeek、GLM 等 OpenAI 兼容 Provider 配置。
- **默认模型切换**：支持在设置页切换默认聊天模型和默认向量模型，不需要频繁修改源码配置。
- **语音服务配置**：ASR/TTS 配置可视化管理，支持语音服务连通性测试。
- **配置安全落盘**：运行时配置默认写入用户目录 `~/.interview-guide/`，支持 API Key 加密配置。

### 用户认证模块

- **邮箱密码登录注册**：支持邮箱 + 密码注册，密码使用 BCrypt 加密存储。
- **Session 会话管理**：浏览器端使用 HttpOnly Cookie + Session，无需手动管理 Token。
- **全站访问保护**：未登录用户自动跳转登录页，登录后回到原先访问的页面。

## 效果展示

简历与面试

### 简历与面试

面试中心：

![](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-interview-hub.png)

Skill 出题 + JD 解析：

![Skill 出题 + JD 解析](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-skill-jd-parse.png)

简历库：

![简历库](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-resume-history.png)

简历上传分析：

![简历上传分析](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-resume-upload-analysis.png)

简历分析详情：

![简历分析详情](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-resume-analysis-detail.png)

面试记录：

![面试记录](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-interview-history.png)

面试详情：

![面试详情](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-interview-detail.png)

模拟面试：

![模拟面试](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-mock-interview.png)

面试安排

![面试安排](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-interview-schedule-list.png)

多模型切换 + 语音服务设置：

![管理聊天模型、向量模型和模块配置](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/llm-settings.png)


### 知识库

知识库管理：

![知识库管理](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-knowledge-base-management.png)

问答助手：

![问答助手](https://oss.javaguide.cn/xingqiu/pratical-project/interview-guide/page-qa-assistant.png)

## 项目结构

```
interview-guide/
├── app/                              # 后端应用
│   ├── src/main/java/interview/guide/
│   │   ├── App.java                  # 主启动类
│   │   ├── common/                   # 通用基础能力
│   │   │   ├── ai/                   # LLM Provider、结构化输出、Prompt 安全
│   │   │   ├── annotation/           # @RateLimit 可重复限流注解
│   │   │   ├── aspect/               # RateLimitAspect + Redis Lua 限流
│   │   │   ├── async/                # Redis Stream 生产者/消费者模板
│   │   │   ├── config/               # CORS、S3、OpenAPI、Jackson 等配置
│   │   │   ├── evaluation/           # 文字/语音共用的统一评估引擎
│   │   │   ├── exception/            # 业务异常与全局异常处理
│   │   │   └── result/               # 统一响应 Result<T>
│   │   ├── infrastructure/           # 基础设施
│   │   │   ├── export/               # PDF 导出
│   │   │   ├── file/                 # 文件解析、校验、清洗、S3 存储
│   │   │   ├── mapper/               # MapStruct 映射器
│   │   │   └── redis/                # RedisService、面试会话缓存
│   │   └── modules/                  # 业务模块
│   │       ├── interview/            # 模拟面试模块
│   │       ├── interviewschedule/    # 面试安排模块
│   │       ├── knowledgebase/        # 知识库模块
│   │       ├── llmprovider/          # 多模型 Provider 与语音配置
│   │       ├── resume/               # 简历模块
│   │       └── voiceinterview/       # 语音面试模块
│   └── src/main/resources/
│       ├── application.yml           # 应用配置
│       ├── prompts/                  # AI 提示词模板（StringTemplate）
│       ├── scripts/                  # Redis Lua 脚本
│       ├── skills/                   # 面试 Skill 定义和参考题库
│       └── voice-interview-opening.yml # 语音面试开场白配置
│
├── frontend/                         # 前端应用
│   ├── src/
│   │   ├── api/                      # API 接口
│   │   ├── components/               # 公共组件
│   │   ├── hooks/                    # 业务 Hooks
│   │   ├── pages/                    # 页面组件
│   │   ├── types/                    # 类型定义
│   │   └── utils/                    # 工具函数
│   ├── package.json
│   └── vite.config.ts
│
├── docker-compose.yml                # 完整部署：前端 + 后端 + PostgreSQL + Redis + MinIO
├── docker-compose.dev.yml            # 本地开发依赖：PostgreSQL + Redis + RustFS
├── docs/                             # 架构设计与改造记录
├── .env.example                      # 环境变量示例
└── README.md
```

## 快速开始

环境要求：

| 依赖          | 版本 | 必需 | 说明                                     |
| ------------- | ---- | ---- | ---------------------------------------- |
| JDK           | 21+  | 是   | 开发语言                                 |
| Node.js       | 18+  | 是   | 前端构建                                 |
| pnpm          | 10+  | 推荐 | 前端包管理器（项目 packageManager 指定 10.26）|
| Docker        | -    | 推荐 | 一键启动依赖服务（PostgreSQL/Redis/RustFS）|

> 如果不用 Docker，需要自行安装 PostgreSQL 14+（含 pgvector 扩展）、Redis 6+ 和 S3 兼容存储。

### 1. 克隆项目

```bash
git clone https://github.com/Snailclimb/interview-guide.git
cd interview-guide
```

### 2. 配置环境变量

推荐复制 `.env.example` 为 `.env`，后端 `bootRun` 会自动读取根目录 `.env`。最少需要填写 `AI_BAILIAN_API_KEY`，用于 DashScope 文本模型、ASR 和 TTS：

```bash
cp .env.example .env

# 编辑 .env
# AI_BAILIAN_API_KEY=your_dashscope_api_key
# AI_MODEL=qwen3.5-flash
```

启动后默认账号：

| 服务         | 地址             | 账号            | 密码            |
| ------------ | ---------------- | --------------- | --------------- |
| PostgreSQL   | `localhost:5432` | `postgres`      | `123456`        |
| Redis        | `localhost:6379` | -               | -               |
| RustFS 控制台 | `localhost:9001` | `rustfsadmin`   | `rustfsadmin`   |

> **注意**：首次启动后需浏览器访问 [http://localhost:9001](http://localhost:9001) 登录 RustFS 控制台，手动创建名为 `interview-guide` 的 Bucket。使用 `docker-compose.dev.yml` + `:app:bootRun` 时，请确保 `.env` 中的 `APP_STORAGE_ACCESS_KEY` / `APP_STORAGE_SECRET_KEY` 与 RustFS 账号一致，例如都设为 `rustfsadmin`。如果本地已有 MinIO 或其他 S3 兼容存储，也可以直接使用，在 `.env` 中修改 `APP_STORAGE_*` 配置即可。

### 4. 启动应用

**后端：**

```bash
./gradlew :app:bootRun
```

后端服务启动于 `http://localhost:8080`

**前端：**

```bash
cd frontend
corepack enable
pnpm install
pnpm dev
```

前端服务启动于 `http://localhost:5173`

### 5. 登录注册

首次访问时会自动跳转到登录页。点击"注册"创建账号（邮箱 + 密码），注册成功后自动登录。密码使用 BCrypt 加密存储，会话通过 HttpOnly Cookie 管理。

**本地开发调试建议**：Docker 只起数据库/Redis/MinIO，后端用 IDEA 直接运行 `App.java` 或 `./gradlew :app:bootRun`，前端用 `pnpm dev`。这样能直接看到后端异常堆栈和前端浏览器报错，不用依赖 Docker 容器日志。


## Docker 快速部署

本项目提供了完整的 Docker 支持，可以一键启动所有服务（前后端、数据库、中间件）。

Docker Compose 编排了 6 个服务：PostgreSQL（pgvector）、Redis、MinIO（S3 兼容存储）、MinIO Bucket 初始化、Spring Boot 后端、React 前端（Nginx）。数据通过 Docker 命名卷持久化，`docker-compose down` 不会丢失数据。

### 1. 前置准备

- 安装 [Docker](https://www.docker.com/products/docker-desktop/) 和 Docker Compose
- 申请阿里云百炼 API Key（用于 AI 对话功能，申请地址：<https://bailian.console.aliyun.com/>）

### 2. 快速启动

在项目根目录下执行：

`.env.example` 中的 PostgreSQL、Redis、MinIO 已与 `docker-compose.yml` 对齐（数据库用户 `postgres` / 密码 `password`，MinIO `minioadmin` / `minioadmin`）。复制为 `.env` 后主要填写 `AI_BAILIAN_API_KEY`；若你曾在旧版本中使用过不同的库密码或对象存储密钥，请同步修改 `.env`，必要时重建 Postgres 卷以免旧数据与密码不一致。

```bash
# 1. 复制环境变量配置文件
cp .env.example .env

# 2. 编辑 .env 文件，填入 AI 配置
# vim .env
# 必填：AI_BAILIAN_API_KEY=your_key_here
# 可选：AI_MODEL=qwen3.5-flash   # 默认值为 qwen3.5-flash
# 也可以在设置页维护 DashScope、Kimi、DeepSeek、GLM、LM Studio 等 Provider
#
# 面试参数配置（可选）：
# APP_INTERVIEW_FOLLOW_UP_COUNT=1         # 每个主问题生成追问数量（默认 1）
# APP_INTERVIEW_EVALUATION_BATCH_SIZE=8   # 回答评估分批大小（默认 8）
# APP_AI_CONFIG_ENCRYPTION_KEY=32_chars   # 可选：运行时 Provider API Key 加密密钥

# 3. 构建并启动所有服务
docker-compose up -d --build
```

> **仅启动依赖服务**：如果只想本地开发调试（用 `./gradlew :app:bootRun` 启动后端），可以只启动基础设施：`docker compose up -d postgres redis minio createbuckets`。将 `.env.example` 复制为 `.env` 并填写 `AI_BAILIAN_API_KEY` 即可，默认账号与 `docker-compose.yml` 一致。

### 3. 服务访问

启动完成后，您可以通过以下地址访问各个服务：

| 服务             | 地址                                           | 默认账号     | 默认密码     | 说明                   |
| ---------------- | ---------------------------------------------- | ------------ | ------------ | ---------------------- |
| **前端应用**     | [http://localhost](http://localhost)           | -            | -            | 用户访问入口           |
| **后端 API**     | [http://localhost:8080](http://localhost:8080) | -            | -            | RESTful API            |
| **接口文档**     | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) | - | - | SpringDoc/Swagger UI |
| **MinIO 控制台** | [http://localhost:9001](http://localhost:9001) | `minioadmin` | `minioadmin` | 对象存储管理           |
| **MinIO API**    | `localhost:9000`                               | -            | -            | S3 兼容接口            |
| **PostgreSQL**   | `localhost:5432`                               | `postgres`   | `password`   | 数据库 (包含 pgvector) |
| **Redis**        | `localhost:6379`                               | -            | -            | 缓存与消息队列         |

### 4. 常用运维命令

```bash
# 查看服务状态
docker-compose ps

# 查看后端日志
docker-compose logs -f app

# 拉取新代码后重新构建部署
docker-compose up -d --build

# 停止并移除所有服务（数据保留在 Docker 卷中）
docker-compose down

# 停止服务并清除数据卷（慎用，会删除数据库和文件）
docker-compose down -v

# 清理无用镜像（构建产生的中间层）
docker image prune -f
```

## 使用场景

| 用户角色        | 使用场景                               |
| --------------- | -------------------------------------- |
| **求职者**      | 上传简历获取分析建议，进行模拟面试练习 |
| **HR/招聘人员** | 批量分析简历，评估候选人能力           |
| **培训机构**    | 提供面试培训服务，管理知识库资源       |


