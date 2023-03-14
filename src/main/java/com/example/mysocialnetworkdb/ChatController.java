package com.example.mysocialnetworkdb;

import com.example.mysocialnetworkdb.domain.Message;
import com.example.mysocialnetworkdb.domain.User;
import com.example.mysocialnetworkdb.domain.validators.FriendshipValidator;
import com.example.mysocialnetworkdb.domain.validators.UserValidator;
import com.example.mysocialnetworkdb.repository.FriendshipDBRepo;
import com.example.mysocialnetworkdb.repository.MessageDBRepo;
import com.example.mysocialnetworkdb.repository.RequestDBRepo;
import com.example.mysocialnetworkdb.repository.UserDBRepo;
import com.example.mysocialnetworkdb.service.Service;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    UserValidator userValidator = new UserValidator();
    FriendshipValidator friendshipValidator = new FriendshipValidator();

    UserDBRepo userDBRepo = new UserDBRepo(userValidator);
    FriendshipDBRepo friendshipDBRepo = new FriendshipDBRepo(friendshipValidator);
    RequestDBRepo requestDBRepo = new RequestDBRepo();
    MessageDBRepo messageDBRepo= new MessageDBRepo();
    Service service = Service.getInstance(userDBRepo,friendshipDBRepo,requestDBRepo,messageDBRepo);

    @FXML
    private Button sendButton;
    @FXML
    private TextField msg;
    @FXML
    private VBox msgs;
    @FXML
    private ScrollPane scrollPane;
    private User user1;
    private User user2;
    private User sender;
    public void setSenderAndReceiver(User user1, User user2,User sender) {
        this.sender = sender;
        this.user1=user1;
        this.user2=user2;
    }
    public void synchronizeChat() {
        List<Message> list = this.service.getAllFromUsers(this.user1, this.user2);
        List<Message> list2 = this.service.getAllFromUsers(this.user2, this.user1);
        List<Message> concatList= new ArrayList<Message>();
        concatList.addAll(list);
        concatList.addAll(list2);
        concatList.sort(Comparator.comparing(Message::getDateTime));
        if(concatList.size()>0){
            for(Message element : concatList) {
                String message = element.getMessage();
                LocalDateTime date= element.getDateTime();
                int id= element.getId();
                if (!message.isEmpty()) {
                    HBox hBox = new HBox();
                    if (element.getId_sender()==service.getUser().getId()) {
                        hBox.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        hBox.setAlignment(Pos.CENTER_LEFT);
                    }


                    Text text = new Text(message);

                    hBox.getChildren().add(text);

                    msgs.getChildren().add(hBox);
                }

            }
        }
    }
    public void redirectToUsers(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("UtilizatorView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello, " + service.getUser().getUsername() + "!");
        stage.setScene(scene);
        stage.show();
    }
    public void handleSend(ActionEvent event)
    {
        String messageToSend = msg.getText();
        if(!messageToSend.isEmpty())
        {
            service.addMessage(user1.getId(), user2.getId(), sender.getId(), messageToSend);
            HBox hbox=new HBox();
            hbox.setAlignment(Pos.CENTER_RIGHT);
            Text text= new Text(messageToSend);
            hbox.getChildren().add(text);
            msgs.getChildren().add(hbox);
        }
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        msgs.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double)newValue);
            }
        });
    }
}
