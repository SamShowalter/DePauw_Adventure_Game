import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.Graphics;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;

public class DePauwAdventureGame {

    // Declare all game window size variables
    private static final int BOARD_WIDTH = 1300;   // board width
    private static final int BOARD_HEIGHT = 710; // board height
    private static final int MAP_END = 965;   //Length of board being used for graphics

    //Official number of opponents and items to populate graph with
    private static final int numAbilitiesInMap = 70;
    private static final int numHealthPotionsInMap = 40;
    private static final int numPowerUpsInMap = 30;
    private static final int numOpponentsInMap = 50;

    // Building hashMap for all of DePauw's academic buildings.
    // Ability (Student and Professor), Power Up, Health Potion, and Outside Opponent reference tables
    private static final HashMap<String,Building> buildingMap = new HashMap<String,Building>();
    private static final HashMap<String,GameElement> refProfessorAbilities = new HashMap<String,GameElement>();
    private static final HashMap<String,GameElement> refStudentAbilities = new HashMap<String,GameElement>();
    private static final HashMap<String,GameElement> refPowerUps = new HashMap<String,GameElement>();
    private static final HashMap<String,GameElement> refHealthPotions = new HashMap<String,GameElement>();
    private static final HashMap<String,GameElement> refOutsideOpponents = new HashMap<String,GameElement>();

    // Other variables pertaining to graphics logistics and storage
    private enum STATE{MENU,GAME,WIN};
    private boolean visible;        //decides if the items will be visible or not
    private boolean restart;        //signifies the game needs to be restarted
    private JFrame frame;
    private STATE state;
    private HashMap<Point, Object> outsideOpponentAndItemMap;
    private DrawingPanel dp;
    private MainPlayer player;
    private BufferedImage background;
    private BufferedImage openingScreenBackground;
    private BufferedImage winScreenBackground;

    //Getters
    public HashMap<String,GameElement> getRefStudentAbilities(){return refStudentAbilities;}
    public HashMap<String,GameElement> getRefProfessorAbilities(){return refProfessorAbilities;}
    public HashMap<String,GameElement> getRefPowerUps(){return refPowerUps;}
    public HashMap<String,GameElement> getRefHealthPotions(){return refHealthPotions;}
    public HashMap<String,GameElement> getRefOutsideOpponents(){return refOutsideOpponents;}
    public boolean getVisible(){return visible;}
    public boolean getRestart(){return restart;}
    public int getBoardWidth() { return BOARD_WIDTH; }
    public int getMapEnd(){return MAP_END;}
    public int getBoardHeight() { return BOARD_HEIGHT; }
    public HashMap<String,Building> getBuildingMap(){return buildingMap;}
    public STATE getState(){return state;}
    public JFrame getFrame(){return frame;}
    public HashMap<Point, Object> getOutsideOpponentAndItemMap() { return outsideOpponentAndItemMap; }
    public DrawingPanel getDrawingPanel() { return dp; }
    public MainPlayer getMainPlayer() { return player; }
    public BufferedImage getBackground(){return background;}
    public BufferedImage getOpeningScreenBackground(){return openingScreenBackground;}

    //Setters
    public void setRestart(boolean pRestart){restart = pRestart;}
    public void setVisible(boolean pVisible){visible = pVisible;}
    public void setFrame(JFrame pFrame){frame = pFrame;}
    public void setState(STATE pState){state = pState;}
    public void setOutsideOpponentAndItemMap(HashMap<Point, Object> pOutsideOpponentAndItemMap){outsideOpponentAndItemMap = pOutsideOpponentAndItemMap;}
    public void setDrawingPanel(DrawingPanel pDP){dp = pDP;}
    public void setMainPlayer(MainPlayer pMP){player = pMP;}
    public void setBackground(BufferedImage pBackground){background = pBackground;}
    public void setOpeningScreenBackground(BufferedImage pOpeningScreenBackground){openingScreenBackground = pOpeningScreenBackground;}
    
    //Constructor for game.
    public DePauwAdventureGame(MainPlayer mp, boolean pVisible)
    {
        //Initialize State to menu
        state = STATE.MENU;

        //Set visibility
        visible = pVisible;

        //Initialize main player
        player = mp;

        //Initialize HashMap for Buildings
        outsideOpponentAndItemMap = new HashMap<Point,Object>();

        //Creating Buildings
        Building Admissions = new Building("DPU Admissions Building", "Administrative", "Where students come in and never go out.", 370,75,505,15,410, 85, 460, 80);
        Building Roy = new Building("Roy O West Library", "Library", "Where productivity goes to die.", 50,225,130,165,85, 235, 90, 230);
        Building Hoover = new Building("Hoover Hall", "Meal Hall", "Where students come in and never go out.", 545,275,630,215,580, 285, 585, 280);
        Building Peeler = new Building("Peeler", "Academic", "Its so artsy you will lose your mind.", 195,460,280,390,235, 470, 240, 465);
        Building Julian = new Building("Julian", "Academic", "The math and science center from your nightmares", 380,450,465,390,420, 460, 425, 455);
        Building GCPA = new Building("The GCPA", "Administrative", "Where students come in and never go out.", 530,435,615,375,570, 445, 575, 440);
        Building PCCM = new Building("The PCCM", "Academic", "Now broadcasting you!", 850,435,940,375,895, 445, 900, 440);
        Building Lilly = new Building("Lilly", "Athletic", "Endless athletic center.", 395,635,480,535,435, 645, 440, 640);

        //Convert Admissions building into Hidden Admissions building with decorator
        HiddenBuilding AdmissionsBuilding = new HiddenBuilding(Admissions);

        //Create building bosses (Must be after Admissions is converted to a hidden object)
        BuildingBoss AdmissionsBoss = new BuildingBoss("Mark McCoy", "President", "Patriarch of DePauw University",AdmissionsBuilding);
        BuildingBoss RoyBoss = new BuildingBoss("Roy O West", "Donor", "The Achilles of Roy O West Library",Roy);
        BuildingBoss HooverBoss = new BuildingBoss("Herbert Hoover", "Businessman", "Ruthless capitalist",Hoover);
        BuildingBoss PeelerBoss = new BuildingBoss("Michael Mackenzie", "Art Professor", "Picasso of DePauw University",Peeler);
        BuildingBoss JulianBoss = new BuildingBoss("Percy Julian", "Mad Scientist", "Smarted DePauw Alumnus",Julian);
        BuildingBoss GCPABoss = new BuildingBoss("Judson and Joyce Greene", "Business mogul and musician", "Musical hypnotist #getout",GCPA);
        BuildingBoss PCCMBoss = new BuildingBoss("Eugene Pulliam", "publicist", "Savage journalist (will blackmail)",PCCM);
        BuildingBoss LillyBoss = new BuildingBoss("Scott Welch", "Packaging CEO", "Power-lifting strongman",Lilly);

        //Populate Building Map
        buildingMap.put(AdmissionsBuilding.getName(),AdmissionsBuilding);
        buildingMap.put(Roy.getName(),Roy);
        buildingMap.put(Hoover.getName(),Hoover);
        buildingMap.put(Peeler.getName(),Peeler);
        buildingMap.put(Julian.getName(),Julian);
        buildingMap.put(GCPA.getName(),GCPA);
        buildingMap.put(PCCM.getName(),PCCM);
        buildingMap.put(Lilly.getName(),Lilly);

        //Create Outside Opponents
        OutsideOpponent Smogor = new OutsideOpponent("Louis Smogor", "Math Professor", "Full of ancient wisdom", this);
        OutsideOpponent Sununu = new OutsideOpponent("Andrea Sununu", "English Professor", "Hemingway's mentor", this);
        OutsideOpponent Rusu = new OutsideOpponent("Dan Rusu", "Math Professor", "Romania's math genius", this);
        OutsideOpponent Barreto = new OutsideOpponent("Humberto Barreto", "Economics Professor", "The father of John Keynes", this);
        OutsideOpponent Roehling = new OutsideOpponent("Allison Roehling", "Economics Professor", "Macroeconomics legend", this);
        OutsideOpponent Kane = new OutsideOpponent("Danielle Kane", "Sociology Professor", "Knows what you are thinking", this);
        OutsideOpponent Luque = new OutsideOpponent("Maria Luque", "Spanish Professor", "Un dios", this);
        OutsideOpponent Bitner = new OutsideOpponent("Ted Bitner", "Psychology Professor", "Can persuade you of anything", this);
        OutsideOpponent Schwipps = new OutsideOpponent("Greg Schwipps", "English Professor", "Wrote the Bible and the Torah", this);
        OutsideOpponent Berque = new OutsideOpponent("Dave Berque", "Computer Science Professor", "Founded Apple, then just gave it away.", this);
        OutsideOpponent Byers = new OutsideOpponent("Chad Byers", "Computer Science Professor", "Charles Darwin of tech.", this);
        OutsideOpponent Wu = new OutsideOpponent("Zhixin Wu", "Math Professor", "Can value any situation with math.", this);
        OutsideOpponent Raghav = new OutsideOpponent("Manu Raghav", "Economics Professor", "Started the Silk Road", this);
        OutsideOpponent Everett = new OutsideOpponent("Jennifer Everett", "Philosophy Professor", "Dated Aristotle for awhile.", this);
        OutsideOpponent Cohen = new OutsideOpponent("Adam Cohen", "Psychology Professor", "Gives 4.0s to all, with a smile.", this);
        OutsideOpponent HooverHallStaff = new OutsideOpponent("Every Hoover Worker", "Lunch Lady", "Never gives out chicken.", this);
        OutsideOpponent Casey = new OutsideOpponent("Brian Casey", "President of Colgate University", "Comes back every year to spit on Mark McCoy.", this);
        OutsideOpponent Lemon = new OutsideOpponent("Gary Lemon", "Economics Professor", "Think there's a free lunch? Think again!", this);
        OutsideOpponent Wilkerson = new OutsideOpponent("Scott Wilkerson", "Geology Professor", "Brings a new meaning to country rock", this);
        OutsideOpponent Mills = new OutsideOpponent("Jim Mills", "Geology Professor", "Head of Team Magma", this);
        OutsideOpponent Hale = new OutsideOpponent("Jacob Hale", "Physics Professor", "Can walk on water using physics", this);
        OutsideOpponent Soster = new OutsideOpponent("Fred Soster", "Geology Professor", "Mr. Clean wannabe", this);
        OutsideOpponent Facilities = new OutsideOpponent("Facilities Management Staff", "Janitors & co.", "Now YOU get to clean the bathrooms!", this);
        OutsideOpponent Brooks = new OutsideOpponent("Howard Brooks", "Physics Professor", "Newton's mentor, and Einstein's protegee", this);
        OutsideOpponent Brown = new OutsideOpponent("Harry Brown", "English Professor", "Knows how to keep AIs from taking over the world", this);
        OutsideOpponent Schwartzman = new OutsideOpponent("Maria Schwartzman", "Computer Science Professor", "The cookie-cutter's approach to coding", this);
        OutsideOpponent Pare = new OutsideOpponent("Craig Pare'", "Music Professor", "The ISO's secret principal percussionist", this);
        
        //Add Outside Opponents to reference HashMap
        refOutsideOpponents.put(Smogor.getName(),Smogor);
        refOutsideOpponents.put(Sununu.getName(),Sununu);
        refOutsideOpponents.put(Rusu.getName(),Rusu);
        refOutsideOpponents.put(Barreto.getName(),Barreto);
        refOutsideOpponents.put(Roehling.getName(),Roehling);
        refOutsideOpponents.put(Kane.getName(),Kane);
        refOutsideOpponents.put(Luque.getName(),Luque);
        refOutsideOpponents.put(Bitner.getName(),Bitner);
        refOutsideOpponents.put(Schwipps.getName(),Schwipps);
        refOutsideOpponents.put(Berque.getName(),Berque);
        refOutsideOpponents.put(Byers.getName(),Byers);
        refOutsideOpponents.put(Wu.getName(),Wu);
        refOutsideOpponents.put(Raghav.getName(),Raghav);
        refOutsideOpponents.put(Everett.getName(),Everett);
        refOutsideOpponents.put(HooverHallStaff.getName(),HooverHallStaff);
        refOutsideOpponents.put(Casey.getName(),Casey);
        refOutsideOpponents.put(Lemon.getName(),Lemon);
        refOutsideOpponents.put(Wilkerson.getName(),Wilkerson);
        refOutsideOpponents.put(Mills.getName(),Mills);
        refOutsideOpponents.put(Hale.getName(),Hale);
        refOutsideOpponents.put(Soster.getName(),Soster);
        refOutsideOpponents.put(Cohen.getName(),Cohen);
        refOutsideOpponents.put(Facilities.getName(),Facilities);
        refOutsideOpponents.put(Brooks.getName(),Brooks);
        refOutsideOpponents.put(Brown.getName(),Brown);
        refOutsideOpponents.put(Schwartzman.getName(),Schwartzman);
        refOutsideOpponents.put(Pare.getName(),Pare);

        //Create health potions
        HealthPotion fullNightSleep = new HealthPotion("Full Night Sleep", "Health Potion", "A date with you and your bed for the night.",35, this);
        HealthPotion cStore = new HealthPotion("C-store", "Health Potion", "Buffalo wraps and chips to the rescue", 15, this);
        HealthPotion fallBreak = new HealthPotion("Fall Break", "Health Potion", "A full week of bliss to recover from midterms",  75, this);
        HealthPotion springBreak = new HealthPotion("Spring Break","Health Potion", "Relaxing your mind in the heat of some other state", 50, this);
        HealthPotion farmFresh = new HealthPotion("Fresh Food", "Health Potion", "Giving Greek food a break with farm fresh", 25,this);
        HealthPotion lillyYoga = new HealthPotion("Lilly Yoga", "Health Potion", "Give your soul some love with yoga",  30, this);
        HealthPotion imSports = new HealthPotion("IM Sports","Health Potion", "Dunk on some fools, and feel good", 45, this);
        HealthPotion goodBook = new HealthPotion("Good Book", "Health Potion", "Maybe its Hemingway, or 50 Shades of Grey",  10, this);
        HealthPotion volunteering = new HealthPotion("Volunteering", "Health Potion", "For a good cause, and benefitting all",  80, this);
        HealthPotion depuppies = new HealthPotion("DePuppies", "HealthPotion", "A puppy can solve any problem",  70, this);
        HealthPotion freeCoffee = new HealthPotion("Free Coffee", "Health Potion", "Gritty and black, but so good",  15, this);
        HealthPotion naturePark = new HealthPotion("Nature Park", "Health Potion", "The rim of the quarry will give you infinit joy",  50, this);
        HealthPotion prindle = new HealthPotion("Prindle", "Health Potion", "Reflect on your past sins, and be cleansed", 60,this);
        HealthPotion movieNight = new HealthPotion("Movie Night", "Health Potion", "Alone or with a friend, lose yourself in cinema", 40,this);
        HealthPotion classCancelled = new HealthPotion("Class Cancelled", "Health Potion", "Your professor needs a break, and so do you", 55, this);
        HealthPotion scheduling = new HealthPotion("Scheduling", "Health Potion", "Buying a planner was the best investment ever", 25, this);
        HealthPotion concert = new HealthPotion("DePauw Concert Series", "Health Potion", "Nothing better than good music to unwind with", 65, this);

        //Add health potions to master HashMap
        refHealthPotions.put(fullNightSleep.getName(),fullNightSleep);
        refHealthPotions.put(cStore.getName(),cStore);
        refHealthPotions.put(fallBreak.getName(),fallBreak);
        refHealthPotions.put(springBreak.getName(),springBreak);
        refHealthPotions.put(farmFresh.getName(),farmFresh);
        refHealthPotions.put(lillyYoga.getName(),lillyYoga);
        refHealthPotions.put(imSports.getName(),imSports);
        refHealthPotions.put(goodBook.getName(),goodBook);
        refHealthPotions.put(volunteering.getName(),volunteering);
        refHealthPotions.put(depuppies.getName(),depuppies);
        refHealthPotions.put(freeCoffee.getName(),freeCoffee);
        refHealthPotions.put(naturePark.getName(),naturePark);
        refHealthPotions.put(prindle.getName(),prindle);
        refHealthPotions.put(movieNight.getName(),movieNight);
        refHealthPotions.put(classCancelled.getName(),classCancelled);
        refHealthPotions.put(scheduling.getName(),scheduling);
        refHealthPotions.put(concert.getName(),concert);

        //Create power ups
        PowerUp aceTest = new PowerUp("Aced Test","Power Up", "Murdering an exam has driven your power up!", 2, this);
        PowerUp scholarship = new PowerUp("Scholarship", "Power Up","Your tuition is covered, and you're born again",7,this);
        PowerUp acePaper = new PowerUp("Ace Paper", "Power Up","George Elliot would be proud of your work",1,this);
        PowerUp tennisWinner = new PowerUp("Tennis Match Winner","Power Up","Its just between friends for beer, but you're the champion",1,this);
        PowerUp golfWinner = new PowerUp("Campus Golf Winner", "Power Up", "You hit East College from 60 yrds. Only winners do that.", 2, this);
        PowerUp raceWinner = new PowerUp("Race Winner", "Power Up", "You crashed track practice, now you're on varsity",3,this);
        PowerUp partyAnimal = new PowerUp("Party Animal", "Power Up", "Sometimes you just need to let loose. That's OK.", 1, this);
        PowerUp studentPres = new PowerUp("Elected Student President", "Power Up","Whether you lead like Frank Underwood or Angela Merkel, you're in charge.",4, this);
        PowerUp skipClass = new PowerUp("Skipping Class", "Power Up", "Its not ideal, but you know yourself and there's power in that", 1,this);
        PowerUp startClub = new PowerUp("Start a Club", "Power Up", "You still do your hobby for fun, but now its officially recognized", 2,this);
        PowerUp getInternship = new PowerUp("Get an Internship", "Power Up", "Your classes have been paying off!", 5, this);
        PowerUp getJob = new PowerUp("Get a Job", "Power Up", "The whole reason you went to college, fulfilled.", 6, this);
        PowerUp TA = new PowerUp("Become a TA", "Power Up", "You know the material so well, you're the teacher now", 3, this);
        PowerUp summerResearch = new PowerUp("Do Summer Research", "Power Up", "Maybe you got published, but you definitely learned", 2, this);
        PowerUp boulderRun = new PowerUp("Boulder Run!", "Power Up", "Snow's on the ground - it's time!", 2, this);
        PowerUp studentOrgs = new PowerUp("Join a Student Org", "Power Up", "...or maybe ten", 4, this);
        PowerUp itap = new PowerUp("Join ITAP", "Power Up", "Helping people connect to campus printers for decades", 3, this);
        PowerUp ubbenLecture = new PowerUp("Hear and Ubben Lecture", "Power Up", "An hour-long speech that changes your outlook forever", 2, this);
        
        //Add power ups to reference HashMap
        refPowerUps.put(aceTest.getName(),aceTest);
        refPowerUps.put(scholarship.getName(),scholarship);
        refPowerUps.put(acePaper.getName(),acePaper);
        refPowerUps.put(tennisWinner.getName(),tennisWinner);
        refPowerUps.put(golfWinner.getName(),golfWinner);
        refPowerUps.put(raceWinner.getName(),raceWinner);
        refPowerUps.put(partyAnimal.getName(),partyAnimal);
        refPowerUps.put(studentPres.getName(),studentPres);
        refPowerUps.put(skipClass.getName(),skipClass);
        refPowerUps.put(startClub.getName(),startClub);
        refPowerUps.put(getInternship.getName(),getInternship);
        refPowerUps.put(getJob.getName(),getJob);
        refPowerUps.put(TA.getName(),TA);
        refPowerUps.put(summerResearch.getName(),summerResearch);
        refPowerUps.put(boulderRun.getName(),boulderRun);
        refPowerUps.put(studentOrgs.getName(),studentOrgs);
        refPowerUps.put(itap.getName(),itap);
        refPowerUps.put(ubbenLecture.getName(),ubbenLecture);

        //Create abilities for Students
        Ability allNighter = new Ability("All Nighter","Student Ability","No exam or assignment can't be finished in 12hrs", 4,this);
        Ability dropClass = new Ability("Drop Class", "Student Ability", "Sometimes things are too tough, and you must run", 3, this);
        Ability strokeOfGenius = new Ability("Stroke of Genius", "Student Ability", "It happened in the shower, a John Nash moment", 7, this);
        Ability studyDrugs = new Ability("Study Drugs", "Student Ability", "Its illegal and unethical, but lets face it, it happens", 3, this);
        Ability classAttendance = new Ability("Attend Class", "Student Ability", "The simplest and best way to beat college", 5, this);
        Ability donate = new Ability("Legacy Donation", "Student Ability", "You donate a building and are newly respected", 3, this);
        Ability planAhead = new Ability("Plan Ahead", "Student Ability", "The single most effective way to win college", 10, this);
        Ability pragmatism = new Ability("Pragmatism", "Student Ability", "Sometimes you have to study Friday night", 8, this);
        Ability officeHours = new Ability("Office Hours","Student Ability", "Might as well learn from the best.", 9, this);
        Ability frontRow = new Ability("Sit in Front Row", "Student Ability","Sucking up has its perks.", 6,this);
        Ability participate = new Ability("Participate in Class", "Student Ability", "You can't learn without trying", 8, this);
        Ability receiveSPAC = new Ability("Receive a SPAC", "Student Ability","Nothing like a VIP access pass to learn", 7, this);
        Ability extension = new Ability("Ask for Extension", "Student Ability", "Get some extra time to fit it all in", 4, this);
        Ability SCenter = new Ability("Visit S Center", "Student Ability", "Learn to speak like Reagan or Obama", 4, this);
        Ability QCenter = new Ability("Visit Q Center", "Student Ability", "One visit turns you into Von Neumann", 4, this);
        Ability WCenter = new Ability("Visit W Center", "Student Ability", "How Orwell edited his 1984 draft", 3, this);
        Ability smartSO = new Ability("Date Someone Smart", "Student Ability","Osmosis is a powerful thing", 10, this);
        Ability goToRoy = new Ability("Study at Roy", "Student Ability", "It took some stairs, but at last - some absolute quiet", 6, this);
        Ability winterTerm = new Ability("Stay for Winter Term", "Student Ability", "Making a half-credit step up for your next classes", 5, this);
        Ability goGreek = new Ability("Go Greek", "Student Ability", "Joining a house just multiplied your study partners", 3, this);
        Ability studyBinge = new Ability("Study Binge", "Student Ability", "You're inspired; 5 hours later, you're completely done", 7, this);
        Ability studySession = new Ability("Study Session", "Student Ability", "If two heads are better than one, how about five?", 5, this);
        Ability studyAbroad = new Ability("Study Abroad", "Student Ability", "Learn something new someplace different!", 4, this);
        
        //The special one :)
        Ability diploma = new Ability("Receive your Diploma", "Student Ability", "Years of hard work finally paid off", 15, this);

        //Add Student Abilities to Student Reference HashMap
        refStudentAbilities.put(allNighter.getName(),allNighter);
        refStudentAbilities.put(dropClass.getName(),dropClass);
        refStudentAbilities.put(strokeOfGenius.getName(),strokeOfGenius);
        refStudentAbilities.put(studyDrugs.getName(),studyDrugs);
        refStudentAbilities.put(classAttendance.getName(),classAttendance);
        refStudentAbilities.put(donate.getName(),donate);
        refStudentAbilities.put(planAhead.getName(),planAhead);
        refStudentAbilities.put(pragmatism.getName(),pragmatism);
        refStudentAbilities.put(officeHours.getName(),officeHours);
        refStudentAbilities.put(frontRow.getName(),frontRow);
        refStudentAbilities.put(participate.getName(),participate);
        refStudentAbilities.put(receiveSPAC.getName(),receiveSPAC);
        refStudentAbilities.put(extension.getName(),extension);
        refStudentAbilities.put(SCenter.getName(),SCenter);
        refStudentAbilities.put(QCenter.getName(),QCenter);
        refStudentAbilities.put(WCenter.getName(),WCenter);
        refStudentAbilities.put(smartSO.getName(),smartSO);
        refStudentAbilities.put(goToRoy.getName(),goToRoy);
        refStudentAbilities.put(winterTerm.getName(),winterTerm);
        refStudentAbilities.put(goGreek.getName(),goGreek);
        refStudentAbilities.put(studyBinge.getName(),studyBinge);
        refStudentAbilities.put(studySession.getName(),studySession);
        refStudentAbilities.put(studyAbroad.getName(),studyAbroad);
        
        //Put the special item at the end
        refStudentAbilities.put(diploma.getName(),diploma);

        //Create abilities for Professors
        Ability midtermExam = new Ability("Midterm Exam","Professor Ability","So much tougher than the review guides", 4,this);
        Ability finalExam = new Ability("Final Exam", "Professor Ability", "Three hours: feels like 3 mins, and everything's impossible", 3, this);
        Ability notCoveredInClass = new Ability("Not Covered in Class", "Professor Ability", "In the reading? Talked about in class? Nope and nope.", 6, this);
        Ability failedYou = new Ability("Failed You", "Professor Ability", "All your \"work\" to be repeated next semester", 9, this);
        Ability problemSets = new Ability("Problem Sets", "Professor Ability", "So many equations, so little time.", 2, this);
        Ability seniorSeminar = new Ability("Senior Seminar", "Professor Ability", "How could one class be so much work? #econguys", 7, this);
        Ability popQuiz = new Ability("Pop Quiz", "Professor Ability", "Weren't those supposed to stop in middle school?", 5, this);
        Ability noGradesBack = new Ability("No Grades Back", "Professor Ability", "Maybe you're doing well, maybe not. Who knows??", 2, this);
        Ability popExam = new Ability("Pop Exam","Professor Ability", "Now that is just cruel", 10, this);
        Ability trappedInClass = new Ability("Petition Rejected", "Professor Ability","You just wanted to quietly drop the class.", 10,this);
        Ability badAdvising = new Ability("Bad Advising", "Professor Ability", "Not know about an advising form? No graduation for you.", 9, this);
        Ability noSPAC = new Ability("No SPAC Granted", "Professor Ability","Please don't make me take something else", 4, this);
        Ability tangentLecture = new Ability("Going Off on Tangent", "Professor Ability", "What are we even learning about? #gametheory", 3, this);
        Ability noRecommendation = new Ability("Bad Recommendation", "Professor Ability", "What will the graduate schools think?", 7, this);
        Ability cheating = new Ability("Accused of Cheating", "Professor Ability", "You were just stretching, honestly.", 7, this);
        Ability boringClass = new Ability("Boring Class", "Professor Ability", "Those eyelids are starting to feel heavy", 4, this);
        Ability lackOfInterest = new Ability("Lack of Interest", "Professor Ability","You did get a PhD in this, didn't you?", 5, this);
        Ability zeroCredit = new Ability("Zero Credit", "Professor Ability","But it was only 10 minutes late!", 6, this);
        Ability hugePaper = new Ability("Huge Paper", "Professor Ability", "Do I have enough print balance to give you a hard copy?", 5, this);

        //Add abilities to professor ability reference table
        refProfessorAbilities.put(midtermExam.getName(),midtermExam);
        refProfessorAbilities.put(finalExam.getName(),finalExam);
        refProfessorAbilities.put(notCoveredInClass.getName(),notCoveredInClass);
        refProfessorAbilities.put(failedYou.getName(),failedYou);
        refProfessorAbilities.put(problemSets.getName(),problemSets);
        refProfessorAbilities.put(seniorSeminar.getName(),seniorSeminar);
        refProfessorAbilities.put(popQuiz.getName(),popQuiz);
        refProfessorAbilities.put(noGradesBack.getName(),noGradesBack);
        refProfessorAbilities.put(popExam.getName(),popExam);
        refProfessorAbilities.put(trappedInClass.getName(),trappedInClass);
        refProfessorAbilities.put(badAdvising.getName(),badAdvising);
        refProfessorAbilities.put(noSPAC.getName(),noSPAC);
        refProfessorAbilities.put(tangentLecture.getName(),tangentLecture);
        refProfessorAbilities.put(noRecommendation.getName(),noRecommendation);
        refProfessorAbilities.put(cheating.getName(),cheating);
        refProfessorAbilities.put(boringClass.getName(),boringClass);
        refProfessorAbilities.put(lackOfInterest.getName(),lackOfInterest);
        refProfessorAbilities.put(zeroCredit.getName(),zeroCredit);
        refProfessorAbilities.put(hugePaper.getName(),hugePaper);

        //Populate the map with random items and abilities
        setOpponentAndItemMap();

        //Add default number of items to main player
        player.addRandomItems(5,0,0,this);
        
        //Assign abilities and items for Building Bosses
        AdmissionsBoss.addRandomItems(20,0,0, this);
        RoyBoss.addRandomItems(20,0,0, this);
        HooverBoss.addRandomItems(20,0,0, this);
        PeelerBoss.addRandomItems(20,0,0, this);
        JulianBoss.addRandomItems(20,0,0, this);
        GCPABoss.addRandomItems(20,0,0, this);
        PCCMBoss.addRandomItems(20,0,0, this);
        LillyBoss.addRandomItems(20,0,0, this);

        //Initialize all images
        try
        {
            //background = ImageIO.read(new File("game_background.png"));
            background = ImageIO.read(DePauwAdventureGame.class.getResource("resources/game_background.png"));
            openingScreenBackground = ImageIO.read(DePauwAdventureGame.class.getResource("resources/opening_screen_background.png"));
            winScreenBackground = ImageIO.read(DePauwAdventureGame.class.getResource("resources/win_screen.png"));
        }

        //Catch exception if the image file cannot be read.
        catch (Exception e)
        {
            System.out.println("Error in reading background pictures.\nDePauw Adventure Game Constructor loop.");
            System.out.println(e.getMessage());
        }

        //Initialize drawing panel and base attributes.
        setDrawingPanel();
    }

    //Sets graphics to listen for key events and fit to window.
    private void setDrawingPanel()
    {
        dp = new DrawingPanel(this);
        frame = new JFrame("DePauw Adventure Game!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        Container pane = frame.getContentPane();
        pane.add(dp);
        frame.addKeyListener(dp);
        frame.setVisible(true);
    }

    //Sets the opponent and item map such that new items and opponents populate
    private  void setOpponentAndItemMap()
    {
        //Generates and places opponents and items
        generateOpponentsAndItems(numOpponentsInMap,numAbilitiesInMap,numHealthPotionsInMap,numPowerUpsInMap);

        //Give each outside Opponents abilities
        for (Point p: outsideOpponentAndItemMap.keySet()){

            if (outsideOpponentAndItemMap.get(p) instanceof OutsideOpponent)
            {
                OutsideOpponent tempOpponent = (OutsideOpponent) outsideOpponentAndItemMap.get(p);
                tempOpponent.addRandomItems(10,0,0, this);
            }
        }
    }

    //Populates map with opponents and items at game start and after a battle sequence (only student abilities collected)
    private void generateOpponentsAndItems(int pOpponentNum, int pAbilityNum, int pHealthPotionNum, int pPowerUpNum)
    {

        //Clear current outside opponent and item map
        outsideOpponentAndItemMap.clear();

        // Random generator to keep things interesting :)
        Random generator = new Random();

        // Array of the values in each array
        Object[] opponentValues = refOutsideOpponents.values().toArray();
        Object[] abilityValues = refStudentAbilities.values().toArray();
        Object[] healthPotionValues = refHealthPotions.values().toArray();
        Object[] powerUpValues = refPowerUps.values().toArray();

        // Fill map with random opponents
        for (int i = 0; i < pOpponentNum; i++) {
            OutsideOpponent randomOpponent = (OutsideOpponent) opponentValues[generator.nextInt(opponentValues.length)];
            OutsideOpponent opponentCopy = randomOpponent.copy(this);
            outsideOpponentAndItemMap.put(opponentCopy.getLocation(), opponentCopy);
        }

        // Fill map with random abilities
        for (int i = 0; i < pAbilityNum; i++) {
            Ability randomAbility;
            while(true) {
            		randomAbility = (Ability) abilityValues[generator.nextInt(abilityValues.length)];
            		if(!randomAbility.getName().equals("Receive your Diploma"))
            			break;
            }
            Ability abilityCopy = randomAbility.copy(this);
            outsideOpponentAndItemMap.put(abilityCopy.getLocation(), abilityCopy);
        }

        // Fill map with random power ups
        for (int i = 0; i < pPowerUpNum; i++) {
            PowerUp randomPowerUp = (PowerUp) powerUpValues[generator.nextInt(powerUpValues.length)];
            PowerUp powerUpCopy = randomPowerUp.copy(this);
            outsideOpponentAndItemMap.put(powerUpCopy.getLocation(), powerUpCopy);
        }

        // Fill map with random health potions
        for (int i = 0; i < pHealthPotionNum; i++) {
            HealthPotion randomHealthPotion = (HealthPotion) healthPotionValues[generator.nextInt(healthPotionValues.length)];
            HealthPotion healthPotionCopy = randomHealthPotion.copy(this);
            outsideOpponentAndItemMap.put(healthPotionCopy.getLocation(), healthPotionCopy);
        }

    }

    // Resets the game if a user wants to immediately play again.
    private void resetGame()
    {
        // Reset main player information
        player.setHealth(100);
        player.setPower(1);
        player.getHealthPotions().clear();
        player.getAbilities().clear();
        player.getPowerUps().clear();
        player.setLocation(10,10);
        player.addRandomItems(5,0,0,this);

        //reset OpponentAndItemMap
        setOpponentAndItemMap();

        //Set all buildings to not defeated and change out bosses
        for (String s: buildingMap.keySet()) {
            Building tempBuilding = buildingMap.get(s);
            BuildingBoss tempBoss = tempBuilding.getBoss().copy();
            tempBuilding.setDefeated(false);
            tempBoss.addRandomItems(20,0,0,this);
            tempBuilding.setBoss(tempBoss);

            if (tempBuilding instanceof HiddenBuilding){
                HiddenBuilding tempHiddenBuilding = (HiddenBuilding) tempBuilding;
                tempHiddenBuilding.setLocked(true);
            }
        }

        //Hide current frame, change game state, and reset drawing panel
        frame.setVisible(false);
        state = STATE.MENU;
        setDrawingPanel();
    }

    //Option pane for the instructions option in the opening menu
    private void getInstructionsJOptionPane()
    {
        JOptionPane.showMessageDialog(null, "Welcome to the DePauw Adventure Game!\n\n" +
                "Just like every incoming DePauw Student, you are placed on campus with a certain level of health\n" +
                "and a certain power level. By roaming campus in search of items (power ups, new abilities, health\n" +
                "potions, etc.), you may run in to professors hiding in the brush.\n\n" +
                "Be ready! These professors will attack you with everything from pop quizes to senior seminar\n" +
                "assignments. But never fear, you can use your abilities (all-nighter, office hours, etc.)\n" +
                "to ward them off and gain power.\n\n" +
                "When you are strong enough, you may enter different academic buildings and battle the building\n" +
                "bosses. These people will be MUCH more difficult to beat, but you can do it! Once you beat all\n" +
                "of the building bosses, you win!\n\n" +
                "If you wish, you may change your settings, health, and abilities in the \"Settings\" section\n" +
                "on the start menu. This will randomly assign abilities and power-ups to you.");
    }

    //Option pane for start game menu option. Sets name and description fields
    private int getMainPlayerInfoJOptionPane()
    {
        //Create two vertical boxes to store text and message boxes
        Box descriptionBox = Box.createVerticalBox();
        Box fieldBox = Box.createVerticalBox();

        //Initialize all text fields.
        JTextField nameField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);

        //Add the settings panel that will store all of this information.
        JPanel settingsPanel = new JPanel();

        //Add the power level option
        descriptionBox.add(Box.createRigidArea(new Dimension(0,15)));
        fieldBox.add(Box.createRigidArea(new Dimension(0,6)));
        descriptionBox.add(new JLabel("Your name (less than 15 characters): "));
        descriptionBox.add(Box.createRigidArea(new Dimension(0,14)));
        fieldBox.add(nameField);
        fieldBox.add(Box.createRigidArea(new Dimension(0,4)));

        //Add the health level option
        descriptionBox.add(new JLabel("Your description: "));
        descriptionBox.add(Box.createRigidArea(new Dimension(0,14)));
        fieldBox.add(descriptionField);
        fieldBox.add(Box.createRigidArea(new Dimension(0,4)));

        //Pack all values into settings panel
        settingsPanel.add(descriptionBox);
        settingsPanel.add(fieldBox);

        //Get result feedback from the user (OK -or- CANCEl)
        int result = JOptionPane.showConfirmDialog(null, settingsPanel,
                "Please enter your name and a personal description.", JOptionPane.OK_CANCEL_OPTION);

        //Verify that the name is short enough and update values.
        if (result == JOptionPane.OK_OPTION) {

            //Get name text input from user
            String name = nameField.getText().trim();

            if (name.length() <= 15 && name.length() > 0)
            {
                player.setName(name);
                player.setDescription((descriptionField.getText().trim()));

                //Returns success state
                return 1;
            }

            //If name field is too long.
            else
            {
                JOptionPane.showMessageDialog(null, "Please try again. Name invalid.");
                return getMainPlayerInfoJOptionPane();
            }
        }
        else {
            //Returns error state.
            return 0;
        }
    }

    //Option pane for the settings option in the opening menu.
    private void getSettingsJOptionPane()
    {
        //Create two vertical boxes to store text and message boxes
        Box descriptionBox = Box.createVerticalBox();
        Box fieldBox = Box.createVerticalBox();

        //Initialize all text fields.
        JTextField powerField = new JTextField(5);
        JTextField healthField = new JTextField(5);
        JTextField numAbilitiesField = new JTextField(5);
        JTextField numPowerUpsField = new JTextField(5);
        JTextField numHealthPotionsField = new JTextField(5);

        //Add the settings panel that will store all of this information.
        JPanel settingsPanel = new JPanel();

        //Add the power level option
        descriptionBox.add(Box.createRigidArea(new Dimension(0,15)));
        fieldBox.add(Box.createRigidArea(new Dimension(0,6)));
        descriptionBox.add(new JLabel("Power you would like to start with (1-100): "));
        descriptionBox.add(Box.createRigidArea(new Dimension(0,14)));
        fieldBox.add(powerField);
        fieldBox.add(Box.createRigidArea(new Dimension(0,4)));

        //Add the health level option
        descriptionBox.add(new JLabel("Health you would like to start with (1-100): "));
        descriptionBox.add(Box.createRigidArea(new Dimension(0,14)));
        fieldBox.add(healthField);
        fieldBox.add(Box.createRigidArea(new Dimension(0,4)));

        //Add the number of abilities option
        descriptionBox.add(new JLabel("Number of abilities you would like to start with (5 - 15): "));
        descriptionBox.add(Box.createRigidArea(new Dimension(0,14)));
        fieldBox.add(numAbilitiesField);
        fieldBox.add(Box.createRigidArea(new Dimension(0,4)));

        //Add the number of power ups option
        descriptionBox.add(new JLabel("Number of power-ups you would like to start with (0 - 5): "));
        descriptionBox.add(Box.createRigidArea(new Dimension(0,14)));
        fieldBox.add(numPowerUpsField);
        fieldBox.add(Box.createRigidArea(new Dimension(0,4)));

        //Add the number of health potions option
        descriptionBox.add(new JLabel("Number of health-potions you would like to start with (0 - 5): "));
        descriptionBox.add(Box.createRigidArea(new Dimension(0,14)));
        fieldBox.add(numHealthPotionsField);
        fieldBox.add(Box.createRigidArea(new Dimension(0,4)));

        //Pack all values into settings panel
        settingsPanel.add(descriptionBox);
        settingsPanel.add(fieldBox);

        //Get result feedback from the user (OK -or- CANCEl)
        int result = JOptionPane.showConfirmDialog(null, settingsPanel,
                "Please enter integer values; only in-range values are considered.", JOptionPane.OK_CANCEL_OPTION);

        //If the user clicks OK
        if (result == JOptionPane.OK_OPTION)
        {
            //try catch to be sure that the user adds valid input
            try
            {
                //Get all text input from user
                String sPower = powerField.getText();
                String sHealth = healthField.getText();
                String sNumAbilities = numAbilitiesField.getText();
                String sNumPowerUps = numPowerUpsField.getText();
                String sNumHealthPotions = numHealthPotionsField.getText();

                //Initialize integer values for each field (negative = invalid)
                int power = -1;
                int health = -1;
                int numAbilities = -1;
                int numPowerUps = -1;
                int numHealthPotions = -1;

                //Parse power
                if (!sPower.trim().equals(""))
                {
                    power = Integer.valueOf(sPower);
                    if (power > 0 && power <= 100){player.setPower(power);}
                    else if(power == -1){return;}
                    else{throw new Exception();}
                }

                //Parse health
                if (!sHealth.trim().equals(""))
                {
                    health = Integer.valueOf(sHealth);
                    if (health > 0 && health <= 100){player.setHealth(health);}
                    else if(health == -1){return;}
                    else{throw new Exception();}
                }

                //Parse number of abilities
                if (!sNumAbilities.trim().equals("")) {
                    int tempNumAbilities = Integer.valueOf(sNumAbilities);
                    if (tempNumAbilities >= 5 && tempNumAbilities <= 15){numAbilities = tempNumAbilities;}
                    else{throw new Exception();}
                }

                //Parse number of PowerUps
                if (!sNumPowerUps.trim().equals("")) {
                    int tempNumPowerUps = Integer.valueOf(sNumPowerUps);
                    if (tempNumPowerUps >= 0 && tempNumPowerUps <= 5){numPowerUps = tempNumPowerUps;}
                    else{throw new Exception();}
                }

                //Parse number of abilities
                if (!sNumHealthPotions.trim().equals("")) {
                    int tempNumHealthPotions = Integer.valueOf(sNumHealthPotions);
                    if (tempNumHealthPotions >= 0 && tempNumHealthPotions <= 5){numHealthPotions = tempNumHealthPotions;}
                    else{throw new Exception();}
                }

                //Add the user-specified number of items to the player. (Abilities >= 5) at start.
                player.addRandomItems(Math.max(numAbilities,5), numHealthPotions,numPowerUps,this);
            }

            //Catch if the input is invalid, and re-start the settings bar.
            catch (Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Please enter in-range integer values, or nothing.");
                getSettingsJOptionPane();
            }
        }
    }

    //Option pane for showing the abilities stored in the knapsack
    private void getAbilitiesJOptionPane()
    {

        //Add the settings panel that will store all of this information.
        JPanel abilitiesPanel = new JPanel();

        //Create two vertical boxes to store text and message boxes
        //A lot of this has to do with formatting the abilities properly
        Box leftBox = Box.createVerticalBox();
        Box rightBox = Box.createVerticalBox();
        leftBox.add(new JLabel("List of abilities with damage mult. and counts."));
        leftBox.add(new JLabel(" "));
        rightBox.add(new JLabel(" "));
        rightBox.add(new JLabel(" "));

        HashMap<String,Ability> abilityList = player.getAbilities();
        int count = 0;

        for (String key: abilityList.keySet())
        {
            if (count == 0)
            {
                leftBox.add(new JLabel(abilityList.get(key).getName() + " (" + abilityList.get(key).getDamageMultiplier() + "x):     " + abilityList.get(key).getQuantity() + "     "));
                count++;
            }

            else
            {
                rightBox.add(new JLabel(abilityList.get(key).getName() + " (" + abilityList.get(key).getDamageMultiplier() + "x):     " + abilityList.get(key).getQuantity() + "\n"));
                count--;
            }
        }

        //Keeps shown abilities visually appealing.
        if (abilityList.size() %2 ==1)
            rightBox.add(new JLabel(" "));

        //List of abilities with damage multipliers and their counts.
        abilitiesPanel.add(leftBox);
        abilitiesPanel.add(rightBox);

        //Show the message dialog for abilities
        JOptionPane.showMessageDialog(null,abilitiesPanel);
    }

    //Option pane for showing the power ups stored in the knapsack
    private void getPowerUpsJOptionPane()
    {
        //Add the settings panel that will store all of this information.
        JPanel powerUpsPanel = new JPanel();

        //Create two vertical boxes to store text and message boxes
        //A lot of this has to do with formatting the abilities properly
        Box leftBox = Box.createVerticalBox();
        Box rightBox = Box.createVerticalBox();
        leftBox.add(new JLabel("List of Power Ups with power points and counts."));
        leftBox.add(new JLabel(" "));
        rightBox.add(new JLabel(" "));
        rightBox.add(new JLabel(" "));

        //Get hash map of power ups
        HashMap<String,PowerUp> powerUpList = player.getPowerUps();
        int count = 0;

        for (String key: powerUpList.keySet())
        {
            if (count == 0)
            {
                leftBox.add(new JLabel(powerUpList.get(key).getName() + " (" + powerUpList.get(key).getPower() + "pts.):     " + powerUpList.get(key).getQuantity() + "     "));
                count++;
            }

            else
            {
                rightBox.add(new JLabel(powerUpList.get(key).getName() + " (" + powerUpList.get(key).getPower() + "pts.):     " + powerUpList.get(key).getQuantity()));
                count--;
            }
        }

        //Keeps the visualized power ups looking visually appealing
        if (powerUpList.size() % 2 ==1)
            rightBox.add(new JLabel(" "));

        //List of abilities with damage multipliers and their counts.
        powerUpsPanel.add(leftBox);
        powerUpsPanel.add(rightBox);

        //Show the message dialog for abilities
        Object[] options = { "Ok","Use Power Up"};
        int result = JOptionPane.showOptionDialog(null, powerUpsPanel, "Power Ups in Knapsack",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (result == 0){return;} //Cancel option
        else if (result == 1)
        {
            if (powerUpList.size() < 1)
            {
                //Show the message dialog for Power Ups if there are none available
                JOptionPane.showMessageDialog(null,"Sorry, you do not have any Power Ups.\nRoam around the map and find some!");
            }

            else
            {
                Object[] abilitiesList = powerUpList.keySet().toArray();

                String typeOfObject = (String) JOptionPane.showInputDialog(null,
                                "Please choose a Power Up you would like to use.\n\n",
                        "Power Ups Knapsack", JOptionPane.OK_CANCEL_OPTION, null,
                        abilitiesList, // Array of choices
                        abilitiesList[0]); // Initial choice

                if (typeOfObject != null) { player.usePowerUp(typeOfObject); }
            }
        }
    }

    //Option pane for showing the health potions stored in the knapsack
    private void getHealthPotionsJOptionPane()
    {
        //Add the settings panel that will store all of this information.
        JPanel healthPotionsPanel = new JPanel();

        //Create two vertical boxes to store text and message boxes
        //A lot of this has to do with formatting the abilities properly
        Box leftBox = Box.createVerticalBox();
        Box rightBox = Box.createVerticalBox();
        leftBox.add(new JLabel("List of Health Potions with Health Points and counts."));
        leftBox.add(new JLabel(" "));
        rightBox.add(new JLabel(" "));
        rightBox.add(new JLabel(" "));

        HashMap<String,HealthPotion> healthPotionList = player.getHealthPotions();
        int count = 0;

        for (String key: healthPotionList.keySet())
        {
            if (count == 0)
            {
                leftBox.add(new JLabel(healthPotionList.get(key).getName() + " (" + healthPotionList.get(key).getHealth() + "pts.):     " + healthPotionList.get(key).getQuantity() + "     "));
                count++;
            }

            else
            {
                rightBox.add(new JLabel(healthPotionList.get(key).getName() + " (" + healthPotionList.get(key).getHealth() + "pts.):     " + healthPotionList.get(key).getQuantity()));
                count--;
            }
        }

        //Keeps formatting looking nice.
        if (healthPotionList.size() % 2 ==1)
            rightBox.add(new JLabel(" "));

        //List of abilities with damage multipliers and their counts.
        healthPotionsPanel.add(leftBox);
        healthPotionsPanel.add(rightBox);

        //Show the message dialog for abilities
        Object[] options = { "Ok","Use Health Potion"};
        int result = JOptionPane.showOptionDialog(null, healthPotionsPanel, "Health Potions in Knapsack",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (result == 0){return;} //Cancel option
        else if (result == 1)
        {
            if (healthPotionList.size() < 1)
            {
                //Show the message dialog for Power Ups if there are none available
                JOptionPane.showMessageDialog(null,"Sorry, you do not have any Health Potions.\nRoam around the map and find some!");
            }

            else
            {
                Object[] abilitiesList = healthPotionList.keySet().toArray();

                String typeOfObject = (String) JOptionPane.showInputDialog(null,
                                "Please choose a Health Potion you would like to use.\n\n",
                        "Health Potion Knapsack", JOptionPane.OK_CANCEL_OPTION, null,
                        abilitiesList, // Array of choices
                        abilitiesList[0]); // Initial choice

                if (typeOfObject != null) { player.useHealthPotion(typeOfObject); }
            }
        }
    }

    //Option pane for getting information about a particular item (Genius Search Bar)
    private void getInformationJOptionPane() {
        String[] choices = {"Student Ability","Professor Ability", "Health Potion", "Power Up", "Outside Opponent", "Building"};
        String typeOfObject = (String) JOptionPane.showInputDialog(null, "Welcome to the information search bar!\n" +
                        "Type in the box below what you would like\n" +
                        "learn more about (e.g. name, description, type, etc.)\n\n",
                "Genius Search Bar", JOptionPane.OK_CANCEL_OPTION, null,
                choices, // Array of choices
                choices[0]); // Initial choice

        //Panel to provide additional information.
        JPanel informationPanel = new JPanel();
        Box infoBox = Box.createVerticalBox();
        JLabel message = new JLabel("What " + typeOfObject + " would you like to learn more about?");
        JLabel spacing = new JLabel(" ");
        TextField additional_info = new TextField();
        infoBox.add(message);
        infoBox.add(spacing);
        infoBox.add(additional_info);
        informationPanel.add(infoBox);


        if (typeOfObject != null) {

            switch (typeOfObject) {
                case "Student Ability":
                    Object[] studentAbilityValues = refStudentAbilities.keySet().toArray();
                    String studentAbilityName = (String) JOptionPane.showInputDialog(null, "What Student Ability would you like to learn more about?\n\n",
                            "Genius Search Bar: Student Abilities", JOptionPane.OK_CANCEL_OPTION, null,
                            studentAbilityValues, // Array of choices
                            studentAbilityValues[0]); // Initial choice
                    if (studentAbilityName != null) {
                        JOptionPane.showMessageDialog(null, refStudentAbilities.get(studentAbilityName).toString());
                    }
                    break;

                case "Professor Ability":
                    Object[] professorAbilityValues = refProfessorAbilities.keySet().toArray();
                    String professorAbilityName = (String) JOptionPane.showInputDialog(null, "What Professor Ability would you like to learn more about?\n\n",
                            "Genius Search Bar: Professor Abilities", JOptionPane.OK_CANCEL_OPTION, null,
                            professorAbilityValues, // Array of choices
                            professorAbilityValues[0]); // Initial choice
                    if (professorAbilityName != null) {
                        JOptionPane.showMessageDialog(null, refProfessorAbilities.get(professorAbilityName).toString());
                    }
                    break;

                case "Health Potion":
                    Object[] healthPotionValues = refHealthPotions.keySet().toArray();
                    String healthPotionName = (String) JOptionPane.showInputDialog(null, "What Health Potion would you like to learn more about?\n\n",
                            "Genius Search Bar: Health Potions", JOptionPane.OK_CANCEL_OPTION, null,
                            healthPotionValues, // Array of choices
                            healthPotionValues[0]); // Initial choice
                    if (healthPotionName != null) {
                        JOptionPane.showMessageDialog(null, refHealthPotions.get(healthPotionName).toString());
                    }
                    break;

                case "Power Up":
                    Object[] powerUpValues = refPowerUps.keySet().toArray();
                    String powerUpName = (String) JOptionPane.showInputDialog(null, "What Power Up would you like to learn more about?\n\n",
                            "Genius Search Bar: Power Ups", JOptionPane.OK_CANCEL_OPTION, null,
                            powerUpValues, // Array of choices
                            powerUpValues[0]); // Initial choice
                    if (powerUpName != null) {
                        JOptionPane.showMessageDialog(null, refPowerUps.get(powerUpName).toString());
                    }
                    break;

                case "Outside Opponent":
                    Object[] outsideOpponentValues = refOutsideOpponents.keySet().toArray();
                    String outsideOpponentName = (String) JOptionPane.showInputDialog(null, "Which Outside Opponent would you like to learn more about?\n\n",
                            "Genius Search Bar: Outside Opponents", JOptionPane.OK_CANCEL_OPTION, null,
                            outsideOpponentValues, // Array of choices
                            outsideOpponentValues[0]); // Initial choice
                    if (outsideOpponentName != null) {
                        JOptionPane.showMessageDialog(null, refOutsideOpponents.get(outsideOpponentName).toString());
                    }
                    break;

                case "Building":

                    Object[] buildingValues = buildingMap.keySet().toArray();
                    String buildingName = (String) JOptionPane.showInputDialog(null, "Which Building would you like to learn more about?\n\n",
                            "Genius Search Bar: Buildings", JOptionPane.OK_CANCEL_OPTION, null,
                            buildingValues, // Array of choices
                            buildingValues[0]); // Initial choice

                    if (buildingName != null) {
                        JOptionPane.showMessageDialog(null, buildingMap.get(buildingName).toString());
                    }

                    break;
            }
        }
    }

    //Drawing panel (private) subclass to implement key listener, Mouse listener, and draw all graphics.
    private class DrawingPanel extends JPanel implements KeyListener, MouseListener
    {
        //Takes the game as its sole variable
        private DePauwAdventureGame game;

        //Constructor activates Key listener and Mouse listener
        private DrawingPanel(DePauwAdventureGame pGame) {
            game = pGame;
            this.addKeyListener(this);
            this.addMouseListener(this);
        }

        //Main graphics method. Paints the DrawingPanel based on the game state.
        public void paintComponent(Graphics g) {
            //Call super class graphics to start initial painting.
            super.paintComponent(g);

            //Convert graphics input into 2D graphics
            Graphics2D g2d = (Graphics2D) g;

            //If we are at the start menu
            if (game.state == STATE.MENU)
            {
                //Information to paint the background of the screen with East College.
                Font fTitle = new Font("arial",Font.BOLD,70);
                g2d.setFont(fTitle);
                g2d.drawImage(openingScreenBackground,0,0,null);

                //Draw Title page
                g2d.setColor(new Color(0,0,0));
                g2d.fillRect(225,105,865,80);
                g2d.setColor(new Color(140,140,140));
                g2d.fillRect(230,110,855,70);
                g2d.setColor(new Color(255,255,255));
                g2d.drawString("DePauw Adventure Game",230,170);

                //Draw New Game Button
                Font fButton = new Font("arial",Font.BOLD,30);
                g2d.setFont(fButton);
                g2d.setColor(new Color(0,0,0));
                g2d.fillRect(540,255,180,50);
                g2d.setColor(new Color(140,140,140));
                g2d.fillRect(545,260,170,40);
                g2d.setColor(new Color(255,255,255));
                g2d.drawString("Start Game",549,290);

                //Draw Settings Button
                g2d.setColor(new Color(0,0,0));
                g2d.fillRect(565,355,135,50);
                g2d.setColor(new Color(140,140,140));
                g2d.fillRect(570,360,125,40);
                g2d.setColor(new Color(255,255,255));
                g2d.drawString("Settings",572,390);

                //Draw Instructions Button
                g2d.setColor(new Color(0,0,0));
                g2d.fillRect(543,455,185,50);
                g2d.setColor(new Color(140,140,140));
                g2d.fillRect(548,460,175,40);
                g2d.setColor(new Color(255,255,255));
                g2d.drawString("Instructions",550,490);

                //Draw Quit Game Button
                g2d.setColor(new Color(0,0,0));
                g2d.fillRect(550,555,170,50);
                g2d.setColor(new Color(140,140,140));
                g2d.fillRect(555,560,160,40);
                g2d.setColor(new Color(255,255,255));
                g2d.drawString("Quit Game",559,590);

            }

            //If the game is active (no longer at start menu)
            else if (game.state == STATE.GAME)
            {

                //Draw background
                g2d.drawImage(background,null,0,0);

                //Draw main player
                g2d.drawImage(player.getVisage(), null, player.getLocation().x, player.getLocation().y);

                // Size of map items; used for debugging purposes
                // System.out.println(outsideOpponentAndItemMap.size());

                if (visible)
                {

                    //Shows where all of the outside opponents and items are.
                    for (Point p: outsideOpponentAndItemMap.keySet()) {
                        Object mysteryObject = outsideOpponentAndItemMap.get(p);

                        if (mysteryObject instanceof OutsideOpponent) {
                            OutsideOpponent oo = (OutsideOpponent) mysteryObject;

                            //System.out.println(" Name: " + oo.getName() + "\t Location: " + oo.getLocation());
                            g2d.setColor(new Color(255, 0, 0));
                            g2d.fillOval(oo.getLocation().x, oo.getLocation().y, 5, 5);
                        } else if (mysteryObject instanceof HealthPotion) {
                            HealthPotion hp = (HealthPotion) mysteryObject;

                            //System.out.println(" Name: " + hp.getName() + "\t Location: " + hp.getLocation());
                            g2d.setColor(new Color(0, 255, 0));
                            g2d.fillOval(hp.getLocation().x, hp.getLocation().y, 5, 5);
                        } else if (mysteryObject instanceof PowerUp) {
                            PowerUp pu = (PowerUp) mysteryObject;

                            //System.out.println(" Name: " + pu.getName() + "\t Location: " + pu.getLocation());
                            g2d.setColor(new Color(0, 0, 255));
                            g2d.fillOval(pu.getLocation().x, pu.getLocation().y, 5, 5);
                        } else if (mysteryObject instanceof Ability) {
                            Ability ab = (Ability) mysteryObject;

                            //System.out.println(" Name: " + ab.getName() + "\t Location: " + ab.getLocation());
                            g2d.setColor(new Color(255, 255, 0));
                            g2d.fillOval(ab.getLocation().x, ab.getLocation().y, 5, 5);
                        } else {
                            System.out.println("Something is wrong here!!");
                        }
                    }
                }

                //Text to show in the right panel of the page. General information about game.
                g2d.setColor(new Color(0,0,0));
                g2d.setFont(new Font("TimesRoman", Font.BOLD, 25));
                g2d.drawString("DePauw Adventure Game", 995, 30);
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 15));
                g2d.drawString("By: Sam Showalter and Zach Wilkerson.", 995, 50);
                g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g2d.drawString("Basic Keyboard Commands:", 995, 100);
                g2d.setFont(new Font("TimesRoman", Font.BOLD, 15));
                g2d.drawString("Arrow Keys      - Move player.", 995, 120);
                g2d.drawString("P                        - Pause game.", 995, 140);
                g2d.drawString("Q                        - Quit game.", 995, 160);
                g2d.drawString("S                        - Get your statistics.", 995, 180);
                g2d.drawString("I                        - Get information.", 995, 200);
                g2d.drawString("A                        - See abilities.", 995, 220);
                g2d.drawString("U                        - See power ups.", 995, 240);
                g2d.drawString("H                        - See health potions.", 995, 260);

                //Player health and power information
                g2d.setFont(new Font("TimesRoman", Font.BOLD, 22));
                g2d.drawString("Other Information:", 995, 300);
                g2d.setFont(new Font("TimesRoman", Font.ITALIC, 18));
                g2d.drawString("Main Player: " + player.getName(),995, 330);
                g2d.setFont(new Font("TimesRoman", Font.BOLD, 16));
                g2d.drawString("Health: " + player.getHealth() + "      Power: " + player.getPower(),994,350);

                // Proximity information for Health Potions (repeated for others)
                g2d.setColor(new Color(0,0,0));
                g2d.setFont(new Font("TimesRoman", Font.ITALIC, 18));
                g2d.drawString("Proximity to Items and Opponents: ",995, 390);
                g2d.setFont(new Font("TimesRoman", Font.BOLD, 16));
                g2d.drawString("Number of steps away from...",995, 410);
                g2d.setFont(new Font("TimesRoman", Font.BOLD, 15));
                g2d.drawString("H-Potion", 1200,435);
                g2d.drawLine(1000,430,1180,430);
                g2d.drawString("8+   7   6   5   4   3   2   1   0", 1000,450);
                g2d.setColor(new Color(0,255,0));
                //Oval for distance from a health potion
                int numHPotionSteps = player.minStepsFromItem("class HealthPotion",game);
                if (numHPotionSteps == 8){g2d.fillOval(1002,425,10,10);}
                else if (numHPotionSteps == 7){g2d.fillOval(1027,425,10,10);}
                else {g2d.fillOval(1027 + ((7-numHPotionSteps) * 20),425,10,10);}


                // Proximity information for Power Ups (repeated for others)
                g2d.setColor(new Color(0,0,0));
                g2d.drawString("Power Up", 1200,485);
                g2d.drawString("8+   7   6   5   4   3   2   1   0", 1000,500);
                g2d.drawLine(1000,480,1180,480);
                g2d.setColor(new Color(0,0,255));
                //Oval for distance from a power up
                int numPowerUpSteps = player.minStepsFromItem("class PowerUp",game);
                if (numPowerUpSteps == 8){g2d.fillOval(1002,475,10,10);}
                else if (numPowerUpSteps == 7){g2d.fillOval(1027,475,10,10);}
                else {g2d.fillOval(1027 + ((7-numPowerUpSteps) * 20),475,10,10);}

                // Proximity information for Abilities (repeated for others)
                g2d.setColor(new Color(0,0,0));
                g2d.drawString("Ability", 1200,535);
                g2d.drawString("8+   7   6   5   4   3   2   1   0", 1000,550);
                g2d.drawLine(1000,530,1180,530);
                g2d.setColor(new Color(255,255,0));
                //Oval for distance from an ability
                int numAbilitySteps = player.minStepsFromItem("class Ability",game);
                if (numAbilitySteps == 8){g2d.fillOval(1002,525,10,10);}
                else if (numAbilitySteps == 7){g2d.fillOval(1027,525,10,10);}
                else {g2d.fillOval(1027 + ((7-(numAbilitySteps)) * 20),525,10,10);}

                // Proximity information for Outside Opponents (repeated for others)
                g2d.setColor(new Color(0,0,0));
                g2d.drawString("Opponent", 1200,585);
                g2d.drawString("8+   7   6   5   4   3   2   1   0", 1000,600);
                g2d.drawLine(1000,580,1180,580);
                g2d.setColor(new Color(255,0,0));
                //Oval for distance from an outside opponent
                int numOpponentSteps = player.minStepsFromItem("class OutsideOpponent",game);
                if (numOpponentSteps == 8){g2d.fillOval(1002,575,10,10);}
                else if (numOpponentSteps == 7){g2d.fillOval(1027,575,10,10);}
                else {g2d.fillOval(1027 + ((7-numOpponentSteps) * 20),575,10,10);}


                //Knapsack information
                g2d.setColor(new Color(0,0,0));
                g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g2d.drawString("          Knapsack Inventory", 995,635);

                //Draw Inventory Button
                g2d.setFont(new Font("TimesRoman",Font.BOLD,16));
                g2d.setColor(new Color(0,0,0));
                g2d.fillRect(995,650,80,24);
                g2d.setColor(new Color(140,140,140));
                g2d.fillRect(998,653,74,18);
                g2d.setColor(new Color(255,255,255));
                g2d.drawString("H-Potions",1000,667);

                //Draw Power Ups Button
                g2d.setFont(new Font("TimesRoman",Font.BOLD,16));
                g2d.setColor(new Color(0,0,0));
                g2d.fillRect(1093,650,86,24);
                g2d.setColor(new Color(140,140,140));
                g2d.fillRect(1096,653,80,18);
                g2d.setColor(new Color(255,255,255));
                g2d.drawString("Power Ups",1098,667);

                //Draw Abilities Button
                g2d.setFont(new Font("TimesRoman",Font.BOLD,16));
                g2d.setColor(new Color(0,0,0));
                g2d.fillRect(1197,650,66,24);
                g2d.setColor(new Color(140,140,140));
                g2d.fillRect(1200,653,60,18);
                g2d.setColor(new Color(255,255,255));
                g2d.drawString("Abilities",1202,667);
            }

            //Show the win screen
            else if (game.state == STATE.WIN) { g2d.drawImage(winScreenBackground,0,0,null); }
        }

        //determines if the mainplayer is ready to fight the final boss (beaten other buildings)
        public boolean readyForFinalBoss()
        {
            int defeatedCount = 0;
            //determining unlock conditions
            for(String s : getBuildingMap().keySet()) {
                Building tempBuilding = buildingMap.get(s);

                if(tempBuilding.getDefeated() && !(tempBuilding instanceof HiddenBuilding)) { defeatedCount++; }
            }

            return defeatedCount == 7;
        }

        //If someone clicks the mouse at the menu screen.
        public void mousePressed(MouseEvent e) {
            //Identify where the mouse was when clicked
            Point mousePoint = new Point(e.getX(),e.getY());

            //If we are on the start menu
            if (game.state == STATE.MENU)
            {
                //Declare four rectangles that correspond to the locations of the buttons
                Rectangle newGame = new Rectangle(550,305,720,255);
                Rectangle settings = new Rectangle(535,405,730,355);
                Rectangle instructions = new Rectangle(543,505,728,455);
                Rectangle quitGame = new Rectangle(550,605,720,555);

                //If mouse clicked on "New Game"
                if (newGame.coversPosition(mousePoint))
                {
                    //Get name and description of main player.
                    int successState = game.getMainPlayerInfoJOptionPane();

                    if (successState == 1)
                    {
                        game.state = STATE.GAME;
                        frame.setVisible(false);
                        game.setDrawingPanel();
                    }
                }

                //If mouse clicked on "Settings
                else if (settings.coversPosition(mousePoint))
                {
                    //System.out.println("Settings");
                    game.getSettingsJOptionPane();
                }

                //If mouse clicks on "Instructions"
                else if (instructions.coversPosition(mousePoint))
                {
                    //System.out.println("Instructions");
                    game.getInstructionsJOptionPane();
                }

                //If mouse clicks on "Quit Game"
                else if (quitGame.coversPosition(mousePoint))
                {
                    //System.out.println("Quit Game");
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
            }

            //If you are playing the game itself
            else if (game.state == STATE.GAME)
            {
                //Declare four rectangles that correspond to the locations of the buttons
                Rectangle HealthPotions = new Rectangle(995,674,1075,650);
                Rectangle PowerUps = new Rectangle(1093,674,1179,650);
                Rectangle Abilities = new Rectangle(1197,674,1263,650);

                //If mouse clicked on "Settings
                if (Abilities.coversPosition(mousePoint))
                {
                    //System.out.println("Settings");
                    getAbilitiesJOptionPane();
                }

                //If mouse clicks on "Instructions"
                else if (HealthPotions.coversPosition(mousePoint))
                {
                    //System.out.println("Health Potions");
                    getHealthPotionsJOptionPane();
                }

                //If mouse clicks on "Quit Game"
                else if (PowerUps.coversPosition(mousePoint))
                {
                    //System.out.println("Power Ups");
                    getPowerUpsJOptionPane();
                }
            }

            //If you have won the game
            else if (game.state == STATE.WIN)
            {
                Rectangle quitGame = new Rectangle(170,575,390,520);
                Rectangle mainMenu = new Rectangle(560, 575,780,520);

                if (quitGame.coversPosition(mousePoint))
                {
                    //System.out.println("Quit Game");
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }

                else if (mainMenu.coversPosition(mousePoint))
                {
                    //Return to main menu to play again
                    resetGame();
                }
            }
        }

        //Main method to track keyboards events and incorporate them into the game.
        public void keyPressed(KeyEvent ke) {

            //try-catch loop to check and make sure the player images are read
            try
            {
                //Reference point for where the player is (so you don't actually move the player...oops)
                Point reference = (Point)player.getLocation().clone();

                //Main engine for consuming key commands
                switch (ke.getKeyCode()) {

                    //If you wish to search the area around you
                    case KeyEvent.VK_I:
                        getInformationJOptionPane();
                        break;

                    //If you want to see the Main Player's abilities
                    case KeyEvent.VK_A:
                        getAbilitiesJOptionPane();
                        break;

                    //If you want to see the Main Player's Power Ups
                    case KeyEvent.VK_U:
                        getPowerUpsJOptionPane();
                        break;

                    //If you want to see Main Players Health Potions
                    case KeyEvent.VK_H:
                        getHealthPotionsJOptionPane();
                        break;

                    //If you want to see the Main Player's statistics
                    case KeyEvent.VK_S:
                        JOptionPane.showMessageDialog(null, " Your statistics:\n\n " + player.toString());
                        break;

                    //If you want to quit the game
                    case KeyEvent.VK_Q:
                        Object[] options = { "Cancel", "Yes" };
                        int result = JOptionPane.showOptionDialog(null, "Are you sure you want to quit?", "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, options, options[0]);
                        if (result == 1) frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                        break;

                    //If you want to pause the game
                    case KeyEvent.VK_P:
                        JOptionPane.showMessageDialog(null, "Game Paused");
                        break;

                    //If you want to move right
                    case KeyEvent.VK_RIGHT:
                        player.setVisage(ImageIO.read(DePauwAdventureGame.class.getResource("resources/sprite_right_final.png")));
                        player.move(new Point(reference.x += 5, reference.y), game);
                        //System.out.println("Right");
                        break;

                    //If you want to move left
                    case KeyEvent.VK_LEFT:
                        player.setVisage(ImageIO.read(DePauwAdventureGame.class.getResource("resources/sprite_left_final.png")));
                        player.move(new Point(reference.x -= 5, reference.y), game);
                        //System.out.println("Left");
                        break;

                    //If you want to move down
                    case KeyEvent.VK_DOWN:
                        player.setVisage(ImageIO.read(DePauwAdventureGame.class.getResource("resources/sprite_final.png")));
                        player.move(new Point(reference.x, reference.y += 5), game);
                        //System.out.println("Down");
                        break;

                    //If you want to move up
                    case KeyEvent.VK_UP:
                        player.setVisage(ImageIO.read(DePauwAdventureGame.class.getResource("resources/sprite_back_final.png")));
                        player.move(new Point(reference.x, reference.y -= 5), game);
                        //System.out.println("Up");
                        break;
                }
                //Very important! Repaints all graphics with given changes
                this.repaint();

                //For debugging purposes; tells where the main player is after each movement.
                //System.out.println("X: " + player.getLocation().x + " Y: " + player.getLocation().y);

                //If the player steps on an outside item (Opponent, power up, ability, etc.)
                if (player.onOutsideOpponentOrItem(game))
                {
                    //Get mystery object
                    Object mysteryObject = outsideOpponentAndItemMap.get(player.getLocation());

                    //If object is an outside opponent
                    if (mysteryObject instanceof OutsideOpponent)
                    {
                        OutsideOpponent newOpponent = (OutsideOpponent) mysteryObject;
                        player.outsideBattleAction(newOpponent, game);
                        outsideOpponentAndItemMap.remove(newOpponent.getLocation());
                        game.setOpponentAndItemMap();
                    }
                    else if (mysteryObject instanceof HealthPotion) {
                        HealthPotion newPotion = (HealthPotion) mysteryObject;
                        player.healthPotionAction(newPotion);
                        outsideOpponentAndItemMap.remove(newPotion.getLocation());
                    }

                    else if (mysteryObject instanceof PowerUp)
                    {
                        PowerUp newPowerUp = (PowerUp) mysteryObject;
                        player.powerUpAction(newPowerUp);
                        outsideOpponentAndItemMap.remove(newPowerUp.getLocation());
                    }

                    else if (mysteryObject instanceof Ability)
                    {
                        Ability newAbility = (Ability) mysteryObject;
                        player.abilityAction(newAbility);
                        outsideOpponentAndItemMap.remove(newAbility.getLocation());
                    }

                }

                //If the player is on the entrance of a building
                else if (player.onEntrance(game))
                {
                    player.onEntranceAction(game);

                    if (getBuildingMap().get("DPU Admissions Building").getDefeated())
                    {
                        //NEED a way to re-start the game.
                        state = STATE.WIN;
                        frame.setVisible(false);
                        setDrawingPanel();
                        frame.setSize(950,650);
                    }

                    //unlock items if the conditions are met
                    else if(readyForFinalBoss() && !player.getBuilding(player.getLocation(),game).getName().equals("DPU Admissions Building")) {
                    		HiddenBuilding finalBoss = (HiddenBuilding)(getBuildingMap().get("DPU Admissions Building"));
                    		if(finalBoss.getLocked()==true) {
                    			finalBoss.setLocked(false);
                                JOptionPane.showMessageDialog(null, "You are now ready for the final boss!\n\n" +
                                "When you are ready, go to the nothern-most building,\n" +
                                "the admissions building, to fight Mark McCoy and beat the game!\n"
                                + "You also get a special ability to help in the battle ahead!");
                                Ability diploma_ = (Ability)(getRefStudentAbilities().get("Receive your Diploma"));
                                player.addAbility(diploma_);
                        }
                    }
                    setOpponentAndItemMap();
                }
            }

            //Catch exception if player images cannot be read.
            catch (Exception e)
            {
                System.out.println("Error in reading player images.");
                e.printStackTrace();
            }
        }

        //Unused methods that must exist to correctly implement the KeyListener and MouseListener Interfaces
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void keyReleased(KeyEvent ke) {}
        public void keyTyped(KeyEvent ke) {}
    }

}


