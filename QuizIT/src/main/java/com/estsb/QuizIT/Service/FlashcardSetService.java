package com.estsb.QuizIT.Service;

import com.estsb.QuizIT.Entity.FlashcardSet;
import com.estsb.QuizIT.Entity.Quiz;
import com.estsb.QuizIT.Entity.User;
import com.estsb.QuizIT.Repository.FlashcardSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FlashcardSetService {

    private final FlashcardSetRepository flashcardSetRepository;
    private final FlashcardService flashcardService;

    // Get all FlashcardSets
    public List<FlashcardSet> getAllFlashcardSets() {
        return flashcardSetRepository.findAll();
    }

    // Get FlashcardSet by ID
    public Optional<FlashcardSet> getFlashcardSetById(Long id) {
        return flashcardSetRepository.findById(id);
    }

    // Create a new FlashcardSet
    public FlashcardSet createFlashcardSet(FlashcardSet flashcardSet) {
        if (flashcardSet.getFlashcards() != null) {
            flashcardSet.getFlashcards()
                    .forEach(flashcard -> flashcard.setFlashcardSet(flashcardSet));
        }
        return flashcardSetRepository.save(flashcardSet);
    }

    // Update existing FlashcardSet
    public FlashcardSet updateFlashcardSet(Long id, FlashcardSet updatedFlashcardSet) {
        if (flashcardSetRepository.existsById(id)) {
            updatedFlashcardSet.setId(id);
            if (updatedFlashcardSet.getFlashcards() != null) {
                updatedFlashcardSet.getFlashcards()
                        .forEach(flashcard -> flashcard.setFlashcardSet(updatedFlashcardSet));
            }
            return flashcardSetRepository.save(updatedFlashcardSet);
        }
        return null; // FlashcardSet not found
    }

    // Delete FlashcardSet by ID
    public boolean deleteFlashcardSet(Long id) {
        if (flashcardSetRepository.existsById(id)) {
            flashcardSetRepository.deleteById(id);
            return true;
        }
        return false; // FlashcardSet not found
    }

    // Set the visibility (public/private) of a FlashcardSet
    public FlashcardSet setFlashcardSetVisibility(Long flashcardSetId, boolean isPublic) {
        return flashcardSetRepository.findById(flashcardSetId).map(quiz -> {
            quiz.setIsPublic(isPublic);
            return flashcardSetRepository.save(quiz);
        }).orElse(null);
    }

    // Search flashcardSets by optional filters: name, visibility, and creator
    public List<FlashcardSet> searchFlashcardSets(
            String name,
            String description,
            Boolean isPublic,
            User createdBy) {
        List<FlashcardSet> allSets = flashcardSetRepository.findAll();

        return allSets.stream()
                .filter(set -> (name == null || set.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(set -> (description == null || set.getDescription().toLowerCase().contains(description.toLowerCase())))
                .filter(set -> (isPublic == null || set.getIsPublic().equals(isPublic)))
                .filter(set -> (createdBy == null || (set.getCreatedBy() != null && set.getCreatedBy().equals(createdBy))))
                .collect(Collectors.toList());
    }
}
