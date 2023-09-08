package projet.game.fight.bestiary;

import java.util.HashMap;
import java.util.Random;

import projet.game.GameModel;
import projet.game.fight.Effects;
import projet.game.hero.Hero;
import projet.game.items.Curse;
import projet.game.items.Items;

public class QueenBee implements Bestiary,Effects{
	private final String imgPath;
	private int[] coordonates = {0,0,0,0};
	private int health;
	private int protection;
	private final int maxHealth;
	private boolean isAlive;
	private final int xp;
	private final int[] dmg;
	private HashMap<String,Integer> nextAction;
	private final String description;
	
	//effets sur les actions
	int haste = 0; // + shield
	int rage = 0 ; // + attack
	int slow = 0; //- shield
	int weak = 0 ; //-attack
	
	//effets dégats
	int poison = 0;
	int burn = 0;
	int freeze = 0;
	int regen = 0;
	
	//effets autres
	boolean zombie = false;
	int charm = 0;
	int dodge = 0;
	int rough_side = 0;
	int sleep = 0;
	int spike = 0;
	
	
	public QueenBee(int health) {
		this.imgPath = "Queen_Bee.png";
		this.health = health;
		this.protection = 0;
		this.maxHealth = 74;
		this.isAlive = true;
		this.xp = 20;
		this.dmg = new int[]{15, 15};
		this.nextAction = prediction();
		this.description = "The queen bee commands the armies!";
	}
	
	@Override
	public String getImgPath() {
		return this.imgPath;
	}

	@Override
	public boolean getIsAlive() {
		return isAlive;
	}
	
	@Override
	public int getProtection() {
		return protection;
	}
	
	public void poison(Hero hero,int poison) {
		hero.setPoison(hero.getPoison() + poison);
	}
	
	public void summon(GameModel gamedata) {
		if (gamedata.getEnemyInFight().size() < 2) {
			gamedata.setenemySpawn(new LilBee(16));
		}
	}
	
	public void cursed(GameModel gamedata) {
		Items curse = gamedata.getItems().randomItemByType(Curse.class);
		curse.setXY(400,250);
		gamedata.getBackpack().addItem(curse);
		gamedata.setIsOrganize(true);
		gamedata.setCursed(true, curse);
	}
	
	public int attack(int damage) {
		return (damage + rage - weak);
	}
	
	public void resetProtection() {
		this.protection = 0;
	}
	
	@Override
	public HashMap<String, Integer> prediction() {
	    Random random = new Random();
	    int action = random.nextInt(3);
	    HashMap<String, Integer> result = new HashMap<>();
	    if (action == 0) {
	    	int mindmg = this.dmg[0]; 
			int maxdmg = this.dmg[1];
			int damage = (int) (Math.random() * (maxdmg - mindmg + 1)) + mindmg;
	        result.put("Attack", damage); 
	    } else if (action == 1) {
			result.put("Curse", 1);
	    } else {
	    	result.put("Summon", 1);
		}
	    return result;
	}

	@Override
	public void turn(GameModel gameData, Hero hero){
		resetProtection();
		//si l'ennemi a burn, alors il prend x brulures
		this.health -= this.burn;
		// Si health est inférieur ou égal à 0, l'ennemi meurt -> drop ressources
		if (health <= 0) {
			this.isAlive = false;
			return;
		}
		// Si l'ennemi a sleep, alors il passe son tour
		if (sleep == 0) {
			// L'ennemi fait son action entre attack et protect
			String action = nextAction.keySet().iterator().next();
			if (action.equals("Attack")) {
				hero.damageTaken(attack(nextAction.get(action)));
			} else if (action.equals("Curse")){
			    cursed(gameData);
			} else {
				summon(gameData);
			}
		}
		if (poison != 0) {
			if (zombie) {
				this.health += poison;
			} else {
				this.health -= poison;
			}
		}
		// À la fin du tour
		// Si poison, si zombie -> health -+ poison
		if (regen != 0) {
		    if (zombie) {
		        this.health -= regen;
		    } else {
		        this.health += regen;
		        if (this.health > maxHealth) {
		            this.health = maxHealth;
		        }
		    }
		}
		// Si health est inférieur ou égal à 0, l'ennemi meurt -> isAlive = false -> drop ressources
		decreaseEffects();
		if (health <= 0) {
			this.isAlive = false;
		} else {
			this.nextAction = prediction();
		}
	}
	
	@Override
	public void damageTaken(int dmg) {
	    if (dodge > 0) {
	        dodge--;
	    } else if (rough_side > 0) {
	        int remainingDamage = (dmg / 2) + freeze - protection;
	        if (remainingDamage > 0) {
	            protection = 0;
	            health -= remainingDamage;
	        } else {
	            protection -= (dmg / 2) + freeze;
	        }
	    } else {
	        int remainingDamage = dmg + freeze - protection;
	        if (remainingDamage > 0) {
	            protection = 0;
	            health -= remainingDamage;
	        } else {
	            protection -= dmg + freeze;
	        }
	    }
	    if (health <= 0) {
	        isAlive = false;
	    }
	}
	
	@Override
	public int getHaste() {
        return haste;
    }
	
	@Override
    public int getRage() {
        return rage;
    }
	
	@Override
    public int getSlow() {
        return slow;
    }
	
	@Override
    public int getWeak() {
        return weak;
    }
	
	@Override
    public int getPoison() {
        return poison;
    }
	
	@Override
    public int getBurn() {
        return burn;
    }
	
	@Override
    public int getFreeze() {
        return freeze;
    }
	
	@Override
    public int getRegen() {
        return regen;
    }
	
	@Override
    public boolean isZombie() {
        return zombie;
    }
	
	@Override
    public int getCharm() {
        return charm;
    }
	
	@Override
    public int getDodge() {
        return dodge;
    }
	
	@Override
    public int getRoughSide() {
        return rough_side;
    }
	
	@Override
    public int getSleep() {
        return sleep;
    }
    
	@Override
    public void setHaste(int haste) {
        this.haste += haste;
    }
	
	@Override
    public void setRage(int rage) {
        this.rage += rage;
    }
	
	@Override
    public void setSlow(int slow) {
        this.slow += slow;
    }
	
	@Override
    public void setWeak(int weak) {
        this.weak += weak;
    }

    @Override
    public void setPoison(int poison) {
        this.poison += poison;
    }
    
    @Override
    public void setBurn(int burn) {
        this.burn += burn;
    }
    
    @Override
    public void setFreeze(int freeze) {
        this.freeze += freeze;
    }
    
    @Override
    public void setRegen(int regen) {
        this.regen += regen;
    }
    
    @Override
    public void setZombie(boolean zombie) {
        this.zombie = zombie;
    }
    
    @Override
    public void setCharm(int charm) {
        this.charm += charm;
    }
    
    @Override
    public void setDodge(int dodge) {
        this.dodge += dodge;
    }
    
    @Override
    public void setRoughSide(int rough_side) {
        this.rough_side += rough_side;
    }
    
    @Override
    public void setSleep(int sleep) {
        this.sleep += sleep;
    }
    @Override
    public HashMap<String, Integer> getEffects() {
        HashMap<String, Integer> effetsActifs = new HashMap<>();
        if (haste > 0)
            effetsActifs.put("Haste", haste);
        if (rage > 0)
            effetsActifs.put("Rage", rage);
        if (slow > 0)
            effetsActifs.put("Slow", slow);
        if (weak > 0)
            effetsActifs.put("Weak", weak);
        if (poison > 0)
            effetsActifs.put("Poison", poison);
        if (burn > 0)
            effetsActifs.put("Burn", burn);
        if (freeze > 0)
            effetsActifs.put("Freeze", freeze);
        if (regen > 0)
            effetsActifs.put("Regen", regen);
        if (zombie)
            effetsActifs.put("Zombie", 1);
        if (charm > 0)
            effetsActifs.put("Charm", charm);
        if (dodge > 0)
            effetsActifs.put("Dodge", dodge);
        if (rough_side > 0)
            effetsActifs.put("Rough_Hide", rough_side);
        if (sleep > 0)
            effetsActifs.put("Sleep", sleep);
        if (spike > 0)
            effetsActifs.put("Spikes", spike);
        return effetsActifs;
    }

    
    public void decreaseEffects() {
        HashMap<String, Integer> effetsActifs = getEffects();
        for (String effet : effetsActifs.keySet()) {
            int nbEffets = 1;
            switch (effet) {
                case "Haste":
                    haste = Math.max(0, haste - nbEffets);
                    break;
                case "Rage":
                    rage = Math.max(0, rage - nbEffets);
                    break;
                case "Slow":
                    slow = Math.max(0, slow - nbEffets);
                    break;
                case "Weak":
                    weak = Math.max(0, weak - nbEffets);
                    break;
                case "Poison":
                    poison = Math.max(0, poison - nbEffets);
                    break;
                case "Burn":
                    burn = Math.max(0, burn - nbEffets);
                    break;
                case "Freeze":
                    freeze = Math.max(0, freeze - nbEffets);
                    break;
                case "Regen":
                    regen = Math.max(0, regen - nbEffets);
                    break;
                case "Zombie":
                    zombie = false;
                    break;
                case "Charm":
                    charm = Math.max(0, charm - nbEffets);
                    break;
                case "Dodge":
                    dodge = Math.max(0, dodge - nbEffets);
                    break;
                case "Rough_Hide":
                    rough_side = Math.max(0, rough_side - nbEffets);
                    break;
                case "Sleep":
                    sleep = Math.max(0, sleep - nbEffets);
                    break;
                case "Spikes":
                    spike = Math.max(0, spike - nbEffets);
                    break;
            }
        }
    }
	@Override
	public int[] getXY() {
		return coordonates;
	}
	
	@Override
	public void setXY(int X, int Y) {
		this.coordonates[0] = X;
		this.coordonates[1] = Y;
		this.coordonates[2]= 90*50;
		this.coordonates[3] = 90*50;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public int getMaxHealth() {
		return maxHealth;
	}
	
	@Override
	public Bestiary copie() {
		return new QueenBee(health);
	}
	
	@Override
	public void setHealth(int i) {
		this.health = i;
	}
	
	@Override
	public String getNextAction() {
		return nextAction.keySet().iterator().next();
	}

	@Override
	public int getIntAction() {
		return nextAction.entrySet().iterator().next().getValue();
	}
	
	@Override
	public int getXp() {
		return xp;
	}

	public String getDescription() {
		return description;
	}
}

