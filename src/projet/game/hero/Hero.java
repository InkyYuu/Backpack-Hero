package projet.game.hero;

import java.util.HashMap;

import projet.game.fight.*;

public class Hero implements Effects {
	private int hpMax;
	private int hp;
	private int pp;
	private int energy;
	private int xp;
	private int level;
	private final HashMap<Integer, Integer> levels;
	
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
	
	@SuppressWarnings("serial")
	public Hero() {
		this.hpMax = 40;
		this.hp = 40;
		this.setPp(0);
		this.setEnergy(3);
		this.xp = 0;
		this.level = 1;
		this.levels = new HashMap<Integer, Integer>() {{put(1, 30);put(2, 100);put(3, 250);put(4, 400);}};
	}
	
	/**
	 * Encaisser les dégâts
	 * @param dmg
	 */
	public void damageTaken(int dmg) {
	    if (dodge > 0) {
	        dodge--;
	    } else if (rough_side > 0) {
	        int remainingDamage = (dmg / 2) + freeze - pp;
	        if (remainingDamage > 0) {
	            pp = 0;
	            hp -= remainingDamage;
	        } else {
	            pp -= (dmg / 2) + freeze;
	        }
	    } else {
	        int remainingDamage = dmg + freeze - pp;
	        if (remainingDamage > 0) {
	            pp = 0;
	            hp -= remainingDamage;
	        } else {
	            pp -= dmg + freeze;
	        }
	    }
	}
	
	/**
	 * pour se rendre des points de vie
	 * @param heal
	 */
	public void healHimself(int heal) {
		if (hp + heal > hpMax) {
			hp = hpMax;
		} else {
			hp += heal;
		}
	}
	
	public int getHp() {
		return hp;
	}
	
	/**
	 * pour se donner de l'armure
	 * @param prot
	 */
	public void protect(int prot) {
		setPp(getPp() + prot);
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel() {
		level += 1;
	}
	
	/**
	 * gagne un niveau
	 * @return
	 */
	public boolean isLevelUp() {
		if (xp >= levels.get(level)) {
			setLevel();
			return true;
		} else {
			return false;
		}
	}
	
	public int getPalierXP(int level) {
		return levels.get(level);
	}
	
	public int getXP() {
		return xp;
	}
	
	public void setXP(int xp) {
		this.xp += xp;
	}


	public int getHaste() {
        return haste;
    }

    public int getRage() {
        return rage;
    }

    public int getSlow() {
        return slow;
    }

    public int getWeak() {
        return weak;
    }

    public int getPoison() {
        return poison;
    }

    public int getBurn() {
        return burn;
    }

    public int getFreeze() {
        return freeze;
    }

    public int getRegen() {
        return regen;
    }

    public boolean isZombie() {
        return zombie;
    }

    public int getCharm() {
        return charm;
    }

    public int getDodge() {
        return dodge;
    }

    public int getRoughSide() {
        return rough_side;
    }

    public int getSleep() {
        return sleep;
    }
    
    public void setHaste(int haste) {
        this.haste = haste;
    }

    public void setRage(int rage) {
        this.rage = rage;
    }

    public void setSlow(int slow) {
        this.slow = slow;
    }

    public void setWeak(int weak) {
        this.weak = weak;
    }

    public void setPoison(int poison) {
        this.poison = poison;
    }

    public void setBurn(int burn) {
        this.burn = burn;
    }

    public void setFreeze(int freeze) {
        this.freeze = freeze;
    }

    public void setRegen(int regen) {
        this.regen = regen;
    }

    public void setZombie(boolean zombie) {
        this.zombie = zombie;
    }

    public void setCharm(int charm) {
        this.charm = charm;
    }

    public void setDodge(int dodge) {
        this.dodge = dodge;
    }

    public void setRoughSide(int rough_side) {
        this.rough_side = rough_side;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }
    
    /**
     * liste les effets actifs
     */
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

    
    /**
     * réduit de 1 les effets par tour
     */
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

	public int getPp() {
		return pp;
	}

	public void setPp(int pp) {
		this.pp = pp;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy += energy;
	}
	
	public void resetEnergy() {
		this.energy = 3;
	}
	
	public void resetBlock() {
		this.pp = 0;
	}

	
	public void addHealthMax(int i) {
		this.hpMax += i;
	}

	public void addHealth(int i) {
	    this.hp = Math.min(hpMax, hp + i);
	}

	public int getMaxHp() {
		return hpMax;
	}
	
	public void resetEffect() {
		haste = 0; 
		rage = 0 ; 
		slow = 0;
		weak = 0 ; 
		poison = 0;
		burn = 0;
		freeze = 0;
		regen = 0;
		zombie = false;
		charm = 0;
		dodge = 0;
		rough_side = 0;
		sleep = 0;
		spike = 0;
		
	}

	public void affectEffects() {
		this.hp -= this.burn;

		if (regen != 0) {
	        this.hp += regen;
	        if (this.hp > hpMax) {
	            this.hp = hpMax;
		    }
		}
		decreaseEffects();
	}
	
	public void doPoisonAndRegen() {
		if (poison != 0) {
			if (zombie) {
				healHimself(poison);
			} else {
				healHimself(-poison);
			}
		}
		// Si regen, si zombie -> health -+ regen
		if (regen != 0) {
		    if (zombie) {
				healHimself(-regen);
		    } else {
		    	healHimself(regen);
		        if (hp > hpMax) {
		            hp = hpMax;
		        }
		    }
		}
	}
}
