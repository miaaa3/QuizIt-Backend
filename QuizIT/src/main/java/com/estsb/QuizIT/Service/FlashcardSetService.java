package com.estsb.QuizIT.Service;

import com.estsb.QuizIT.Entity.FlashcardSet;
import com.estsb.QuizIT.Repository.FlashcardSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FlashcardSetService {
    private final FlashcardSetRepository flashcardSetRepository;
    private final FlashcardService flashcardService;

    public List<FlashcardSet> getAllFlashcardSets() {
        return flashcardSetRepository.findAll();
    }

    public Optional<FlashcardSet> getFlashcardSetById(Long id) {
        return flashcardSetRepository.findById(id);
    }

    public FlashcardSet createFlashcardSet(FlashcardSet flashcardSet) {
        // Set the FlashcardSet for each Flashcard in the set
        if (flashcardSet.getFlashcards() != null) {
            flashcardSet.getFlashcards().forEach(flashcard -> flashcard.setFlashcardSet(flashcardSet));
        }
        return flashcardSetRepository.save(flashcardSet);
    }

    public FlashcardSet updateFlashcardSet(Long id, FlashcardSet updatedFlashcardSet) {
        if (flashcardSetRepository.existsById(id)) {
            updatedFlashcardSet.setId(id);
            // Set the FlashcardSet for each Flashcard in the set
            if (updatedFlashcardSet.getFlashcards() != null) {
                updatedFlashcardSet.getFlashcards().forEach(flashcard -> flashcard.setFlashcardSet(updatedFlashcardSet));
            }
            return flashcardSetRepository.save(updatedFlashcardSet);
        }
        return null; // FlashcardSet not found
    }

    public boolean deleteFlashcardSet(Long id) {
        if (flashcardSetRepository.existsById(id)) {
            flashcardSetRepository.deleteById(id);
            return true;
        }
        return false; // FlashcardSet not found
    }
}
