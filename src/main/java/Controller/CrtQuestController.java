package Controller;

import Business.Service;
import Domain.User;
import grup.accesaproj.MainApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;

public class CrtQuestController {
    private Service service;
    private User user;

    @FXML
    TextField questionTextField;

    @FXML
    TextField answerTextField;

    @FXML
    Slider pointsSlider;

    @FXML
    Label pointsLabel;

    public void setData(Service service,User user){
        this.service=service;
        this.user=user;
        initData();
    }


    public void initData(){
        pointsLabel.setText("0");
        pointsSlider.setMax(user.getPoints());
        pointsSlider.valueProperty().addListener((obs, oldval, newVal) ->
                pointsSlider.setValue(Math.round(newVal.doubleValue())));

        pointsSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                pointsLabel.setText(String.valueOf(newValue.intValue())));

    }


    public void createQuestHandler() throws IOException {
        String question = questionTextField.getText();
        String answer = answerTextField.getText();
        int points = (int)Math.round(pointsSlider.getValue());
        if(question.equals("") || answer.equals("") || points == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops!");
            alert.setContentText("You have to fill all the fields and select the number of points");
            alert.showAndWait();
        }
        else{
            service.createQuestHandler(user,question,answer,points);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("Quest creator");
            alert.setContentText("Quest created succesfully!");
            alert.showAndWait();

            Stage questCreatorStage = (Stage) pointsSlider.getScene().getWindow();
            questCreatorStage.close();

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
    public void cancelCreationHandler()throws IOException{
        Stage questCreatorStage = (Stage) pointsSlider.getScene().getWindow();
        questCreatorStage.close();

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
