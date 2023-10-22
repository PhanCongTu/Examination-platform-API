package com.example.springboot.dto.request;

import com.example.springboot.constant.Constants;
import com.example.springboot.validate.ValidateCreateObjectiveTest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateCreateObjectiveTest
public class CreateObjectiveTestDTO {

    private String objectiveTestName;

    private Long classRoomId;

    private Integer testingTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = Constants.EXAM_DATE_PATTERN)
    private LocalDateTime startDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = Constants.EXAM_DATE_PATTERN)
    private LocalDateTime endDate;
}
