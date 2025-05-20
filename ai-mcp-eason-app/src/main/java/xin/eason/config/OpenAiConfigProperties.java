package xin.eason.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OpenAI 配置属性类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("spring.ai.openai")
public class OpenAiConfigProperties {
    /**
     * 调用 API Key 的 Base URL
     */
    private String baseUrl;

    /**
     * 调用的 API Key
     */
    private String apiKey;

    /**
     * 嵌入模型
     */
    private String embeddingModel;
}
