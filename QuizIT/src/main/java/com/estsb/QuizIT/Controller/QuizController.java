package com.estsb.QuizIT.Controller;

import com.estsb.QuizIT.Entity.Category;
import com.estsb.QuizIT.Entity.Difficulty;
import com.estsb.QuizIT.Entity.Quiz;
import com.estsb.QuizIT.Entity.User;
import com.estsb.QuizIT.Repository.UserRepository;
import com.estsb.QuizIT.Service.Authentication.AuthenticationService;
import com.estsb.QuizIT.Service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final UserRepository userRepository;


    // Add a new quiz
    @PostMapping("/add")
    public ResponseEntity<Quiz> addQuiz(@RequestBody Quiz quiz, Principal principal) {
        if (principal != null) {
            // Get the username (email) from the principal
            String username = principal.getName();

            // Retrieve the User object from the UserRepository
            Optional<User> authenticatedUser = userRepository.findByEmail(username);

            // Check if the user is present before setting createdBy
            authenticatedUser.ifPresent(user -> quiz.setCreatedBy(user));

        }
        // Create the quiz
        Quiz createdQuiz = quizService.createQuiz(quiz);
        return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
    }


    // Get all quizzes
    @GetMapping("/all")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    // Get a quiz by ID
    @GetMapping("getQuizById/{quizId}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long quizId) {
        Optional<Quiz> quiz = quizService.getQuizById(quizId);
        return quiz.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<Quiz>> getQuizzesByCategory(@PathVariable String category) {
        try {
            // Normalize the category by converting to uppercase and removing spaces
            Category enumCategory = normalizeCategory(category);
            List<Quiz> quizzes = quizService.getQuizzesByCategory(enumCategory);
            return !quizzes.isEmpty() ?
                    new ResponseEntity<>(quizzes, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            System.out.println("Unavailable quizzes");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getQuizzesByCategoryAndDifficulty")
    public ResponseEntity<List<Quiz>> getQuizzesByCategoryAndDifficulty(@RequestParam String category,@RequestParam String difficulty) {
        try {
            // Normalize the category by converting to uppercase and removing spaces
            Category enumCategory = normalizeCategory(category);
            Difficulty enumDifficulty = Difficulty.valueOf(difficulty.toUpperCase());

            List<Quiz> quizzes = quizService.getQuizzesByCategoryAndDifficulty(enumCategory, enumDifficulty);

            return !quizzes.isEmpty() ?
                    new ResponseEntity<>(quizzes, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            System.out.println("Unavailable quizzes");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private Category normalizeCategory(String category) {
        // Normalize the category by converting to uppercase and removing spaces
        return Category.valueOf(category.toUpperCase().replaceAll("\\s", ""));
    }

    // Update a quiz by ID
    @PutMapping("/{quizId}/update")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long quizId, @RequestBody Quiz updatedQuiz) {
        Quiz quiz = quizService.updateQuiz(quizId, updatedQuiz);
        return (quiz != null) ?
                new ResponseEntity<>(quiz, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete a quiz by ID
    @DeleteMapping("/{quizId}/delete")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long quizId) {
        boolean deleted = quizService.deleteQuiz(quizId);
        return (deleted) ?
                new ResponseEntity<>("Quiz deleted successfully", HttpStatus.OK) :
                new ResponseEntity<>("Quiz not found", HttpStatus.NOT_FOUND);
    }
}
