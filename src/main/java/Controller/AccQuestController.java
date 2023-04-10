package Controller;
import Domain.Quest;
import Business.Service;
import Domain.TriviaQuest;
import Domain.User;
import grup.accesaproj.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class AccQuestController {

    private Service service;
    private User currentUser;
    private User questOwner;

    private TriviaQuest quest;

    private int tries;

    @FXML
    Label questionLabel;

    @FXML
    TextField answerTextField;



    public void setData(Service service, User currentUser, User questOwner,TriviaQuest quest){
        this.service=service;
        this.currentUser=currentUser;
        this.questOwner=questOwner;
        this.quest=quest;
        this.initData();

    }

    public void initData(){
        questionLabel.setText(quest.getQuestion());
        tries=3;


    }

    public void confirmAnswerHandler(){
        String answer = answerTextField.getText();
        if(!answer.equals(quest.getAnswer())){
            this.tries--;
            if(this.tries==0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("Wrong");
                alert.setContentText("Sorry. Out of tries");
                alert.showAndWait();
                try {
                    this.abandonQuestHandler();
                }catch(IOException ie){
                    ie.printStackTrace();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("Wrong");
                alert.setContentText("Wrong answer " + String.valueOf(tries) + " tries remaining. Try again!");
                alert.showAndWait();
            }
        }
        else{
            service.completeQuestHandler(currentUser,questOwner,quest);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("Congrats");
            alert.setContentText("Answer is correct!");
            alert.showAndWait();

            try {
                this.abandonQuestHandler();
            }catch(IOException ie){
                ie.printStackTrace();
            }

        }

    }



    public void abandonQuestHandler()throws IOException{
        Stage mainStage = (Stage) answerTextField.getScene().getWindow();
        mainStage.close();

        Stage questsStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("quests-view.fxml"));
        Scene scene;
        scene = new Scene(fxmlLoader.load(), 700, 500);
        questsStage.setTitle("Main Menu");
        questsStage.setScene(scene);

        QuestsController questsController = fxmlLoader.getController();
        questsController.setData(service,currentUser);
        questsStage.show();
    }


}
