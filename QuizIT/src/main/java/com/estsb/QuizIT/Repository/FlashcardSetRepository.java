package com.estsb.QuizIT.Repository;

import com.estsb.QuizIT.Entity.FlashcardSet;
import com.estsb.QuizIT.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlashcardSetRepository extends JpaRepository<FlashcardSet,Long> {
    Optional<FlashcardSet> findById(Long id);
    List<FlashcardSet> findByNameContainingIgnoreCase(String name);
    List<FlashcardSet> findByDescriptionContainingIgnoreCase(String description);
    List<FlashcardSet> findByIsPublic(Boolean isPublic);
    List<FlashcardSet> findByCreatedBy(User user);
}
