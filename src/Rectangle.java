import java.awt.*;

public class Rectangle
{
    //Bottom Left and Top Right points
    private Point bottomLeft;
    private Point topRight;

    public Rectangle(int BLX, int BLY, int TRX, int TRY)
    {
        bottomLeft = new Point(BLX,BLY);
        topRight = new Point(TRX,TRY);
    }

    //Getters
    public Point getBL(){return bottomLeft;}
    public Point getTR(){return topRight;}

    //Setters
    public void setBL(Point p){bottomLeft = p;}
    public void setTR(Point p){topRight = p;}

    //If a Rectangle covers a certain point (for generated buttons only)
    public boolean coversPosition(Point p)
    {
        if (p.x < bottomLeft.x || p.x > topRight.x){return false;}
        else if (p.y > bottomLeft.y || p.y < topRight.y){return false;}
        else {return true;}
    }

}
