package projet.game.hero;

import java.util.ArrayList;

import projet.game.GameModel;
import projet.game.items.*;

public class Backpack {
	private int UID = 1;
	private int[][] backpack;
	private ArrayList<Items> inventory;
	
	public Backpack() {
		this.backpack = new int[][] {{-1,-1,-1,-1,-1,-1,-1},{-1,0,0,0,0,0,-1},{-1,0,0,0,0,0,-1},{-1,0,0,0,0,0,-1},{-1,-1,-1,-1,-1,-1,-1}};
		this.inventory = new ArrayList<Items>();
	}
	
	public int[][] backpack() {
		return backpack;
	}
	
	public ArrayList<Items> inventory(){
		return inventory;
	}
	
	public int getBottomRow() {
	    int lastRowIndex = -1;
	    for (int i = 0; i < backpack.length; i++) {
	        for (int j = 0; j < backpack[0].length; j++) {
	            if (backpack[i][j] != -1) {
	                lastRowIndex = i;
	            }
	        }
	    }
	    return lastRowIndex;
	}
	
	public int getTopRow() {
		for (int i = 0; i < backpack.length; i++) {
			for (int j = 0; j < backpack[0].length; j++) {
				if (backpack[i][j] != -1) {
					return i;
				}
			} 
		}
		return -1;
	}
	
	public int getUIDInBackpack(int I, int J) {
		return backpack[J][I];
	}
	
	public int[] getCellWithCoordonates(int x, int y) {
		int I = (int)((x - (1920/2-(backpack[0].length*90)/2))/90);
		int J = (int)((y - 50) / 90);
		if (I < 0 || I > 6 || J < 0 || J > 4){
			return null;
		}
		return new int[] {I,J};
	}
	
	public Items getItemWithUID(int UID) {
		for (Items item : inventory) {
			if (item.getUID() == UID) {
				return item;
			}
		}
		return null;
	}
	
	public ArrayList<int[]> getCellOfItem (int UID) {
		ArrayList<int[]> coordinates = new ArrayList<>();
        int numRows = backpack.length;
        int numCols = backpack[0].length;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (backpack[i][j] == UID) {
                    int[] coordinate = {i, j};
                    coordinates.add(coordinate);
                }
            }
        }
        return coordinates;
	}
	
	public int getUIDWithCoordonates(int x, int y) {
		for (Items item : inventory) {
			int[] XY = item.getXY();
			if (XY[0] < x && x < XY[0] + XY[3] && XY[1] < y && y < XY[1] + XY[2]) {
				return item.getUID();
			}
		}
		return 0;
	}
	
	public Items getItemWithCoordonates(int x, int y) {
        for (Items item : inventory) {
            int[] XY = item.getXY();
            int j = (x - XY[0]) / 80;
            int i = (y - XY[1]) / 80;
            if (XY[0] < x && x < XY[0] + XY[3] && XY[1] < y && y < XY[1] + XY[2]) {
            	if (item.getSize()[i][j] != 0) {
            		return item;
            	}
            }
        }
        return null;
    }
	
	/**
	 * supprime de l'écran
	 * @param UID
	 */
	public void deleteItem(int UID) {
        inventory.remove(getItemWithUID(UID));
        this.removeItem(UID);
    }
	
	/**
	 * ajoute sur l'écran mais pas dans le sac
	 * @param item
	 */
	public void addItem(Items item) {
		item.setUID(UID);
		UID += 1;
		inventory.add(item);
	}
	
	/**
	 * vérifie le passif des objets qui ont besoin d'un emplacement spécial
	 * @param UID
	 * @param verif
	 * @return
	 */
	public boolean verifPassif(int UID, ArrayList<int[]> verif) {
		if (getItemWithUID(UID).getPassifInteraction().containsKey("Float")) {
			if (verif.get(0)[0]-1 < 0) {
				return true;		
			} else if (backpack[verif.get(0)[0]-1][verif.get(0)[1]] == -1) {
				return true;
			} else {
				return false;
			}
		} else if (getItemWithUID(UID).getPassifInteraction().containsKey("Heavy")) {
			if (verif.get(0)[0]+1 > backpack.length) {
				return true;		
			} else if (backpack[verif.get(0)[0]+1][verif.get(0)[1]] == -1) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * ajoute une malédiction au sac
	 * @param I
	 * @param J
	 * @param UID
	 */
	public void putCurse(int I, int J, int UID) {
		var size = this.getItemWithUID(UID).getSize();
        var verif = new ArrayList<int[]>();
        var items = new ArrayList<Integer>();
        for (int i = 0; i < size.length; i++) {
            for (int j = 0; j < size[0].length; j++) {
                if (J+i >= 0 && J+i <= 4 && I+j >= 0 && I+j <= 6) {
                    if (backpack[J+i][I+j] != -1 && backpack[J+i][I+j] != UID) {
                        int[] xy = {i+J,j+I};
                        verif.add(xy);
                        if (backpack[J+i][I+j] != 0 && backpack[J+i][I+j] != UID) {
                        	items.add(backpack[J+i][I+j]);
                        }
                    }
                }
            }
        }
        if (verif.size() == size.length * size[0].length) {
        	for (int allUID : items) {
        		if (getItemWithUID(allUID).getClass() == Curse.class) {
        			return;
        		}
        	}
        	for (int deleteUID : items) {
        		deleteItem(deleteUID);
        	}
        	for (int[] particulars : verif) {
                backpack[particulars[0]][particulars[1]] = UID;
                this.getItemWithUID(UID).setXY(I*90+(1920/2-(backpack[0].length*90)/2)+5, J*90+10+50);
            }
        }
	}
	
	/**
	 * ajoute un item au sac
	 * @param I
	 * @param J
	 * @param UID
	 */
	public void putItem(int I, int J, int UID) {
		if (getItemWithUID(UID).getClass() == Curse.class) {
			putCurse(I, J, UID);
			return;
		}
        var size = this.getItemWithUID(UID).getSize();
        var verif = new ArrayList<int[]>();
        for (int i = 0; i < size.length; i++) {
            for (int j = 0; j < size[0].length; j++) {
                if (J+i >= 0 && J+i <= 4 && I+j >= 0 && I+j <= 6) {
                    if (size[i][j] == 1 && backpack[J+i][I+j] == 0) {
                        int[] xy = {i+J,j+I};
                        verif.add(xy);
                    } else if (size[i][j] == 0 && backpack[J+i][I+j] != -1) {
                        int[] xy = {-1,-1};
                        verif.add(xy);
                    }
                }
            }
        }
        if (verif.size() == size.length * size[0].length) {
        	if (getItemWithUID(UID).getClass() != Manastone.class && getItemWithUID(UID).getClass() != Gold.class) {
	        	if (verifPassif(UID, verif)) {
		            for (int[] particulars : verif) {
		                if (particulars[0] != -1 && particulars[1] != -1) {
		                    backpack[particulars[0]][particulars[1]] = UID;
		                }
		                this.getItemWithUID(UID).setXY(I*90+(1920/2-(backpack[0].length*90)/2)+5, J*90+10+50);
		            }
	        	} else {
	        		this.getItemWithUID(UID).setXY(350,250);
	        	}
        	} else {
	            for (int[] particulars : verif) {
	                if (particulars[0] != -1 && particulars[1] != -1) {
	                    backpack[particulars[0]][particulars[1]] = UID;
	                }
	                this.getItemWithUID(UID).setXY(I*90+(1920/2-(backpack[0].length*90)/2)+5, J*90+10+50);
	            }
        	}
        }
    }
	 
	public void removeItem(int UID) {
		for (int i = 0; i < backpack.length; i++) {
			for (int j = 0; j < backpack[0].length; j++) {
				if (backpack[i][j] == UID) {
					backpack[i][j] = 0;
				} 
			}
		}
	}
	
	public String toString() {
		var string = new StringBuilder("Sac à dos\n");
		for (int i = 0; i < backpack.length; i++) {
			for (int j = 0; j < backpack[0].length; j++) {
				string.append(backpack[i][j]+" ");
			}
			string.append("\n");
		}
		string.append("Taille : "+inventory.size()+"\n");
		return string.toString();
	}
	
	public ArrayList<Items> getInventory() {
		return inventory;
	}
	
	/**
	 * renvoi tous les items qui ne sont pas dans le sac
	 * @param data
	 * @return
	 */
	public ArrayList<Integer> getAllItemNotInBackpack(GameModel data) {
		ArrayList<Integer> AllUID = new ArrayList<Integer>();
		for (int i = 0; i < backpack.length; i++) {
			for (int j = 0; j < backpack[0].length; j++) {
				if (backpack[i][j] != -1 && backpack[i][j] != 0) {
					AllUID.add(backpack[i][j]);
				}
			}
		}
		ArrayList<Integer> UID = new ArrayList<Integer>();
		for (Items item : inventory) {
			if (!AllUID.contains(item.getUID())) {
				if (item.getClass() == Curse.class) {
					data.doPassifInteraction(item, true);
				}
				UID.add(item.getUID());
			}
		}
		return UID;
	}

	public void unlockCell(int I, int J) {
		if (getUIDInBackpack(I, J) == -1) {
			backpack[J][I] = 0;
		}
	}
	
	public void resetAllManastone() {
        for (Items item : inventory) {
            if (item instanceof Manastone) {
                ((Manastone) item).resetManastone();
            }
        }
	}
	
	public void resetAllItems() {
        for (Items item : inventory) {
            item.resetAction();
        }
	}
	
	public Gold getGold() {
        for (Items item : inventory) {
            if (item.getClass() == Gold.class) {
                return (Gold) item;
            }
        }
        return null;
    }

    /**
     * ajoute de l'or au sac
     * @param quantity
     */
    public void addGold(int quantity) {
            Gold item = getGold();
            if (item != null) {
                (item).changeQuantity(quantity);
                if (item.getAction() <= 0) {
                	deleteItem(item.getUID());
                }
            } else {
            	Gold gold = new Gold("Gold", "Gold.png", quantity, new int[][] {{1}});
            	gold.setXY(800, 500);
                addItem(gold);
            }
    }
}
