package xin.eason.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Ollama AI 配置属性类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("spring.ai.ollama")
public class OllamaConfigProperties {
    /**
     * 嵌入模型
     */
    private String embedingModel;
}
