//Make hidden class to lock items (hidden decorator class)
public class HiddenBuilding extends Building{

    //Locks the building
    private boolean locked;

    //Constructor for decorator
    public HiddenBuilding(Building pBuilding)
    {
        super(  pBuilding.getName(),
                pBuilding.getType(),
                pBuilding.getDescription(),
                pBuilding.getPosition().getBL().x,
                pBuilding.getPosition().getBL().y,
                pBuilding.getPosition().getTR().x,
                pBuilding.getPosition().getTR().y,
                pBuilding.getEntrance().getBL().x,
                pBuilding.getEntrance().getBL().y,
                pBuilding.getEntrance().getTR().x,
                pBuilding.getEntrance().getTR().y);

        locked = true;
    }

    //Getters
    public boolean getLocked(){return locked;}

    //Setters
    public void setLocked(boolean pLocked){locked = pLocked;}
}
