package quiz.scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Question;
import quiz.database.DatabaseHandler;

public class UpdateQuestion {

    @FXML
    public Label errorText, successText;

    @FXML
    public TextArea questionTextField;

    @FXML
    public TextField option1TextField, option2TextField, option3TextField, option4TextField;

    // level textfield is numeric text input
    @FXML
    public TextField answerTextField, topicTextField, levelTextField;

    public Question question;

    public void setQuestion(Question q) {
        question = q;
        if(question != null) {
            fillAllFields();
        }
    }

    public void fillAllFields(){
        questionTextField.setText(question.question.getValue());
        option1TextField.setText(question.option1.getValue());
        option2TextField.setText(question.option2.getValue());
        option3TextField.setText(question.option3.getValue());
        option4TextField.setText(question.option4.getValue());
        answerTextField.setText(question.answer.getValue());
        topicTextField.setText(question.topic.getValue());
        levelTextField.setText(question.level.getValue().toString());
    }

    @FXML
    public void updateQuestion(ActionEvent ae) {
        String sql = String.format("UPDATE questions SET question='%s',  option1='%s', option2='%s', option3='%s', option4='%s', answer='%s', topic='%s', level='%d' WHERE qid='%d';",
            questionTextField.getText(), option1TextField.getText(), option2TextField.getText(), option3TextField.getText(),
            option4TextField.getText(), answerTextField.getText(), topicTextField.getText(), Integer.valueOf(levelTextField.getText()), question.id.getValue());
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.connect();
            int res = databaseHandler.executeUpdate(sql);
            databaseHandler.disconnect();
            if(res == 0) {
                errorText.setText("Error updating the question");
            } else {
                ((Stage) questionTextField.getScene().getWindow()).close();
            }
    }

}
