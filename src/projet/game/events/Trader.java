package projet.game.events;

import java.util.HashMap;

import projet.game.GameModel;
import projet.game.items.*;

public class Trader implements Events{
	private final String imgPath = "data/Pockets_Win.gif";
	private final HashMap<Items, Integer> store;
	
	/**
	 * on genère le stock du marchand
	 * @param gamedata
	 */
	public Trader(GameModel gamedata) {
		var store = new HashMap<Items, Integer>();
		for (int c = 1; c < 5; c++) {
			store.put(gamedata.getItems().randomItemByRarety("Common"),6);
		}
		for (int u = 1; u < 3; u++) {
			store.put(gamedata.getItems().randomItemByRarety("Uncommon"),12);
		}
		for (int r = 1; r < 3; r++) {
			store.put(gamedata.getItems().randomItemByRarety("Rare"),20);
		}
		for (int l = 1; l < 2; l++) {
			store.put(gamedata.getItems().randomItemByRarety("Legendary"),35);
		}
		int x = 600;
		int y = 550;
		int count = 0;

		for (Items i : store.keySet()) {
		    i.setXY(x, y);
		    x = i.getXY()[2] + i.getXY()[0] + 50;
		    count++;

		    if (count % 5 == 0) {
		        y += 200;
		        x = 600; 
		    }
		}

		this.store = store;
	}

	public HashMap<Items, Integer> getStore() {
		return store;
	}
	
	/**
	 * enlève l'objet du magasin (il ne coute plus d'argent)
	 * @param item
	 */
	public void removeItemFromStore(Items item) {
	    if (store.containsKey(item)) {
	        int quantity = store.get(item);
	        if (quantity > 1) {
	            store.put(item, quantity - 1);
	        } else {
	            store.remove(item);
	        }
	    }
	}

	public String getImgPath() {
		return imgPath;
	}

}
