package quiz.scene;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Question;
import models.User;
import quiz.database.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Qna {

    @FXML
    Label topicLabel, levelLabel;

    @FXML
    AnchorPane parent;

    Map<Integer, ToggleGroup> answers = new HashMap<>();

    int level;

    String topic;

    User user;

    List<Question> questions;

    public void setInputData(User user, String selectedTopic, int level, List<Question> questionList) {
        this.user = user;
        topic = selectedTopic;
        this.level = level;
        questions = questionList;
        initViews();
    }

    public void initViews() {
        topicLabel.setText(topic);
        levelLabel.setText(String.valueOf(level));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color:white;");
        scrollPane.setLayoutY(62);
        scrollPane.setMinWidth(800);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefViewportHeight(460);

        VBox vBox = new VBox();
        vBox.setMinWidth(800);
        vBox.setPadding(new Insets(16, 16, 16, 16));
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        if(questions != null) {
            for(int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                addQuestionLayout(vBox, q.getId(), q.getQuestion(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            }
        }

        scrollPane.setContent(vBox);

        parent.getChildren().add(scrollPane);
    }

    @FXML
    public void submit(ActionEvent actionEvent) {
        for (Integer key : answers.keySet()) {
            RadioButton rb = (RadioButton) answers.get(key).getSelectedToggle();
            if(rb != null) {
                System.out.println(key + " " + rb.getText());
                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.connect();
                String sql=String.format("insert into answers values('%s', '%d', '%s')", user.userName, key, rb.getText());
                databaseHandler.executeUpdate(sql);
            }
        }
        back();
    }

    @FXML
    public void backClicked(ActionEvent e){
        back();
    }

    public void back(){
        ((Stage) parent.getScene().getWindow()).close();
        loadUserDashboard();
    }


    public RadioButton getOptionRow(String option, ToggleGroup toggleGroup) {
        RadioButton radioButton1 = new RadioButton();
        radioButton1.setText(option);
        radioButton1.setToggleGroup(toggleGroup);
        return radioButton1;
    }

    public HBox getHBoxWithHeight(int height) {
        HBox hBox2 = new HBox();
        hBox2.setMinHeight(height);
        return hBox2;
    }

    public void addQuestionLayout(VBox vBox, int qno, String question, String op1, String op2, String op3, String op4) {

        ToggleGroup toggleGroup = new ToggleGroup();

        Text qtext = new Text(question);

        vBox.getChildren().add(qtext);

        vBox.getChildren().add(getHBoxWithHeight(30));

        vBox.getChildren().add(getOptionRow(op1, toggleGroup));

        vBox.getChildren().add(getHBoxWithHeight(15));

        vBox.getChildren().add(getOptionRow(op2, toggleGroup));

        vBox.getChildren().add(getHBoxWithHeight(15));

        vBox.getChildren().add(getOptionRow(op3, toggleGroup));

        vBox.getChildren().add(getHBoxWithHeight(15));

        vBox.getChildren().add(getOptionRow(op4, toggleGroup));

        vBox.getChildren().add(getHBoxWithHeight(30));

        answers.put(qno, toggleGroup);
    }

    public void loadUserDashboard() {
        try{
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                    "/quiz/scene/userDashboard.fxml"
                )
            );
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Quiz");
            stage.setScene(new Scene(loader.load(), 800,600));
            UserDashboard controller = loader.getController();
            controller.setData(user, topic, level);
            stage.show();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
