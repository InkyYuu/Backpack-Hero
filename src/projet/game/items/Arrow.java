package projet.game.items;

import java.util.HashMap;
import java.util.Objects;

public class Arrow implements Weapons {
	private int UID = 0;
	private int[] coordonates = {0,0,0,0};
	private final String name;
	private final String imgPath;
	private final HashMap<String, Integer> passifInteraction;
	private final HashMap<String, Integer> eachTurnInteraction;
	private final HashMap<String, Integer> onUseInteraction;
	private int modificateur = 0;
	private int[][] size;
	private final String rarety;
	private final HashMap<String,Integer> cost;
	private final String description;
	private double rotation = 0;
	
	
	public Arrow(String name, String imgPath,  HashMap<String, Integer> passif, HashMap<String, Integer> eachTurn, HashMap<String, Integer> onUse, int[][] size, int energy, int gold, int mana, String rarety, String description) {
		this.name = Objects.requireNonNull(name);
		this.imgPath = Objects.requireNonNull(imgPath);
		this.passifInteraction = passif;
		this.eachTurnInteraction = eachTurn;
		this.onUseInteraction = onUse;
		this.size = size;
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		map.put("Energy", energy);
		map.put("Gold", gold);
		map.put("Mana", mana);
		this.cost = map;
		this.rarety = rarety;
		this.description = description;
	}
	
	/**
	 * renvoi une copie de l'item
	 */
	@Override
	public Arrow copie() {
		return new Arrow(name, imgPath, passifInteraction, eachTurnInteraction, onUseInteraction, size, cost.get("Energy"), cost.get("Gold"), cost.get("Mana"), rarety, description);
	}

	@Override
	public int getUID() {
		return UID;
	}
	
	@Override
	public void setUID(int UID) {
		this.UID = UID;
	}
	
	@Override
	public int[] getXY() {
		return coordonates;
	}
	
	@Override
	public void setXY(int X, int Y) {
		this.coordonates[0] = X;
		this.coordonates[1] = Y;
		this.coordonates[2]= 80*this.getSize().length;
		this.coordonates[3] = 80*this.getSize()[0].length;
	}
	
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public int getAction() {
		return modificateur;
	}
	
	@Override
	public void setAction(int I) {
		modificateur += I;
	}


	@Override
	public String getName() {
		return name;
	}


	@Override
	public String getImgPath() {
		return imgPath;
	}
	
	@Override
	public HashMap<String, Integer> getOnUseInteraction() {
		return onUseInteraction;
	}
	
	@Override
	public HashMap<String, Integer> getEachTurnInteraction() {
		return eachTurnInteraction; 
	}
	
	@Override
	public HashMap<String, Integer> getPassifInteraction() {
		return passifInteraction;
	}

	@Override
	public HashMap<String, Integer> getCost() {
		return cost;
	}


	@Override
	public int[][] getSize() {
		return size;
	}
	
	@Override
	public void setSize(int[][] matrix) {
		size = matrix;
	}

	@Override
	public String getRarety() {
		return rarety;
	}
	
	@Override
	public double getRotation() {
		return rotation;
	}

	@Override
	public void setRotation() {
		rotation += 90.0;
	}
	
	@Override
	public void switchLenght() {
		int temp = coordonates[2];
		coordonates[2] = coordonates[3];
		coordonates[3] = temp;
	}

	@Override
	public void resetAction() {
		modificateur = 0;
	}
}
