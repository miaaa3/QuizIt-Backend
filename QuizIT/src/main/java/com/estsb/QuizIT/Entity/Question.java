package com.estsb.QuizIT.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "Questions")
@EqualsAndHashCode(exclude = {"quiz"})
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private String question;

    private String description;

    @ElementCollection
    @CollectionTable(name = "question_answers", joinColumns = @JoinColumn(name = "question_id"))
    @MapKeyColumn(name = "answer_option")
    @Column(name = "answer")
    private Map<String, String> answers;
    
    private boolean multipleCorrectAnswers;

    @ElementCollection
    @CollectionTable(name = "question_correct_answers", joinColumns = @JoinColumn(name = "question_id"))
    @MapKeyColumn(name = "answer_option")
    @Column(name = "is_correct")
    private Map<String, Boolean> correctAnswers;

    private String explanation;

    private String tip;

    @ElementCollection
    @CollectionTable(name = "question_tags", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "tag")
    private List<String> tags;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @JsonBackReference(value = "quiz-questions")
    private Quiz quiz;

    private String userAnswer;
}
