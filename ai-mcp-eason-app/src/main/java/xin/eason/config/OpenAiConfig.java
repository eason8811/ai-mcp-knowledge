package xin.eason.config;

import io.micrometer.observation.ObservationRegistry;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClientBuilder;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.observation.ChatClientObservationConvention;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 * Open AI 配置类
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenAiConfigProperties.class)
public class OpenAiConfig {

    /**
     * OpenAI 配置属性对象
     */
    private final OpenAiConfigProperties openAiConfigProperties;

    /**
     * 注入 OpenAI 的 API Bean对象
     *
     * @return OpenAI 的 API Bean对象
     */
    @Bean
    public OpenAiApi openAiApi() {
        return OpenAiApi.builder()
                .baseUrl(openAiConfigProperties.getBaseUrl())
                .apiKey(openAiConfigProperties.getApiKey())
                .build();
    }

    /**
     * 注入 {@link SimpleVectorStore} 简易向量存储器对象, 用于本地存储向量信息 ( 嵌入模型: text-embedding-ada-002 )
     *
     * @param openAiApi {@link  OpenAiApi} 的 Bean 对象
     * @return {@link SimpleVectorStore} Bean 对象
     */
    @Bean
    public SimpleVectorStore simpleVectorStoreOpenAi(OpenAiApi openAiApi) {
        OpenAiEmbeddingModel openAiEmbeddingModel = new OpenAiEmbeddingModel(openAiApi);
        return SimpleVectorStore.builder(openAiEmbeddingModel).build();
    }

    /**
     * 注入 {@link PgVectorStore} 可使用数据库存储的向量储存器对象 ( 嵌入模型: text-embedding-ada-002 )
     *
     * @param openAiApi    {@link  OpenAiApi} 的 Bean 对象
     * @param jdbcTemplate 用于调用数据库的对象
     * @return {@link PgVectorStore} Bean 对象
     */
    @Bean
    public PgVectorStore pgVectorStoreOpenAi(OpenAiApi openAiApi, JdbcTemplate jdbcTemplate) {
        OpenAiEmbeddingModel openAiEmbeddingModel = new OpenAiEmbeddingModel(openAiApi);
        return PgVectorStore.builder(jdbcTemplate, openAiEmbeddingModel)
                .vectorTableName("vector_store_openai")
                .build();
    }

    /**
     * 指定 {@link DefaultChatClientBuilder} 使用的模型
     *
     * @param openAiChatModel OpenAI 模型
     * @return 对话客户端构建器
     */
    @Bean
    public ChatClient.Builder chatClientBuilder(OpenAiChatModel openAiChatModel) {
        return new DefaultChatClientBuilder(openAiChatModel, ObservationRegistry.NOOP, (ChatClientObservationConvention) null);
    }

    /**
     * 注入对话客户端的 Bean 对象
     *
     * @param chatClientBuilder    对话客户端构建器
     * @param toolCallbackProvider 工具回调函数提供器
     * @return 对话客户端的 Bean 对象
     */
    @Bean
    public ChatClient client(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
        return chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultOptions(OpenAiChatOptions.builder().model(openAiConfigProperties.getChattingModel()).build())
                .build();
    }
}
