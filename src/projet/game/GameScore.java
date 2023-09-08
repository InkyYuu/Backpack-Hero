package projet.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.umlv.zen5.KeyboardKey;
import projet.game.items.Items;

public class GameScore {
	private final LocalDate date;
	private int score;
	private String namegame;
    private final String SCORES_FILE = "savefile.txt";
	
	public GameScore() {
		this.date = LocalDate.now();
		this.score = 0;
		this.namegame = "";
	}
	
	/**
     * Ajoute 10 points au score pour chaque ennemi tué
     */
	public void addScoreKillEnemy() {
		score += 10;
	}
	
	 /**
     * Calcule le score final en fonction des paramètres spécifiés et met à jour le score
     *
     * @param hpMax        points de vie maximum
     * @param level        niveau du jeu
     * @param actualFloor  étage actuel
     * @param inventory    inventaire des objets
     * @param isWin        indique si le jeu a été gagné
     */
	public void FinalScore(int hpMax, int level, int actualFloor, ArrayList<Items> inventory, boolean isWin) {
		for (Items i : inventory) {
			if (i.getRarety() != null) {
			    String rarete = i.getRarety();
			    switch (rarete) {
			        case "Common":
			        	score += 6;
			            break;
			        case "Uncommon" :
			        	score += 10;
			        	break;
			        case "Rare" :
			        	score += 24;
			        	break;
			        case "Legendary" :
			        	score += 50;
			        	break;
			        default:
			            break;
			    }
		    }
		}
		score += 50*level;
		score += 100*actualFloor;
		score *= hpMax;
		if (isWin) {
			score *= 2;
		}
	}
	
	/**
	 * renvoi le score int
	 * @return
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * renvoi l'objet en String
	 */
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Game Score:\n");
	    sb.append("Date: ").append(date.toString()).append("\n");
	    sb.append("Score: ").append(score).append("\n");
	    sb.append("Game Name: ").append(namegame).append("\n");
	    return sb.toString();
	}
	
	/**
     * Enregistre le score dans un fichier.
     */
	public void recordScore() {
	    String playerName = namegame + "_" + date + "_" + score;
	    HashMap<String, String> scoresMap = loadScores();
	    scoresMap.put(playerName, date + "::" + namegame + "::" + score);
	    saveScores(scoresMap);
	}

	/**
     * Charge les trois meilleurs scores du fichier
     * @return 
     */
	public ArrayList<String> loadFirstThreeLines() {
		ArrayList<String> linesList = new ArrayList<>();
	    try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
	        String line;
	        int lineCount = 0;
	        while ((line = reader.readLine()) != null && lineCount < 3) {
	            String[] parts = line.split("::");
	            if (parts.length == 4) {
	                String namegame = parts[2];
	                String score = parts[3];
	                String timestamp = parts[1];

	                String formattedString = score + " : " + namegame + " - le " + timestamp;
	                linesList.add(formattedString);
	            }
	            lineCount++;
	        }
	    } catch (IOException e) {
	        e.printStackTrace(); // Gestion de l'erreur de lecture du fichier
	    }
	    return linesList;
	}
	
	/**
	 * renvoi une map du fichier contenant les scores
	 * @return
	 */
	private HashMap<String, String> loadScores() {
	    HashMap<String, String> scoresMap = new HashMap<>();
	    try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split("::");
	            if (parts.length == 4) {
	                String playerName = parts[0];
	                String record = parts[1] + "::" + parts[2] + "::" + parts[3];
	                scoresMap.put(playerName, record);
	            }
	        }
	    } catch (IOException e) {

	    }
	    return scoresMap;
	}

	/**
	 * enregistre les scores dans le fichier avec un tri décroissant 
	 * @param scoresMap
	 */
	private void saveScores(HashMap<String, String> scoresMap) {
	    List<Map.Entry<String, String>> sortedScores = scoresMap.entrySet().stream()
	            .sorted(Comparator.comparingInt((Map.Entry<String, String> entry) -> {
	                String[] parts = entry.getValue().split("::");
	                if (parts.length >= 3) {
	                    return Integer.parseInt(parts[2]);
	                }
	                return 0;
	            }).reversed())
	            .collect(Collectors.toList());

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE))) {
	        for (Map.Entry<String, String> entry : sortedScores) {
	            writer.write(entry.getKey() + "::" + entry.getValue());
	            writer.newLine();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
    /**
     * Ajoute une lettre au nom du joueur
     * @param keyboardKey
     */
    public void addLetter(KeyboardKey keyboardKey) {
    	namegame += keyboardKey.toString();
    }
    
    /**
     *  Enlève la dernière lettre au nom du joueur
     */
    public void delLetter() {
        if (namegame.length() > 0) {
            namegame = namegame.substring(0, namegame.length() - 1);
        }
    }
    
    public String getNameGame() {
    	return namegame;
    }

	
}
