public class OutsideOpponent extends Player
{

    public OutsideOpponent(String pName, String pType, String pDesc, DePauwAdventureGame pGame)
    {
        super(pName, pType, pDesc);
        setLocation(generateRandomPosition(pGame));
        setPower(generateRandomPower(this));
    }

    //Copy Outside Opponent
    public OutsideOpponent copy(DePauwAdventureGame pGame) { return new OutsideOpponent(getName(), getType(), getDescription(),pGame); }

    //toString
    public String toString()
    {
        return super.toString().replace("Player:", " Outside Opponent:");
    }

}
