package projet.game.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import projet.game.hero.Backpack;
import projet.game.hero.Hero;
import projet.game.items.Items;

public class Healer implements Events {
	private final String imgPath = "data/Napsack_Win.gif";
	//private final String greetingQuote;
	//private final String farewellQuote;
	private final HashMap<String, HashMap<Integer, HashMap<String, String>>> trade = new HashMap<String, HashMap<Integer, HashMap<String, String>>>();
	
	public Healer() {
		//Random greeting and farewell quote
		//ArrayList<String> allGreetingsQuotes = new ArrayList<String>(Arrays.asList("Come hero, let me heal you", "We are here to protect the brave!", "Welcome to our healing center!"));
		//ArrayList<String> allFarewellsQuotes = new ArrayList<String>(Arrays.asList("My sisters are here for you!", "Stay safe!", "May we meet again!"));
		//this.greetingQuote = allGreetingsQuotes.get(new Random().nextInt(allGreetingsQuotes.size()));
		//this.farewellQuote = allFarewellsQuotes.get(new Random().nextInt(allFarewellsQuotes.size()));
		
		//Random quote for all trade
		
		//Curse
		var quotes1 = new HashMap<String,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put("The curses can't hurt you now.","\"I sense a great disturbance.\" she says. \"Something horrible has been placed in your backpack. Let me see.\" She removes a single quill and pokes it into your bag carefully in several spots. \"That should do it,\" she says.");
		}};
		var all1 = new HashMap<Integer, HashMap<String, String>>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put(3, quotes1);
		}};
		this.trade.put("Remove all curses (3 gold)", all1);
		
		//Heal
		var healerQuotes1 = new ArrayList<String>(Arrays.asList("Glad to have helped you!", "You look stronger my dear!"));
		var quotes2 = new HashMap<String,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put("\"She runs her paw across your wounds. The quills on her back shake and you are lost in the sound of them. When you come to, many wounds are healed.\"", healerQuotes1.get(new Random().nextInt(healerQuotes1.size())));
		}};
		var all2 = new HashMap<Integer, HashMap<String, String>>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put(5, quotes2);
		}};
		this.trade.put("Heal 25 health (5 gold)", all2);
		
		//Max Heal
		var narratorQuotes2 = new ArrayList<String>(Arrays.asList("\"The healer taps a staff against your nose. The boop makes you stronger\"" , "\"She reaches out to you. You feel a great strength in you rising. Your paws are firmly planted, but it almost feels that you are floating.\""));
		var healerQuotes2 = new ArrayList<String>(Arrays.asList("I see your strength.", "You are stronger now than before." ,"May you achieve what seemed impossible.","Your bravery astounds me."));
		var quotes3 = new HashMap<String,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put(narratorQuotes2.get(new Random().nextInt(narratorQuotes2.size())), healerQuotes2.get(new Random().nextInt(healerQuotes2.size())));
		}};
		var all3 = new HashMap<Integer, HashMap<String, String>>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put(8, quotes3);
		}};
		this.trade.put("Gain 5 max health (8 gold)", all3);
		
		//Nothing
		var quotes4 = new HashMap<String,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put("\"Very well\" she says.","We are with you sister.");
		}};
		var all4 = new HashMap<Integer, HashMap<String, String>>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put(8, quotes4);
		}};
		this.trade.put("Nothing for me!", all4);
	}
	
	/**
	 * donne l'effet désiré au joueur en échange d'or
	 * @param x
	 * @param y
	 * @param hero
	 * @param backpack
	 */
	public void getEffect(int x, int y, Hero hero, Backpack backpack) {
		System.out.println("Je suis là !");
		if (650 <= x && x <= 1350 && 225 <= y && y <= 275) {
			if (backpack.getGold() != null ) {
				if (backpack.getGold().getAction() >= 5) {
					hero.addHealth(25);
					backpack.addGold(-5);
				}
			}
		} else if (650 <= x && x <= 1350 && 285 <= y && y <= 335) {
			if (backpack.getGold() != null ) {
				if (backpack.getGold().getAction() >= 8) {
				hero.addHealthMax(5);
				backpack.addGold(-8);
			}}
		} else if (650 <= x && x <= 1350 && 345 <= y && y <= 395) {
			if (backpack.getGold() != null) {
			    if (backpack.getGold().getAction() >= 3) {
			        for (Items i : backpack.getInventory()) {
			            System.out.println(i.getClass().getSimpleName());
			            if (i.getClass().getSimpleName().equals("Curses")) {
			                backpack.removeItem(i.getUID());
			            }
			        }
			        backpack.addGold(-3);
			    }
			}
		} else {
			
		}
	}

	public String getImgPath() {
		return imgPath;
	}
}
