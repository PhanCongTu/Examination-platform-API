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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "score")
public class Score extends AbstractAuditingEntity {

    private static final String TOTAL_SCORE = "total_core";
    private static final String TOTAL_CORRECT = "total_correct";
    private static final String SUBMITTED_DATE = "submitted_date";
    private static final String MULTIPLE_CHOICE_TEST_ID = "multiple_choice_test_id";
    private static final String TARGET_SCORE = "target_score";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = TOTAL_SCORE)
    private double totalCore;

    @Column(name = TOTAL_CORRECT)
    private Long totalCorrect;

    @Column(name = SUBMITTED_DATE)
    private Long submittedDate;

    private boolean isLate;

    @Column(name = TARGET_SCORE)
    private Double targetScore;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = MULTIPLE_CHOICE_TEST_ID, referencedColumnName = "id")
    private MultipleChoiceTest multipleChoiceTest;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserProfile userProfile;

    @OneToMany(
            mappedBy = "score",
            cascade = CascadeType.ALL
    )
    private List<SubmittedQuestion> submittedQuestions;
}
