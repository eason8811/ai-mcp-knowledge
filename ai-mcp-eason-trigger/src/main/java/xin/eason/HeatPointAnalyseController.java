package xin.eason;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * MCP Client 热点分析网络接口触发器, 用于接受用户需要进行热点分析的请求
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/mcp/api")
public class HeatPointAnalyseController {
    /**
     * 对话客户端
     */
    private final ChatClient chatClient;
    /**
     * 系统提示词
     */
    private final String systemPrompt = """
            你是一个资深的数据分析员，擅长利用自动化工具分析和挖掘实时热点信息。
                - 用户提问后，第一步请先使用工具获取今天所有主要的时事热点（如热搜、要闻、文娱等板块），在用户没有特别要求的情况下，只取排名前5的热点做进一步分析。
                - 获得热点列表后，针对每一个热点主题，请进一步调用工具获取讨论该热点的帖子的详细内容（在用户没有特别要求的情况下，每个热点只取前5个帖子进行分析），包括每个帖子的具体内容、以及每个帖子的转发、评论、点赞等社会关注度数据。
                - 对于获取到的帖子内容和社会关注度，请进行深入分析，提取出公众对于每一热点的主要观点、看法及其影响力（如通过转评赞数量体现），并分析不同观点的热度和传播度。
                - 请将你的结果整理为“今日实时热点报告”，内容包括：今日主要热点及其详细内容 (可以是概括查询到的帖子提到的信息) 、各热点的代表性观点及其社会热度、公众反响的总结，并在报告中合适的地方添加一些比较丰富的Emoji，以确保报告的亲和力。
                - 请严格按照上述流程多步使用工具，不要只停留在第一步，要逐步深入每个热点话题，最大限度利用工具获取详细数据，最后再进行综合分析。
            如果需要多次工具调用，请每次获取一组新数据后，继续推进到下一步，直到获得所有所需细节数据后再总结分析。
            """;

    /**
     * 热点分析网络接口, 对用户提供的提示词获取相应的热点信息进行分析, 然后流式返回热点分析报告
     *
     * @param userPrompt 用户提示词
     * @return 流式返回热点分析报告
     */
    @GetMapping("/heat/point/analyse")
    public Flux<ChatResponse> heatPointAnalyse(String userPrompt) {
        log.info("已经收到用户的提示词: {}", userPrompt);

        SystemMessage systemMessage = new SystemPromptTemplate(systemPrompt).create().getSystemMessage();
        UserMessage userMessage = new UserMessage(userPrompt);

        return chatClient.prompt(new Prompt(List.of(systemMessage, userMessage))).stream().chatResponse();
    }

    @GetMapping("/test")
    public Flux<ChatResponse> test(String userPrompt) {
        log.info("已经收到用户的提示词: {}", userPrompt);
        return chatClient.prompt(userPrompt).stream().chatResponse();
    }
}


