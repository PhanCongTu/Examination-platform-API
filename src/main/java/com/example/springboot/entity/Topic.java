package com.example.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "topic")
public class Topic extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;
    private static final String TOPIC_ID = "id";
    private static final String TOPIC_NAME = "topic_name";
    private static final String CODE = "code";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TOPIC_ID, nullable = false)
    private Long id;

    @Column(name = TOPIC_NAME)
    private String topicName;

    @Column(name = CODE)
    private String code;

    @OneToMany(
            mappedBy = "topic",
            cascade = CascadeType.ALL
    )
    private List<Examination> examinations;
}
