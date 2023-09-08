package projet.game;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;

import fr.umlv.zen5.ApplicationContext;
import projet.game.fight.bestiary.Bestiary;
import projet.game.hero.Backpack;
import projet.game.items.*;
import projet.game.items.Manastone;
import projet.game.map.RandomMap;
import projet.game.map.Room;

public record SimpleGameView(int xOrigin, int yOrigin, int height, int width,ImageLoader loader) {
	
	public static SimpleGameView initGameGraphics(int xOrigin, int yOrigin, int length,ImageLoader images) {
		Objects.requireNonNull(images);
		return new SimpleGameView(xOrigin, yOrigin, length, length, images);
	}

	private static void checkRange(double min, double value, double max) {
		if (value < min || value > max) {
			throw new IllegalArgumentException("Invalid coordinate: " + value);
		}
	}

	private int indexFromRealCoord(float coord, int origin) {
		return (int) ((coord - origin));
	}

	public int lineFromY(float y) {
		checkRange(yOrigin, y, y + width);
		return indexFromRealCoord(y, yOrigin);
	}

	public int columnFromX(float x) {
		checkRange(xOrigin, x, x + height);
		return indexFromRealCoord(x, xOrigin);
	}

	private float realCoordFromIndex(int index, int origin) {
		return origin + index ;
	}

	private float xFromI(int i) {
		return realCoordFromIndex(i, xOrigin);
	}


	private float yFromJ(int j) {
		return realCoordFromIndex(j, yOrigin);
	}
	
	//----------------------------------- LES STRINGS AFFICHAGES ----------------------------------------------------------------------------------------------------------------------------
	private void drawStr(Graphics2D graphics, String string, int i, int j, int size, Color color) {
	    Font myFont = new Font("Courier New", Font.BOLD, size);
	    graphics.setFont(myFont);
	    graphics.setColor(color);
	    int lineHeight = graphics.getFontMetrics().getHeight();
	    String[] lines = string.split("\n");
	    float y = yFromJ(j);
        for (String line : lines) {
            graphics.drawString(line, xFromI(i), y); 
            y += lineHeight;
        }
	}

	/**
	 * dessine une string
	 * @param context
	 * @param view
	 * @param string
	 * @param i
	 * @param j
	 * @param size
	 * @param color
	 */
	public static void drawStringR(ApplicationContext context, SimpleGameView view, String string, int i, int j, int size, Color color) {
	    context.renderFrame(graphics -> view.drawStr((Graphics2D) graphics, string, i, j, size, color));
	}
	
	//----------------------------------- LES IMAGES AFFICHAGES ----------------------------------------------------------------------------------------------------------------------------
	/**
	 * dessine une image
	 * @param context
	 * @param view
	 * @param images
	 * @param x
	 * @param y
	 * @param dimX
	 * @param dimY
	 */
	public static void draw(ApplicationContext context, SimpleGameView view, BufferedImage images, float x, float y, float dimX, float dimY) {
		context.renderFrame(graphics -> view.draw(graphics,images,x,y,dimX,dimY)); // do not modify
	}
	
	private void draw(Graphics2D graphics, BufferedImage images, float x, float y, float dimX, float dimY) {
		SimpleGameView.drawImage(graphics, images, x, y, dimX, dimY);
	}
	
	static void drawImage(Graphics2D graphics, BufferedImage image, float x, float y, float dimX, float dimY) {
	    var width = image.getWidth();
	    var height = image.getHeight();
	    var scale = Math.min(dimX / width, dimY / height);
	    var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
	            y + (dimY - scale * height) / 2);
	    graphics.setComposite(AlphaComposite.SrcOver);
	    graphics.drawImage(image, transform, null);
	}
	
	/**
	 * dessine un rectangle
	 * @param context
	 * @param view
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param alpha
	 */
	public static void drawRectangle(ApplicationContext context, SimpleGameView view, float x, float y, float width, float height, Color color, float alpha) {
	    context.renderFrame(graphics -> {
	        Graphics2D g2d = (Graphics2D) graphics;
	        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	        g2d.setComposite(alphaComposite);
	        g2d.setColor(color);
	        g2d.fillRect((int) x, (int) y, (int) width, (int) height);
	    });
	}
	
	/**
	 * dessine les bords d'un rectangle
	 * @param context
	 * @param view
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param strokeWidth
	 */
	public static void drawRectangleOutline(ApplicationContext context, SimpleGameView view, float x, float y, float width, float height, Color color, float strokeWidth) {
	    context.renderFrame(graphics -> {
	        Graphics2D g2d = (Graphics2D) graphics;
	        g2d.setColor(color);
	        g2d.setStroke(new BasicStroke(strokeWidth));
	        g2d.drawRect((int) x, (int) y, (int) width, (int) height);
	    });
	}
	//----------------------------------- LES MODIFICATIONS DES IMAGES ----------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * fait une rotation de l'image
	 * @param image
	 * @param angle
	 * @return
	 */
	public static BufferedImage rotateImage(BufferedImage image, double angle) {
	    double radians = Math.toRadians(angle);

	    double sin = Math.abs(Math.sin(radians));
	    double cos = Math.abs(Math.cos(radians));

	    int width = image.getWidth();
	    int height = image.getHeight();

	    int newWidth = (int) Math.round(width * cos + height * sin);
	    int newHeight = (int) Math.round(width * sin + height * cos);

	    BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = rotatedImage.createGraphics();
	    g2d.translate(newWidth / 2, newHeight / 2);
	    g2d.rotate(radians);
	    g2d.drawImage(image, -width / 2, -height / 2, null);
	    g2d.dispose();

	    return rotatedImage;
	}
	
	//----------------------------------- LES ELEMENTS AFFICHAGES ----------------------------------------------------------------------------------------------------------------------------
	
	public static void drawMap(ApplicationContext context, SimpleGameView view, RandomMap map) {	
		SimpleGameView.draw(context, view, view.loader.image("map.png"), 500, -210, 900, 900);
		int i = 0;
		for (Room[] ligne : map.getCarte()) {
		    int j = 0;
		    for (Room salle : ligne) {
		        if (salle != null) {
		            SimpleGameView.draw(context, view, view.loader.image("tilemap.png"), 1920/2 - (11*75/2)+(75*j), 50+(75*i), 75, 75);
		            switch (salle.getApparition()) {
			            case 1:
			                SimpleGameView.draw(context, view, view.loader.image("IconeMob.png"), 1920/2 - (11*75/2)+(75*j), 50+(75*i), 75, 75);
			                break;
			            case 2:
			                SimpleGameView.draw(context, view, view.loader.image("IconeChest.png"), 1920/2 - (11*75/2)+(75*j), 50+(75*i), 75, 75);
			                break;
			            case 3:
			                SimpleGameView.draw(context, view, view.loader.image("Iconehealer.png"), 1920/2 - (11*75/2)+(75*j), 50+(75*i), 75, 75);
			                break;
			            case 4:
			                SimpleGameView.draw(context, view, view.loader.image("IconeTrader.png"), 1920/2 - (11*75/2)+(75*j), 50+(75*i), 75, 75);
			                break;
			            default:
			                break;
		        }
		            if (salle.getIsFinal()) {
		                SimpleGameView.draw(context, view, view.loader.image("IconeStair.png"), 1920/2 - (11*75/2)+(75*j), 50+(75*i), 75, 75);
		            }
		        }
		        j++;
		    }
		    i++;
		}
	}
	
	public static void drawCharacterPosition(ApplicationContext context, SimpleGameView view,GameModel data) {
		int x = 1920/2 - (11*75/2)+(75*data.getActualMap().getRoomPosY(data.getHeroPosition()));
		int y = 50+(75*(data.getActualMap().getRoomPosX(data.getHeroPosition())));
		SimpleGameView.draw(context, view, view.loader.image("Little_mouse.png"), x, y, 75, 75);
	}
	
    public static void drawBackpack(ApplicationContext context, SimpleGameView view, Backpack backpack, GameModel data) {
        SimpleGameView.draw(context, view, view.loader.image("backpack.png"),(1920/2 - 180 -(backpack.backpack()[0].length*90/2)), 10, 990, 550); 
        for (int i = 0; i < backpack.backpack().length; i++) {
            for (int j = 0; j < backpack.backpack()[0].length; j++) {
                if (backpack.backpack()[i][j] != -1) {
                    SimpleGameView.draw(context, view, view.loader.image("1x1_Shape.png"), (1920/2 - (backpack.backpack()[0].length*90/2)) + (90*j), 50 + (90*i), 90, 90);
                } else if (data.isLevelUp() > 0 && !data.getIsFighting()) {
                    SimpleGameView.draw(context, view, view.loader.image("1x1_Shape.png"), (1920/2 - (backpack.backpack()[0].length*90/2)) + (90*j), 50 + (90*i), 90, 90);
                    SimpleGameView.drawRectangle(context, view, (1920/2 - (backpack.backpack()[0].length*90/2)) + (90*j), 50 + (90*i), 90, 90, Color.RED, 0.5f);
                }
            }
        }
    }
	
	public static void drawItems(ApplicationContext context, SimpleGameView view, GameModel data) {
        if (data.getIsSelected()) {
    		int[][] size = data.getItemSelected().getSize();
            for (int i = 0; i < size.length; i++) {
                for (int j = 0; j < size[0].length; j++) {
                	if (data.getIsOrganize()) {
	                	if (size[i][j] == 1 && (i != 0 || j != 0)) {
	                    	drawRectangle(context, view, data.getItemSelected().getXY()[0]+j*80, data.getItemSelected().getXY()[1]+i*80, 80, 80, Color.YELLOW, 0.5f);    
	                	} else if (i == 0 && j == 0) {
	                    	drawRectangle(context, view, data.getItemSelected().getXY()[0], data.getItemSelected().getXY()[1], 80, 80, Color.BLUE, 0.5f);      
	                	}
                	} else if (data.getIsFighting() && data.getHeroTurn() != 0) {
	                	if (size[i][j] == 1) {
	                		drawRectangle(context, view, data.getItemSelected().getXY()[0]+j*80, data.getItemSelected().getXY()[1]+i*80, 80, 80, Color.RED, 0.5f);
	                	}
                	}
                }
            }
        }
		for (Items i : data.getBackpack().inventory()) {	
            SimpleGameView.draw(context, view, rotateImage(view.loader.image(i.getImgPath()), i.getRotation()), i.getXY()[0], i.getXY()[1], i.getXY()[3], i.getXY()[2]); 
        	if (i.getClass() == Manastone.class) {
        		drawStringR(context, view, i.getAction() + "/" + ((Manastone) i).getMaximumMana(), i.getXY()[0]+55, i.getXY()[1]+60, 25, Color.WHITE);
        	} else if (i.getClass() == Gold.class) {
        		drawStringR(context, view, String.valueOf(i.getAction()), i.getXY()[0]+55, i.getXY()[1]+60, 25, Color.WHITE);
        	}
		}
	}
	
	public static void drawWall(ApplicationContext context, SimpleGameView view,GameModel data) {
        SimpleGameView.draw(context, view, view.loader.image("wall.png"), 0, 580, 1920, 500);
	}
	
	public static void drawHero(ApplicationContext context, SimpleGameView view,GameModel data) {
        SimpleGameView.draw(context, view, view.loader.image("Purse_Idle_Gif.gif"), 300, 750, 200, 200);
        //SimpleGameView.drawStringR(context, view, "HP :", 325, 735, 20, Color.WHITE);
        SimpleGameView.drawRectangle(context, view, 380, 725, 100, 20, Color.GRAY, 1f);
        Color color;
        if (data.getHero().getHp()*100/data.getHero().getMaxHp() >= 50) {
        	color = Color.green;
        } else if (data.getHero().getHp()*100/data.getHero().getMaxHp() < 50 && data.getHero().getHp()*100/data.getHero().getMaxHp() >= 10) {
        	color = Color.orange;
        } else {
        	color = Color.red;
        }
        SimpleGameView.drawRectangle(context, view, 380, 725, data.getHero().getHp()*100/data.getHero().getMaxHp(), 20, color, 1f);
        SimpleGameView.drawRectangleOutline(context, view, 380, 725, 100, 20, Color.BLACK, 3.0f);
        SimpleGameView.drawStringR(context, view, "" + data.getHero().getHp(), 415, 737, 20, Color.WHITE);
        drawEffects(context,view,data,data.getHero().getEffects(),300,650);

        SimpleGameView.draw(context, view, view.loader.image("Energy.png"), 225, 700, 75, 75);
        SimpleGameView.drawStringR(context, view, "" + data.getHero().getEnergy(), 243, 745, 45, Color.WHITE);
        
        SimpleGameView.draw(context, view, view.loader.image("logoBlock.png"), 310, 710, 50, 50);
        SimpleGameView.drawStringR(context, view, "" + data.getHero().getPp(), 323, 740, 30, Color.WHITE);
        
    }
	
	public static void drawBeasts(ApplicationContext context, SimpleGameView view,GameModel data) {
        int i = 0;
        for (Bestiary beast : data.getEnemyInFight()) {
            SimpleGameView.draw(context, view, view.loader.image(beast.getImgPath()), 1200+i, 750, 200, 200);
            SimpleGameView.draw(context, view, view.loader.image("logoBlock.png"), 1225+i, 715, 50, 50);
            SimpleGameView.drawStringR(context, view, "" + beast.getProtection(), 1238+i, 745, 30, Color.WHITE);
            
            SimpleGameView.drawRectangle(context, view, 1280+i, 725, 100, 20, Color.GRAY, 1f);
            Color color;
            if (beast.getHealth()*100/beast.getMaxHealth() >= 50) {
                color = Color.green;
            } else if (beast.getHealth()*100/40 < 50 && beast.getHealth()*100/40 >= 10) {
                color = Color.orange;
            } else {
                color = Color.red;
            }
            SimpleGameView.drawRectangle(context, view, 1280+i, 725, beast.getHealth()*100/beast.getMaxHealth(), 20, color, 1f);
            SimpleGameView.drawRectangleOutline(context, view, 1280+i, 725, 100, 20, Color.BLACK, 3.0f);
            SimpleGameView.drawStringR(context, view, "" + beast.getHealth(), 1312+i, 737, 20, Color.WHITE);
            
            String img = "logo" + beast.getNextAction() + ".png";
            SimpleGameView.draw(context, view, view.loader.image(img), 1200+i+75, 600, 75, 75);
            SimpleGameView.drawStringR(context, view, "" + beast.getIntAction(), 1292+i, 640, 35, Color.WHITE);
            
            drawEffects(context,view,data,beast.getEffects(),1200+i,660);
            if (data.getEnemySelected() == beast) {
            	drawRectangleOutline(context, view, 1200+i, 750, 200, 200, Color.RED, 3.0f);            	
            }
            i += 400;
        }
    }
	
	public static void drawEvent(ApplicationContext context, SimpleGameView view, GameModel data) {
        switch (data.getEventOnPosition()) {
            case 2:
                SimpleGameView.draw(context, view, view.loader.image("Chest.png"), 1300, 750, 200, 200);
                break;
            case 3:
                SimpleGameView.draw(context, view, view.loader.image("Napsack_Win.gif"), 1300, 750, 200, 200);
                break;
            case 4:
                SimpleGameView.draw(context, view, view.loader.image("Pockets_Win.gif"), 1500, 750, 200, 200);
                break;
            default:
                break;
        }
    }
	
	public static void drawHealer(ApplicationContext context, SimpleGameView view, GameModel data) {
		SimpleGameView.draw(context, view, view.loader.image("healpage.png"), 550, 40, 900, 500);
		drawCard(context,view,900,-10,100,200,30,"HEALER");
    }

	public static void drawCard(ApplicationContext context, SimpleGameView view, float x, float y, float width, float height, int font, String string) {
	    SimpleGameView.draw(context, view, view.loader.image("Card.png"), x, y, height, width);
	    SimpleGameView.drawStringR(context, view, string, (int)x+25, (int)(y+height/4), font, Color.WHITE);
	}
	
	/**
	 * dessine la fiche de description d'un item
	 * @param context
	 * @param view
	 * @param data
	 */
	private static void drawExplainCard(ApplicationContext context, SimpleGameView view, GameModel data) {
		var item = data.getItemSelected();
		if (item.getClass() != Manastone.class && item.getClass() != Gold.class) {
			SimpleGameView.draw(context, view, view.loader.image("explaincard.png"), 1350, 30, 500, 500);
			
			//EN HAUT
			drawStringR(context, view, item.getName(), 1500, 100, 28, Color.WHITE);
			SimpleGameView.draw(context, view, view.loader.image(item.getImgPath()), 1550, 150, 100, 100);
			int[][] size = data.getItemSelected().getSize();
	        for (int i = 0; i < size.length; i++) {
	            for (int j = 0; j < size[0].length; j++) {
	                	if (size[i][j] == 1) {
	                    	drawRectangle(context, view, 1450+j*30, 130+i*30, 30, 30, Color.WHITE, 0.5f);    
	
			        }
				}
	        }
	        int i = 0;
	        if (item.getCost() != null) {
		        for (var cost : item.getCost().keySet()) {
		        	if (item.getCost().get(cost) != 0) {
			            SimpleGameView.draw(context, view, view.loader.image(cost + ".png"), 1610 + i, 30, 50, 50);
			            drawStringR(context, view, String.valueOf(item.getCost().get(cost)), 1622 + i, 60, 30, Color.WHITE);
		        	}
			        i += 60;
		        }
	        }

	        HashMap<String, Integer> passifs = item.getPassifInteraction();
	        if (!passifs.isEmpty()) {
		        int j = 0; 
	        	if (passifs.containsKey("Projectile")) {
		            SimpleGameView.draw(context, view, view.loader.image("Projectile_Icon.png"), 1790, 64+j, 50, 50);
		            j += 60;
	        	}
	        	if (passifs.containsKey("Conductive")) {
		            SimpleGameView.draw(context, view, view.loader.image("Conductive_Icon.png"), 1790, 64+j, 50, 50);
		            j += 60;
	        	}
	        	if (passifs.containsKey("Heavy")) {
		            SimpleGameView.draw(context, view, view.loader.image("Heavy_Icon.png"), 1790, 64+j, 50, 50);
		            j += 60;
	        	}
	        	if (passifs.containsKey("Float")) {
		            SimpleGameView.draw(context, view, view.loader.image("Float_Icon.png"), 1790, 64+j, 50, 50);
		            j += 60;
	        	}
	        }
			// MILIEU
	        Color color = null;
	        if (item.getRarety() != null ) {
		        if (item.getRarety().equals("Common")) {
		            color = Color.WHITE;
		        } else if (item.getRarety().equals("Uncommon")) {
		        	color = Color.GREEN;
		        } else if (item.getRarety().equals("Rare")) {
		        	color = Color.ORANGE;
		        } else if (item.getRarety().equals("Legendary")) {
		        	color = Color.MAGENTA;
		        }
				drawStringR(context, view, item.getRarety(), 1630, 300, 20, color);
	        }
			drawStringR(context, view, item.getClass().getSimpleName(), 1480, 300, 20, Color.WHITE);
			drawRectangle(context, view, 1450, 315, 310, 2, Color.WHITE, 1);    
			// BAS
			drawStringR(context, view, item.getDescription(), 1440, 340, 15, Color.WHITE);
			if (item.getAction() != 0) {
				drawRectangle(context, view, 1450, 440, 310, 2, Color.WHITE, 1);
				drawStringR(context, view, "Modificateur appliqué :\n" + " " + item.getAction(), 1440, 475, 15, Color.WHITE);
			}
		} 
	}
	
	/**
	 * dessine la carte du marchand
	 * @param context
	 * @param view
	 * @param data
	 */
	public static void drawExplainCardWithGold(ApplicationContext context, SimpleGameView view, GameModel data) {
		var item = data.getItemSelected();
		drawExplainCard(context,view,data);
		if (item.getCost() != null) {
			drawCard(context, view, 1490, 500, 100, 200, 28, "Cost: " + data.getTrader().getStore().get(item));
		}
		
	}
	
	public static void drawInterface (ApplicationContext context, SimpleGameView view, GameModel data) {
        drawRectangle(context, view, 0, 0, 1920, 580, Color.darkGray, 1f);
        drawCard(context, view, 5, -20, 100, 200, 20, "Floor : " + data.getActualFloor());
        drawCard(context, view, 230, -20, 100, 200, 20, "Lvl " + data.getHero().getLevel() + " : " + data.getHero().getXP() + "/" + data.getHero().getPalierXP(data.getHero().getLevel()));

        drawWall(context, view, data);
        if (data.getIsFighting()) {
            drawCard(context, view, 50, 300, 100, 200, 20, "Scratch !");
            drawCard(context, view, 50, 380, 100, 200, 20, "Finir Tour");

        } else {
            if (data.isBackpackOpen()) {
                drawCard(context, view, 50, 380, 100, 200, 20, "Ouvrir carte");
            } else {
                drawCard(context, view, 50, 380, 100, 200, 20, "Ouvrir sac");
            }
        }
        if (data.getIsOrganize()) {
            drawCard(context, view, 50, 220, 100, 200, 15, "Fin Organisation");
        } else {
            drawCard(context, view, 50, 220, 100, 200, 20, "Organisation");
        }
    }
	
    public static void drawStartingScreen (ApplicationContext context, SimpleGameView view, GameModel data) {
        drawRectangle(context, view, 0, 0, 1920, 580, Color.darkGray, 1f);
        SimpleGameView.draw(context, view, view.loader.image("wall.png"), 0, -950, 4500, 3000);
        SimpleGameView.draw(context, view, view.loader.image("Title.png"), 100, 40, 1200, 600);
        SimpleGameView.draw(context, view, view.loader.image("portalart.png"), 1350, 400, 400, 400);
        SimpleGameView.draw(context, view, view.loader.image("Purse_Idle_Gif.gif"), 1200, 450, 350, 350);
        drawCard(context, view, 260, 650, 200, 400, 30,"Cliquer pour jouer");
        drawCard(context, view, 740, 650, 200, 400, 30,"Voir Hall of Fame");
    }
    
    public static void drawEndingScreen (ApplicationContext context, SimpleGameView view, GameModel data) {
        System.out.println(data.getScoreString());
        drawRectangle(context, view, 0, 0, 1920, 1400, Color.darkGray, 1f);
        drawStringR(context,view,"YOU DIED",500,250,200,Color.RED);
        drawStringR(context,view,"Your score was :   "+data.getScore(),550,350,50,Color.WHITE);
        SimpleGameView.draw(context, view, view.loader.image("death.png"), 650, 250, 600, 600);
        drawCard(context, view, 520, 750, 200, 400, 20, "Sauvegarder");
        drawCard(context, view, 980, 750, 200, 400, 20,"Quitter");
    }
    
    public static void drawEndingScreenWin (ApplicationContext context, SimpleGameView view, GameModel data) {
        drawRectangle(context, view, 0, 0, 1920, 1400, Color.darkGray, 1f);
        drawStringR(context,view,"YOU WON !",380,250,200,Color.GREEN);
        drawStringR(context,view,"Your score is :   "+data.getScore(),550,350,50,Color.WHITE);
        SimpleGameView.draw(context, view, view.loader.image("death.png"), 650, 250, 600, 600);
        drawCard(context, view, 520, 750, 200, 400, 20, "Sauvegarder");
        drawCard(context, view, 980, 750, 200, 400, 20,"Quitter");
    }
    
    public static void drawSavingScreen (ApplicationContext context, SimpleGameView view, GameModel data) {
        drawRectangle(context, view, 0, 0, 1920, 1400, Color.darkGray, 1f);
        drawStringR(context,view,"SAUVEGARDER VOTRE SCORE",90,250,100,Color.WHITE);
        drawStringR(context,view,"(Seulement les caracteres minuscules)",500,400,40,Color.LIGHT_GRAY);
        drawStringR(context,view,"Your name is :   "+data.getGameScore().getNameGame(),550,500,50,Color.WHITE);
        drawCard(context, view, 520, 750, 200, 400, 20, "Confirmer");
        drawCard(context, view, 980, 750, 200, 400, 20,"Quitter");
    }
    
    public static void drawHallOfFame(ApplicationContext context, SimpleGameView view, GameModel data) {
        drawRectangle(context, view, 0, 0, 1920, 1400, Color.darkGray, 1f);
        drawStringR(context, view, "HALL OF FAME", 470, 120, 150, Color.WHITE);
        var lstStr = data.getGameScore().loadFirstThreeLines();
        
        if (lstStr.size() >= 1) {
            SimpleGameView.draw(context, view, view.loader.image("gold-medal.png"), 300, 200, 170, 170);
            drawStringR(context, view, lstStr.get(0), 550, 300, 45, Color.WHITE);
        }
        
        if (lstStr.size() >= 2) {
            SimpleGameView.draw(context, view, view.loader.image("silver-medal.png"), 300, 450, 170, 170);
            drawStringR(context, view, lstStr.get(1), 550, 550, 45, Color.WHITE);
        }
        
        if (lstStr.size() >= 3) {
            SimpleGameView.draw(context, view, view.loader.image("bronze-medal.png"), 300, 700, 170, 170);
            drawStringR(context, view, lstStr.get(2), 550, 800, 45, Color.WHITE);
        }
        
        drawCard(context, view, 780, 850, 200, 400, 20, "Quitter");
    }
	
	public static void drawEffects(ApplicationContext context, SimpleGameView view, GameModel data, HashMap<String, Integer> effets, int x, int y) {
	    for (String effet : effets.keySet()) {
	        String img = "logoStatus_" + effet + ".png";
	        SimpleGameView.draw(context, view, view.loader.image(img), x, y, 45, 45);
	        drawStringR(context, view, String.valueOf(effets.get(effet)), x+25, y+50, 32, Color.WHITE);
	        x += 50;
	    }
	}

    //----------------------------------------DEMARRAGE DU DESSIN --------------------------------------------------------------------------------------------------------------
    /**
     * lance le dessin du jeu
     * @param context
     * @param view
     * @param data
     */
    public static void draw(ApplicationContext context, SimpleGameView view, GameModel data) {
        context.renderFrame(graphics -> view.drawALL(context,view,data)); // do not modify
    }

    /**
     * permet de tout dessiner en fonction du GameModel
     * @param context
     * @param view
     * @param data
     */
    private void drawALL(ApplicationContext context, SimpleGameView view, GameModel data) {
        if (data.getIsBegin()) {
            drawStartingScreen(context,view,data);
        } else if (data.hallOfFame()) {
            drawHallOfFame(context,view,data);
        } else if (data.isSaving()) {
            drawSavingScreen(context,view,data);
        } else if (data.getHero().getHp() <= 0) {
            drawEndingScreen(context,view,data); 
        } else if (data.isWin()) {
            drawEndingScreenWin(context,view,data);
        } else {
            drawInterface(context,view,data);
            drawHero(context,view,data);
            drawBeasts(context,view,data);
            drawEvent(context,view,data);
            if (data.isBackpackOpen() || data.getIsFighting()) {
                 drawBackpack(context,view,data.getBackpack(), data);
                 drawItems(context,view,data);
                 if (data.getItemSelected() != null) {
                     if (data.getEventOnPosition() == 4) {
                         drawExplainCardWithGold(context,view,data);
                       }  else {
                         drawExplainCard(context,view,data);
                       }
                 }
                 if (data.getEventOnPosition() == 3) {
                      drawHealer(context,view,data);
                  }  
            } else {
                drawMap(context, view, data.getActualMap());
                drawCharacterPosition(context, view, data);
            }

        }
    }
   

}