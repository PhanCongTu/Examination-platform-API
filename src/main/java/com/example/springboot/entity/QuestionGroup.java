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
import javax.persistence.Table;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "question_group")
public class QuestionGroup extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;
    private static final String ID = "id";
    private static final String CODE = "code";
    private static final String NAME = "name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    private Long id;

    @Column(name = CODE)
    private String code;

    @Column(name = NAME)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassRoom classRoom;

    @OneToMany(
            mappedBy = "questionGroup",
            cascade = CascadeType.ALL
    )
    private List<Question> questions;
}
