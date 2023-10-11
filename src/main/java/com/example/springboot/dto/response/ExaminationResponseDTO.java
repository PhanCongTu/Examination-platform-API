package com.example.springboot.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class ExaminationResponseDTO {

    private Long id;

    private String examName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private TopicResponseDTO topic;
}
