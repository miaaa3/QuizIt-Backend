package com.estsb.QuizIT.Repository;

import com.estsb.QuizIT.Entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard,Long> {
}
