package Test;

import Business.Service;
import Business.ServiceException;
import Domain.QuestDTO;
import Domain.Rank;
import Domain.TriviaQuest;
import Domain.User;
import Repository.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class MainTest {

    private Service service;

    private UserRepository userRepository;

    private TriviaQuestRepository triviaQuestRepository;

    private QuestOwnerRepository questOwnerRepository;

    private BadgeOwnerRepository badgeOwnerRepository;


    public MainTest() {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bdtest.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bdtest.config "+e);
        }

        this.userRepository = new UserDBRepository(props);
        this.triviaQuestRepository = new TriviaQuestDBRepository(props);
        this.questOwnerRepository = new QuestOwnerDBRepository(props);
        this.badgeOwnerRepository = new BadgeOwnerDBRepository(props);

        this.service = new Service(userRepository,triviaQuestRepository,questOwnerRepository,badgeOwnerRepository);

    }

    public void run(){
        this.serviceTest();
    }

    public void serviceTest(){
        //signUp Test
        String username="Ionut";
        String password="123";
        try {
            service.signUpHandler(username, password);
        }catch (ServiceException se){
            assert false;
        }

        try {
            service.signUpHandler(username, password);
            assert false;
        }catch (ServiceException se){
            assert true;
        }
        User user = new User(username,password);
        assert service.findUserById(user.getId()) ==user;


        //login Test
        assert service.logInHandler(username,password) == user;

        assert service.getAllUserDTOs().size()==1;

        //User rank test
        assert service.getUsersRank(user).equals(Rank.Bronze);

        //create quest test
        user.setPoints(200);
        userRepository.update(user,username);
        assert service.getUsersRank(user).equals(Rank.Silver);
        String question ="What does “www” stand for in a website browser?";
        String answer = "world wide web";
        service.createQuestHandler(user,question,answer,200);
        assert service.findUserById(username).getPoints()==0;
        assert service.getAllQuestDTOs().size()==1;
        QuestDTO questDTO = service.getAllQuestDTOs().get(0);
        TriviaQuest triviaQuest = new TriviaQuest(questDTO.getId(),questDTO.getQuestion(),questDTO.getAnswer(),questDTO.getPoints());


        //complete quest test
        service.completeQuestHandler(user,user,triviaQuest);

        //badges and updated points test
        assert service.findUserById(username).getPoints()==200;
        assert service.getUsersBadges(user) =="Helper,Beginner";

        //ddelete badges of a user test and delete user test
        service.deleteUsersBadges(user);
        userRepository.delete(user);



    }
}
