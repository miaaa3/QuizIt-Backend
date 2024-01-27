package com.estsb.QuizIT.Controller;

import com.estsb.QuizIT.Entity.FlashcardSet;
import com.estsb.QuizIT.Entity.User;
import com.estsb.QuizIT.Repository.UserRepository;
import com.estsb.QuizIT.Service.FlashcardSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/flashcard-sets")
public class FlashcardSetController {

    private final FlashcardSetService flashcardSetService;
    private final UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<FlashcardSet>> getAllFlashcardSets() {
        List<FlashcardSet> flashcardSets = flashcardSetService.getAllFlashcardSets();
        return new ResponseEntity<>(flashcardSets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashcardSet> getFlashcardSetById(@PathVariable Long id) {
        Optional<FlashcardSet> flashcardSet = flashcardSetService.getFlashcardSetById(id);
        return flashcardSet.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<FlashcardSet> addFlashcardSet(@RequestBody FlashcardSet flashcardSet, Principal principal) {
        if (principal != null) {
            // Get the username (email) from the principal
            String username = principal.getName();

            // Retrieve the User object from the UserRepository
            Optional<User> authenticatedUser = userRepository.findByEmail(username);

            // Check if the user is present before setting createdBy
            authenticatedUser.ifPresent(flashcardSet::setCreatedBy);
        }

        // Create the flashcardSet
        FlashcardSet createdFlashcardSet = flashcardSetService.createFlashcardSet(flashcardSet);
        return new ResponseEntity<>(createdFlashcardSet, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<FlashcardSet> updateFlashcardSet(@PathVariable Long id, @RequestBody FlashcardSet updatedFlashcardSet) {
        FlashcardSet flashcardSet = flashcardSetService.updateFlashcardSet(id, updatedFlashcardSet);
        return (flashcardSet != null) ?
                new ResponseEntity<>(flashcardSet, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteFlashcardSet(@PathVariable Long id) {
        boolean deleted = flashcardSetService.deleteFlashcardSet(id);
        return (deleted) ?
                new ResponseEntity<>("FlashcardSet deleted successfully", HttpStatus.OK) :
                new ResponseEntity<>("FlashcardSet not found", HttpStatus.NOT_FOUND);
    }

}
