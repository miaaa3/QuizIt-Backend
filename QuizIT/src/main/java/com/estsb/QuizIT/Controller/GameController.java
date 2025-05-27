package com.estsb.QuizIT.Controller;

import com.estsb.QuizIT.Entity.Game;
import com.estsb.QuizIT.Entity.Player;
import com.estsb.QuizIT.Entity.User;
import com.estsb.QuizIT.Repository.UserRepository;
import com.estsb.QuizIT.Service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        User teacher = userRepository.findByEmail(teacherUsername)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        Game newGame = gameService.createGame(questionType, numberOfQuestions, flashcardSetId, teacher.getId());

        return ResponseEntity.ok(newGame);
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinGame(@RequestParam String gameCode, @RequestParam String username) {
        boolean success = gameService.joinGame(gameCode, username);
        if (success) {
            return ResponseEntity.ok("Joined the game successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to join: invalid code or already joined.");
        }
    }

    @PostMapping("/start/{gameId}")
    public ResponseEntity<String> startGame(@PathVariable Long gameId) {
        boolean started = gameService.startGame(gameId);
        if (started) {
            return ResponseEntity.ok("Game started!");
        } else {
            return ResponseEntity.badRequest().body("Cannot start game: not enough players.");
        }
    }

    @PostMapping("/check/{gameId}")
    public ResponseEntity<String> checkAnswer(
            @PathVariable Long gameId,
            @RequestParam Long flashcardId,
            @RequestParam String answer,
            @RequestParam Long playerId) {

        String result = gameService.checkAnswer(gameId, flashcardId, answer, playerId);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/finish/{gameId}/{playerId}")
    public ResponseEntity<String> finishGame(
            @PathVariable Long gameId,
            @PathVariable Long playerId) {

        String result = gameService.finishGame(gameId, playerId);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/end/{gameId}")
    public ResponseEntity<String> endGame(@PathVariable Long gameId) {
        String leaderboard = gameService.endGame(gameId);

        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Game>> getActiveGames() {
        List<Game> activeGames = gameService.getActiveGames();
        return ResponseEntity.ok(activeGames);
    }

    @GetMapping("/byCode/{gameCode}")
    public ResponseEntity<Game> getGameByCode(@PathVariable String gameCode) {
        Game game = gameService.getGameByCode(gameCode);
        return ResponseEntity.ok(game);
    }

    // Note: Players are now Player entities, not User entities.
    @GetMapping("/players/{gameId}")
    public ResponseEntity<List<Player>> getPlayersForGame(@PathVariable Long gameId) {
        List<Player> players = gameService.getPlayersForGame(gameId);
        return ResponseEntity.ok(players);
    }
}
