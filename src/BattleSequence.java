import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;


// This class is used to coordinate all battle information and actions
// Accordingly, it will also house functionality relating to communicating
// with other players and opponents.
// As of now, the functionality for text communication is not ready.
public class BattleSequence {

	// A battle is between a player and an opponent
	// The opponent can be outside or a building boss
	private Player opponent;
	private MainPlayer player;

	//Constructor to build a Battle Sequence
	public BattleSequence(MainPlayer pPlayer, Player pOpp) {
		player = pPlayer;
		opponent = pOpp;
		if (opponent instanceof OutsideOpponent) {
			opponent = (OutsideOpponent) (opponent);
		} else {
			opponent = (BuildingBoss) (opponent);
		}
	}

	//Commands to run the battle sequence itself.
	public void engage(DePauwAdventureGame pGame) {
		
		//generic "Ok" option for JOptionPanes
		String[] okOption = {"Ok"};

		//Initial condition
		if (player.getHealth() == 0) {
			JOptionPane.showMessageDialog(null, "You're out of health!\n\n"
					+ "You're in no condition to battle!\n"
					+ "Find some health potions and rest up first.");
			return;
		}

		String pName = player.getName();
		String oName = opponent.getName();

		//This could include a custom message from the opponent...?
		String[] battleBegin = {"Begin"};
		JOptionPane.showOptionDialog(null, "Hello " + opponent.getName(), "Battle",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, battleBegin, battleBegin[0]);
		int run = 0;

		//The battle itself
		while (true) {

			//default-quits if the player is out of abilities
			if (player.getAbilities().keySet().size() == 0) {
				JOptionPane.showOptionDialog(null, "You are out of Abilities!", "Battle",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, okOption, okOption[0]);
				run = 2;
				break;
			}

			//Opponent is out of abilities
			else if (opponent.getAbilities().size() == 0) {
				JOptionPane.showOptionDialog(null, opponent.getName() + " is out of abilities!",
						"Battle", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, okOption,
						okOption[0]);
				break;
			}

			//Player action
			while (true) {

				//base menu for the player
				String[] playerActionChoices = {"Use Ability", "Use H-Potion", "Use PowerUp", "Run"};
				int playerAction = JOptionPane.showOptionDialog(null,
						pName + "  vs.  " + oName + "\n\n" +
								pName + "'s Health:  " + player.getHealth() + "\n" +
								pName + "'s Power:  " + player.getPower() + "\n\n" +
								oName + "'s Health:  " + opponent.getHealth() + "\n" +
								oName + "'s Power:  " + opponent.getPower(), "Battle",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						playerActionChoices, playerActionChoices[0]);

				//runs away
				if (playerAction == 3) {
					run = 2;
					break;
				}

				//Chooses Ability
				else if (playerAction == 0) {
					if (player.getAbilities().keySet().size() > 0) {
						Object[] abilitiesList = player.getAbilities().keySet().toArray();
						String abilityChoice = (String) JOptionPane.showInputDialog(null,
								"Please choose an Ability you would like to use.\n\n",
								"Battle", JOptionPane.OK_CANCEL_OPTION, null,
								abilitiesList, // Array of choices
								abilitiesList[0]); // Initial choice
						if (abilityChoice != null) {
							player.useAbility(player.getAbilities().get(abilityChoice), opponent);
							break;
						}
					}
					else
						JOptionPane.showOptionDialog(null, "You are out of Abilities!", "Battle",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, okOption, okOption[0]);
				}

				//Chooses PowerUp
				else if (playerAction == 2) {
					if (player.getPowerUps().keySet().size() > 0) {
						Object[] powerUpsList = player.getPowerUps().keySet().toArray();
						String powerUpChoice = (String) JOptionPane.showInputDialog(null,
								"Please choose an Ability you would like to use.\n\n",
								"Battle", JOptionPane.OK_CANCEL_OPTION, null,
								powerUpsList, // Array of choices
								powerUpsList[0]); // Initial choice
						if (powerUpChoice != null) {
							player.usePowerUp(powerUpChoice);
							break;
						}
					}
					else
						JOptionPane.showOptionDialog(null, "You are out of Power Ups!", "Battle",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, okOption, okOption[0]);
				}

				//Chooses Health Potion
				else if (playerAction == 1) {
					if (player.getHealthPotions().keySet().size() > 0) {
						Object[] hPotionsList = player.getHealthPotions().keySet().toArray();
						String healthPotionChoice = (String) JOptionPane.showInputDialog(null,
								"Please choose an Ability you would like to use.\n\n",
								"Battle", JOptionPane.OK_CANCEL_OPTION, null,
								hPotionsList, // Array of choices
								hPotionsList[0]); // Initial choice
						if (healthPotionChoice != null) {
							player.useHealthPotion(healthPotionChoice);
							break;
						}
					}
					else
						JOptionPane.showOptionDialog(null, "You are out of Health Potions!", "Battle",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, okOption, okOption[0]);
				}
			}

			//Intermediate housekeeping
			if (run == 1 || run == 2) {
				break;
			} else if (opponent.getHealth() == 0) {
				break;
			}

			//The Opponent responds (weighted average so that opponents pick abilities that deal more damage more often)
			Random rnd = new Random();
			int sum = 0;
			Ability pAbility;
			ArrayList<Ability> aList = new ArrayList<Ability>();
			for (Object s : opponent.getAbilities().keySet().toArray()) {
				pAbility = opponent.getAbilities().get((String) (s));
				sum += pAbility.getDamageMultiplier();
				for (int i = 0; i < pAbility.getDamageMultiplier(); i++) {
					aList.add(pAbility);
				}
			}
			int choice = rnd.nextInt(sum);
			opponent.useAbility(aList.get(choice), player);
			JOptionPane.showOptionDialog(null, opponent.getName() + " used " + aList.get(choice).getName() + "!",
					"Battle", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, okOption, okOption[0]);

			//If the player is out of health, quit
			if (player.getHealth() == 0) {
				run = 1;
				break;
			}
		}

		//Different wrap-up scenarios
		if (run == 2) {
			JOptionPane.showOptionDialog(null, "You ran away. Time for more studying!", "Battle",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, okOption, okOption[0]);
			//No penalty for running

			//Resetting health and items (for if the opponent was a building boss)
			opponent.setHealth(100);
			if (opponent instanceof BuildingBoss) {opponent.addRandomItems(20, 0, 0, pGame); }
			else if (opponent instanceof OutsideOpponent) {opponent.addRandomItems(10, 0, 0, pGame); }
		}

		else if (run == 1) {
				JOptionPane.showOptionDialog(null, "Mwahaha, you are beaten!", "Battle",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, okOption, okOption[0]);
				if (player.getHealth() == 0) {
					JOptionPane.showOptionDialog(null, "You ran out of health!\nFind some health potions to heal yourself!", "Battle",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, okOption, okOption[0]);
				}

				//Penalty for losing the battle
				player.setPower(Math.max(player.getPower() - (int) (Math.ceil(((double) opponent.getPower()) / 20)), 1));

				//Resetting health and items (for if the opponent was a building boss)
				opponent.setHealth(100);
				if (opponent instanceof BuildingBoss) {opponent.addRandomItems(20, 0, 0, pGame); }
				else if (opponent instanceof OutsideOpponent) {opponent.addRandomItems(10, 0, 0, pGame); }
		}


			else if (run == 0) {
				//If the opponent was a building boss, you defeat that building
				if (opponent instanceof BuildingBoss) {
					BuildingBoss bossOpponent = (BuildingBoss) opponent;
					bossOpponent.getDojo().setDefeated(true);
				}
				JOptionPane.showOptionDialog(null, "Victory!", "Battle",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, okOption, okOption[0]);

				//Bonus for winning the battle
				player.setPower(Math.min(player.getPower() + (int) (Math.ceil(((double) opponent.getPower()) / 20)), 100)); }
	}
}

