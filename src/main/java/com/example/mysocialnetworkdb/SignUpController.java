package com.example.mysocialnetworkdb;

import com.example.mysocialnetworkdb.domain.validators.FriendshipValidator;
import com.example.mysocialnetworkdb.domain.validators.UserValidator;
import com.example.mysocialnetworkdb.repository.FriendshipDBRepo;
import com.example.mysocialnetworkdb.repository.MessageDBRepo;
import com.example.mysocialnetworkdb.repository.RequestDBRepo;
import com.example.mysocialnetworkdb.repository.UserDBRepo;
import com.example.mysocialnetworkdb.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    UserValidator userValidator = new UserValidator();
    FriendshipValidator friendshipValidator = new FriendshipValidator();

    UserDBRepo userDBRepo = new UserDBRepo(userValidator);
    FriendshipDBRepo friendshipDBRepo = new FriendshipDBRepo(friendshipValidator);
    RequestDBRepo requestDBRepo = new RequestDBRepo();
    MessageDBRepo messageDBRepo= new MessageDBRepo();
    Service service = Service.getInstance(userDBRepo,friendshipDBRepo,requestDBRepo,messageDBRepo);
    @FXML
    TextField idTextField;
    @FXML
    TextField usernameTextField;
    @FXML
    TextField emailTextField;
    @FXML
    PasswordField passwordPasswordField;

    public void signUp(ActionEvent event) throws IOException {
        int id = Integer.parseInt(idTextField.getText());
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordPasswordField.getText();

        if(id == 0 || username.isEmpty() || email.isEmpty() || password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid data!");
            alert.setContentText("Fields cannot be empty!");
            alert.show();
        }
        else{
            try{
                service.addUser(id, username, password, email);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Your account has been successfully created!");
                alert.setContentText("Press Log In");
                alert.show();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Invalid data!");
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
    }

    public void login(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("NetworkFX");
        stage.setScene(scene);
        stage.show();
    }
}
