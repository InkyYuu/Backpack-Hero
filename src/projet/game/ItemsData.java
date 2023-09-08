package projet.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import projet.game.fight.bestiary.Bestiary;
import projet.game.fight.bestiary.LilBee;
import projet.game.fight.bestiary.LivingShadow;
import projet.game.fight.bestiary.QueenBee;
import projet.game.fight.bestiary.RatWolf;
import projet.game.fight.bestiary.SmallRatWolf;
import projet.game.fight.bestiary.WizardFrog;
import projet.game.items.Armor;
import projet.game.items.Arrow;
import projet.game.items.Bow;
import projet.game.items.Curse;
import projet.game.items.Items;
import projet.game.items.Magic;
import projet.game.items.Manastone;
import projet.game.items.Melee;
import projet.game.items.Shield;
import projet.game.items.Structure;
import projet.game.items.Wand;

public class ItemsData {
	
	private final ArrayList<Items> allItems = allItems();
	private final HashMap<String, ArrayList<Items>> allItemsByRarety = new HashMap<String,ArrayList<Items>>();
	private final HashMap<Class<?>, ArrayList<Items>> allItemsByType = new HashMap<Class<?>,ArrayList<Items>>();
	private final ArrayList<Bestiary> allEnemies = allEnemies();
	
	public ItemsData() {
		for (Items item : allItems) {
			if (!allItemsByRarety.containsKey(item.getRarety())) {
				allItemsByRarety.put(item.getRarety(), new ArrayList<Items>(Arrays.asList(item)));
			} else {
				allItemsByRarety.get(item.getRarety()).add(item);
			}
			
			if (!allItemsByType.containsKey(item.getClass())) {
				allItemsByType.put(item.getClass(), new ArrayList<Items>(Arrays.asList(item)));
			} else {
				allItemsByType.get(item.getClass()).add(item);
			}
		}
	}
	
	// Fonction de création
	
		/**
		 * création des items du jeu
		 * @return
		 */
		@SuppressWarnings("serial")
		public static ArrayList<Items> allItems() {
			ArrayList<Items> allItems = new ArrayList<Items>();
			allItems.add(new Melee("Wooden Sword","Wooden_sword.png",new HashMap<String, Integer>(),new HashMap<String, Integer>(), new HashMap<String, Integer>() {{put("Damage", 7);}},  new int[][] {{1},{1},{1}}, 1, 0, 0, "Common", "On use :	\nDeals 7 Damages"));                                                         
			allItems.add(new Shield("Rough Blocker","Rough_Buckler.png",new HashMap<String, Integer>(),new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Block", 7);}},  new int[][] {{1,1},{1,1}} , 1, 0, 0, "Common", "On use:	\nAdds 7 Block"
					+ "  Adds 7 Block"));
			allItems.add(new Bow("Mouse Bow", "Mouse_Bow.png", new HashMap<String, Integer>() {{put("GiveArrow", 1);}},new HashMap<String, Integer>(), new HashMap<String, Integer>() {{put("FiresArrow", 0);}} ,  new int[][] {{1},{1},{1}}, 1, 0, 0, "Uncommon", "The first arrow in each\nrow to the right gets +1 Damage\n"
					+ "On use:\n"
					+ "  Fires the first arrow in each\n row to the direction\n"
					+ "  Used to fire arrows"));
			allItems.add(new Melee("Vampire Blade","Vampire_Blade.png", new HashMap<String, Integer>(),new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Vampirism",2);}},  new int[][] {{1},{1}}, 1, 0, 0, "Uncommon", "On use:\n"
					+ "  Deals 2 Vampirism"));
			allItems.add(new Melee("Needler","Needler.png",new HashMap<String, Integer>(),new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Damage", 7);put("Poison",2); put("TurnDamage",-1);}},  new int[][] {{1,1},{1,0}}, 1, 0, 0, "Rare", "On Use:\n"
					+ "  Deals 7 damage\n"
					+ "  Adds 2 poison to enemy\n"
					+ "  -1 Damage for this turn"));
			allItems.add(new Armor("Iron Helmet", "Iron_Helmet.png", new HashMap<String, Integer>() {{put("Top", 0);put("GiveAdjacent", 1);}}, new HashMap<String, Integer>() {{put("Block", 2);}}, new HashMap<String, Integer>(),  new int[][] {{1}}, 0, 0, 0, "Common", "Adjacent Armor gets +1 Block\n"
					+ "If this isn't in the top row:\n"
					+ "  This item is disabled\n"
					+ "Each turn:\n"
					+ "  Adds 2 Block\n"));
			allItems.add(new Armor("Leather Cap", "Leather_Cap.png", new HashMap<String, Integer>() {{put("Down", 1);}},new HashMap<String, Integer>() {{put("Block", 0);}}, new HashMap<String, Integer>() ,  new int[][] {{1}}, 0, 0, 0, "Uncommon", "For each space below:\n"
					+ "  This item gets +1 Block\n"
					+ "Each turn:\n"
					+ "  Adds 0 Block\n"));
			allItems.add(new Armor("Feather Cap", "Feather_Cap.png", new HashMap<String, Integer>() {{put("Down", 2);}},new HashMap<String, Integer>() {{put("Block", 0);}}, new HashMap<String, Integer>() ,  new int[][] {{1,1}}, 0, 0, 0, "Legendary", "For each space is below:\n"
					+ "  This item gets +2 Block\n"));
			allItems.add(new Armor("Tunic", "Tunic.png", new HashMap<String, Integer>() {{put("GiveAround", 1);}}, new HashMap<String, Integer>() {{put("Block", 5);}}, new HashMap<String, Integer>(),  new int[][] {{1,1},{1,1}}, 0, 0, 0, "Common", "Each turn:\n  Adds 5 Block\n"
					+ "  Adjacent and diagonal Armor \n gets +1 Block"));
			allItems.add(new Armor("Leather Boots", "Leather_Boots.png",new HashMap<String, Integer>() {{put("Up", 1);}}, new HashMap<String, Integer>() {{put("Block", 0);}}, new HashMap<String, Integer>(),  new int[][] {{1,1}}, 0, 0, 0, "Common", "Each Turn:\n  Adds 0 Block\nFor each space is above this:\n"
					+ "  This item gets +1 Block"));
			allItems.add(new Wand("Electric Wand", "Electric_Wand.png", new HashMap<String, Integer>() {{put("Conductive", 0);}}, new HashMap<String, Integer>(), new HashMap<String, Integer>() {{put("Damage", 5);}}, new int[][] {{0,1},{1,0}}, "Common",  0, 0, 1, "On Use:\n"
					+ "  Deals 7 Damage"));
			allItems.add(new Manastone("Small Manastone", "Mana.png", 1, new int[][] {{1}} ));
			allItems.add(new Manastone("Medium Manastone", "Mana.png", 2, new int[][] {{1}} ));
			allItems.add(new Manastone("Big Manastone", "Mana.png", 3, new int[][] {{1}} ));
			allItems.add(new Arrow("Arrow", "Arrow.png", new HashMap<String, Integer>() {{put("Projectile",0);put("Direction", 1);}}, new HashMap<String, Integer>(), new HashMap<String, Integer>() {{put("Damage", 2);}},  new int[][] {{1,1}}, 0, 0, 0, "Common", "On use:\n"
					+ "  Deals 2 Damage\n"
					+ "For each empty space to the right:\n"
					+ "  This item gets +1 Damage"));
			allItems.add(new Arrow("Fire Arrow", "Fire_Arrow.png", new HashMap<String, Integer>() {{put("Projectile",0);put("Direction", 2);}},new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Burn", 2);}},  new int[][] {{1,1}}, 0, 0, 0, "Uncommon", "On Use:\n"
					+ "  Adds 2 Burn to Enemy\n"
					+ "For each empty space to the \n(Directional):\n"
					+ "  This item gets +2 Burn"));
			allItems.add(new Structure("Brick", "Brick.png",new HashMap<String, Integer>() {{put("Heavy", 0);put("GainAdjacent",1);}},new HashMap<String, Integer>() {{put("Block", 0);}},new HashMap<String, Integer>() ,  new int[][] {{1}}, 0, 0, 0, "Common", "Each turn:\n  Adds 0 Block\n"
					+ "For each adjacent structure:"
					+ "\n"
					+ "  This item gets +1 Block"));
			allItems.add(new Melee("Spiky Club","Spiky_Club.png",new HashMap<String, Integer>(),new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Damage", 7);put("Slow",1);}},  new int[][] {{1},{1}}, 1, 0, 0, "Uncommon", "On Use:\n"
					+ "\n"
					+ "  Deals 7 Damage\n"
					+ "  Adds 1 Slow to Enemy"));
			allItems.add(new Melee("Brutal Spear","Brutal_Spear.png",new HashMap<String, Integer>(),new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Damage", 7);put("TurnDamage",1);}},  new int[][] {{1},{1},{1}}, 1, 0, 0, "Uncommon", "On Use:\n"
					+ "\n"
					+ "  Deals 7 Damage\n"
					+ "  This item gets +1 Damage this turn"));
			allItems.add(new Melee("Vampiric Axe","Vampiric_Axe.png",new HashMap<String, Integer>(),new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Vampirism", 10);put("PoisonSelf",3);}},  new int[][] {{1},{1},{1}}, 1, 0, 0, "Rare", "On Use:\n"
					+ "\n"
					+ "  Deals 10 Vampirism\n"
					+ "  Adds 3 Poison to self"));
			allItems.add(new Melee("Mace","Mace.png",new HashMap<String, Integer>(),new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Damage", 16);put("Slow",2);put("Weak",2);}},  new int[][] {{1},{1}}, 2, 0, 0, "Rare", "On Use:\n"
					+ "  Deals 16 Damage\n"
					+ "  Adds 2 Slow to enemy"
					+ "  Adds 2 Weak to enemy"));
			allItems.add(new Melee("Venom Sword","Venom_Sword.png",new HashMap<String, Integer>(),new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("DamageOnPoison",3);}},  new int[][] {{1},{1},{1}}, 1, 0, 0, "Legendary", "On Use:\n"
					+ "  Deals Damage equal to 100% \n of your current Poison"));
			allItems.add(new Melee("Cleaver","Cleaver.png",new HashMap<String, Integer>(),new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Damage",4);}},  new int[][] {{1}}, 1, 0, 0, "Common", "On Use:\n"
					+ "  Deals 4 Damage"));
			allItems.add(new Arrow("Poison Arrow", "Poison_Arrow.png", new HashMap<String, Integer>() {{put("Projectile",0);put("Direction", 2);}},new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Poison", 2);}},  new int[][] {{1,1}}, 0, 0, 0, "Uncommon", "On Use:\n"
					+ "  Adds 2 Poison to Enemy\n"
					+ "For each empty space to the \n(Directional):\n"
					+ "  This item gets +2 Poison"));
			allItems.add(new Arrow("Explosive Arrow", "Explosive_Arrow.png", new HashMap<String, Integer>() {{put("Projectile",0);put("Direction", 1);}},new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("DamageAll", 1);}},  new int[][] {{1,1}}, 0, 0, 0, "Rare", "On Use:\n"
					+ "  Deals 1 Damage to all Enemies\n"
					+ "For each empty space to the \n(Directional):\n"
					+ "  This item gets +1 Damage"));
			allItems.add(new Wand("Skull Wand", "Skull_Wand.png", new HashMap<String, Integer>() {{put("Conductive", 0);put("Projectile",0);}}, new HashMap<String, Integer>(), new HashMap<String, Integer>() {{put("DamageAll", 5);}}, new int[][] {{0,1},{1,0}}, "Rare",  0, 0, 2, "On Use:\n"
					+ "  Deals 5 Damage to all Enemies"));
			allItems.add(new Magic("Lightning Bold", "Lightning.png", new HashMap<String, Integer>() {{put("Conductive", 0);put("Projectile",0);put("onUse", 0);}}, new HashMap<String, Integer>() , new HashMap<String, Integer>() {{put("DamageAll", 4);}}, new int[][] {{1,0},{1,1},{0,1}}, "Rare",  0, 0, 0, "On Use:\n"
					+ "  Deals 4 Damage to all Enemies\n"
					+ "Each turn:\n"
					+ "  This item is used"));
			allItems.add(new Shield("Sweaty Towel","Sweaty_Towel.png",new HashMap<String, Integer>(),new HashMap<String, Integer>(),new HashMap<String, Integer>() {{put("Block", 12);put("BurnSelf", 3);}},  new int[][] {{1},{1}} , 1, 0, 0, "Uncommon", "On use:\n"
					+ "  Adds 12 Block"
					+ "  Adds 3 Burn to self"));
			allItems.add(new Curse("Classic Curse", "Classic_Curse.png",new HashMap<String, Integer>() {{put("Hurt", 2);}}, new HashMap<String, Integer>(), new HashMap<String, Integer>() {{put("Destroy", 0);}},   new int[][] {{1}}, "If you skip:\n Take 2 Damage\nOn Use:\n Destroy item"));
			allItems.add(new Curse("Long Curse", "Long_Curse.png",new HashMap<String, Integer>() {{put("Hurt", 3);}}, new HashMap<String, Integer>() {{put("SlowSelf", 2);}}, new HashMap<String, Integer>() {{put("Destroy", 0);}},   new int[][] {{1},{1}}, "If you skip:\n Take 3 Damage\nEach turn:\n Adds 2 Slow to self\nOn Use:\n Destroy item"));
			allItems.add(new Curse("Big Curse", "Big_Curse.png",new HashMap<String, Integer>() {{put("Hurt", 4);}}, new HashMap<String, Integer>() {{put("Hurt", 2);}}, new HashMap<String, Integer>() {{put("Destroy", 0);}},   new int[][] {{1,1},{1,1}}, "If you skip:\n Take 4 Damage\nEach turn:\n Take 2 Damage\nOn Use:\n Destroy item"));
			return allItems;
		}
		
		/**
		 * creation des ennemis du jeu
		 * @return
		 */
		public static ArrayList<Bestiary> allEnemies() {
            ArrayList<Bestiary> allEnemies = new ArrayList<Bestiary>();
            allEnemies.add(new SmallRatWolf(32));
            allEnemies.add(new RatWolf(45));
            allEnemies.add(new LilBee(16));
            allEnemies.add(new LivingShadow(50));
            allEnemies.add(new QueenBee(74));
            allEnemies.add(new WizardFrog(45));
            return allEnemies;
        }
		
		public Items getItem(String name) {
			for (Items item : allItems) {
				if (item.getName().equals(name)) {
					return item.copie();
				}
			}
			return null;
		}
		
		public ArrayList<Bestiary> getAllEnemies() {
            return allEnemies;
        }
		
		/**
		 * selection d'un item aleatoire
		 * @return
		 */
		public Items randomItem() {
			return allItems.get(new Random().nextInt(allItems.size())).copie();
		}
		
		public Items randomItemByRarety(String rarety) {
			return allItemsByRarety.get(rarety).get(new Random().nextInt(allItemsByRarety.get(rarety).size())).copie();
		}
		
		public Items randomItemByType(Class<?> type) {
			return allItemsByType.get(type).get(new Random().nextInt(allItemsByType.get(type).size())).copie();
		}
	}

