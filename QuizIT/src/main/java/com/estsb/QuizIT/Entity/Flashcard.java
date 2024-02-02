package com.estsb.QuizIT.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Flashcards")
@AllArgsConstructor
@NoArgsConstructor
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String term;

    @Column(nullable = false)
    private String definition;

    @ManyToOne
    @JoinColumn(name = "flashcard_set_id")
    @JsonBackReference(value = "flashcard-cards")
    private FlashcardSet flashcardSet;
}
