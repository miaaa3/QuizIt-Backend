package com.estsb.QuizIT.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "games")
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Teacher who created the game (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonBackReference(value = "user-games")
    private User createdBy;

    // Players of the game (ManyToMany)
    @ManyToMany
    @JoinTable(
            name = "game_users",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore  // Optional: avoid infinite recursion or large JSON payloads
    private List<User> players;

    // Flashcard set used for this game (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "flashcard_set_id")
    @JsonBackReference(value = "flashcard-set-games") // If FlashcardSet has matching JsonManagedReference
    private FlashcardSet flashcardSet;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType; // TERM, DEFINITION, RANDOM

    private Boolean isActive = true;  // To track if the game is still running
    private String gameCode; // Generated code to be used in the QR code

    @ElementCollection
    @MapKeyColumn(name = "player_id")
    @Column(name = "score")
    private Map<Long, Integer> playerScores; // Map to track each player's score

    @ElementCollection
    @MapKeyColumn(name = "player_id")
    @Column(name = "finish_time")
    private Map<Long, Long> playerFinishTimes;

    @Column(nullable = false)
    private int numberOfQuestions; // Number of questions for the game
}
