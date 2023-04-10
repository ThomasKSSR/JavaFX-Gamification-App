package grup.accesaproj;

import Business.Service;
import Controller.LogInController;
import Repository.*;
import Test.MainTest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        MainTest mainTest = new MainTest();
        mainTest.run();

        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        UserRepository userRepository = new UserDBRepository(props);
        TriviaQuestRepository triviaQuestRepository = new TriviaQuestDBRepository(props);
        QuestOwnerRepository questOwnerRepository = new QuestOwnerDBRepository(props);
        BadgeOwnerRepository badgeOwnerRepository = new BadgeOwnerDBRepository(props);


        Service service = new Service(userRepository,triviaQuestRepository,questOwnerRepository, badgeOwnerRepository);



        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 300);
        stage.setTitle("Login");

        LogInController logInController = fxmlLoader.getController();
        logInController.setService(service);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
