package com.example.springboot.controller;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateClassroomDTO;
import com.example.springboot.dto.request.CreateMultipleChoiceTestDTO;
import com.example.springboot.dto.request.UpdateClassroomDTO;
import com.example.springboot.dto.request.UpdateMultipleChoiceTestDTO;
import com.example.springboot.exception.NotEnoughQuestionException;
import com.example.springboot.exception.QuestionGroupNotFoundException;
import com.example.springboot.exception.QuestionNotFoundException;
import com.example.springboot.service.MultipleChoiceTestService;
import com.example.springboot.util.CustomBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedHashMap;

@Validated
@RestController
@RequestMapping("/api/v1/multiple-choice-test")
@Slf4j
@AllArgsConstructor
public class MultipleChoiceTestController {
    private static final String DEFAULT_SEARCH = "";
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_COLUMN = "id";
    private static final String DEFAULT_SIZE = "12";
    private static final String DEFAULT_SORT_INCREASE = "asc";

    private final MultipleChoiceTestService multipleChoiceTestService;

    @GetMapping(value = "/{testId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public ResponseEntity<?> getMultipleChoiceTest(
            @PathVariable(name = "testId") Long testId){
        return multipleChoiceTestService.
                getMultipleChoiceTest(testId);
    }
    @GetMapping(value = "/my/info/{testId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getInfoMyMultipleChoiceTest(
            @PathVariable(name = "testId") Long testId){
        return multipleChoiceTestService.
                getInfoMultipleChoiceTest(testId);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMyMultipleChoiceTests(
            @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType,
            @RequestParam(defaultValue = "false") boolean isEnded
    ){
        return multipleChoiceTestService.
                getMyMultipleChoiceTests(isEnded, search, page, column, size, sortType);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(value = "/me/two-weeks-around", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMyMultipleChoiceTestsOf2WeeksAround( @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
                                                                     @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                                     @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
                                                                     @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                                                     @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType){
        return multipleChoiceTestService.
                getMyMultipleChoiceTestsOf2WeeksAround(search, page, column, size, sortType);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(value = "/me/specific-day", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMyMultipleChoiceTestsOfASpecificDay(@RequestParam Long startOfDate,
                                                                    @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
                                                                    @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                                    @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
                                                                    @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                                                    @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType){
        return multipleChoiceTestService.
                getMyMultipleChoiceTestsToday(startOfDate, search, page, column, size, sortType);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(value = "/me/next-two-weeks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMyMultipleChoiceTestsNext2Weeks( @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
                                                                 @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                                 @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
                                                                 @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                                                 @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType){
        return multipleChoiceTestService.
                getMyMultipleChoiceTestsNext2Weeks(search, page, column, size, sortType);
    }

    @GetMapping(value = "/classroom/{classroomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMultipleChoiceTestsOfClassroom(
            @PathVariable(name = "classroomId") Long classroomId,
            @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType,
            @RequestParam(defaultValue = "false") boolean isEnded
    ){
        return multipleChoiceTestService.
                getMultipleChoiceTestsOfClassroom(classroomId, isEnded, search, page, column, size, sortType);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> createMultipleChoiceTest(@Valid @RequestBody CreateMultipleChoiceTestDTO DTO){
        try {
            return multipleChoiceTestService.createMultipleChoiceTest(DTO);
        } catch (NotEnoughQuestionException ex) {
            LinkedHashMap<String, String> response = new LinkedHashMap<>();
            response.put(Constants.ERROR_CODE_KEY, ErrorMessage.NOT_ENOUGH_QUESTION.getErrorCode());
            response.put(Constants.MESSAGE_KEY, String.format(ErrorMessage.NOT_ENOUGH_QUESTION.getMessage(), ex.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (QuestionNotFoundException ex) {
            return CustomBuilder.buildQuestionNotFoundResponseEntity();
        } catch (QuestionGroupNotFoundException ex) {
            return CustomBuilder.buildQuestionGroupNotFoundResponseEntity();
        }
    }
    @DeleteMapping(value = "/delete/{testId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> deleteMultipleChoiceTest(@PathVariable(name = "testId") Long testId){
        return multipleChoiceTestService.deleteMultipleChoiceTest(testId);
    }
    @PutMapping(value = "/update/info/{testId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> updateMultipleChoiceTest(@PathVariable(name = "testId") Long testId,
                                             @RequestBody UpdateMultipleChoiceTestDTO DTO){
        return multipleChoiceTestService.updateMultipleChoiceTest(testId, DTO);
    }
}
