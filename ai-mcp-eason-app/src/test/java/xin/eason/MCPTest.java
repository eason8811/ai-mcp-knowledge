package xin.eason;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
    public void testFileSystemWorkFlow() {
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
                
                将你生成的内容发布成文章到CSDN, 然后将发布文章的响应结果一并输出给我
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

    @Test
    public void testWorkFlow() {
        String userPrompt = """
                我需要你帮我生成一篇文章，要求如下；
                
                1. 场景为互联网大厂java求职者面试
                2. 面试管提问 Java 核心知识、JUC、JVM、多线程、线程池、HashMap、ArrayList、Spring、SpringBoot、MyBatis、Dubbo、RabbitMQ、xxl-job、Redis、MySQL、Linux、Docker、设计模式、DDD等不限于此的各项技术问题。
                3. 按照故事场景，以严肃的面试官和搞笑的水货程序员谢飞机进行提问，谢飞机对简单问题可以回答，回答好了面试官还会夸赞。复杂问题胡乱回答，回答的不清晰。
                4. 每次进行3轮提问，每轮可以有3-5个问题。这些问题要有技术业务场景上的衔接性，循序渐进引导提问。最后是面试官让程序员回家等通知类似的话术。
                5. 提问后把问题的答案，写到文章最后，最后的答案要详细讲述出技术点，让小白可以学习下来。
                
                根据以上内容，不要阐述其他信息，请直接提供；文章标题、文章内容、文章标签（多个用英文逗号隔开且必须提供）、文章简述（100字）
                
                将你生成的内容发布成文章到CSDN, 然后将文章标题和文章内容以markdown形式写入到 D:\\Code\\codes\\ai-mcp-eason\\ai-mcp-eason-app\\src\\main\\resources\\file-system-test\\csdn-posts 目录下, 以md格式保存,
                然后将发布文章的响应结果和写入位置, 以及写入成功与否的结果一并输出给我
                """;
        ChatClient chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o").build())
                .build();
        System.out.println("用户问题: " + userPrompt);
        System.out.println("回答:" + chatClient.prompt(userPrompt).call().content());
    }

    @Test
    public void testHeatPointAnalyse() {
        String systemPrompt = """
            你是一个资深的数据分析员，擅长利用自动化工具分析和挖掘实时热点信息。
                - 用户提问后，第一步请先使用工具获取今天所有主要的时事热点（如热搜、要闻、文娱等板块），在用户没有特别要求的情况下，只取排名前5的热点做进一步分析。
                - 获得热点列表后，针对每一个热点主题，请进一步调用工具获取讨论该热点的帖子的详细内容（在用户没有特别要求的情况下，每个热点只取前5个帖子进行分析），包括每个帖子的具体内容、以及每个帖子的转发、评论、点赞等社会关注度数据。
                - 对于获取到的帖子内容和社会关注度，请进行深入分析，提取出公众对于每一热点的主要观点、看法及其影响力（如通过转评赞数量体现），并分析不同观点的热度和传播度。
                - 请将你的结果整理为“今日实时热点报告”，内容包括：今日主要热点及其详细内容 (可以是概括查询到的帖子提到的信息) 、各热点的代表性观点及其社会热度、公众反响的总结。
                - 请严格按照上述流程多步使用工具，不要只停留在第一步，要逐步深入每个热点话题，最大限度利用工具获取详细数据，最后再进行综合分析。
            如果需要多次工具调用，请每次获取一组新数据后，继续推进到下一步，直到获得所有所需细节数据后再总结分析。
            """;
        String userPrompt = "今天发生了什么时事新闻";

        List<Message> messageList = List.of(new SystemMessage(systemPrompt), new UserMessage(userPrompt));

        ChatClient chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o").build())
                .build();
        System.out.println("用户问题: " + userPrompt);
        System.out.println("回答: \n" + chatClient.prompt(new Prompt(messageList)).call().content());
    }
}