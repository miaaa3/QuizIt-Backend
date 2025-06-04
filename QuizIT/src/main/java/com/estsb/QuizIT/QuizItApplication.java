package com.estsb.QuizIT;

import com.estsb.QuizIT.Dto.RegisterRequest;
import com.estsb.QuizIT.Entity.FlashcardSet;
import com.estsb.QuizIT.Entity.Question;
import com.estsb.QuizIT.Entity.Quiz;
import com.estsb.QuizIT.Repository.*;
import com.estsb.QuizIT.Service.Authentication.AuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@SpringBootApplication
public class QuizItApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizItApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(
			UserRepository userRepository,
			FlashcardSetRepository flashcardSetRepository,
			QuizRepository quizRepository,
			QuestionRepository questionRepository,
			AuthenticationService authenticationService
	) {
		return args -> {
			// Create admin user if not exists
			if (userRepository.findByEmail("admin@quizit.com").isEmpty()) {
				RegisterRequest registerRequest = new RegisterRequest();
				registerRequest.setEmail("admin@quizit.com");
				registerRequest.setUsername("admin");
				registerRequest.setPassword("password123");
				authenticationService.register(registerRequest);
				System.out.println("✅ Default admin user created: admin@quizit.com");
			} else {
				System.out.println("ℹ️ Admin user already exists.");
			}

			if (flashcardSetRepository.count() == 0) {
				// You can add flashcard set seeding here if needed
			}

			if (quizRepository.count() == 0) {
				var adminUser = userRepository.findByEmail("admin@quizit.com").get();

				Quiz basicsQuiz = new Quiz();
				basicsQuiz.setName("English Basics Quiz");
				basicsQuiz.setDescription("Test basic English vocabulary knowledge");
				basicsQuiz.setIsPublic(true);
				basicsQuiz.setCreatedAt(LocalDateTime.now());
				basicsQuiz.setCreatedBy(adminUser);
				quizRepository.save(basicsQuiz);

				Quiz verbQuiz = new Quiz();
				verbQuiz.setName("English Verb Quiz");
				verbQuiz.setDescription("Quiz on common English verbs");
				verbQuiz.setIsPublic(true);
				verbQuiz.setCreatedAt(LocalDateTime.now());
				verbQuiz.setCreatedBy(adminUser);
				quizRepository.save(verbQuiz);

				Quiz adjQuiz = new Quiz();
				adjQuiz.setName("English Adjective Quiz");
				adjQuiz.setDescription("Quiz on descriptive adjectives");
				adjQuiz.setIsPublic(true);
				adjQuiz.setCreatedAt(LocalDateTime.now());
				adjQuiz.setCreatedBy(adminUser);
				quizRepository.save(adjQuiz);

				Map<String, String> appleAnswers = Map.of(
						"A", "A common fruit, typically red or green",
						"B", "A vehicle",
						"C", "A type of furniture",
						"D", "An animal"
				);

				Map<String, String> bookAnswers = Map.of(
						"A", "A set of printed pages bound together",
						"B", "A device to call people",
						"C", "A type of fruit",
						"D", "A musical instrument"
				);

				// Correct constructor parameters order:
				// (Long questionId, String questionText, String questionDescription, Map<String,String> answers, String correctAnswer, String explanation, String tip, Quiz quiz, String userAnswer)
				questionRepository.saveAll(Arrays.asList(
						new Question(null, "What is the meaning of \"Apple\"?", null, appleAnswers, "A", null, null, basicsQuiz, null),
						new Question(null, "What is a \"Book\"?", null, bookAnswers, "A", null, null, basicsQuiz, null),
						new Question(null, "What is a \"Chair\"?", null,
								Map.of(
										"A", "A piece of furniture for sitting",
										"B", "A type of fruit",
										"C", "A book",
										"D", "A vehicle"
								), "A", null, null, basicsQuiz, null)
				));

				questionRepository.saveAll(Arrays.asList(
						new Question(null, "What does \"Run\" mean?", null,
								Map.of(
										"A", "To move quickly on foot",
										"B", "To sleep",
										"C", "To eat",
										"D", "To write"
								), "A", null, null, verbQuiz, null),
						new Question(null, "What does \"Eat\" mean?", null,
								Map.of(
										"A", "To consume food",
										"B", "To run fast",
										"C", "To read",
										"D", "To sleep"
								), "A", null, null, verbQuiz, null),
						new Question(null, "What does \"Speak\" mean?", null,
								Map.of(
										"A", "To talk or communicate verbally",
										"B", "To walk",
										"C", "To cook",
										"D", "To write"
								), "A", null, null, verbQuiz, null)
				));

				questionRepository.saveAll(Arrays.asList(
						new Question(null, "What does \"Happy\" mean?", null,
								Map.of(
										"A", "Feeling or showing pleasure",
										"B", "Feeling sad",
										"C", "Feeling tired",
										"D", "Feeling angry"
								), "A", null, null, adjQuiz, null),
						new Question(null, "What does \"Large\" mean?", null,
								Map.of(
										"A", "Of great size or extent",
										"B", "Very small",
										"C", "Very fast",
										"D", "Very bright"
								), "A", null, null, adjQuiz, null),
						new Question(null, "What does \"Bright\" mean?", null,
								Map.of(
										"A", "Giving off a lot of light or vivid",
										"B", "Very dark",
										"C", "Very loud",
										"D", "Very slow"
								), "A", null, null, adjQuiz, null)
				));

				System.out.println("✅ Sample quizzes and questions seeded.");
			}
		};
	}
}
