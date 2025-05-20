package xin.eason.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Ollama AI 配置类
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OllamaConfigProperties.class)
public class OllamaConfig {

    /**
     * Ollama 配置属性对象
     */
    private final OllamaConfigProperties ollamaConfigProperties;

    /**
     * 注入 {@link SimpleVectorStore} 简易向量存储器对象, 用于本地存储向量信息 ( 嵌入模型: nomic-embed-text )
     *
     * @param ollamaApi {@link  OllamaApi} 的 Bean 对象
     * @return {@link SimpleVectorStore} Bean 对象
     */
    @Bean
    public SimpleVectorStore simpleVectorStoreOllamaAi(OllamaApi ollamaApi) {
        OllamaEmbeddingModel ollamaEmbeddingModel = OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(
                        OllamaOptions.builder()
                        .model(ollamaConfigProperties.getEmbedingModel())
                        .build()
                )
                .build();
        return SimpleVectorStore.builder(ollamaEmbeddingModel).build();
    }

    /**
     * 注入 {@link PgVectorStore} 可使用数据库存储的向量储存器对象 ( 嵌入模型: nomic-embed-text )
     *
     * @param ollamaApi    {@link  OllamaApi} 的 Bean 对象
     * @param jdbcTemplate 用于调用数据库的对象
     * @return {@link PgVectorStore} Bean 对象
     */
    @Bean
    public PgVectorStore pgVectorStoreOllamaAi(OllamaApi ollamaApi, JdbcTemplate jdbcTemplate) {
        OllamaEmbeddingModel ollamaEmbeddingModel = OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(
                        OllamaOptions.builder()
                                .model(ollamaConfigProperties.getEmbedingModel())
                                .build()
                )
                .build();
        return PgVectorStore.builder(jdbcTemplate, ollamaEmbeddingModel)
                .vectorTableName("vector_store_ollama_deepseek")
                .build();
    }
}
