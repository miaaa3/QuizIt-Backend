package com.estsb.QuizIT.Repository;

import com.estsb.QuizIT.Entity.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardSetRepository extends JpaRepository<FlashcardSet,Long> {
}
