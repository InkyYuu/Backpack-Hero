package projet.game.fight;

import java.util.ArrayList;
import java.util.Random;

import projet.game.GameModel;
import projet.game.items.Items;
import projet.game.items.Manastone;

public class Chest{
	private final String imgPath;
	private int[] coordonates = {0,0,0,0};
	private final String description;
	
	public Chest() {
		this.imgPath = "data/Chest.png";
		this.description = "A simple chest";
	}	
	
	/**
	 * génere la liste des récompenses du coffre
	 * @param gamedata
	 * @return
	 */
	public ArrayList<Items> dropRessources(GameModel gamedata) {
		ArrayList<Items> ressources = new ArrayList<Items>();
		for (int x = 1; x <= new Random().nextInt(2,5); x++) {
			int chance = new Random().nextInt(0,10);
			if (0 <= chance && chance <= 3) {
				ressources.add(gamedata.getItems().randomItemByRarety("Common"));
			} else if (4 <= chance && chance <= 6) {
				ressources.add(gamedata.getItems().randomItemByRarety("Uncommon"));
			} else if (chance == 7 || chance == 8) {
				ressources.add(gamedata.getItems().randomItemByRarety("Rare"));
			} else {
				ressources.add(gamedata.getItems().randomItemByRarety("Legendary"));
			}
			
		}
		int rd2 = new Random().nextInt(2);
		if ( rd2 == 1) {
			ressources.add(gamedata.getItems().randomItemByType(Manastone.class));
		}
		int x = 200;
		for (Items i : ressources) {
			i.setXY(x, 750);
			x = i.getXY()[2] + i.getXY()[0] + 50;
		}

		return ressources;
	}
	
	public int[] getXY() {
		return coordonates;
	}
	
	public void setXY(int X, int Y) {
		this.coordonates[0] = X;
		this.coordonates[1] = Y;
		this.coordonates[2]= 90*50; //hauteur image
		this.coordonates[3] = 90*50; // longueur image
	}

	public String getImgPath() {
		return imgPath;
	}

	public String getDescription() {
		return description;
	}


}
