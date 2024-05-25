package com.theokanning.openai.runs;

import com.theokanning.openai.assistants.Tool;
import lombok.*;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class StreamRunCreateRequest extends RunCreateRequest {
    final boolean stream = true;

    @Builder
    public StreamRunCreateRequest(
            String assistantId,
            String model,
            String instructions,
            List<Tool> tools,
            Map<String, String> metadata
    ) {
        super(assistantId, model, instructions, tools, metadata);
    }
}
