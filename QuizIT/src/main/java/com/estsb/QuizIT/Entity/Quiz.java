package com.estsb.QuizIT.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Quizzes")
@EqualsAndHashCode(exclude = {"questions","createdBy"})
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    private String quizName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    @JsonManagedReference(value = "quiz-questions")
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonBackReference(value = "user-quiz")
    private User createdBy;

    @Column(nullable = false)
    private Boolean isPublic = true;

    private LocalDateTime createdAt = LocalDateTime.now();

}
