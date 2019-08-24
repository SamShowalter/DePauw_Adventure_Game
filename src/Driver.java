import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class Driver {



	public static void main(String[] args) {

		try
		{
			//This will need to be included in the project .zip file (else it generates an error)!
			BufferedImage studentVisage = ImageIO.read(DePauwAdventureGame.class.getResource("resources/sprite_final.png"));

			//Create the main player that will be used in the game.
			MainPlayer mp = new MainPlayer("defaultName", "Student", "defaultDescription", studentVisage);

			//activate the game main loop. (visible set to true for debugging or demonstration purposes only)
			DePauwAdventureGame final_game = new DePauwAdventureGame(mp, false);
		}

		catch (Exception e)
		{
			System.out.println("Error in Main Loop.");
			e.printStackTrace();
		}
	}
}
