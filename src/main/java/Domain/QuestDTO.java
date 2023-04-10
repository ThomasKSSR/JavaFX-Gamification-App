package Domain;

public class QuestDTO {
    private int id;
    private String name;
    private int points;
    private String question;
    private String answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public QuestDTO(int id, String name, int points, String question, String answer) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.question = question;
        this.answer = answer;
    }
}
