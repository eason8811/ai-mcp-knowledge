# AI Agent驱动的热点分析助手

## **项目介绍**
基于AI MCP（大模型上下文协议）构建的热点分析 AI Agent系统，
实现从用户指令解析→工具调度→数据获取→AI分析→流式报告生成的全流程闭环。
系统包含前端对话界面、MCP Client（指令路由代理）和 MCP Server（工具执行服务），
通过Function Calling机制动态调度工具链，实现对时事热点的智能追踪与分析，并且项目部署上线到服务器。

---

## **核心技术栈**
- Spring AI + Spring WebFlux + AI MCP协议 + Retrofit2 + SSE + OpenAI Function Calling + VUE前端

---
## **项目亮点**
* 设计 **AI Agent** 分层架构：
  * **MCP Client**：接收用户 **user prompt**，组合 **system prompt** 指令以及动态生成 **Function Calling** 请求
  * **MCP Server**：提供热点数据抓取工具集（如微博热搜不同板块），通过Retrofit2实现高效数据采集
  * **大模型分析与总结**：大模型根据实时采集的数据生成结构化报告，通过SSE流式推送给用户，提高用户的使用体验
* **实现动态工具调度机制**：基于 **OpenAI Function Calling** 协议，**MCP Client** 自动解析大模型返回的工具调用请求，路由至对应 **MCP Server** 执行，解决大模型无法实时联网的局限。 
* **构建端到端流式处理管道**：前端请求 → MCP Client指令组合 → 大模型工具调度 → MCP Server数据获取 → AI分析 → SSE流式返回。