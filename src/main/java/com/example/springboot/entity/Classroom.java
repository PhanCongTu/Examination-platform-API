package com.example.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "class_room")
@ToString
public class Classroom extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;
    private static final String CLASS_ID = "id";
    private static final String CLASS_NAME = "class_name";
    private static final String CLASS_CODE = "class_code";
    private static final String DESCRIPTION = "description";
    private static final String IS_PRIVATE = "is_private";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = CLASS_ID, nullable = false)
    private Long id;

    @Column(name = CLASS_NAME)
    private String className;

    @Column(name = CLASS_CODE)
    private String classCode;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = IS_PRIVATE)
    private Boolean isPrivate;

    @OneToMany(
            mappedBy = "classRoom",
            cascade = CascadeType.ALL
    )
    private List<ClassroomRegistration> ClassroomRegistrations;

    @OneToMany(
            mappedBy = "classRoom",
            cascade = CascadeType.ALL
    )
    private List<QuestionGroup> questionGroups;

    @OneToMany(
            mappedBy = "classRoom",
            cascade = CascadeType.ALL
    )
    private List<MultipleChoiceTest> multipleChoiceTests;
}