package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.response.TestTrackingResponse;
import com.example.springboot.entity.MultipleChoiceTest;
import com.example.springboot.entity.TestTracking;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.MultipleChoiceTestRepository;
import com.example.springboot.repository.TestTrackingRepository;
import com.example.springboot.service.TestTrackingService;
import com.example.springboot.util.CustomBuilder;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TestTrackingServiceImpl implements TestTrackingService {
    private final TestTrackingRepository testTrackingRepository;
    private final WebUtils webUtils;
    private final MultipleChoiceTestRepository multipleChoiceTestRepository;
    @Override
    public ResponseEntity<?> getTestingInProgress(Long testId) {
        Long  myId = webUtils.getCurrentLogedInUser().getUserID();
        Optional<TestTracking> currentTest =
                testTrackingRepository
                        .findByMultipleChoiceTestIdAndUserProfileUserID(testId, myId);
        if (currentTest.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        TestTrackingResponse response = CustomBuilder.buildTestTrackingResponse(currentTest.get());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> createTestingInProcess(Long testId) {
        UserProfile me = webUtils.getCurrentLogedInUser();
        Optional<MultipleChoiceTest> multipleChoiceTestOp = multipleChoiceTestRepository.findById(testId);
        if(multipleChoiceTestOp.isEmpty()) {
            return CustomBuilder.buildMultipleChoiceTestNotFoundResponseEntity();
        }
        MultipleChoiceTest multipleChoiceTest = multipleChoiceTestOp.get();
        Long maxTestingTime = Timestamp.from(ZonedDateTime.now().toInstant().plus(
                multipleChoiceTest.getTestingTime(),
                ChronoUnit.MINUTES
        )).getTime();
        Long dueTime = Math.min(
                multipleChoiceTest.getEndDate(),
                maxTestingTime
        );
        TestTracking testTracking = TestTracking.builder()
                .dueTime(dueTime)
                .firstTimeAccess(Timestamp.from(ZonedDateTime.now().toInstant()).getTime())
                .userProfile(me)
                .multipleChoiceTest(multipleChoiceTest)
                .build();
        testTracking = testTrackingRepository.save(testTracking);
        TestTrackingResponse response = CustomBuilder.buildTestTrackingResponse(testTracking);
        return ResponseEntity.ok(response);
    }
}
