package quiz.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Question;
import quiz.database.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    ObservableList<Question> list = FXCollections.observableArrayList();

    @FXML
    TableView<Question> qTable;

    @FXML
    TableColumn<Question, Integer> idCol;

    @FXML
    TableColumn<Question, String> questionCol;

    @FXML
    TableColumn<Question, String> option1Col;

    @FXML
    TableColumn<Question, String> option2Col;

    @FXML
    TableColumn<Question, String> option3Col;

    @FXML
    TableColumn<Question, String> option4Col;

    @FXML
    TableColumn<Question, String> answerCol;

    @FXML
    TableColumn<Question, String> topicCol;

    @FXML
    TableColumn<Question, Integer> levelCol;

    @FXML
    Label errorText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCols();
        loadData();
    }

    public void initCols() {
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            questionCol.setCellValueFactory(new PropertyValueFactory<>("question"));
            option1Col.setCellValueFactory(new PropertyValueFactory<>("option1"));
            option2Col.setCellValueFactory(new PropertyValueFactory<>("option2"));
            option3Col.setCellValueFactory(new PropertyValueFactory<>("option3"));
            option4Col.setCellValueFactory(new PropertyValueFactory<>("option4"));
            answerCol.setCellValueFactory(new PropertyValueFactory<>("answer"));
            topicCol.setCellValueFactory(new PropertyValueFactory<>("topic"));
            levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
    }

    @FXML
    public void addQuestion(ActionEvent e) {
        loadWindow("/quiz/scene/addQuestion.fxml", "Quiz");
    }

    @FXML
    public void logout() {
        ((Stage) errorText.getScene().getWindow()).close();
        loadWindow("/quiz/scene/login.fxml", "Quiz");
    }

    @FXML
    public void removeQuestion(ActionEvent e) {
        errorText.setText("");

        TableView.TableViewSelectionModel<Question> selectionModel = qTable.getSelectionModel();
        Question question = selectionModel.getSelectedItem();
        if(question != null) {
            String sql = "delete from questions where qid=" + question.id.getValue();
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.connect();
            databaseHandler.executeUpdate(sql);
            databaseHandler.disconnect();
            list.removeAll(list);
            loadData();
        } else {
            errorText.setText("Select an item to be removed");
        }
    }

    @FXML
    public void refreshData(ActionEvent e) {
        errorText.setText("");

        list.removeAll(list);
        loadData();
    }

    @FXML
    public void updateQuestion(ActionEvent e) {
        errorText.setText("");

        TableView.TableViewSelectionModel<Question> selectionModel = qTable.getSelectionModel();
        Question question = selectionModel.getSelectedItem();
        if(question != null) {
            loadUpdateQuestion(question);
        } else {
            errorText.setText("Select an item to be updated");
        }
    }

    public void loadData() {
        try {
            String sql = "SELECT * FROM questions";
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.connect();
            ResultSet set = databaseHandler.execute(sql);
            while (set.next()) {
                int id = set.getInt("qid");
                String question1 = set.getString("question");
                String option1 = set.getString("option1");
                String option2 = set.getString("option2");
                String option3 = set.getString("option3");
                String option4 = set.getString("option4");
                String answer = set.getString("answer");
                String topic = set.getString("topic");
                int level = set.getInt("level");
                Question question = new Question(id, question1, option1, option2, option3, option4, answer, topic, level);
                list.add(question);
            }
            set.close();
            databaseHandler.disconnect();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        qTable.getItems().clear();
        qTable.getItems().setAll(list);
    }

    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent, 800,600));
            /*stage.initOwner(((Stage) qTable.getScene().getWindow()));
            stage.initModality(Modality.WINDOW_MODAL);*/
            stage.show();
        } catch (IOException ex) {
            errorText.setText(ex.getMessage());
            ex.printStackTrace();
        }
    }

    void loadUpdateQuestion(Question question) {
        try{
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                    "/quiz/scene/updateQuestion.fxml"
                )
            );
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Quiz");
            stage.setScene(new Scene(loader.load(), 800,600));
            UpdateQuestion controller = loader.getController();
            controller.setQuestion(question);
            stage.show();
        }catch (IOException ex) {
            errorText.setText(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
