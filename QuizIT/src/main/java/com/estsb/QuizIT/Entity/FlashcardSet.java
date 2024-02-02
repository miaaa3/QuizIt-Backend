package com.estsb.QuizIT.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Entity
@Table(name = "flashcard_sets")
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(nullable = false)
    private String description;

    @JsonManagedReference(value = "flashcard-cards")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flashcardSet" )
    private List<Flashcard> flashcards;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-flashcard")
    private User createdBy;
}
