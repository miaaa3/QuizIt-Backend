package com.estsb.QuizIT.Service;

import com.estsb.QuizIT.Entity.Quiz;
import com.estsb.QuizIT.Entity.User;
import com.estsb.QuizIT.Repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    // Create a new quiz
    public Quiz createQuiz(Quiz quiz) {
        try {
            return quizRepository.save(quiz);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // Retrieve all quizzes
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    // Retrieve a quiz by its ID
    public Optional<Quiz> getQuizById(Long quizId) {
        return quizRepository.findById(quizId);
    }

    // Retrieve the last quiz (based on list order)
    public Optional<Quiz> getLastId() {
        List<Quiz> quizzes = quizRepository.findAll();
        return quizzes.isEmpty() ? Optional.empty() : Optional.of(quizzes.get(quizzes.size() - 1));
    }

    // Update an existing quiz by ID
    public Quiz updateQuiz(Long quizId, Quiz updatedQuiz) {
        if (quizRepository.existsById(quizId)) {
            updatedQuiz.setQuizId(quizId);
            return quizRepository.save(updatedQuiz);
        } else {
            return null;
        }
    }

    // Delete a quiz by ID
    public boolean deleteQuiz(Long quizId) {
        if (quizRepository.existsById(quizId)) {
            quizRepository.deleteById(quizId);
            return true;
        } else {
            return false;
        }
    }

    // Set the visibility (public/private) of a quiz
    public Quiz setQuizVisibility(Long quizId, boolean isPublic) {
        return quizRepository.findById(quizId).map(quiz -> {
            quiz.setIsPublic(isPublic);
            return quizRepository.save(quiz);
        }).orElse(null);
    }

    // Search quizzes by optional filters: name, visibility, and creator
    public List<Quiz> searchQuizzes(
            String quizName,
            Boolean isPublic,
            User createdBy) {

        List<Quiz> allQuizzes = quizRepository.findAll();

        return allQuizzes.stream()
                .filter(q -> quizName == null || q.getQuizName() != null && q.getQuizName().toLowerCase().contains(quizName.toLowerCase()))
                .filter(q -> isPublic == null || (q.getIsPublic() != null && q.getIsPublic().equals(isPublic)))
                .filter(q -> createdBy == null || (q.getCreatedBy() != null && q.getCreatedBy().equals(createdBy)))
                .collect(Collectors.toList());
    }
}
