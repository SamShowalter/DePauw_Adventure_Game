import javax.swing.*;
import java.util.Random;
import java.util.HashMap;

public class Player extends GameElement {
    private String name;
    private int powerLevel;
    private int healthLevel;
    private HashMap<String, Ability> abilities;
    private HashMap<String, PowerUp> powerUps;
    private HashMap<String, HealthPotion> healthPotions;

    //Player constructor, inherits from gameElement
    public Player(String pName, String pType, String pDesc) {
        super(pName, pType, pDesc);
        powerLevel = 1;
        healthLevel = 100;
        abilities = new HashMap<String,Ability>();
        powerUps = new HashMap<String,PowerUp>();
        healthPotions = new HashMap<String,HealthPotion>();
    }

    //Getters
    public int getPower(){return powerLevel;}
    public int getHealth(){return healthLevel;}
    public HashMap<String, Ability> getAbilities(){return abilities;};
    public HashMap<String, PowerUp> getPowerUps(){return powerUps;};
    public HashMap<String, HealthPotion> getHealthPotions(){return healthPotions;}

    //Setters
    public void setPower(int pPowerLevel){powerLevel = pPowerLevel;}
    public void setHealth(int pHealthLevel){healthLevel = pHealthLevel;}
    public void setAbilities(HashMap<String, Ability> pAbilities) {abilities = pAbilities;};
    public void setPowerUps(HashMap<String, PowerUp> pPowerUps){powerUps = pPowerUps;};
    public void setHealthPotions(HashMap<String, HealthPotion> pHealthPotions){healthPotions = pHealthPotions;}

    //toString()
    public String toString()
    {
        return     "Player:\n\n" + super.toString() + "\tHealth Level: " + getHealth() +
                "\n\tPower Level: " + getPower();

    }

    //Generates random power level for a player, depending on what type it is.
    public static int generateRandomPower(Object o) {
        if (o instanceof OutsideOpponent)
        {
            return (int) (new Random().nextGaussian()*10 + 30);
        }
        else //Case it is a building boss.
        {
            return (int) (new Random().nextGaussian()*20 + 60);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////
    //Functions to add PowerUps, Health Potions, and Abilities randomly at game start.//
    ////////////////////////////////////////////////////////////////////////////////////

    public void addRandomItems(int pAbilityNum, int pHealthPotionNum, int pPowerUpNum, DePauwAdventureGame pGame)
    {
        // Random generator to keep things interesting :)
        Random generator = new Random();

        // Array of the values in each array
        Object[] studentAbilityValues = pGame.getRefStudentAbilities().values().toArray();
        Object[] professorAbilityValues = pGame.getRefProfessorAbilities().values().toArray();
        Object[] healthPotionValues = pGame.getRefHealthPotions().values().toArray();
        Object[] powerUpValues = pGame.getRefPowerUps().values().toArray();

        //Clear current outside opponent and item map
        abilities.clear();
        powerUps.clear();
        healthPotions.clear();

        // Fill opponents and main player with random abilities
            if (getClass().toString().equals("class MainPlayer"))
            {
                for (int i = 0; i < pAbilityNum; i++) {
                		Ability randomAbility;
                		while(true) {
                			randomAbility = (Ability) studentAbilityValues[generator.nextInt(studentAbilityValues.length)];
                			if(!randomAbility.getName().equals("Receive your Diploma"))
                    			break;
                		}
                    Ability abilityCopy = randomAbility.copy(pGame);
                    addAbility(abilityCopy);
                }

            }

            else if (getClass().toString().equals("class OutsideOpponent") || getClass().toString().equals("class BuildingBoss"))
            {
                for (int i = 0; i < pAbilityNum; i++) {
                		Ability randomAbility;
                		while(true) {
                			randomAbility = (Ability) professorAbilityValues[generator.nextInt(professorAbilityValues.length)];
                			if(!randomAbility.getName().equals("Receive your Diploma"))
                    			break;
                		}
                		Ability abilityCopy = randomAbility.copy( pGame);
                    addAbility(abilityCopy);
                }
            }

        // Fill map with random power ups
        for (int i = 0; i < pPowerUpNum; i++) {
            PowerUp randomPowerUp = (PowerUp) powerUpValues[generator.nextInt(powerUpValues.length)];
            PowerUp powerUpCopy = randomPowerUp.copy(pGame);
            addPowerUp(powerUpCopy);
        }

        // Fill map with random health potions
        for (int i = 0; i < pHealthPotionNum; i++) {
            HealthPotion randomHealthPotion = (HealthPotion) healthPotionValues[generator.nextInt(healthPotionValues.length)];
            HealthPotion healthPotionCopy = randomHealthPotion.copy(pGame);
            addHealthPotion(healthPotionCopy);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //Functions to add PowerUps, Health Potions, and Abilities to the players Knapsack//
    ////////////////////////////////////////////////////////////////////////////////////

    //Add power up
    public void addPowerUp(PowerUp pPowerUp)
    {
        String key = pPowerUp.getName();

        if (powerUps.containsKey(key))
        {
            PowerUp pu = powerUps.get(key);
            pu.setQuantity(pu.getQuantity() + 1);
        }
        else { powerUps.put(key,pPowerUp); }
    }

    //Add health potion
    public void addHealthPotion(HealthPotion pHealthPotion)
    {
        String key = pHealthPotion.getName();

        if (healthPotions.containsKey(key))
        {
            HealthPotion hp = healthPotions.get(key);
            hp.setQuantity(hp.getQuantity() + 1);
        }
        else {healthPotions.put(key,pHealthPotion); }
    }

    //Add ability
    public void addAbility(Ability pAbility)
    {
        String key = pAbility.getName();

        if (abilities.containsKey(key))
        {
            Ability ab = abilities.get(key);
            ab.setQuantity(ab.getQuantity() + 1);
        }
        else { abilities.put(key,pAbility); }
    }

    //////////////////////////////////////////////////////////////////////////////
    //Functions for use of PowerUps, Health Potions, and Abilities by the Player//
    //////////////////////////////////////////////////////////////////////////////

    //Use power up
    public void usePowerUp(PowerUp pPowerUp) {
        if(pPowerUp.getQuantity() > 0) {
            setPower(powerLevel + pPowerUp.getPower());
            if (pPowerUp.getQuantity() == 1){powerUps.remove(pPowerUp.getName());}
            pPowerUp.setQuantity(pPowerUp.getQuantity() - 1);
        }
        else {
            JOptionPane.showMessageDialog(null, "Error: You do not have that power up!\n" +
                    "Please try a different power up, or something else.");
            return;
        }
    }

    //Use health potion
    public void useHealthPotion(HealthPotion pPotion) {
        if(pPotion.getQuantity() > 0) {
            setHealth(Math.min((healthLevel + pPotion.getHealth()),100));
            if (pPotion.getQuantity() == 1){healthPotions.remove(pPotion.getName());}
            else{pPotion.setQuantity(pPotion.getQuantity() - 1);}
        }
        else {
            JOptionPane.showMessageDialog(null, "Error: You do not have that health potion!\n" +
                    "Please try a different health potion, or something else.");
            return;
        }
    }

    public void useAbility(Ability pAbility, Player pOpp) {
        if(pAbility.getQuantity() > 0) {
        	
        		//Initial damage system concept
            //pOpp.setHealth(Math.max(pOpp.getHealth() - (int)(powerLevel*pAbility.getDamageMultiplier()), 0));
        	
        		//New damage system concept
        		pOpp.setHealth(Math.max(pOpp.getHealth() - (int)(Math.ceil(((double)powerLevel)/10)*pAbility.getDamageMultiplier()), 0));            
            
            if(pAbility.getQuantity()==1) {abilities.remove(pAbility.getName());}
            else{pAbility.setQuantity(pAbility.getQuantity()-1);}
        }
        else{
            JOptionPane.showMessageDialog(null, "Error: You do not have that ability!\n" +
                    "Please try a different ability, or do something else.");
            return;
        }
    }

    //Use power up (String)
    public void usePowerUp(String pPowerUpName) { usePowerUp(powerUps.get(pPowerUpName)); }

    //Use health potion (String)
    public void useHealthPotion(String pPotionName) {useHealthPotion(healthPotions.get(pPotionName)); }
}
