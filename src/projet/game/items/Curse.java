package projet.game.items;

import java.util.HashMap;
import java.util.Objects;

public class Curse implements Items {
	private int UID = 0;
	private int[] coordonates = {0,0,0,0};
	private final String name;
	private final HashMap<String, Integer> passifInteraction;
	private final HashMap<String, Integer> eachTurnInteraction;
	private final HashMap<String, Integer> onUseInteraction;
	private final String imgPath;
	private int modificateur = 0;
	private int[][] size;
	private final String description;
	private double rotation = 0;
	
	
	public Curse(String name, String imgPath,HashMap<String, Integer> passif, HashMap<String, Integer> eachTurn, HashMap<String, Integer> onUse, int[][] size, String description) {
		this.name = Objects.requireNonNull(name);
		this.imgPath = Objects.requireNonNull(imgPath);
		this.passifInteraction = passif;
		this.eachTurnInteraction = eachTurn;
		this.onUseInteraction = onUse;
		this.size = size;
		this.description = description;
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
	public void switchLenght() {}


	@Override
	public void setSize(int[][] matrix) {}


	@Override
	public String getDescription() {
		return description;
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
	public int getAction() {
		return modificateur;
	}
	
	@Override
	public void setAction(int I) {
		modificateur += I;
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
		return null;
	}


	@Override
	public int[][] getSize() {
		return size;
	}


	@Override
	public String getRarety() {
		return "Curses";
	}


	@Override
	public double getRotation() {
		return rotation;
	}


	@Override
	public void setRotation() {
		rotation = 0;
	}


	@Override
	public Items copie() {
		return new Curse(name, imgPath, passifInteraction, eachTurnInteraction, onUseInteraction, size, description);
	}
	
	@Override
	public void resetAction() {
		modificateur = 0;
	}
}
