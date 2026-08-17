public class Question {

    private final String questionText;
    private final String[] options;
    private final int correctOption;

    public Question(
            String questionText,
            String[] options,
            int correctOption) {

        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean isCorrect(int answer) {
        return answer == correctOption;
    }
}