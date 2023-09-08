package projet.game;

import java.awt.Color;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;

public class GameController {
	
	/**
	 * on detecte si il y a eu un clic ou une touche appuyée
	 * @param data
	 * @param event
	 * @param action
	 * @return
	 */
	public static int[] DetectionAction(GameModel data, Event event, Action action) {
		if (event != null) {
			//On clique avec la souris
			if (action == Action.POINTER_DOWN) {
		        return new int[] {(int)event.getLocation().getX(),(int)event.getLocation().getY()};
			} else if (action == Action.KEY_PRESSED) {
				return new int[] {-1,-1};
			}
		}
		return null;
	}
	

	/**
	 * Boucle principale du jeu - Partie Controlleur du MVC - va detecter une action et modifier le GameModel en conséquence de l'action qui va modifier la vue
	 * @param context
	 * @param data
	 * @param view
	 * @return true
	 */
	private static boolean gameLoop(ApplicationContext context, GameModel data, SimpleGameView view) {

        var event = context.pollOrWaitEvent(999999999);
        if (event == null) {
            return true;
        }
        var action = event.getAction();
        
        int[] xy = DetectionAction(data, event, action);

        if (xy != null) {
        	if (xy[0] == -1 && xy[1] == -1){
             	data.KeyAction(event);
            } else { 
            	data.PointerAction(xy[0], xy[1]);
            }
            data.Loop(xy[0], xy[1]);
        }
        
        if ((data.getHero().getHp() <= 0 || data.isWin()) && !data.isScoreAdded()) {
            data.addScore(data.isWin());
            data.setScoreAdded(true);
        }
        if (action != Action.POINTER_UP ) {
            SimpleGameView.draw(context, view, data);
        }

        return true;
	}
	    	
	    	
	    	
	private static void backPackHero(ApplicationContext context) {
			var screenInfo = context.getScreenInfo();
	        var width = screenInfo.getWidth();
	        var height = screenInfo.getHeight();
	        System.out.println("size of the screen (" + width + " x " + height + ")");

			GameModel data = new GameModel();
			
			var images = new ImageLoader(
				    "data",
				    "Rat_Wolf.png",
				    "Chest.png",
				    "Little_mouse.png",
				    "1x1_Shape.png",
				    "avatar2.png",
				    "fond.png",
				    "Mouse_Bow.png",
				    "Napsack_Win.gif",
				    "Old_Log.png",
				    "Pockets_Win.gif",
				    "Purse_Idle_Gif.gif",
				    "Rough_Buckler.png",
				    "Small_Ratword_Animated.gif",
				    "Wooden_sword.png",
				    "wall.png",
				    "backpack.png",
				    "Gold.png",
				    "Card.png",
				    "Mana.png",
				    "tinybackpack.png",
				    "Curse1x1.png",
				    "tinymap.png",
				    "Title.png",
				    "portalart.png",
				    "tilemap.png",
				    "map.png",
				    "IconeChest.png",
				    "IconeTrader.png",
				    "IconeStair.png",
				    "IconeMob.png",
				    "Iconehealer.png",
				    "death.png",
				    "logoAttack.png",
				    "logoPoison.png",
				    "logoRegen.png",
				    "logoSleep.png",
				    "logoSummon.png",
				    "logoStatus_Burn.png",
				    "logoStatus_Regen.png",
				    "logoStatus_Spikes.png",
				    "logoStatus_Freeze.png",
				    "logoRob.png",
				    "logoBlock.png",
				    "logoStatus_Rage.png",
				    "logoStatus_Weak.png",
				    "logoStatus_Haste.png",
				    "logoFlee.png",
				    "logoStatus_Sleep.png",
				    "logoCurse.png",
				    "logoStatus_Poison.png",
				    "logoHeal.png",
				    "logoStatus_Rough_Hide.png",
				    "Queen_Bee.png",
				    "Lil_Bee.gif",
				    "logoStatus_Slow.png",
				    "WizardFrog.png",
				    "Living_Shadow.png",
				    "Brick.png",
				    "Electric_Wand.png",
				    "Feather_Cap.png",
				    "Leather_Cap.png",
				    "Arrow.png",
				    "Fire_Arrow.png",
				    "Leather_Boots.png",
				    "Left_Glove.png",
				    "Right_Glove.png",
				    "Tunic.png",
				    "Iron_Helmet.png",
				    "Needler.png",
				    "Vampire_Blade.png",
				    "Energy.png",
				    "explaincard.png",
				    "Conductive_Icon.png",
				    "Float_Icon.png",
				    "Heavy_Icon.png",
				    "Projectile_Icon.png",
				    "healpage.png",
				    "Spiky_Club.png",
				    "Brutal_Spear.png",
				    "Vampiric_Axe.png",
				    "Mace.png",
				    "Venom_Sword.png",
				    "Cleaver.png",
				    "Poison_Arrow.png",
				    "Explosive_Arrow.png",
				    "Wizard_Staff.png",
				    "Skull_Wand.png",
				    "Lightning.png",
				    "Sweaty_Towel.png",
				    "Classic_Curse.png",
				    "Long_Curse.png",
				    "Big_Curse.png",
				    "gold-medal.png",
                    "silver-medal.png",
                    "bronze-medal.png"
				);

			var view = SimpleGameView.initGameGraphics(5, 5, (int) Math.min(width, height) - 2 * 5, images);
			
			SimpleGameView.draw(context, view, data);

			while (true) {
				gameLoop(context,data,view);
				if (!gameLoop(context, data, view)) {
					System.out.println("Thank you for quitting!");
					context.exit(0);
					return;
				}
			}
		}

		public static void main(String[] args) {
			Application.run(Color.DARK_GRAY, GameController::backPackHero);
		}
	}
