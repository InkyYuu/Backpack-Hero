package projet.game.items;

import java.util.HashMap;
import java.util.Objects;

public class Gold implements Items {
	private int UID = 0;
	private int[] coordonates = {0,0,0,0};
	private final String name;
	private final String imgPath;
	private int quantity;
	private int[][] size;
	
	public Gold(String name, String imgPath, int quantity, int[][] size) {
		this.name = Objects.requireNonNull(name);
		this.imgPath = Objects.requireNonNull(imgPath);
		this.quantity = quantity;
		this.size = size;
	}
	
	@Override
	public Gold copie() {
		return new Gold(name, imgPath, quantity, size);
	}
	
	public void changeQuantity(int Q) {
		this.quantity += Q;
	}
	
	@Override
	public int getUID() {
		return UID;
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
		return null;
	}

	@Override
	public void setUID(int UID) {
		this.UID = UID;
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
		return null;
	}
	
	@Override
	public HashMap<String, Integer> getEachTurnInteraction() {
		return null; 
	}
	
	@Override
	public HashMap<String, Integer> getPassifInteraction() {
		return null;
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
	public void setSize(int[][] matrix) {
		size = matrix;
	}

	@Override
	public String getRarety() {
		return null;
	}

	@Override
	public int getAction() {
		return quantity;
	}
	
	@Override
	public void setAction(int I) {}
	
	@Override
	public double getRotation() {
		return 0.0;
	}

	@Override
	public void setRotation() {
	}

	@Override
	public void switchLenght() {
		int temp = coordonates[2];
		coordonates[2] = coordonates[3];
		coordonates[3] = temp;
	}
	
	@Override
	public void resetAction() {}
}
