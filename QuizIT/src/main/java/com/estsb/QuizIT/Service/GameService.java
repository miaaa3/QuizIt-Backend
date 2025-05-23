package com.estsb.QuizIT.Service;


import com.estsb.QuizIT.Entity.*;
import com.estsb.QuizIT.Repository.FlashcardRepository;
import com.estsb.QuizIT.Repository.FlashcardSetRepository;
import com.estsb.QuizIT.Repository.GameRepository;
import com.estsb.QuizIT.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final FlashcardRepository flashcardRepository;
    private final FlashcardSetRepository flashcardSetRepository;
    private final UserRepository userRepository;

    public Game createGame(String questionType, int numberOfQuestions, Long flashcardSetId, Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        FlashcardSet flashcardSet = flashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard set not found"));

        Game newGame = new Game();
        newGame.setPlayers(new ArrayList<>()); // empty list, no players yet
        newGame.setCreatedBy(teacher);
        newGame.setFlashcardSet(flashcardSet);
        newGame.setQuestionType(QuestionType.valueOf(questionType.toUpperCase()));
        newGame.setNumberOfQuestions(numberOfQuestions);
        newGame.setIsActive(false); // not started yet

        String gameCode = generateGameCode();
        newGame.setGameCode(gameCode);

        gameRepository.save(newGame);
        return newGame;
    }

    public boolean joinGame(String gameCode, Long playerId) {
        Game game = gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        User player = userRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        if (game.getPlayers().stream().anyMatch(u -> u.getId().equals(playerId))) {
            return false; // already joined
        }

        game.getPlayers().add(player);
        gameRepository.save(game);
        return true;
    }



    // Generate a unique game code
    private String generateGameCode() {
        Random random = new Random();
        return "GAME-" + (random.nextInt(999999) + 100000);  // Random 6-digit code
    }

    public boolean startGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        if (game.getPlayers().size() < 1) { // or your minimum number of players
            return false;
        }

        game.setIsActive(true);
        gameRepository.save(game);
        return true;
    }


    // Generate questions based on the game settings
    private List<Flashcard> generateQuestions(Game game) {
        List<Flashcard> questions = new ArrayList<>();

        // Fetch all flashcards from the selected FlashcardSet
        List<Flashcard> allFlashcards = flashcardRepository.findByFlashcardSet(game.getFlashcardSet());

        // Randomly select questions based on the number of questions in the game
        Random random = new Random();
        for (int i = 0; i < game.getNumberOfQuestions(); i++) {
            Flashcard question = allFlashcards.get(random.nextInt(allFlashcards.size()));
            questions.add(question);
        }

        return questions;
    }

    public String checkAnswer(Long gameId, Long flashcardId, String answer, Long playerId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        Flashcard flashcard = flashcardRepository.findById(flashcardId).orElseThrow(() -> new IllegalArgumentException("Flashcard not found"));

        String correctAnswer = flashcard.getDefinition();  // If question type is TERM
        if (game.getQuestionType() == QuestionType.DEFINITION) {
            correctAnswer = flashcard.getTerm();  // If question type is DEFINITION
        }

        // Check if the answer is correct
        if (answer.equalsIgnoreCase(correctAnswer)) {
            // Update the score for this player
            game.getPlayerScores().put(playerId, game.getPlayerScores().getOrDefault(playerId, 0) + 1);
            gameRepository.save(game);
            return "Correct!";
        } else {
            // 4-second pause logic
            try {
                Thread.sleep(4000);  // Pause for 4 seconds if the answer is wrong
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Incorrect. Try again.";
        }
    }

    public String finishGame(Long gameId, Long playerId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));

        // Record the player's finish time when they complete the game
        long finishTime = System.currentTimeMillis();
        game.getPlayerFinishTimes().put(playerId, finishTime);

        // Save the game with updated finish time
        gameRepository.save(game);
        return "Player finished!";
    }



    public String endGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));

        // Mark the game as inactive
        game.setIsActive(false);
        gameRepository.save(game);

        // Sort players by their score first (descending), then by finish time (ascending)
        List<Map.Entry<Long, Integer>> sortedPlayers = game.getPlayerScores().entrySet()
                .stream()
                .sorted((entry1, entry2) -> {
                    // Sort by score in descending order
                    int scoreComparison = entry2.getValue().compareTo(entry1.getValue());
                    if (scoreComparison != 0) {
                        return scoreComparison;
                    }
                    // If scores are equal, sort by finish time in ascending order (earliest first)
                    return game.getPlayerFinishTimes().get(entry1.getKey()).compareTo(game.getPlayerFinishTimes().get(entry2.getKey()));
                })
                .toList();

        // The first player in the sorted list is the winner
        Long winnerId = sortedPlayers.get(0).getKey();
        Integer winnerScore = sortedPlayers.get(0).getValue();

        // Construct the leaderboard
        StringBuilder leaderboard = new StringBuilder("Leaderboard:\n");
        int rank = 1;
        for (Map.Entry<Long, Integer> entry : sortedPlayers) {
            leaderboard.append("Rank ").append(rank++).append(": Player ").append(entry.getKey()).append(" - Score: ").append(entry.getValue()).append("\n");
        }

        // Declare the winner
        leaderboard.append("Winner: Player ").append(winnerId).append(" with ").append(winnerScore).append(" points!");

        return leaderboard.toString();
    }


    // Fetch active games
    public List<Game> getActiveGames() {
        return gameRepository.findByIsActiveTrue();
    }

    // Fetch a game by its code
    public Game getGameByCode(String gameCode) {
        return gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with code " + gameCode));
    }

    // Get players for a specific game
    public List<User> getPlayersForGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        return game.getPlayers();
    }
}