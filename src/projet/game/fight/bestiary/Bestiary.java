package projet.game.fight.bestiary;

import java.util.HashMap;

import projet.game.GameModel;
import projet.game.fight.Effects;
import projet.game.hero.Hero;

public interface Bestiary extends Effects {
	//public static final int xp;
	int[] getXY();
	void setXY(int X, int Y);
	/**
	 * prédit quelle action l'ennemi va effectuer
	 * @return
	 */
	public HashMap<String,Integer> prediction();
	/**
	 * tour de l'ennemi durant le combat
	 * @param gamedata
	 * @param hero
	 */
	public void turn(GameModel gamedata,Hero hero);
	String getImgPath();
	int getHealth();
	int getMaxHealth();
	/**
	 * action de l'ennemi
	 * @return
	 */
	String getNextAction();
	int getIntAction();
	Bestiary copie();
	void setHealth(int i);
	/**
	 * encaisse les dégâts du héro
	 * @param dmg
	 */
	void damageTaken(int dmg);
	boolean getIsAlive();
	int getProtection();
	int getXp();
}
