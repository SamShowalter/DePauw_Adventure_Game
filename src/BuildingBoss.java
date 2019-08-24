public class BuildingBoss extends Player
{
    private Building dojo;

    public BuildingBoss(String pName, String pType, String pDesc, Building pDojo)
    {
        super(pName, pType, pDesc);
        dojo = pDojo;
        dojo.setBoss(this);
        setPower(generateRandomPower(this));
    }

    //Getters
    public Building getDojo(){return dojo;}

    //Setters
    public void setDojo(Building pDojo){dojo = pDojo;}

    //Copy Building Boss
    public BuildingBoss copy() { return new BuildingBoss(getName(), getType(), getDescription(), getDojo()); }

    //toString
    public String toString()
    {
        return super.toString().replace("Player:"," Building Boss:");
    }
}
