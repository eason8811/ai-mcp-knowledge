package xin.eason;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class MCPTest {

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    @Autowired
    private ToolCallbackProvider toolCallbackProvider;

    @Test
    public void testToolUsable() {
        ChatClient chatClient = chatClientBuilder.defaultToolCallbacks(toolCallbackProvider)
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o").build())
                .build();

        String userPrompt = "有哪些工具可以使用";
        System.out.println("用户问题: " + userPrompt);
        System.out.println("回答:" + chatClient.prompt(userPrompt).call().content());
    }

    @Test
    public void testWorkFlow() {
        String userPrompt = "获取 Eason 的电脑配置";

        ChatClient chatClient = chatClientBuilder.defaultToolCallbacks(toolCallbackProvider)
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o").build())
                .build();

        System.out.println("用户问题: " + userPrompt);
        System.out.println("回答: " + chatClient.prompt(userPrompt).call().content());
    }
}
