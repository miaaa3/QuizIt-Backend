package com.estsb.QuizIT.Service;

import com.estsb.QuizIT.Entity.Category;
import com.estsb.QuizIT.Entity.Difficulty;
import com.estsb.QuizIT.Entity.Quiz;
import com.estsb.QuizIT.Repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;


    public Quiz createQuiz(Quiz quiz) {
        try {
            String categoryName = quiz.getCategory().name().toUpperCase().replaceAll("\\s", "");
            Category normalizedCategory = Category.valueOf(categoryName);
            quiz.setCategory(normalizedCategory);
            return quizRepository.save(quiz);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + quiz.getCategory(), e);
        }
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long quizId) {
        return quizRepository.findById(quizId);
    }

    public List<Quiz> getQuizzesByCategory(Category category) {
        return quizRepository.findByCategory(category);
    }

    public List<Quiz> getQuizzesByCategoryAndDifficulty(Category category, Difficulty difficulty) {
        return quizRepository.findByCategoryAndDifficulty(category, difficulty);
    }

    public Quiz updateQuiz(Long quizId, Quiz updatedQuiz) {
        if (quizRepository.existsById(quizId)) {
            updatedQuiz.setQuizId(quizId);
            return quizRepository.save(updatedQuiz);
        } else {
            return null;
        }
    }

    public boolean deleteQuiz(Long quizId) {
        if (quizRepository.existsById(quizId)) {
            quizRepository.deleteById(quizId);
            return true;
        } else {
            return false;
        }
    }
}
