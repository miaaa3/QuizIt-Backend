package com.estsb.QuizIT.Service;

import com.estsb.QuizIT.Entity.Question;
import com.estsb.QuizIT.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    // Create
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    // Read
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(Long questionId) {
        return questionRepository.findById(questionId);
    }

    // Update
    public Question updateQuestion(Long questionId, Question updatedQuestion) {
        if (questionRepository.existsById(questionId)) {
            updatedQuestion.setQuestionId(questionId);
            return questionRepository.save(updatedQuestion);
        } else {
            return null;
        }
    }

    public boolean checkUserAnswer(Question question, String userAnswer) {
        // Get the correct answers from the question entity
        Map<String, Boolean> correctAnswers = question.getCorrectAnswers();

        // Check if the user's answer is correct
        return correctAnswers.containsKey(userAnswer) && correctAnswers.get(userAnswer);
    }

    // Delete
    public boolean deleteQuestion(Long questionId) {
        if (questionRepository.existsById(questionId)) {
            questionRepository.deleteById(questionId);
            return true;
        } else {
            return false;
        }
    }
}
