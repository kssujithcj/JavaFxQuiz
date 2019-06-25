package quiz.scene;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.User;
import quiz.database.DatabaseHandler;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField nameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorText;

    @FXML
    private Button primaryAction;

    @FXML
    private Label secondaryAction;

    @FXML
    private Label tertiaryAction;

    @FXML
    private Text header;

    @FXML
    private void loginOrRegister(ActionEvent event) {
        if(primaryAction.getText().contains("Sign In")) {
            if(isValid()) {
                if(header.getText().equals("Admin")) {
                    signInAdmin();
                } else {
                    signInUser();
                }
            }
        } else {
            if(isValid()) {
                signUpUser();
            }
        }
    }

    @FXML
    private void switchAction() {
        if(primaryAction.getText().equals("Sign In")) {
            primaryAction.setText("Sign Up");
        } else {
            primaryAction.setText("Sign In");
        }

        if(secondaryAction.getText().equals("Sign Up")) {
            secondaryAction.setText("Sign In");
        } else {
            secondaryAction.setText("Sign Up");
        }
    }

    @FXML
    private void switchAdminUser() {
        if(header.getText().equals("User")) {
            header.setText("Admin");
            secondaryAction.setVisible(false);
            primaryAction.setText("Sign In");
            tertiaryAction.setText("Switch to User");
        } else {
            header.setText("User");
            secondaryAction.setVisible(true);
            secondaryAction.setText("Sign Up");
            primaryAction.setText("Sign In");
            tertiaryAction.setText("Switch to Admin");
        }
    }

    public boolean isValid() {
        if(nameTextField.getText().isEmpty()) {
            errorText.setText("Name can't be empty");
            return  false;
        }
        if(passwordField.getText().isEmpty()) {
            errorText.setText("Password can't be empty");
            return  false;
        }
        return true;
    }

    public void signUpUser() {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.connect();
        String sql=String.format("insert into users values('%s','%s')", nameTextField.getText(), passwordField.getText());
        if(databaseHandler.executeUpdate(sql)==0)
            errorText.setText("Error registering user, username may exist");
    }

    public void signInUser() {
        if(validateUser(nameTextField.getText(), passwordField.getText())) {
            ((Stage) nameTextField.getScene().getWindow()).close();
            User usr = new User();
            usr.userName = nameTextField.getText();
            loadUserDashboard(usr);
        } else {
            errorText.setText("Invalid credentials");
        }
    }

    public void signInAdmin() {
        if(validateAdmin(nameTextField.getText(), passwordField.getText())){
            ((Stage) nameTextField.getScene().getWindow()).close();
            loadWindow("/quiz/scene/admin.fxml", "Admin");
        } else {
            errorText.setText("Invalid credentials");
        }
    }

    public boolean validateAdmin(String name, String password) {
        try {
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.connect();
            ResultSet resultSet = databaseHandler.execute("select * from admin");
            while (resultSet.next()) {
                if(name.equals(resultSet.getString("username"))) {
                    if(password.equals(resultSet.getString("password"))) {
                        return true;
                    }
                }
            }
            resultSet.close();
            databaseHandler.disconnect();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateUser(String name, String password) {
        try{
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.connect();
            ResultSet resultSet = databaseHandler.execute("select * from users");
            while (resultSet.next()) {
                if(name.equals(resultSet.getString("username"))){
                    if(password.equals(resultSet.getString("password"))) {
                        return true;
                    }
                }
            }
            resultSet.close();
            databaseHandler.disconnect();
            return false;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    public void loadUserDashboard(User user) {
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
            controller.setUser(user);
            stage.show();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
