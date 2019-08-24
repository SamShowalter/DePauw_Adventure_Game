public class Item extends GameElement
{
    //private boolean activeLocation;
    private int quantity;

    public Item(String pName, String pType, String pDesc, DePauwAdventureGame pGame)
    {
        super(pName, pType, pDesc);
        quantity = 1;
        setLocation(generateRandomPosition(pGame));
    }

    //Getters
    public int getQuantity() {return quantity;}

    //Setters
    public void setQuantity(int newQuantity) {quantity = newQuantity;}

    //toString
    public String toString()
    {
        String s = " Item:\n\n" + super.toString() +
                   " Quantity: " + quantity + "\n";
        return s;
    }
}
