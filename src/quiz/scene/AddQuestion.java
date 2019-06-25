package quiz.scene;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import quiz.database.DatabaseHandler;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;


public class AddQuestion  implements Initializable{

    @FXML
    public Label errorText, successText;

    @FXML
    public TextArea questionTextField;

    @FXML
    public TextField option1TextField, option2TextField, option3TextField, option4TextField;

    // level textfield is numeric text input
    @FXML
    public TextField answerTextField, topicTextField, levelTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //restrict textfield to only allow numbers since database accepts only number for level
        levelTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    levelTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    public void addQuestion(ActionEvent ae) {
        errorText.setText("");
        if(isValid()) {
            String question = questionTextField.getText();
            String option1 = option1TextField.getText();
            String option2 = option2TextField.getText();
            String option3 = option3TextField.getText();
            String option4 = option4TextField.getText();
            String answer = answerTextField.getText();
            String topic = topicTextField.getText();
            int level = Integer.valueOf(levelTextField.getText());
            String sql = String.format("insert into questions(question, option1, option2, option3, option4, answer, topic, level) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%d')",
                question, option1, option2, option3, option4, answer, topic, level);
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.connect();
            int res = databaseHandler.executeUpdate(sql);
            if (res == 0) {
                errorText.setText("Couldn't add new question");
            } else {
                clearAllTextFields();
                successText.setText("Done");
                clearSuccessText();
            }
            databaseHandler.disconnect();
        }
    }

    @FXML
    public void exit(ActionEvent ae) {
        ((Stage) questionTextField.getScene().getWindow()).close();
    }

    public boolean isValid() {
        if(questionTextField.getText().isEmpty()) {
            errorText.setText("Invalid Question");
            return false;
        }
        if(option1TextField.getText().isEmpty()) {
            errorText.setText("Invalid Option 1");
            return false;
        }
        if(option2TextField.getText().isEmpty()) {
            errorText.setText("Invalid Option 2");
            return false;
        }
        if(option3TextField.getText().isEmpty()) {
            errorText.setText("Invalid Option3");
            return false;
        }
        if(option4TextField.getText().isEmpty()) {
            errorText.setText("Invalid Option 4");
            return false;
        }
        if(answerTextField.getText().isEmpty()) {
            errorText.setText("Invalid answer");
            return false;
        }
        if(topicTextField.getText().isEmpty()) {
            errorText.setText("Invalid topic");
            return false;
        }
        if(levelTextField.getText().isEmpty()) {
            errorText.setText("Invalid level, level can only take number values");
            return false;
        }
        return true;
    }

    public void clearAllTextFields() {
        questionTextField.setText("");
        option1TextField.setText("");
        option2TextField.setText("");
        option3TextField.setText("");
        option4TextField.setText("");
        answerTextField.setText("");
        topicTextField.setText("");
        levelTextField.setText("");
    }

    public void clearSuccessText() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(() -> {
                        successText.setText("");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
