package com.estsb.QuizIT.Repository;

import com.estsb.QuizIT.Entity.Category;
import com.estsb.QuizIT.Entity.Difficulty;
import com.estsb.QuizIT.Entity.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardSetRepository extends JpaRepository<FlashcardSet,Long> {

    List<FlashcardSet> findByCategory(Category category);

    List<FlashcardSet> findByDifficulty(Difficulty difficulty);

    List<FlashcardSet> findByCategoryAndDifficulty(Category category, Difficulty difficulty);
}
