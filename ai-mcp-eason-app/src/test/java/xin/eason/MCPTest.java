package xin.eason;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

    @Test
    public void testCsdnPostArticle() {
        String userPrompt = """
                我需要你帮我生成一篇文章，要求如下；
                
                1. 场景为互联网大厂java求职者面试
                2. 面试管提问 Java 核心知识、JUC、JVM、多线程、线程池、HashMap、ArrayList、Spring、SpringBoot、MyBatis、Dubbo、RabbitMQ、xxl-job、Redis、MySQL、Linux、Docker、设计模式、DDD等不限于此的各项技术问题。
                3. 按照故事场景，以严肃的面试官和搞笑的水货程序员谢飞机进行提问，谢飞机对简单问题可以回答，回答好了面试官还会夸赞。复杂问题胡乱回答，回答的不清晰。
                4. 每次进行3轮提问，每轮可以有3-5个问题。这些问题要有技术业务场景上的衔接性，循序渐进引导提问。最后是面试官让程序员回家等通知类似的话术。
                5. 提问后把问题的答案，写到文章最后，最后的答案要详细讲述出技术点，让小白可以学习下来。
                
                根据以上内容，不要阐述其他信息，请直接提供；文章标题、文章内容、文章标签（多个用英文逗号隔开且必须提供）、文章简述（100字）
                
                将你生成的内容发布成文章到CSDN, 然后将发布文章的响应结果一并输出给我, 如果响应为 null 代表发布失败
                """;
        ChatClient chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o").build())
                .build();

        System.out.println("用户问题: " + userPrompt);
        System.out.println("回答:" + chatClient.prompt(userPrompt).call().content());
    }

    @Test
    public void testCsdn() {
        String userPrompt = "请在CSDN上发一篇测试文章，不要阐述其他信息，请直接提供；文章标题、文章内容、文章标签（多个用英文逗号隔开且必须提供）、文章简述（100字）\n 将你生成的内容发布成文章到CSDN, 然后将发布文章的响应结果一并输出给我, 如果响应为 null 代表发布失败";
        ChatClient chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o").build())
                .build();

        System.out.println("用户问题: " + userPrompt);
        System.out.println("回答:" + chatClient.prompt(userPrompt).call().content());
    }

    @Test
    public void testCharset() throws UnsupportedEncodingException {
        String msg = "嗯，集合就是一个装东西的袋子，HashMap就像一个超市，可以通过产品编号快速找到产品，而ArrayList就像一个便利贴，可以随意添加，顺序存放。\\n";
        System.out.println("Origin:" + msg);
        System.out.println("UTF-8 解码 -> UTF-8 编码: " + new String(msg.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        System.out.println("UTF-8 解码 -> GBK 编码:" + new String(msg.getBytes(StandardCharsets.UTF_8), Charset.forName("GBK")));

        String errorMsg = "锛氬棷锛岄泦鍚堝氨鏄\uE219竴涓\uE047\uE5CA涓滆タ鐨勮\uE570瀛愶紝HashMap灏卞儚涓�涓\uE047秴甯傦紝鍙\uE219互閫氳繃浜у搧缂栧彿蹇\uE0A6�熸壘鍒颁骇鍝併�傝�孉rrayList灏卞儚涓�涓\uE043究鍒╄创锛屽彲浠ラ殢鎰忔坊鍔狅紝椤哄簭瀛樻斁銆俓n";
        System.out.println("Origin:" + errorMsg);
        System.out.println("UTF-8 -> GBK: " + new String(errorMsg.getBytes(StandardCharsets.UTF_8), "GBK"));
        System.out.println("GBK -> UTF-8: " + new String(errorMsg.getBytes("GBK"), StandardCharsets.UTF_8));

    }
}
