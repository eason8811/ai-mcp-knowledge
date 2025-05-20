package xin.eason.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

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
}
