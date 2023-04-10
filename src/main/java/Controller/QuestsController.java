package Controller;

import Business.Service;
import Domain.QuestDTO;
import Domain.TriviaQuest;
import Domain.User;
import Domain.UserDTO;
import Utils.Observer;
import grup.accesaproj.HelloApplication;
import grup.accesaproj.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class QuestsController implements Observer {

    private ObservableList<UserDTO> usersModel= FXCollections.observableArrayList();

    private ObservableList<QuestDTO> questsModel= FXCollections.observableArrayList();

    @FXML
    Label usernameLabel;

    @FXML
    Label rankLabel;

    @FXML
    Label pointsLabel;



    @FXML
    TableView<QuestDTO> questsTableView;

    @FXML
    TableColumn<QuestDTO,String> questsColumn;

    @FXML
    TableColumn<QuestDTO,Integer> questsPointsColumn;

    @FXML
    TableView<UserDTO> usersTableView;

    @FXML
    TableColumn<UserDTO,String> usernameColumn;

    @FXML
    TableColumn<UserDTO,String> rankColumn;

    @FXML
    TableColumn<UserDTO,String> badgesColumn;



    private Service service;

    private User user;


    public void setData(Service service,User user){
        this.service=service;
        this.user=user;
        service.addObserver(this);
        initTables();
    }

    @FXML
    public void initialize(){
        usernameColumn.setCellValueFactory(new PropertyValueFactory<UserDTO,String>("username"));
        rankColumn.setCellValueFactory(new PropertyValueFactory<UserDTO,String>("rank"));
        badgesColumn.setCellValueFactory(new PropertyValueFactory<UserDTO,String>("badgesNames"));
        usersTableView.setItems(usersModel);

        questsColumn.setCellValueFactory(new PropertyValueFactory<QuestDTO,String>("name"));
        questsPointsColumn.setCellValueFactory(new PropertyValueFactory<QuestDTO,Integer>("points"));
        questsTableView.setItems(questsModel);
    }

    public void initTables(){
        usernameLabel.setText(user.getUsername());
        pointsLabel.setText("Points: "+String.valueOf( user.getPoints()));
        rankLabel.setText("Rank: "+service.getUsersRank(user).toString());
        usersModel.setAll(service.getAllUserDTOs());
        questsModel.setAll(service.getAllQuestDTOs());
    }

    public void acceptQuestHandler()throws IOException{
        QuestDTO questDTO = questsTableView.getSelectionModel().getSelectedItem();
        if(questDTO != null) {
            TriviaQuest triviaQuest = new TriviaQuest(questDTO.getId(), questDTO.getQuestion(), questDTO.getAnswer(), questDTO.getPoints());
            User currentUser = user;
            String userId = service.getUserIdByQuestId(String.valueOf(questDTO.getId()));
            User questOwner = service.findUserById(userId);

            Stage questsStage = (Stage) usernameLabel.getScene().getWindow();
            questsStage.close();

            Stage acceptQuestStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("accquest-view.fxml"));
            Scene scene;
            scene = new Scene(fxmlLoader.load(), 416, 214);
            acceptQuestStage.setTitle(questDTO.getName());
            acceptQuestStage.setScene(scene);

            AccQuestController accQuestController = fxmlLoader.getController();
            accQuestController.setData(service,currentUser,questOwner,triviaQuest);

            acceptQuestStage.show();

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops!");
            alert.setContentText("You have to select a quest");
            alert.showAndWait();
        }


    }

    public void createQuestHandler() throws IOException {
        Stage questsStage = (Stage) usernameLabel.getScene().getWindow();
        questsStage.close();

        Stage createQuestStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("crtquest-view.fxml"));
        Scene scene;
        scene = new Scene(fxmlLoader.load(), 362, 279);
        createQuestStage.setTitle("Quest");
        createQuestStage.setScene(scene);

        CrtQuestController crtQuestController = fxmlLoader.getController();
        crtQuestController.setData(service,user);
        createQuestStage.show();

    }

    public void logoutHandler()throws IOException{
        Stage questsStage = (Stage) usernameLabel.getScene().getWindow();
        questsStage.close();

        Stage loginStage = new Stage();


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 300);
        loginStage.setTitle("Login");

        LogInController logInController = fxmlLoader.getController();
        logInController.setService(service);
        loginStage.setScene(scene);
        loginStage.show();
    }


    @Override
    public void update() {
        initTables();
    }
}
