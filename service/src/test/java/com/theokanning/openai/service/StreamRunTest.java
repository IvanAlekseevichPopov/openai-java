package com.theokanning.openai.service;

import com.theokanning.openai.assistants.Assistant;
import com.theokanning.openai.assistants.AssistantRequest;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.RunChunk;
import com.theokanning.openai.runs.StreamRunCreateRequest;
import com.theokanning.openai.threads.Thread;
import com.theokanning.openai.threads.ThreadRequest;
import com.theokanning.openai.utils.TikTokensUtil;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

class StreamRunTest {
    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    @Timeout(10)
    void createStreamRun() {
        AssistantRequest assistantRequest = AssistantRequest.builder()
                .model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName())
                .name("MATH_TUTOR")
                .instructions("You are a personal Math Tutor.")
                .build();
        Assistant assistant = service.createAssistant(assistantRequest);

        ThreadRequest threadRequest = ThreadRequest.builder().build();
        Thread thread = service.createThread(threadRequest);

        MessageRequest messageRequest = MessageRequest.builder()
                .content("Hello")
                .build();

        service.createMessage(thread.getId(), messageRequest);

        StreamRunCreateRequest runCreateRequest = StreamRunCreateRequest.builder()
                .assistantId(assistant.getId())
                .build();

        Flowable<RunChunk> run = service.createRun(thread.getId(), runCreateRequest);

        assertNotNull(run);

        StringBuilder message = new StringBuilder();
        StringBuilder error = new StringBuilder();
        run
                .doOnError(error::append)
                .blockingForEach(chunk -> {
                    String delta = chunk.getDelta();
                    if (delta != null && !delta.isEmpty()) {
                        message.append(delta);
                    }
                });

        assertTrue(error.toString().isEmpty(), "Error in run processing: " + error);
        assertNotNull(message, "Invalid message received from OA"); //TODO test it locally

//        assertNotNull(message, "Invalid message received from OA");
    }
}
