package com.example.mysocialnetworkdb;

import com.example.mysocialnetworkdb.domain.Friendship;
import com.example.mysocialnetworkdb.domain.Request;
import com.example.mysocialnetworkdb.domain.User;
import com.example.mysocialnetworkdb.domain.validators.FriendshipValidator;
import com.example.mysocialnetworkdb.domain.validators.UserValidator;
import com.example.mysocialnetworkdb.exceptions.RepositoryException;
import com.example.mysocialnetworkdb.repository.FriendshipDBRepo;
import com.example.mysocialnetworkdb.repository.MessageDBRepo;
import com.example.mysocialnetworkdb.repository.RequestDBRepo;
import com.example.mysocialnetworkdb.repository.UserDBRepo;
import com.example.mysocialnetworkdb.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserController {
    UserValidator userValidator = new UserValidator();
    FriendshipValidator friendshipValidator = new FriendshipValidator();

    UserDBRepo userDBRepo = new UserDBRepo(userValidator);
    FriendshipDBRepo friendshipDBRepo = new FriendshipDBRepo(friendshipValidator);
    RequestDBRepo requestDBRepo = new RequestDBRepo();
    MessageDBRepo messageDBRepo= new MessageDBRepo();
    Service service = Service.getInstance(userDBRepo,friendshipDBRepo,requestDBRepo,messageDBRepo);
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> usersTableView;
    @FXML
    TableColumn<User,Integer> idColumn;
    @FXML
    TableColumn<User,String> idEmailColumn;
    @FXML
    TableColumn<User,String> idUsernameColumn;

    @FXML
    private TextField username;

    @FXML
    public void initialize() throws RepositoryException {
        idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        idEmailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        idUsernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        usersTableView.setItems(model);
        initModel();
    }

    private void initModel() throws RepositoryException {
        int id = service.getUser().getId();
        List<User> users = new ArrayList<>();
        for (Friendship friendship : service.findAllFriendships()) {
            if (friendship.getIDU1() == id) {
                users.add(service.findOneUser(friendship.getIDU2()));
            } else if (friendship.getIDU2() == id) {
                users.add(service.findOneUser(friendship.getIDU1()));
            }
        }
        model.setAll(users);
    }

    public void logOut(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("NetworkFX");
        stage.setScene(scene);
        stage.show();
    }

    public void removeFriend(ActionEvent event) throws RepositoryException {
        User user1 = service.getUser();
        int id1 = user1.getId();
        User userRemove = usersTableView.getSelectionModel().getSelectedItem();
        int id2 = userRemove.getId();
        for (Friendship friendship : service.findAllFriendships()) {
            if ((id1 == friendship.getIDU1() && id2 == friendship.getIDU2()) || (id2 == friendship.getIDU1() && id1 == friendship.getIDU2())) {
                service.removeFriendship(friendship.getId());
                for (Request request : service.getAllRequests()) {
                    if ((user1.getUsername().equals(request.getUsername1()) && userRemove.getUsername().equals(request.getUsername2())) || (user1.getUsername().equals(request.getUsername2()) && userRemove.getUsername().equals(request.getUsername1()))) {
                        service.deleteRequest(request.getId());
                    }
                }
            }
        }
        initModel();
    }

    public void addFriend(ActionEvent event){
        User user1 = service.getUser();
        String username1 = user1.getUsername();
        String username2 = username.getText();
        if (username2.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid data!");
            alert.setContentText("Username cannot be empty!");
            alert.show();
        }
        User user2 = null;
        for (User user : service.findAllUsers()) {
            if (Objects.equals(user.getUsername(), username2)) {
                user2 = user;
            }
        }
        if (user2 == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid data!");
            alert.setContentText("Username does not exist!");
            alert.show();
        } else try {
            service.addRequest(username1, username2);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Request sent!");
            alert.setContentText("");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid data!");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void friendRequests(ActionEvent event) throws IOException{
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("RequestsView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Friend requests");
        stage.setScene(scene);
        stage.show();
    }
    public void handleOpenChat(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ChatView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        ChatController chatController = fxmlLoader.getController();
        User selectedUser =usersTableView.getSelectionModel().getSelectedItem();
        chatController.setSenderAndReceiver(service.getUser(), selectedUser, service.getUser());

        chatController.synchronizeChat();
        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();
    }
}
