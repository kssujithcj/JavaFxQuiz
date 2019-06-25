package quiz.scene;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Answer;
import models.Question;
import models.User;
import quiz.database.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDashboard implements Initializable {

    User user;

    @FXML
    private Label headerLabel, metaLevelText, resultText;

    @FXML
    private ComboBox<String> topicCombobox;

    @FXML
    private TilePane radioTilePane;

    public List<Integer> levels;

    public String selectedTopic = null;

    public ToggleGroup levelToggleGroup;

    public Button quizButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Remove this line
        user = new User();
        user.userName = "a";

        quizButton.setVisible(false);

        levelToggleGroup = new ToggleGroup();
        levelToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton rb = (RadioButton) levelToggleGroup.getSelectedToggle();
                if(rb != null) {
                    String s = rb.getText();
                    getResult(s);
                }
            }
        });
        getTopics();
        topicCombobox.setOnAction(event -> {
            selectedTopic = topicCombobox.getValue();
            levels = getLevels(topicCombobox.getValue());
            setRadioButtons();
            metaLevelText.setText("Select a level to check result or take a quiz");
            resultText.setText("");
            quizButton.setVisible(false);
        });
    }

    public void getTopics() {
        try {
            Set<String> topics = new HashSet<>();
            String sql = "select topic from questions";
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.connect();
            ResultSet resultSet = databaseHandler.execute(sql);
            while (resultSet.next()) {
                topics.add(resultSet.getString("topic"));
            }
            resultSet.close();
            databaseHandler.disconnect();
            topicCombobox.setItems(FXCollections.observableList(new ArrayList<>(topics)));
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setRadioButtons() {
        if(radioTilePane.getChildren().size() > 0) {
            radioTilePane.getChildren().clear();
        }
        if(levels != null && levels.size() > 0) {
            levels.forEach((level -> {
                radioTilePane.getChildren().add(getRadioButton(level));
            }));
        }
    }

    public List<Integer> getLevels(String topic) {
        Set<Integer> levelset = new HashSet<>();
        try {
            String sql = String.format("select * from questions where topic = '%s'", topic);
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.connect();
            ResultSet resultSet = databaseHandler.execute(sql);
            while (resultSet.next()) {
                levelset.add(resultSet.getInt("level"));
            }
            resultSet.close();
            databaseHandler.disconnect();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(levelset);
    }

    public void setUser(User usr) {
        user = usr;
        headerLabel.setText("Welcome "+user.userName);
        resultText.setText("");
    }

    public void setData(User usr, String topic, int level) {
        user = usr;
        selectedTopic = topic;
        headerLabel.setText("Welcome "+user.userName);
        topicCombobox.getSelectionModel().select(selectedTopic);
        levels = getLevels(topicCombobox.getValue());
        setRadioButtons();
        resultText.setText("");
        quizButton.setVisible(false);
    }

    @FXML
    public void logout() {
        ((Stage)headerLabel.getScene().getWindow()).close();
        loadWindow("/quiz/scene/login.fxml", "Quiz");
    }

    public void loadWindow(String path, String title) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource(path));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RadioButton getRadioButton(int level) {
        RadioButton radioButton = new RadioButton(String.valueOf(level));
        radioButton.setPadding(new Insets(10.0f));
        radioButton.setToggleGroup(levelToggleGroup);
        return radioButton;
    }

    public void getResult(String level) {
        List<Question> questions = getQuestions(selectedTopic, Integer.valueOf(level));
        List<Answer> answers = getAnswers(user.userName);
        evaluate(questions, answers);
    }

    public void evaluate(List<Question> questions, List<Answer> answers) {
        int score = 0;
        boolean hasAnswered = false;
        for(int i = 0; i < questions.size(); i++) {
            for(int j = 0; j < answers.size(); j++) {
                if (questions.get(i).id.getValue().equals(answers.get(j).qno)) {
                    hasAnswered = true;
                    if(questions.get(i).answer.getValue().toLowerCase().equals(answers.get(j).response.toLowerCase())) {
                        score = score + 1;
                        break;
                    }
                }
            }
        }
        if(score == 0 && !hasAnswered) {
            resultText.setText("It seems like you haven't taken up the quiz");
            quizButton.setVisible(true);
        } else {
            float total = questions.size();
            float res = (score / total) * 100;
            resultText.setText("Your score is " + String.valueOf(res) + "%");
            quizButton.setVisible(false);
        }
    }

    public List<Answer> getAnswers(String user) {
        List<Answer> answers = new ArrayList<>();
        try{
            String sql = String.format("select * from answers where username = '%s'", user);
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.connect();
            ResultSet resultSet = databaseHandler.execute(sql);
            while(resultSet.next()){
                Answer answer = new Answer();
                answer.qno = resultSet.getInt("qno");
                answer.response = resultSet.getString("response");
                answer.userName = resultSet.getString("username");
                answers.add(answer);
            }
            resultSet.close();
            databaseHandler.disconnect();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

    public List<Question> getQuestions(String topic, int level) {
        List<Question> questions = new ArrayList<>();
        try{
            String sql = String.format("select * from questions where topic = '%s' and level = %d", topic, level);
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.connect();
            ResultSet resultSet = databaseHandler.execute(sql);
            while(resultSet.next()){
                int id = resultSet.getInt("qid");
                String questionString = resultSet.getString("question");
                String option1 = resultSet.getString("option1");
                String option2 = resultSet.getString("option2");
                String option3 = resultSet.getString("option3");
                String option4 = resultSet.getString("option4");
                String answer = resultSet.getString("answer");
                String topicString = resultSet.getString("topic");
                int levelInt = resultSet.getInt("level");
                questions.add(new Question(id, questionString, option1, option2, option3, option4, answer, topicString, levelInt));
            }
            resultSet.close();
            databaseHandler.disconnect();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    @FXML
    public void viewQuiz(ActionEvent event) {
        RadioButton rb = (RadioButton) levelToggleGroup.getSelectedToggle();
        int level = Integer.valueOf(rb.getText());
        List<Question> questions =  getQuestions(selectedTopic, level);
        try{
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                    "/quiz/scene/qna.fxml"
                )
            );
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Quiz");
            stage.setScene(new Scene(loader.load(), 800,600));
            Qna controller = loader.getController();
            controller.setInputData(user, selectedTopic, level, questions);
            ((Stage)headerLabel.getScene().getWindow()).close();
            stage.show();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
