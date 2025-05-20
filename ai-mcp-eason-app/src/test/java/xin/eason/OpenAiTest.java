package xin.eason;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class OpenAiTest {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Test
    public void testCall() {
        String userMsg = "1+1等于几";
        ChatResponse response = openAiChatModel.call(new Prompt(userMsg, OpenAiChatOptions.builder().model("gpt-4o").build()));
        log.info("{}", response);
    }
}
