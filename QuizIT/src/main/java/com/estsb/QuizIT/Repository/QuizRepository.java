package com.estsb.QuizIT.Repository;

import com.estsb.QuizIT.Entity.Category;
import com.estsb.QuizIT.Entity.Difficulty;
import com.estsb.QuizIT.Entity.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    List<Quiz> findByCategory(Category category);
    List<Quiz> findByCategoryAndDifficulty(Category category, Difficulty difficulty);

}
