package Controller;

import Business.Service;
import Business.ServiceException;
import Domain.User;
import grup.accesaproj.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {

    Service service;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    TextField textFieldUsername;

    @FXML
    PasswordField textFieldPassword;

    public void logInHandler() throws IOException {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        if(username == "" || password == ""){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops!");
            alert.setContentText("Both fields must be filled");
            alert.showAndWait();
        }
        else{
            User user = service.logInHandler(username,password);
            if(user == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Oops!");
                alert.setContentText("Invalid match between username and password");
                alert.showAndWait();
            }
            else{
                Stage mainStage = (Stage) textFieldPassword.getScene().getWindow();
                mainStage.close();

                Stage questsStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("quests-view.fxml"));
                Scene scene;
                scene = new Scene(fxmlLoader.load(), 700, 500);
                questsStage.setTitle("Main Menu");
                questsStage.setScene(scene);

                QuestsController questsController = fxmlLoader.getController();
                questsController.setData(service,user);
                questsStage.show();
            }
        }

    }

    public void signUpHandler(){
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        if(username == "" || password == ""){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops!");
            alert.setContentText("Both fields must be filled");
            alert.showAndWait();
        }else {
            try{
                service.signUpHandler(username,password);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("Signed up succesfully!");
                alert.setContentText(username+" user created succesfully");
                alert.showAndWait();

            }catch (ServiceException se){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Oops!");
                alert.setContentText(se.getMessage());
                alert.showAndWait();
            }
        }
    }

}
