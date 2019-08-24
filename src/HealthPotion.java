
public class HealthPotion extends Item {
	
	private int healthValue;
	
	public HealthPotion(String pName, String pType, String pDesc, int pHealth, DePauwAdventureGame pGame) {
		super(pName, pType, pDesc, pGame);
		healthValue = pHealth;
	}

	//Getters
	public int getHealth() {return healthValue;}

	//Setters
	public void setHealth(int newHealth) {healthValue = newHealth;}

	//Copy
	public HealthPotion copy(DePauwAdventureGame pGame) { return new HealthPotion(getName(),getType(),getDescription(),getHealth(), pGame); }

	//toString
	public String toString()
	{
		String s = super.toString().replace("Item:","Health Potion:");
		s += " Health Value: " + healthValue + "\n";
		return s;
	}


}
