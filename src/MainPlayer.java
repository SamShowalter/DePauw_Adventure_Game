import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class MainPlayer extends Player
{
    //The picture for the player
    private BufferedImage visage;

    //Constructor for Main Player
    public MainPlayer(String pName, String pType, String pDesc, BufferedImage pVisage)
    {
        super(pName, pType, pDesc);
        visage = pVisage;
        setLocation(10,10);
    }

    //Getters
    public BufferedImage getVisage()
    {
        return visage;
    }

    //Setters
    public void setVisage(BufferedImage pVisage)
    {
        visage = pVisage;
    }

    //toString
    public String toString()
    {
        String s = super.toString().replace("Player:", "Main Player: ");
        s += "\n\tX: " + getLocation().x + " Y: " + getLocation().y + "\n";
        return s;
    }

    //Get the closest Item in steps away (helper from distance from item)
    public int getMinSteps(HashMap<Point,Object> pItems,String itemClass)
    {
        int minSteps = 1000;    //arbitrarily high number to start
        int xLocation = getLocation().x;
        int yLocation = getLocation().y;

        for (Point p: pItems.keySet()) {
            if (pItems.get(p).getClass().toString().equals(itemClass)) {
                int itemX = p.x;
                int itemY = p.y;

                int numSteps = (Math.abs(itemX - xLocation) + Math.abs(itemY - yLocation)) / 5;
                if (numSteps < minSteps) {
                    minSteps = numSteps;
                }
            }
        }

        return minSteps;
    }

    //Distance player is from an item
    public int minStepsFromItem(String itemClass, DePauwAdventureGame pGame)
    {
        int minSteps = getMinSteps(pGame.getOutsideOpponentAndItemMap(),itemClass);

        if (minSteps >= 8)
            return 8;

        else if (minSteps < 0)
        {
            System.out.println("ERROR: fatal error finding minimum steps to an item");
        }

        return minSteps;

    }


    //If a player is on an item that is outside
    public boolean onOutsideOpponentOrItem(DePauwAdventureGame game)
    {
        HashMap<Point,Object> reference = game.getOutsideOpponentAndItemMap();
        return reference.containsKey(getLocation());
    }

    //If a player is on the edge of the board.
    private boolean onEdge(Point newPos, DePauwAdventureGame game)
    {
        return (((newPos.x <= 0) || (newPos.x >= game.getMapEnd())) || (newPos.y <= 0) || (newPos.y >= game.getBoardHeight()));
    }

    //Returns the building that a player is on the entrance of
    public Building getBuilding(Point position, DePauwAdventureGame game)
    {
        HashMap<String,Building> buildingMap = game.getBuildingMap();

        for (String s : buildingMap.keySet()) {if (buildingMap.get(s).onEntrance(position)) return buildingMap.get(s); }
        return null;
    }

    //Action for a power up
    public void powerUpAction(PowerUp pu)
    {
        Object[] options = { "Store","Use", "Leave" };
        int result = JOptionPane.showOptionDialog(null, " You have stumbled upon something!\n\n" + pu.toString() + "\n What do you want to do with this Power Up?", "Power Up Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (result == 0) {addPowerUp(pu); }
        else if(result == 1){usePowerUp(pu);}
        else if(result == 2){return;}
    }

    //Action for a health potion
    public void healthPotionAction(HealthPotion hp)
    {
        Object[] options = { "Store","Use", "Leave" };
        int result = JOptionPane.showOptionDialog(null, " You have stumbled upon something!\n\n" +hp.toString() + "\n What do you want to do with this Health Potion?", "Health Potion Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (result == 0) {addHealthPotion(hp); }
        else if(result == 1){useHealthPotion(hp);}
        else if(result == 2){return;}
    }

    //Action for an Ability
    public void abilityAction(Ability ab)
    {
        Object[] options = { "Store", "Leave" };
        int result = JOptionPane.showOptionDialog(null, " You have stumbled upon something!\n\n" +ab.toString() + "\n What do you want to do with this Ability?", "Ability Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (result == 0) {addAbility(ab); }
        else if(result == 1){return;}
    }

    //Action for an ourside opponent
    public void outsideBattleAction(OutsideOpponent oo, DePauwAdventureGame pGame)
    {
        Object[] options = { "Run", "Fight"};
        int result = JOptionPane.showOptionDialog(null, " You ran into an opponent!\n\n" +oo.toString() + "\n What do you want to do?", "Outside Battle Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[1]);

        if(result == 1){
        		BattleSequence battle = new BattleSequence(this, oo);
        		battle.engage(pGame);
        }
    }

    //Action if a player is on the entrance of a building
    public void onEntranceAction(DePauwAdventureGame pGame)
    {
        //Get building they are on the entrance of
        Building pBuilding = getBuilding(getLocation(), pGame);

        //Options for the Option pane to show
        String[] options = { "Fight", "Run" };

        int result = JOptionPane.showOptionDialog(null, "You have reached the entrance of a building!\n" +
                        "Here is some information about it:\n\n"
                        + pBuilding.toString() + "\n\n Do you want to go inside and battle?",
                "Notice", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        //If someone chooses to fight.
        if(result == 0) {

            if (pBuilding instanceof HiddenBuilding) {
                HiddenBuilding hBuilding = (HiddenBuilding) pBuilding;
                if (hBuilding.getLocked()) {
                    JOptionPane.showMessageDialog(null, "You have not yet unlocked this building!\n\n" +
                            "To unlock it, please beat all of the other buildings\nand building bosses first.");
                    return;
                }

            }

            else if (pBuilding.getDefeated()) {
                String pBossName = pBuilding.getBoss().getName();

                JOptionPane.showMessageDialog(null, "You have already defeated " + pBossName +
                        ":\n\n" + "Aside from stealing " + pBossName + "'s lunch money,\n" +
                        "there is nothing left for you to do.");
                return;
            }

            //Begin the battle 
            BattleSequence battle = new BattleSequence(this, pBuilding.getBoss());
            battle.engage(pGame);

        }

    }

    //Move the player, given that they cannot go through buildings and should wrap around the other side of the screen, if necessary.
    public void move(Point proposedLocation, DePauwAdventureGame game)
    {
        //System.out.println(onEdge(proposedLocation, game));
        if (onEdge(proposedLocation, game))
        {
            if (getLocation().x <= 5) setLocation(game.getMapEnd() - 5,getLocation().y);
            else if (getLocation().y <=5) setLocation(getLocation().x,game.getBoardHeight() - 5);
            else if (getLocation().x >= game.getMapEnd()- 5) setLocation(5,getLocation().y);
            else if (getLocation().y >= game.getBoardHeight() - 5) setLocation(getLocation().x,5);
        }

        else
        {
            if (canMove(proposedLocation, game)) {setLocation(proposedLocation); }
            else {return;}
        }
    }




}
