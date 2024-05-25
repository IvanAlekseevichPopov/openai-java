package com.theokanning.openai.runs;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.theokanning.openai.utils.DeltaDeserializer;
import lombok.Data;

/**
 * Object containing a response chunk from the assistants streaming api.
 */
@Data
public class RunChunk {
    /**
     * Unique id assigned to this chat completion.
     */
    String id;

    /**
     * The type of object returned, can be "thread.run", "thread.message.delta"
     */
    String object;

    /**
     * The creation time in epoch seconds.
     */
    long created;

    /**
     * The model used.
     */
    String model;

    /**
     * The chunk of openAI api assistant message
     */
    @JsonDeserialize(using = DeltaDeserializer.class)
    String delta;
}
