package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateCreateTopic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateCreateTopic
public class CreateTopicDTO {
    private String code;
    private String topicName;
}
