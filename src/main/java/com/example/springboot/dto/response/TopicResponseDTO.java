package com.example.springboot.dto.response;

import lombok.AllArgsConstructor;
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

    public TopicResponseDTO(Long id, String code, String topicName) {
        this.id = id;
        this.code = code;
        this.topicName = topicName;
    }
}
