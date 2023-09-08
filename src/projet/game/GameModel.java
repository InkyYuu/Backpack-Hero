package projet.game;

import projet.game.items.*;
import projet.game.map.RandomMap;
import projet.game.map.Room;

import java.util.ArrayList;
import java.util.Random;

import fr.umlv.zen5.KeyboardKey;
import fr.umlv.zen5.Event;
import projet.game.events.Healer;
import projet.game.events.Trader;
import projet.game.fight.*;
import projet.game.fight.bestiary.Bestiary;
import projet.game.hero.Backpack;
import projet.game.hero.Hero;

public class GameModel {

	// Data
	private final ItemsData items = new ItemsData();
	private final Hero hero = new Hero();
	private final Backpack backpack = new Backpack();
	private final ItemsInteractions interaction = new ItemsInteractions();
	private final GameScore score = new GameScore();
	
	private int actualFloor = 1;
	private RandomMap actualMap = new RandomMap(actualFloor);
	private Room heroPosition = actualMap.getRoom(actualMap.getStartPosition());
	private int eventOnPosition = heroPosition.getApparition();
	private Healer healer = null;
	private Trader trader = null;
	
	private boolean isBackpackOpen = true;
	private boolean isOrganize = true;
	
	private boolean isFighting = false;
	private final ArrayList<Bestiary> enemyInFight = new ArrayList<Bestiary>();
	private Bestiary enemySelected = null;
	private int heroTurn = 0;
	private Items confirmAttack = null;
	private Bestiary enemySpawn = null;
	private boolean isCursed = false;
	
	private boolean isSelected= false;
	private Items itemSelected = null;
	private boolean rotateItem = false;
	private boolean itemDropBackpack = false;
	private boolean itemDrop = false;
	private boolean isBegin = true;
	private int isLevelUp = 0;
	
	private boolean isScoreAdded = false;
	private boolean isWin = false;
	private boolean isSaving = false;
	private boolean hallOfFame = false;

	
	// Fonction d'initialisation
	
	public GameModel() {
		Items I1 = getItems().getItem("Wooden Sword");
		I1.setXY(600, 750);
		backpack.addItem(I1);
		Items I2 = getItems().getItem("Mouse Bow");
		I2.setXY(800, 750);
		backpack.addItem(I2);
		Items I3 = getItems().getItem("Rough Blocker");
		I3.setXY(1000, 750);
		backpack.addItem(I3);
		Items I4 = getItems().getItem("Arrow");
		I4.setXY(1200, 750);
		backpack.addItem(I4);
	}
	
    private void handleBegin(int x, int y) {
        if (260 < x && x < 660 && 600 < y && y < 1200) {
            isBegin = false;
        } else if (740 < x && x < 1140 && 700 < y && y < 1300) {
            hallOfFame = true;
            isBegin = false;
        }
    }

    private void handleHallOfFame(int x, int y) {
        if (780 < x && x < 1200 && 850 < y && y < 1100) {
            hallOfFame = false;
            isBegin = true;
        }
    }
    
    private void handleGameOver(int x, int y) {
        if (950 < x && x < 1150 && 700 < y && y < 1300) {
            System.exit(0);
        } else if (500 < x && x < 750 && 700 < y && y < 1300) {
            isSaving = true;
        }
    }
	
	private void handleSaving(int x, int y) {
	    if (950 < x && x < 1150 && 700 < y && y < 1300) {
	        System.exit(0);
	    } else if (500 < x && x < 750 && 700 < y && y < 1300) {
	        score.recordScore();
	        System.exit(0);
	    }
	}

	private void handleBackpackOpen(int x, int y) {
	    if (50 < x && x < 250 && 380 < y && y < 480 && !isFighting) {
	        isBackpackOpen = !isBackpackOpen;
	        if (isOrganize) {
	            isOrganize = false;
	        }
	    }
	}

	private void handleOrganize(int x, int y) {
	    if (isBackpackOpen && 50 < x && x < 250 && 220 < y && y < 320) {
	        isOrganize = !isOrganize;
	        if (isFighting && !isOrganize && !isCursed && hero.getEnergy() >= 3) {
	            hero.setEnergy(-3);
	        }
	    }
	}

	private void handleItemSelected(int x, int y) {
	    Items item = backpack.getItemWithCoordonates(x, y);
	    Room room = actualMap.getRoomWithCoordonates(x, y);
	    int[] cell = backpack.getCellWithCoordonates(x, y);

	    if (isBackpackOpen && isOrganize && isSelected) {
	        handleOrganizeItemSelected(cell);
	    } else if (isBackpackOpen && isOrganize && !isSelected && item != null) {
	        isSelected = true;
	        itemSelected = item;
	    } else if (isFighting) {
	        handleFightingItemSelected(item, x, y);
	    } else if (eventOnPosition == 3 && healer != null && isBackpackOpen) {
	        healer.getEffect(x, y, hero, backpack);
	    } else if (!isBackpackOpen && room != null) {
	        handleMapItemSelected(room);
	    }
	}

	private void handleOrganizeItemSelected(int[] cell) {
	    if (cell != null) {
	        if (backpack.getUIDInBackpack(cell[0], cell[1]) == -1) {
	            itemDrop = true;
	        } else {
	            itemDropBackpack = true;
	        }
	    } else {
	        itemDrop = true;
	    }
	}

	private void handleFightingItemSelected(Items item, int x, int y) {
	    if (!isSelected && item != null && heroTurn != 0) {
	        isSelected = true;
	        itemSelected = item;
	    } else if (isSelected && item != null && heroTurn != 0) {
	        if (confirmAttack == null && item == itemSelected) {
	            confirmAttack = item;
	        } else {
	            itemSelected = item;
	        }
	    } else if (50 < x && x < 250 && 380 < y && y < 480) {
	        isSelected = false;
	        itemSelected = null;
	        heroTurn = 0;
	    }
	}

	private void handleMapItemSelected(Room room) {
	    if (getActualMap().isValidPath(getActualMap().getRoomPosX(getHeroPosition()), getActualMap().getRoomPosY(getHeroPosition()), getActualMap().getRoomPosX(room), getActualMap().getRoomPosY(room))) {
	        if (heroPosition.getApparition() != 0) {
	            heroPosition.setApparition(0);
	            trader = null;
	            healer = null;
	        }
	        heroPosition = room;
	        eventOnPosition = heroPosition.getApparition();
	        if (eventOnPosition == 1) {
	            isBackpackOpen = true;
	            isFighting = true;
	            heroTurn = 1;
	            setEnemyInFight();
	        } else if (eventOnPosition == 2) {
	            chestEvent();
	        } else if (eventOnPosition == 3) {
	            HealerEvent();
	        } else if (eventOnPosition == 4) {
	            TraderEvent();
	        } else if (heroPosition.getIsFinal()) {
	            changeFloor();
	        }
	   }
	}

    /**
     * utilise la detection de clic de GameControlleur pour determiner quelle action le joueur a voulu effectuer
     * @param x
     * @param y
     */
    public void PointerAction(int x, int y) {
        if (isBegin) {
            handleBegin(x, y);
        } else if (isSaving) {
            handleSaving(x,y);
        } else if (hallOfFame) {    
            handleHallOfFame(x,y);
        } else if (hero.getHp() <= 0 || isWin) {
            handleGameOver(x, y);
        } else if (50 < x && x < 250 && 380 < y && y < 480 && !isFighting) {
            handleBackpackOpen(x, y);
        } else if (isBackpackOpen && 50 < x && x < 250 && 220 < y && y < 320) {
            handleOrganize(x, y);
        } else {
            handleItemSelected(x, y);
        }
    }

	
	/**
	 * utilise la detection de touche de GameControlleur pour determiner quelle aciton le joueur a voulu effectuer
	 * @param event
	 */
	public void KeyAction(Event event) {    
	//On clique sur une touche
		if (isSaving) {
			if (event.getKey() == KeyboardKey.LEFT || event.getKey() == KeyboardKey.UNDEFINED) {
				score.delLetter();
			} else {
				score.addLetter(event.getKey());
			}
		} else {
			if (isOrganize && isSelected) {
				if (event.getKey() == KeyboardKey.R) { //On dit qu'on souhaite une rotation de l'item
					rotateItem = true;
				}
	        } else if (isFighting) {
	        	int new_index;
				if (event.getKey() == KeyboardKey.LEFT) { //On dit qu'on souhaite changer d'ennemi ciblé
					if (!enemyInFight.isEmpty()) {
						int index = enemyInFight.indexOf(enemySelected); 
						if (index - 1 < 0) {
							new_index = enemyInFight.size()-1;
						} else {
							new_index = index -1;
						}
						enemySelected = enemyInFight.get(new_index);
					}
				} else if (event.getKey() == KeyboardKey.RIGHT) { //On dit qu'on souhaite changer d'ennemi ciblé
					if (!enemyInFight.isEmpty()) {
						int index = enemyInFight.indexOf(enemySelected); 
						if (index + 1 > enemyInFight.size()-1) {
							new_index = 0;
						} else {
							new_index = index + 1;
						}
						enemySelected = enemyInFight.get(new_index);
					}
				}
	        }
		}
	}
	
	/**
	 * boucle principale de GameModel
	 * elle detecte selon où le joueur se trouve quelle action peut-il effectuer
	 * @param x
	 * @param y
	 */
	public void Loop(int x, int y) {
	    if (isLevelUp() > 0 && !isFighting) {
	        handleLevelUp(x, y);
	    } else if (isBackpackOpen && isOrganize && hero.getEnergy() >= 3) {
	        handleBackpackOpenAndOrganize(x, y);
	    } else if (isFighting) {
	        handleFighting(x, y);
	    }

	    if (!isOrganize) {
	        cleanUpItems();
	    } else {
	    }

	    if (isBackpackOpen && isOrganize && isSelected && rotateItem) {
	        rotateSelectedItem();
	    }
	}

	private void handleLevelUp(int x, int y) {
	    int[] cell = backpack.getCellWithCoordonates(x, y);
	    if (cell != null && backpack.backpack()[cell[0]][cell[1]] == -1) {
	        backpack.unlockCell(cell[0], cell[1]);
	        isLevelUp -= 1;
	    }
	}

	/**
	 * fonction principale d'organisation du sac
	 * @param x
	 * @param y
	 */
	private void handleBackpackOpenAndOrganize(int x, int y) {
		if (isSelected) {
            backpack.removeItem(itemSelected.getUID());
            if (itemDrop) {
                itemSelected.setXY(x, y);
                itemDrop = false;
                isSelected = false;
                itemSelected = null;
            } else if (itemDropBackpack) {
            	if (eventOnPosition == 4) {
            	    if (trader.getStore().containsKey(itemSelected)) {
            	        if (getTrader().getStore().get(itemSelected) <= backpack.getGold().getAction()) {
            	            backpack.getGold().changeQuantity(-getTrader().getStore().get(itemSelected));
            	            trader.removeItemFromStore(itemSelected);
            	            int[] cell = backpack.getCellWithCoordonates(x, y);
            	            backpack.putItem(cell[0], cell[1], itemSelected.getUID());
            	            itemDropBackpack = false;
            	            isSelected = false;
            	            itemSelected = null;
            	        }
            	    }
            	} else {
                    int[] cell = backpack.getCellWithCoordonates(x, y);
                    backpack.putItem(cell[0], cell[1], itemSelected.getUID());;
                    if (isCursed) {
                    	isCursed = false;
                    	isOrganize = false;
                        isSelected = false;
                        itemSelected = null;
                    }
                    itemDropBackpack = false;
                    isSelected = false;
                    itemSelected = null;
            	}
            }
        }
	}

	/**
	 * fonction principale de combat
	 * @param x
	 * @param y
	 */
	private void handleFighting(int x, int y) {
	    if (hero.getSleep() != 0) {
	        heroTurn = 0;
	    }
	    if (enemyInFight.isEmpty()) {
	        isFighting = false;
	        flushEnemyInFight();
	    }
	    if (heroTurn != 0) {
	        if (heroTurn == 1) {
	    		hero.doPoisonAndRegen();
	            hero.resetBlock();
	            backpack.resetAllItems();
	            for (Items item : backpack.inventory()) {
	                if (item.getClass() != Manastone.class && item.getClass() != Gold.class) {
	    	    		if (hero.getWeak() != 0 && item.getClass() != Curse.class) {
	    	    			if (item.getAction()-hero.getWeak() >= 0) {
	    	    				item.setAction(-hero.getWeak());
	    	    			}
	    	    		}
	    	    		if (hero.getRage() != 0 && item.getClass() != Curse.class) {
	    	    			item.setAction(hero.getRage());
	    	    		}
	                    doPassifInteraction(item, false);
	                }
	            }
	            for (Items item : backpack.inventory()) {
	                if (item.getClass() != Manastone.class && item.getClass() != Gold.class) {
	                	if (item.getClass() != Curse.class || !isCursed) {
	                		doEachTurnInteraction(item, true);
	                	}
	                }
	            }
	    		if (hero.getSlow() != 0)  {
	    			if (hero.getPp()-hero.getSlow()>=0) {
	    				hero.setPp(-hero.getSlow());
	    			}
	    		}
	    		if (hero.getHaste() != 0) {
	    			hero.setPp(hero.getHaste());
	    		}
	        }
	        if (isSelected && confirmAttack != null) {
	            if (confirmAttack.getClass() != Manastone.class && confirmAttack.getClass() != Gold.class && confirmAttack.getClass() != Arrow.class && confirmAttack.getClass() != Magic.class) {
	            	doOnUseInteraction(confirmAttack);
	                confirmAttack = null;
	                heroTurn += 1;
	            } else {
	                confirmAttack = null;
	                heroTurn += 1;
	            }
	        } else if (50 < x && x < 250 && 300 < y && y < 400 && hero.getEnergy() >= 1) {
	            enemySelected.damageTaken(3);
	            hero.setEnergy(-1);
	        } else if (50 < x && x < 250 && 220 < y && y < 320 && isOrganize) {
	            isOrganize = false;
	        }
	        heroTurn++;
	    } else {
	        hero.affectEffects();
	        for (Bestiary b : enemyInFight) {
	            b.turn(this, hero);
	        }
	        if (enemySpawn != null) {
	            addEnemyInFight(enemySpawn);
	            enemySpawn = null;
	        }
	        heroTurn = 1;
	        hero.resetEnergy();
	        hero.resetBlock();
	        backpack.resetAllItems();
	        for (Items item : backpack.inventory()) {
	            if (item.getClass() != Manastone.class && item.getClass() != Gold.class) {
	                doPassifInteraction(item, false);
	            }
	        }
	        for (Items item : backpack.inventory()) {
	            if (item.getClass() != Manastone.class && item.getClass() != Gold.class) {
	                doEachTurnInteraction(item, false);
	            }
	        }
    		if (hero.getSlow() != 0) {
    			if (hero.getPp()-hero.getSlow()>=0) {
    				hero.setPp(-hero.getSlow());
    			}
    		}
    		if (hero.getHaste() != 0) {
    			hero.setPp(hero.getHaste());
    		}
	    }
	    clearEnemyInFight();
	}

	private void cleanUpItems() {
	    for (int UID : backpack.getAllItemNotInBackpack(this)) {
	        backpack.deleteItem(UID);
	    }
	}

	private void rotateSelectedItem() {
	    itemSelected.setSize(Items.rotateItem(itemSelected.getSize()));
	    itemSelected.switchLenght();
	    itemSelected.setRotation();
	    rotateItem = false;
	}

	// Getter Setter
	
	public Hero getHero() {
        return hero;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public int getEventOnPosition() {
        return eventOnPosition;
    }

    public RandomMap getActualMap() {
        return actualMap;
    }

    public Room getHeroPosition() {
        return heroPosition;
    }

    public boolean getIsOrganize() {
    	return isOrganize;
    }
    
    public boolean getIsFighting() {
    	return isFighting;
    }
    
    public boolean getIsSelected() {
    	return isSelected;
    }
    
    public Items getItemSelected() {
    	return itemSelected;
    }

	public ItemsData getItems() {
		return items;
	}

	public boolean getIsBegin() {
		return isBegin;
	}

	public boolean isBackpackOpen() {
		return isBackpackOpen;
	}

	public int isLevelUp() {
		if (hero.isLevelUp()) {
			isLevelUp +=  new Random().nextInt(2) + 3;
		}
		return isLevelUp;
	}

	/**
	 * création des ennemis en combat
	 * @return
	 */
	public ArrayList<Bestiary> getEnemyInFight() {
		return enemyInFight;
	}
	
	 public void setEnemyInFight() {
	        int index = (int) (Math.random() * items.getAllEnemies().size());
	        Bestiary enemy = items.getAllEnemies().get(index).copie();
	        enemyInFight.add(enemy);

	        int chance = (int) (Math.random() * 100);
	        if (chance < 90 && items.getAllEnemies().size() > 1) {
	            int secondIndex;
	            do {
	                secondIndex = (int) (Math.random() * items.getAllEnemies().size());
	            } while (secondIndex == index);

	            Bestiary secondEnemy = items.getAllEnemies().get(secondIndex).copie();
	            enemyInFight.add(secondEnemy);
	        }
	        enemySelected = enemy;
	 }
	 
	 public void flushEnemyInFight() {
	        enemyInFight.clear();
	 }
	 
	 public void addEnemyInFight(Bestiary enemySpawn2) {
		    enemyInFight.add(enemySpawn2);
	 }
		    
	 /**
	 * verification des ennemis morts dans un combat et si on gagne le combat
	 */
	public void clearEnemyInFight() {
		 if (!enemyInFight.isEmpty()) {
			Bestiary enemy = null;
	        for (Bestiary b : enemyInFight) {
	            if (!b.getIsAlive()) {
	                enemy = b;
	            }
	        }
	        if (enemy != null) {
	        	enemyInFight.remove(enemy);
	        	score.addScoreKillEnemy();
	        	hero.setXP(enemy.getXp());
	        	if (!enemyInFight.isEmpty()) {
	        		enemySelected = enemyInFight.get(0);
	        	}
	        } 
		 } else {
			isSelected = false;
			itemSelected = null;
			confirmAttack = null;
	        isBackpackOpen = true;
	        isOrganize = true;
	        heroPosition.setApparition(0);
	        heroTurn = 0;
	        dropRessources();
	        backpack.resetAllManastone();
	        hero.resetEnergy();
	        hero.resetEffect();
	        hero.resetBlock();
	        flushEnemyInFight();
	     }
	}

	public Bestiary getEnemySelected() {
		return enemySelected;
	}
	
	public void doPassifInteraction(Items item, boolean affichage) {
		interaction.passif(item, this, affichage);
	}
	
	public void doEachTurnInteraction(Items item, boolean affichage) {
		interaction.eachTurn(item, this, affichage);
	}
	
	public void doOnUseInteraction(Items item) {
		interaction.onUse(item, this);
	}

	/**
	 * apparition d'un coffre
	 */
	public void chestEvent() {
        Chest chest = new Chest();
        isBackpackOpen = true;
        isOrganize = true;
        for (Items i : chest.dropRessources(this)) {
            backpack.addItem(i);
        }
    }
	
	/**
	 * apparition d'un guérisseur
	 */
	private void HealerEvent() {
		healer = new Healer();
		isBackpackOpen = true;
	}
	
	/**
	 * apparition d'un marchand
	 */
	private void TraderEvent() {
		trader = new Trader(this);
		isBackpackOpen = true;
		isOrganize = true;
        for (Items i : trader.getStore().keySet()) {
            backpack.addItem(i);
        }
	}
	
	public Trader getTrader() {
        return trader;
    }

	/**
	 * fonction si on arrive à la fin d'un étage
	 */
	public void changeFloor() {
		actualFloor++;
		if (actualFloor == 4) {
			isWin = true;
		} else {
			actualMap = new RandomMap(actualFloor);
			heroPosition = actualMap.getRoom(actualMap.getStartPosition());
			eventOnPosition = heroPosition.getApparition();
		}
	}
	
	public int getActualFloor() {
		return actualFloor;
	}

	public int getScore() {
        return score.getScore();
    }

    public String getScoreString() {
        return score.toString();
    }

	public int getHeroTurn() {
		return heroTurn;
	}

	public Items getConfirmAttack() {
		return confirmAttack;
	}
	
	/**
	 * fonction pour generer aléatoirement des objets
	 */
	public void dropRessources() {
		var Xa = 500;
		Items item = null;
		int rd = new Random().nextInt(2,5);
		for (int x = 1; x < rd ; x++) {
			int chance = new Random().nextInt(0,10);
			if (0 <= chance && chance <= 3) {
				item = getItems().randomItemByRarety("Common");
				backpack.addItem(item);				
			} else if (4 <= chance && chance <= 6) {
				item = getItems().randomItemByRarety("Uncommon");
				backpack.addItem(item);				
			} else if (chance == 7 || chance == 8) {
				item = getItems().randomItemByRarety("Rare");
				backpack.addItem(item);				
			} else {
				item = getItems().randomItemByRarety("Legendary");
				backpack.addItem(item);
			}
			item.setXY(Xa, 750);
			Xa = item.getXY()[2] + item.getXY()[0] + 50;
		}
		int rd4 = new Random().nextInt(1,6);
		backpack.addGold(rd4);
		int rd2 = new Random().nextInt(2);
		if (rd2 == 1) {
			item = getItems().randomItemByType(Manastone.class);
			backpack.addItem(item);
			item.setXY(Xa, 750);
		}
	}

	public void setIsOrganize(boolean b) {
		this.isOrganize = b;
	}

	public void setenemySpawn(Bestiary b) {
		this.enemySpawn = b;
		
	}

	public void setCursed(boolean isCursed, Items curse) {
		if (isCursed == true) {
			isSelected = true;
			itemSelected = curse;
		}
		this.isCursed = isCursed;
	}

	public void addScore(boolean isWin) {
		score.FinalScore(hero.getMaxHp(), hero.getLevel(), actualFloor, backpack.inventory(), isWin);
	}

	public boolean isScoreAdded() {
		return isScoreAdded;
	}

	public void setScoreAdded(boolean isScoreAdded) {
		this.isScoreAdded = isScoreAdded;
	}

	public boolean isWin() {
		return isWin;
	}

	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}
	
    public GameScore getGameScore() {
    	return score;
    }

	public boolean isSaving() {
		return isSaving;
	}

	public boolean hallOfFame() {
		return hallOfFame;
	}

}