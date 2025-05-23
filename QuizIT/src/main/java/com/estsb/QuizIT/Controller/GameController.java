package com.estsb.QuizIT.Controller;

import com.estsb.QuizIT.Entity.Game;
import com.estsb.QuizIT.Entity.User;
import com.estsb.QuizIT.Repository.UserRepository;
import com.estsb.QuizIT.Service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/game")
public class GameController {

    private final GameService gameService;
    private final UserRepository userRepository;


    @PostMapping("/create")
    public ResponseEntity<Game> createGame(
            @RequestParam String questionType,
            @RequestParam int numberOfQuestions,
            @RequestParam Long flashcardSetId,
            Principal principal) {

        String teacherUsername = principal.getName();
        User teacher = userRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        Game newGame = gameService.createGame(questionType, numberOfQuestions, flashcardSetId, teacher.getId());

        return ResponseEntity.ok(newGame);
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinGame(@RequestParam String gameCode, @RequestParam Long playerId) {
        boolean success = gameService.joinGame(gameCode, playerId);
        if (success) {
            return ResponseEntity.ok("Joined the game successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to join: invalid code or already joined.");
        }
    }


    // Start the game and generate the list of questions
    @PostMapping("/start/{gameId}")
    public ResponseEntity<String> startGame(@PathVariable Long gameId) {
        boolean started = gameService.startGame(gameId);
        if (started) {
            return ResponseEntity.ok("Game started!");
        } else {
            return ResponseEntity.badRequest().body("Cannot start game: not enough players.");
        }
    }


    // Check the player's answer
    @PostMapping("/check/{gameId}")
    public ResponseEntity<String> checkAnswer(
            @PathVariable Long gameId,
            @RequestParam Long flashcardId,
            @RequestParam String answer,
            @RequestParam Long playerId) {

        // Check the player's answer using GameService
        String result = gameService.checkAnswer(gameId, flashcardId, answer, playerId);

        return ResponseEntity.ok(result); // Return the result of checking the answer
    }

    // Mark the player's finish time
    @PostMapping("/finish/{gameId}/{playerId}")
    public ResponseEntity<String> finishGame(
            @PathVariable Long gameId,
            @PathVariable Long playerId) {

        // Mark the player as finished
        String result = gameService.finishGame(gameId, playerId);

        return ResponseEntity.ok(result); // Return the result of marking the player's finish
    }

    // End the game, declare the winner, and return the leaderboard
    @PostMapping("/end/{gameId}")
    public ResponseEntity<String> endGame(@PathVariable Long gameId) {
        // End the game and get the leaderboard
        String leaderboard = gameService.endGame(gameId);

        return ResponseEntity.ok(leaderboard); // Return the leaderboard and declare the winner
    }

    // Fetch active games
    @GetMapping("/active")
    public ResponseEntity<List<Game>> getActiveGames() {
        List<Game> activeGames = gameService.getActiveGames();
        return ResponseEntity.ok(activeGames); // Return the list of active games
    }

    // Fetch a game by its game code
    @GetMapping("/byCode/{gameCode}")
    public ResponseEntity<Game> getGameByCode(@PathVariable String gameCode) {
        Game game = gameService.getGameByCode(gameCode);
        return ResponseEntity.ok(game); // Return the game found by its code
    }

    // Fetch the list of players for a specific game
    @GetMapping("/players/{gameId}")
    public ResponseEntity<List<User>> getPlayersForGame(@PathVariable Long gameId) {
        List<User> players = gameService.getPlayersForGame(gameId);
        return ResponseEntity.ok(players); // Return the list of players for the game
    }
}
