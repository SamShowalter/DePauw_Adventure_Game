public class PowerUp extends Item {

	private int powerValue;
	
	public PowerUp(String pName, String pType, String pDesc, int pPower, DePauwAdventureGame pGame) {
		super(pName, pType, pDesc, pGame);
		powerValue = pPower;
	}

	//Getters
	public int getPower() {return powerValue;}

	//Setters
	public void setPower(int newPower) {powerValue = newPower;}

	//Copy
	public PowerUp copy(DePauwAdventureGame pGame) { return new PowerUp(getName(),getType(),getDescription(),getPower(), pGame); }

	//toString
	public String toString()
	{
		String s = super.toString().replace("Item:","Power Up:");
		s += " Power Value: " + powerValue + "\n";
		return s;
	}
}
