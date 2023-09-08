package projet.game.items;

import projet.game.GameModel;
import projet.game.fight.bestiary.Bestiary;
import projet.game.hero.Backpack;
import projet.game.hero.Hero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ItemsInteractions {
	
	public ItemsInteractions() {}
	
	/**
	 * calcul les passifs des items
	 * @param item
	 * @param data
	 * @param damage
	 */
	public void passif (Items item, GameModel data, boolean damage) {
		Backpack bp = data.getBackpack();
		boolean disabled = false;
		HashMap<String,Integer> passifs = item.getPassifInteraction();
		for (Map.Entry<String,Integer> map : passifs.entrySet()) {
			if (!item.getPassifInteraction().containsKey("Disabled")) {
				if (map.getKey().equals("Top")) {
					if(bp.getCellOfItem(item.getUID()).get(0)[0] != bp.getTopRow()) {
						disabled = true;
						break;
					}
				}
				
				if (map.getKey().length() > 3) {
					if (map.getKey().substring(0,4).equals("Give")) {
						Set<Integer> UIDList = null;
						if (map.getKey().substring(4).equals("Adjacent"))  {
							UIDList = itemAdjacent(item, data, item.getClass());
						} else if (map.getKey().substring(4).equals("Diagonale")) {
							UIDList = itemDiagonale(item, data, item.getClass());
						} else if (map.getKey().substring(4).equals("Around")) {
							UIDList = itemAround(item, data, item.getClass());
						}
						if (UIDList != null) {
							for (Integer UID : UIDList) {
								bp.getItemWithUID(UID).setAction(map.getValue());
							}
						}
					}
						
					if (map.getKey().substring(0,4).equals("Gain")) {
						Set<Integer> UIDList = null;
						if (map.getKey().substring(4).equals("Adjacent"))  {
							UIDList = itemAdjacent(item, data, item.getClass());
						} else if (map.getKey().substring(4).equals("Diagonale")) {
							UIDList = itemDiagonale(item, data, item.getClass());
						} else if (map.getKey().substring(4).equals("Around")) {
							UIDList = itemAround(item, data, item.getClass());
						}
						if (UIDList != null) {
							item.setAction(UIDList.size() * map.getValue());
						}
					}
				}
				
				if (map.getKey().equals("Up") || map.getKey().equals("Down") || map.getKey().equals("Left") || map.getKey().equals("Right")) {
					item.setAction(findSpaceDirection(item, data,  map.getKey())* map.getValue());
				}
				
				if (map.getKey().equals("Direction")) {
					item.setAction(map.getValue()*findEmptyDirection(item, data, getDirection(item.getRotation())));
				}
				
				if (map.getKey().equals("GiveArrow")) {
					Set<Integer> UIDList = itemInDirection(item, data, getDirection(item.getRotation()), Arrow.class);
					if (UIDList != null) {
						for (Integer UID : UIDList) {
							bp.getItemWithUID(UID).setAction(map.getValue());
						}
					}
				}
				
				if (map.getKey().equals("Hurt") && damage) {
					data.getHero().damageTaken(map.getValue());
				}
				
				if (map.getKey().equals("onUse")) {
					data.doOnUseInteraction(item);
				}

			}
		}	
		if (disabled) {
			if (bp.getCellOfItem(item.getUID()).get(0)[0] != bp.getTopRow()) {
				passifs.put("Disabled",0);
			} else if (bp.getCellOfItem(item.getUID()).get(0)[0]  == bp.getTopRow() && passifs.containsKey("Disabled")) {
				passifs.remove("Disabled");
			}
		}
	}
	
	/**
	 * actions des items chaque tours après les passifs
	 * @param item
	 * @param data
	 * @param damage
	 */
	public void eachTurn (Items item, GameModel data, boolean damage) {
		Hero hero = data.getHero();
		
		HashMap<String,Integer> eachTurns = item.getEachTurnInteraction();
		if (!item.getPassifInteraction().containsKey("Disabled")) {
			for (Map.Entry<String,Integer> map : eachTurns.entrySet()) {
				
				if (map.getKey().equals("Hurt") && damage) {
					hero.damageTaken(map.getValue());
				}
				
				if (map.getKey().equals("SlowSelf") && damage) {
					hero.setSlow(map.getValue() + hero.getSlow());
				}
				
				if (map.getKey().equals("Block")) {
					hero.protect(map.getValue() + item.getAction());
				}
			}
		}
	}
	
	/**
	 * verifie si on peut utiliser l'objet
	 * @param item
	 * @param data
	 * @return
	 */
	public boolean validCost (Items item, GameModel data) {
		boolean valid = false;
		Hero hero = data.getHero();
		Backpack backpack = data.getBackpack();
		Set<Items> mana = null;
		Gold gold = null;
		
		if (item.getClass() == Curse.class) {
			if (hero.getEnergy() >= 1) {
				hero.setEnergy(-1);
				return true;
			} else {
				return false;
			}
		}
		
		if (item.getCost().get("Energy") == 0 && item.getCost().get("Gold") == 0 && item.getCost().get("Mana") == 0) {
			return true;
		}
		
		for (Map.Entry<String,Integer> cost : item.getCost().entrySet()) {
			if (cost.getKey().equals("Energy") && cost.getValue() != 0) {
				valid = hero.getEnergy()-cost.getValue() >= 0;
			} else if (cost.getKey().equals("Mana") && cost.getValue() != 0) {
				mana = findMana(item, data, new HashSet<Items>(), new HashSet<Integer>());
				if (!mana.isEmpty()) {
					int totalMana = 0;
					for (Items manastone : mana) {
						totalMana += manastone.getAction();
					}
					valid = totalMana-cost.getValue() >= 0;
				}
			} else if (cost.getKey().equals("Gold") && cost.getValue() != 0) {
				gold = backpack.getGold();
				if (gold != null) {
					valid = cost.getValue()-gold.getAction() >= 0;
				}
			}
		}
		
		if (valid) {
			hero.setEnergy(-item.getCost().get("Energy"));
			if (mana != null) {
				if (!mana.isEmpty()) {
					int totalMana = item.getCost().get("Mana");
					for (Items manastone : mana) {
						if (manastone.getAction()-totalMana >= 0) {
							((Manastone) manastone).changeQuantity(-totalMana);
							totalMana = 0;
						} else if (manastone.getAction()-totalMana < 0) {
							totalMana -= manastone.getAction();
							((Manastone) manastone).changeQuantity(-manastone.getAction());
						}
					}
				}
			}
			if (gold != null) {
				backpack.addGold(-item.getCost().get("Gold"));
			}
		}
		return valid;
	}
	
	/**
	 * action principale des items
	 * @param item
	 * @param data
	 */
	public void onUse (Items item, GameModel data) {
		Hero hero = data.getHero();
		Bestiary enemy = data.getEnemySelected();
		ArrayList<Bestiary> allEnemy = data.getEnemyInFight();
		
		HashMap<String,Integer> onUse = item.getOnUseInteraction();
		if (!item.getPassifInteraction().containsKey("Disabled")) {
				if (validCost(item, data) ) {
					
					if (onUse.containsKey("Destroy")) {
						data.getBackpack().deleteItem(item.getUID());					
					}
					
					if (onUse.containsKey("Damage")) {
						enemy.damageTaken(onUse.get("Damage") + item.getAction());
					}
					
					if (onUse.containsKey("FiresArrow")) {
						Set<Integer> UIDList = itemInDirection(item, data, getDirection(item.getRotation()), Arrow.class);
						if (!UIDList.isEmpty()) {
							for (Integer UID : UIDList) {
								data.doOnUseInteraction(data.getBackpack().getItemWithUID(UID));
							}
						} else {
							hero.setEnergy(1);
						}
					}
					
					if (onUse.containsKey("TurnDamage")) {
						item.setAction(onUse.get("TurnDamage"));
					}
					
					if (onUse.containsKey("Block")) {
						hero.protect(onUse.get("Block") + item.getAction());
					}
					
					if (onUse.containsKey("DamageAll")) {
						for (Bestiary E : allEnemy) {
							E.damageTaken(onUse.get("DamageAll") + item.getAction());
						}
					}
					
					if (onUse.containsKey("Vampirism")) {
						if (enemy.getProtection() == 0) {
							hero.healHimself(onUse.get("Vampirism"));
						}
						enemy.damageTaken(onUse.get("Vampirism"));
					}
					
					if (onUse.containsKey("Poison")) {
						if (item.getClass() == Arrow.class) {
							enemy.setPoison(onUse.get("Poison") + item.getAction());
						} else {
							enemy.setPoison(onUse.get("Poison"));
						}
					}
					
					if (onUse.containsKey("DamageOnPoison")) {
						enemy.damageTaken(hero.getPoison());
					}
					
					if (onUse.containsKey("PoisonSelf")) {
						hero.setPoison(hero.getPoison() + onUse.get("PoisonSelf"));
					}
						
					if (onUse.containsKey("Burn")) {
						if (item.getClass() == Arrow.class) {
							enemy.setBurn(onUse.get("Burn") + item.getAction());
						} else {
							enemy.setBurn(onUse.get("Burn"));
						}
					}
					
					if (onUse.containsKey("BurnSelf")) {
						hero.setBurn(hero.getBurn() + onUse.get("BurnSelf"));
					}
					
					if (onUse.containsKey("Slow")) {
						enemy.setSlow(onUse.get("Slow"));
					}
					
					if (onUse.containsKey("Weak")) {
						enemy.setWeak(onUse.get("Weak"));
					}
				}
		}
	}
	
    public String getDirection(double angle) {
        double normalizedAngle = angle % 360;

        if (normalizedAngle == 0) {
            return "Right";
        } else if (normalizedAngle == 90) {
            return "Down";
        } else if (normalizedAngle == 180) {
            return "Left";
        } else {
            return "Up";
        }
    }
    
    public Set<Items> findMana(Items item, GameModel data, Set<Items> manaAdjacent, Set<Integer> visitedCells) {
        for (int[] coordinate : data.getBackpack().getCellOfItem(item.getUID())) {
            int i = coordinate[0];
            int j = coordinate[1];
            int[][] directions = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

            for (int[] dir : directions) {
                int row = i + dir[0];
                int col = j + dir[1];

                if (row >= 0 && row < data.getBackpack().backpack().length && col >= 0 && col < data.getBackpack().backpack()[0].length) {
                    int cellValue = data.getBackpack().backpack()[row][col];
                    if (!visitedCells.contains(cellValue)) {
                        visitedCells.add(cellValue);
                        Items itemAdjacent = data.getBackpack().getItemWithUID(cellValue);
                        if (itemAdjacent != null) {
                            if (itemAdjacent.getUID() != item.getUID() && itemAdjacent.getClass().equals(Manastone.class)) {
                                manaAdjacent.add(itemAdjacent);
                            } else if (itemAdjacent.getUID() != item.getUID() && itemAdjacent.getPassifInteraction() != null && itemAdjacent.getPassifInteraction().containsKey("Conductive")) {
                                findMana(itemAdjacent, data, manaAdjacent, visitedCells);
                            }
                        }
                    }
                }
            }
        }
        return manaAdjacent;
    }
	
	public Set<Integer> itemAdjacent(Items item, GameModel data, Class<?> classe) {
		Set<Integer> UIDAdjacent = new HashSet<Integer>();
		 for (int[] coordinate : data.getBackpack().getCellOfItem(item.getUID())) {
	            int i = coordinate[0];
	            int j = coordinate[1];
					int[][] directions = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
			        
			        for (int[] dir : directions) {
			            int row = i + dir[0];
			            int col = j + dir[1];
			           
				        if (row >= 0 && row < data.getBackpack().backpack().length && col >= 0 && col < data.getBackpack().backpack()[0].length) {
				        	Items itemAdjacent = data.getBackpack().getItemWithUID(data.getBackpack().backpack()[row][col]);
				        	if (itemAdjacent != null) {
					        	if (itemAdjacent.getUID() != item.getUID() && itemAdjacent.getClass().equals(classe)) {
					        		UIDAdjacent.add(itemAdjacent.getUID());
					        	}
				        	}
				        }
			        }
				}
		return UIDAdjacent;
	}
	
	public Set<Integer> itemDiagonale(Items item, GameModel data, Class<?> classe) {
		Set<Integer> UIDDiagonale = new HashSet<Integer>();
		 for (int[] coordinate : data.getBackpack().getCellOfItem(item.getUID())) {
	            int i = coordinate[0];
	            int j = coordinate[1];
					int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
			        
			        for (int[] dir : directions) {
			            int row = i + dir[0];
			            int col = j + dir[1];
			           
				        if (row >= 0 && row < data.getBackpack().backpack().length && col >= 0 && col < data.getBackpack().backpack()[0].length) {
				        	Items itemDiagonale= data.getBackpack().getItemWithUID(data.getBackpack().backpack()[row][col]);
				        	if (itemDiagonale != null) {
					        	if (itemDiagonale.getUID() != item.getUID() && itemDiagonale.getClass().equals(classe)) {
					        		UIDDiagonale.add(itemDiagonale.getUID());
					        	}
				        	}
				        }
			        }
		}
		return UIDDiagonale;
	}
	
	/**
	 * renvoi la liste des items autour d'un item
	 * @param item
	 * @param data
	 * @param classe
	 * @return
	 */
	public Set<Integer> itemAround(Items item, GameModel data, Class<?> classe) {
		Set<Integer> UIDAdjacent = itemAdjacent(item, data, classe);
		Set<Integer> UIDDiagonale = itemDiagonale(item, data, classe);
	    
	    Set<Integer> uniqueUIDs = new HashSet<>();
	    uniqueUIDs.addAll(UIDAdjacent);
	    uniqueUIDs.addAll(UIDDiagonale);
	    
	    return uniqueUIDs;
	}
	
	 public Set<Integer> itemInDirection(Items item, GameModel data, String direction, Class<?> classe) {
	        Backpack backpack = data.getBackpack();
			Set<Integer> UIDDirection = new HashSet<Integer>();

			 for (int[] coordinate : backpack.getCellOfItem(item.getUID())) {
		            int row = coordinate[0];
		            int col = coordinate[1];
	
		            if (direction.equals("Up") && row-1 >= 0 && row-1 < backpack.backpack().length) {
		            	Items itemDirection = data.getBackpack().getItemWithUID(backpack.backpack()[row-1][col]);
		            	if (itemDirection != null) {
				            if (itemDirection.getClass().equals(classe)) {
				            	UIDDirection.add(itemDirection.getUID());
				            }
		            	}
		            } else if (direction.equals("Down") && row+1 >= 0 && row+1 < backpack.backpack().length) {
		            	Items itemDirection = data.getBackpack().getItemWithUID(backpack.backpack()[row+1][col]);
		            	if (itemDirection != null) {
				            if (itemDirection.getClass().equals(classe)) {
				            	UIDDirection.add(itemDirection.getUID());
				            }
		            	}
		            } else if (direction.equals("Left") && col-1 >= 0 && col-1 < backpack.backpack()[0].length) {
		            	Items itemDirection = data.getBackpack().getItemWithUID(backpack.backpack()[row][col-1]);
		            	if (itemDirection != null) {
				            if (itemDirection.getClass().equals(classe)) {
				            	UIDDirection.add(itemDirection.getUID());
				            }
		            	}
		            } else if (direction.equals("Right") && col+1 >= 0 && col+1 < backpack.backpack()[0].length) {
		            	Items itemDirection = data.getBackpack().getItemWithUID(backpack.backpack()[row][col+1]);
		            	if (itemDirection != null) {
				            if (itemDirection.getClass().equals(classe)) {
				            	UIDDirection.add(itemDirection.getUID());
				            }
		            	}
		            }
			 }
			 return UIDDirection;		
	 }
	
	 /**
	  * permet de savoir le nombre de cases de l'inventaire vide dans une direction donnée
	 * @param item
	 * @param data
	 * @param direction
	 * @return
	 */
	public int findEmptyDirection(Items item, GameModel data, String direction) {
	        int[][] backpack = data.getBackpack().backpack();
	        int maxCount = 0;

			 for (int[] coordinate : data.getBackpack().getCellOfItem(item.getUID())) {
		            int row = coordinate[0];
		            int col = coordinate[1];
		            int count = 0;
	
		            if (direction.equals("Up")) {
			            for (int i = row - 1; i >= 0; i--) {
			                if (backpack[i][col] != -1 && backpack[i][col] == 0) {
			                    count++;
			                } else {
			                    break;
			                }
			            }
		            } else if (direction.equals("Down")) {
		 	 	        int numRows = backpack.length;
			            for (int i = row + 1; i < numRows; i++) {
			                if (backpack[i][col] != -1 && backpack[i][col] == 0) {
			                	count++;
			                } else {
			                    break;
			                }
			            }
		            } else if (direction.equals("Left")) {
		                for (int j = col - 1; j >= 0; j--) {
		                    if (backpack[row][j] != -1 && backpack[row][j] == 0) {
		                    	count++;
		                    } else {
		                        break;
		                    }
		                }
		            } else if (direction.equals("Right")) {
		                int numCols = backpack[0].length;
		                for (int j = col + 1; j < numCols; j++) {
		                    if (backpack[row][j] != -1 && backpack[row][j] == 0) {
		                    	count++;
		                    } else {
		                        break;
		                    }
		                }

		            }
		
			        if (count > maxCount) {
			             maxCount = count;
			        }	   
	        }
	        return maxCount;
	 }
	

	public int findSpaceDirection(Items item, GameModel data, String direction) {
	        int[][] backpack = data.getBackpack().backpack();
	        int maxCount = 0;

			 for (int[] coordinate : data.getBackpack().getCellOfItem(item.getUID())) {
		            int row = coordinate[0];
		            int col = coordinate[1];
		            int count = 0;
	
		            if (direction.equals("Up")) {
			            for (int i = row - 1; i >= 0; i--) {
			                if (backpack[i][col] != -1) {
			                	if (backpack[i][col] != item.getUID()) {
			                    	count++;
			                	}
			                } else {
			                    break;
			                }
			            }
		            } else if (direction.equals("Down")) {
		 	 	        int numRows = backpack.length;
			            for (int i = row + 1; i < numRows; i++) {
			                if (backpack[i][col] != -1) {
			                	if (backpack[i][col] != item.getUID()) {
			                		count++;
			                	}
			                } else {
			                    break;
			                }
			            }
		            } else if (direction.equals("Left")) {
		                for (int j = col - 1; j >= 0; j--) {
		                    if (backpack[row][j] != -1) {
		                    	if (backpack[row][j] != item.getUID()) {
		                    		count++;
		                    	}
		                    } else {
		                        break;
		                    }
		                }
		            } else if (direction.equals("Right")) {
		                int numCols = backpack[0].length;
		                for (int j = col + 1; j < numCols; j++) {
		                    if (backpack[row][j] != -1) {
		                    	if (backpack[row][j] != item.getUID()) {
		                    		count++;
		                    	}
		                    } else {
		                        break;
		                    }
		                }

		            }
		
			        if (count > maxCount) {
			             maxCount = count;
			        }	   
	        }
	        return maxCount;
	 }
	 	
}