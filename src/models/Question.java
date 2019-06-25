package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Question {
    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty id;

    public String getQuestion() {
        return question.get();
    }

    public SimpleStringProperty questionProperty() {
        return question;
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public String getOption1() {
        return option1.get();
    }

    public SimpleStringProperty option1Property() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1.set(option1);
    }

    public String getOption2() {
        return option2.get();
    }

    public SimpleStringProperty option2Property() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2.set(option2);
    }

    public String getOption3() {
        return option3.get();
    }

    public SimpleStringProperty option3Property() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3.set(option3);
    }

    public String getOption4() {
        return option4.get();
    }

    public SimpleStringProperty option4Property() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4.set(option4);
    }

    public String getAnswer() {
        return answer.get();
    }

    public SimpleStringProperty answerProperty() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public String getTopic() {
        return topic.get();
    }

    public SimpleStringProperty topicProperty() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic.set(topic);
    }

    public int getLevel() {
        return level.get();
    }

    public SimpleIntegerProperty levelProperty() {
        return level;
    }

    public void setLevel(int level) {
        this.level.set(level);
    }

    public SimpleStringProperty question;
    public SimpleStringProperty option1;
    public SimpleStringProperty option2;
    public SimpleStringProperty option3;
    public SimpleStringProperty option4;
    public SimpleStringProperty answer;
    public SimpleStringProperty topic;
    public SimpleIntegerProperty level;

    public Question(int id, String question, String op1, String op2, String op3, String op4,String answer, String topic, int level) {
        this.id =new SimpleIntegerProperty(id);
        this.question =new SimpleStringProperty(question);
        this.option1 =new SimpleStringProperty(op1);
        this.option2 = new SimpleStringProperty(op2);
        this.option3 = new SimpleStringProperty(op3);
        this.option4 = new SimpleStringProperty(op4);
        this.answer = new SimpleStringProperty(answer);
        this.topic = new SimpleStringProperty(topic);
        this.level = new SimpleIntegerProperty(level);
    }

    public void print() {
        System.out.println(id+" "+question+" "+option1+" "+option2+" "+option3+" "+option4+" "+answer+" "+topic+" "+String.valueOf(level));
    }
}
