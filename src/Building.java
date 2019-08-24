import java.awt.*;

public class Building extends GameElement
{
    //Main variables to construct building
    private Rectangle position;
    private Rectangle entrance;
    private BuildingBoss boss;
    private boolean defeated;

    //Constructor for building object
    public Building(String pName,
                    String pType,
                    String pDesc,
                    int BLX, int BLY,
                    int TRX, int TRY,
                    int onPosBLX, int onPosBLY,
                    int onPosTRX, int onPosTRY)
    {
        super(pName,pType,pDesc);
        position = new Rectangle(BLX,BLY,TRX,TRY);
        entrance = new Rectangle(onPosBLX,onPosBLY,onPosTRX,onPosTRY);
        defeated = false;
    }

    //Getters
    public Rectangle getPosition(){return position;}
    public Rectangle getEntrance(){return entrance;}
    public BuildingBoss getBoss(){return boss;}
    public boolean getDefeated(){return defeated;}

    //Setters
    public void setPosition(Rectangle pPosition){position = pPosition;}
    public void setEntrance(Rectangle pEntrance){entrance = pEntrance;}
    public void setBoss(BuildingBoss pBoss){boss = pBoss;}
    public void setDefeated(boolean pDefeated){defeated = pDefeated;}

    //toString
    public String toString()
    {
        String s = super.toString();
        s += " Defeated: " + defeated + "\n\n";
        s += boss.toString();

        return s;
    }

    //If a building covers a certain point
    public boolean coversPosition(Point p)
    {
        Point bottomLeft = new Point(position.getBL());
        Point topRight = new Point(position.getTR());

        if (p.x < bottomLeft.x || p.x > topRight.x){return false;}
        else if (p.y > bottomLeft.y || p.y < topRight.y){return false;}
        else {return true;}
    }

    //If a point is on the entrance of the building
    public boolean onEntrance(Point p)
    {
        Point onPosBottomLeft = new Point(entrance.getBL());
        Point onPosTopRight = new Point(entrance.getTR());

        if (p.x < onPosBottomLeft.x || p.x > onPosTopRight.x){return false;}
        else if (p.y > onPosBottomLeft.y || p.y < onPosTopRight.y){return false;}
        else {return true;}
    }

}
