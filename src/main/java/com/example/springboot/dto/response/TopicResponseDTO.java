package com.example.springboot.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TopicResponseDTO {
    private Long id;
    private String code;
    private String topicName;
}
