public class Ability extends Item {

	private int damageMultiplier;
	
	public Ability(String pName, String pType, String pDesc, int pMult, DePauwAdventureGame pGame) {
		super(pName, pType, pDesc,pGame);
		damageMultiplier = pMult;
	}

	//Getters
	public int getDamageMultiplier() {return damageMultiplier;}

	//Setters
	public void setDamageMultiplier(int newMult) {damageMultiplier = newMult;}

	//Copy
	public Ability copy(DePauwAdventureGame pGame) { return new Ability(getName(),getType(),getDescription(),getDamageMultiplier(), pGame); }

	//toString
	public String toString()
	{
		String s = super.toString().replace("Item:","Ability:");
		s += " Damage Multiplier: " + damageMultiplier + "x\n";
		return s;
	}

}

