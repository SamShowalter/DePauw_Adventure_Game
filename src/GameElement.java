import java.awt.Point;
import java.util.HashMap;
import java.util.Random;

public class GameElement {

    // General variables found in all game elements
    // (but not necessarily used)
	private String name;
	private String type;
	private String description;
	private Point location;

	//Constructor
	public GameElement(String pName, String pType, String pDesc) {
		name = pName;
		type = pType;
		description = pDesc;
		location = new Point(0,0);
	}

	//Getters
	public String getName() {return name;}
	public String getType() {return type;}
	public String getDescription() {return description;}
    public Point getLocation(){return location;}

    //Setters
	public void setName(String pName) {name=pName;}
	public void setType(String pType) {type=pType;}
	public void setDescription(String pDesc) {description=pDesc;}
	public void setLocation(int pX, int pY){location.x = pX; location.y = pY;}
	public void setLocation(Point pPoint){location = pPoint;}

	//toString() (location not included)
	public String toString() {
		String ret =
			"\tName: " + name + "\n" +
			"\tType: " + type + "\n" +
			"\tDescription: " + description + "\n";
		return ret;
	}

	//If a player can move to a certain new position
	public static boolean canMove(Point newPos, DePauwAdventureGame game) {

        HashMap<String,Building> buildingMap = game.getBuildingMap();

        for (String s : buildingMap.keySet()) {
            if (buildingMap.get(s).coversPosition(newPos))
                return false;
        }
        return true;
    }

    //If a point is on the entrance of any building (static version)
    public static boolean onEntrance(Point position, DePauwAdventureGame game)
    {
        HashMap<String,Building> buildingMap = game.getBuildingMap();

        for (String s : buildingMap.keySet()) { if (buildingMap.get(s).onEntrance(position)){return true;} }
        return false;
    }

    //If a payer is on the entrance of any building (Main Player version)
    public  boolean onEntrance(DePauwAdventureGame game)
    {
        // test if newPos puts one snake on another
        HashMap<String,Building> buildingMap = game.getBuildingMap();

        for (String s : buildingMap.keySet()) { if (buildingMap.get(s).onEntrance(getLocation())){return true;} }
        return false;
    }

    //Generates a random position that is not on a building or its entrance
    public static Point generateRandomPosition(DePauwAdventureGame game) {
        while (true) {
            HashMap<Point, Object> reference = game.getOutsideOpponentAndItemMap();

            int upperXRange = game.getMapEnd() / 5 - 1;
            int upperYRange = game.getBoardHeight() / 5 - 1;

            Point potential = new Point(((new Random().nextInt(upperXRange)) + 1) * 5, ((new Random().nextInt(upperYRange)) + 1) * 5);

            if (!reference.containsKey(potential) && canMove(potential, game) && !onEntrance(potential,game)) {
                return potential;
            }
        }
    }
}
