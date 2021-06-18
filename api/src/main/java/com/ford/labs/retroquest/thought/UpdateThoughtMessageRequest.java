package com.ford.labs.retroquest.thought;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateThoughtMessageRequest {
    String message;
}
