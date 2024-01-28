package com.estsb.QuizIT.Controller;

import com.estsb.QuizIT.Entity.CheckAnswerResponse;
import com.estsb.QuizIT.Entity.Question;
import com.estsb.QuizIT.Entity.Requests.UserAnswerRequest;
import com.estsb.QuizIT.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/questions")
public class QuestionController {
    private final QuestionService questionService;

    // Create a new question
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        Question createdQuestion = questionService.createQuestion(question);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    // Check the correctness of the user's answer
    @PostMapping("/check-answer")
    public ResponseEntity<CheckAnswerResponse> checkAnswer(@RequestBody UserAnswerRequest userAnswerRequest) {
        try {
            Optional<Question> optionalQuestion = questionService.getQuestionById(userAnswerRequest.getQuestionId());

            if (optionalQuestion.isPresent()) {
                Question question = optionalQuestion.get();
                boolean isCorrect = questionService.checkUserAnswer(question, userAnswerRequest.getUserAnswer());

                // Decide whether to pass the entire Map or a specific value
                Object correctAnswers = question.getCorrectAnswers(); // or question.getCorrectAnswers().get("yourSpecificKey");

                return ResponseEntity.ok(new CheckAnswerResponse(isCorrect, correctAnswers, "Answer checked successfully."));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CheckAnswerResponse(false, null, "Error checking answer."));
        }
    }


    // Get all questions
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    // Get a question by ID
    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long questionId) {
        Optional<Question> question = questionService.getQuestionById(questionId);
        return question.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a question by ID
    @PutMapping("/{questionId}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long questionId, @RequestBody Question updatedQuestion) {
        Question question = questionService.updateQuestion(questionId, updatedQuestion);
        return (question != null) ?
                new ResponseEntity<>(question, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete a question by ID
    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId) {
        boolean deleted = questionService.deleteQuestion(questionId);
        return (deleted) ?
                new ResponseEntity<>("Question deleted successfully", HttpStatus.OK) :
                new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
    }
}
