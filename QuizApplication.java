import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class QuizApplication {

    private static final int TIME_LIMIT_SECONDS = 15;

    private final Scanner scanner;
    private final List<Question> questions;

    public QuizApplication() {

        scanner = new Scanner(System.in);
        questions = new ArrayList<>();

        loadQuestions();
    }

    private void loadQuestions() {

        questions.add(new Question(
                "Which keyword is used to create a class in Java?",
                new String[]{
                        "define",
                        "class",
                        "struct",
                        "object"
                },
                2
        ));

        questions.add(new Question(
                "Which method is the entry point of a Java program?",
                new String[]{
                        "start()",
                        "run()",
                        "main()",
                        "execute()"
                },
                3
        ));

        questions.add(new Question(
                "Which data type is used to store decimal values?",
                new String[]{
                        "int",
                        "boolean",
                        "char",
                        "double"
                },
                4
        ));

        questions.add(new Question(
                "Which keyword is used to inherit a class?",
                new String[]{
                        "implements",
                        "extends",
                        "inherits",
                        "super"
                },
                2
        ));

        questions.add(new Question(
                "Which collection does not allow duplicate elements?",
                new String[]{
                        "List",
                        "ArrayList",
                        "Set",
                        "Array"
                },
                3
        ));
    }

    public void startQuiz() {

        int score = 0;

        System.out.println();
        System.out.println("==============================================");
        System.out.println("             JAVA QUIZ APPLICATION");
        System.out.println("==============================================");
        System.out.println(
                "You have " + TIME_LIMIT_SECONDS
                        + " seconds for each question."
        );

        for (int i = 0; i < questions.size(); i++) {

            System.out.println();
            System.out.println(
                    "Question " + (i + 1)
                            + " of " + questions.size()
            );

            boolean correct = askQuestion(
                    questions.get(i)
            );

            if (correct) {
                score++;
            }
        }

        displayResult(score);
        scanner.close();
    }

    private boolean askQuestion(Question question) {

        System.out.println();
        System.out.println(question.getQuestionText());

        String[] options = question.getOptions();

        for (int i = 0; i < options.length; i++) {

            System.out.println(
                    (i + 1) + ". " + options[i]
            );
        }

        System.out.println(
                "You have " + TIME_LIMIT_SECONDS
                        + " seconds to answer."
        );

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        Future<Integer> future = executor.submit(
                this::readAnswer
        );

        try {

            int answer = future.get(
                    TIME_LIMIT_SECONDS,
                    TimeUnit.SECONDS
            );

            if (question.isCorrect(answer)) {

                System.out.println("Correct answer!");
                return true;

            } else {

                System.out.println("Incorrect answer.");
                return false;
            }

        } catch (TimeoutException e) {

            future.cancel(true);

            System.out.println(
                    "\nTime's up! Question skipped."
            );

            return false;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                    "Quiz interrupted."
            );

            return false;

        } catch (ExecutionException e) {

            System.out.println(
                    "Unable to read your answer."
            );

            return false;

        } finally {

            executor.shutdownNow();
        }
    }

    private int readAnswer() {

        while (true) {

            System.out.print("Enter your answer (1-4): ");

            if (scanner.hasNextInt()) {

                int answer = scanner.nextInt();

                if (answer >= 1 && answer <= 4) {
                    return answer;
                }

                System.out.println(
                        "Please choose an option from 1 to 4."
                );

            } else {

                System.out.println(
                        "Invalid input. Enter a number from 1 to 4."
                );

                scanner.next();
            }
        }
    }

    private void displayResult(int score) {

        int totalQuestions = questions.size();

        double percentage =
                (double) score / totalQuestions * 100;

        System.out.println();
        System.out.println("==============================================");
        System.out.println("                 QUIZ RESULT");
        System.out.println("==============================================");

        System.out.println(
                "Correct Answers: "
                        + score + "/" + totalQuestions
        );

        System.out.printf(
                "Percentage     : %.2f%%%n",
                percentage
        );

        if (percentage >= 80) {

            System.out.println("Performance    : Excellent!");

        } else if (percentage >= 60) {

            System.out.println("Performance    : Good!");

        } else if (percentage >= 40) {

            System.out.println("Performance    : Average.");

        } else {

            System.out.println("Performance    : Keep practicing.");
        }

        System.out.println("==============================================");
    }

    public static void main(String[] args) {

        QuizApplication quiz = new QuizApplication();

        quiz.startQuiz();
    }
}