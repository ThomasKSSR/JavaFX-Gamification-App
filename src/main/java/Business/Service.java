package Business;

import Domain.*;
import Repository.BadgeOwnerRepository;
import Repository.QuestOwnerRepository;
import Repository.TriviaQuestRepository;
import Repository.UserRepository;
import Utils.Observable;
import Utils.Observer;

import java.util.ArrayList;
import java.util.List;




public class Service implements Observable {

    private List<Observer> observers;

    private UserRepository userRepository;

    private TriviaQuestRepository triviaQuestRepository;

    private QuestOwnerRepository questOwnerRepository;

    private BadgeOwnerRepository badgeOwnerRepository;




    public Service(UserRepository userRepository, TriviaQuestRepository triviaQuestRepository, QuestOwnerRepository questOwnerRepository, BadgeOwnerRepository badgeOwnerRepository) {
        this.userRepository = userRepository;
        this.triviaQuestRepository = triviaQuestRepository;
        this.questOwnerRepository = questOwnerRepository;
        this.badgeOwnerRepository = badgeOwnerRepository;
        this.observers = new ArrayList<>();
    }


    public User logInHandler(String username,String password){
        return userRepository.findMatchingUser(username,password);

    }

    public List<UserDTO> getAllUserDTOs(){
        List<UserDTO> userDTOS = new ArrayList<>();

        for(User user :userRepository.getAll()){
            List<BadgeOwner> badgesOwners = badgeOwnerRepository.findAllByUser(user);
            String badges= this.getUsersBadges(user);
            if(badges == null){
                badges = "No Badges";
            }

            Rank rank=this.getUsersRank(user);

            UserDTO userDTO= new UserDTO(user.getUsername(),user.getPassword(),user.getPoints(),badges,rank);
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    public String getUsersBadges(User user){
        List<BadgeOwner> badgesOwners = badgeOwnerRepository.findAllByUser(user);
        String badges;
        if(badgesOwners.isEmpty()){
            return null;
        }
        else{
            badges="";
            for(BadgeOwner badgeOwner :badgesOwners){
                badges+=badgeOwner.getBadgeid()+",";
            }
            badges =badges.substring(0, badges.length() - 1);
        }
        return badges;
    }

    public Rank getUsersRank(User user){
        int points=user.getPoints();
        Rank rank;
        if(points<100){
            rank = Rank.Bronze;
        }
        else if(points<300){
            rank=Rank.Silver;
        }
        else if(points< 500){
            rank=Rank.Gold;
        }
        else if(points <1000){
            rank=Rank.Platinum;
        }
        else{
            rank = Rank.Diamond;
        }
        return rank;
    }

    public List<QuestDTO> getAllQuestDTOs(){
        List<QuestDTO> questDTOs= new ArrayList<>();
        for(QuestOwner questOwner: questOwnerRepository.getAll()){
            User user=userRepository.findById(questOwner.getUsername());
            TriviaQuest triviaQuest = triviaQuestRepository.findById(Integer.parseInt(questOwner.getQuestId()));
            String questName="Quest made by "+user.getUsername();
            QuestDTO questDTO = new QuestDTO(triviaQuest.getId(),questName,triviaQuest.getPoints(),triviaQuest.getQuestion(),triviaQuest.getAnswer());
            questDTOs.add(questDTO);

        }
        return questDTOs;
    }


    public void createQuestHandler(User user, String question,String answer,int points){
        int maxID = this.getMaxQuestID()+1;
        TriviaQuest triviaQuest = new TriviaQuest(maxID,question,answer,points);

        QuestOwner questOwner = new QuestOwner(user.getUsername(),String.valueOf(maxID));
        user.setPoints(user.getPoints() -points);
        user.setCreatedQuests(user.getCreatedQuests()+1);

        this.updateUserBadgesOnCreatedQuests(user);

        triviaQuestRepository.add(triviaQuest);
        questOwnerRepository.add(questOwner);
        userRepository.update(user,user.getUsername());

        this.notifyAllObs();
    }

    public int getMaxQuestID(){
        int max=0;
        for(TriviaQuest triviaQuest :triviaQuestRepository.getAll()){
            if(triviaQuest.getId() >max){
                max = triviaQuest.getId();
            }
        }
        return max;
    }

    public String getUserIdByQuestId(String questID){
        return questOwnerRepository.getUserIDByQuestID(questID);
    }

    public User findUserById(String userID){
        return userRepository.findById(userID);
    }




    public void completeQuestHandler(User currentUser,User userQuestOwner,TriviaQuest quest){
        currentUser.setCompletedQuests(currentUser.getCompletedQuests()+1);
        this.updateUserBadgesOnCompletedQuest(currentUser);
        currentUser.setPoints(currentUser.getPoints()+quest.getPoints());

        userRepository.update(currentUser,currentUser.getId());
        QuestOwner questOwner = new QuestOwner(userQuestOwner.getId(),String.valueOf(quest.getId()));

        questOwnerRepository.delete(questOwner);
        triviaQuestRepository.delete(quest);
        this.notifyAllObs();
    }

    public void updateUserBadgesOnCompletedQuest(User currentUser){

        if(currentUser.getCompletedQuests()==1){
            Badge badge= new Badge("Beginner");
            BadgeOwner badgeOwner= new BadgeOwner(currentUser.getId(),badge.getId());
            badgeOwnerRepository.add(badgeOwner);
        }else if(currentUser.getCompletedQuests()==5){
            Badge badge= new Badge("Mid");
            BadgeOwner badgeOwner= new BadgeOwner(currentUser.getId(),badge.getId());
            badgeOwnerRepository.add(badgeOwner);
            badgeOwnerRepository.delete(new BadgeOwner(currentUser.getId(),"Beginner"));

        }else if(currentUser.getCompletedQuests()==10){
            Badge badge= new Badge("Advanced");
            BadgeOwner badgeOwner= new BadgeOwner(currentUser.getId(),badge.getId());
            badgeOwnerRepository.add(badgeOwner);
            badgeOwnerRepository.delete(new BadgeOwner(currentUser.getId(),"Mid"));

        }else if(currentUser.getCompletedQuests() ==15){
            Badge badge= new Badge("Master");
            BadgeOwner badgeOwner= new BadgeOwner(currentUser.getId(),badge.getId());
            badgeOwnerRepository.add(badgeOwner);
            badgeOwnerRepository.delete(new BadgeOwner(currentUser.getId(),"Advanced"));
        }
    }

    public void updateUserBadgesOnCreatedQuests(User currentUser){
        if(currentUser.getCreatedQuests()==1){
            Badge badge= new Badge("Helper");
            BadgeOwner badgeOwner= new BadgeOwner(currentUser.getId(),badge.getId());
            badgeOwnerRepository.add(badgeOwner);
        }else if(currentUser.getCompletedQuests()==5){
            Badge badge= new Badge("Moderator");
            BadgeOwner badgeOwner= new BadgeOwner(currentUser.getId(),badge.getId());
            badgeOwnerRepository.add(badgeOwner);
            badgeOwnerRepository.delete(new BadgeOwner(currentUser.getId(),"Helper"));

        }else if(currentUser.getCompletedQuests()==10){
            Badge badge= new Badge("Admin");
            BadgeOwner badgeOwner= new BadgeOwner(currentUser.getId(),badge.getId());
            badgeOwnerRepository.add(badgeOwner);
            badgeOwnerRepository.delete(new BadgeOwner(currentUser.getId(),"Moderator"));

        }else if(currentUser.getCompletedQuests() ==15){
            Badge badge= new Badge("GM");
            BadgeOwner badgeOwner= new BadgeOwner(currentUser.getId(),badge.getId());
            badgeOwnerRepository.add(badgeOwner);
            badgeOwnerRepository.delete(new BadgeOwner(currentUser.getId(),"Admin"));
        }


    }
    public void signUpHandler(String username,String password)throws ServiceException{
        User user=new User(username,password);
        if(userRepository.findById(username) != null){
            throw new ServiceException("Username already taken!");

        }
        else{
            userRepository.add(user);
        }
    }

    public void deleteUsersBadges(User user){
        badgeOwnerRepository.deleteUsersBadges(user);
    }



    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObs() {
        for(Observer obs:observers){
            obs.update();
        }
    }
}
