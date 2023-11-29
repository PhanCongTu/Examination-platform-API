package com.example.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "multiple_choice_test")
public class MultipleChoiceTest extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;
    private static final String TEST_ID = "id";
    private static final String TEST_NAME = "test_name";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String TESTING_TIME = "testing_time";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TEST_ID, nullable = false)
    private Long id;

    @Column(name = TEST_NAME)
    private String testName;

    @Column(name = START_DATE)
    private Long startDate; // milliseconds since January 1, 1970

    @Column(name = END_DATE)
    private Long endDate; // milliseconds since January 1, 1970

    @Column(name = TESTING_TIME)
    private Long testingTime; // minutes

    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classRoom;

    @OneToMany(
            mappedBy = "multipleChoiceTest",
            cascade = CascadeType.ALL
    )
    private List<TestQuestion> testQuestions;

    @OneToOne(mappedBy = "multipleChoiceTest")
    private Score score;

    @OneToMany(
            mappedBy = "multipleChoiceTest",
            cascade = CascadeType.ALL
    )
    private List<TestTracking> doTestHistories;
}
