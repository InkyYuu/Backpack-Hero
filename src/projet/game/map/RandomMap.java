package projet.game.map;

import java.util.ArrayList;
import java.util.Random;

public class RandomMap {
    private final int floor;
    private Room[][] carte;
    private String begin;

    public RandomMap(int floor) {
        this.floor = floor;
        this.begin = null;
        this.carte = generateRandomMap(floor);
    }
    
    // FONCTION GENERATION MAP ---------------------------------------------------------------------------------------------------------------------------

	private Room[][] generateRandomMap(int floor) {
        var map = new Room[5][11];
        
        Random random = new Random();
        int startRow = random.nextInt(5);
        int startCol = 0;
        var b = new Room(generateIUD(floor, startRow, startCol), Openings.UN_UN_UN_UN, 0, false);
        this.begin = b.getRoomName();
        		
        int interRow = random.nextInt(5);
        int interCol = random.nextInt(2) + 3;
        
        int secoRow;
        do {
        	secoRow = random.nextInt(5);
        } while ( secoRow == interRow);
        int secoCol = random.nextInt(3) + 6;
        
        int endRow = random.nextInt(5);
        int endCol = 10;
        
        generatePath(map, startRow, startCol, interRow, interCol);
        generatePath(map, interRow, interCol, secoRow, secoCol);
        generatePath(map, secoRow, secoCol, endRow, endCol);
        map[endRow][endCol].setIsFinal(true);
        
        generateAdjacentRooms(map, floor, startRow, startCol, endRow, endCol);      
        map[endRow][endCol].setIsFinal(true);
        
        generateApparitions(map, startRow, startCol, endRow, endCol);
        map[endRow][endCol].setIsFinal(true);
        return map;
    }
    
	/**
	 * créer un chemin entre deux points
	 * @param map
	 * @param startRow
	 * @param startCol
	 * @param endRow
	 * @param endCol
	 */
	private void generatePath(Room[][] map, int startRow, int startCol, int endRow, int endCol) {
		int currentRow = startRow;
        int currentCol = startCol;
        map[currentRow][currentCol] = new Room(generateIUD(floor, currentRow, currentCol), Openings.UN_UN_UN_UN, 0, false);
        
        while (currentRow != endRow || currentCol != endCol) {
            int nextRow, nextCol;
            if (currentRow < endRow) {
                nextRow = currentRow + 1;
                nextCol = currentCol;
            } else if (currentRow > endRow) {
                nextRow = currentRow - 1;
                nextCol = currentCol;
            } else if (currentCol < endCol) {
                nextRow = currentRow;
                nextCol = currentCol + 1;
            } else {
                nextRow = currentRow;
                nextCol = currentCol - 1;
            }
            
            map[nextRow][nextCol] = new Room(generateIUD(floor, nextRow, nextCol), Openings.UN_UN_UN_UN, 0, false);
            currentRow = nextRow;
            currentCol = nextCol;
        }
	}
	
	/**
	 * génère des room adjacentes sur les bords du chemin principal
	 * @param map
	 * @param floor
	 * @param startRow
	 * @param startCol
	 * @param endRow
	 * @param endCol
	 */
	private void generateAdjacentRooms(Room[][] map, int floor, int startRow, int startCol, int endRow, int endCol) {
	    Random random = new Random();
	    ArrayList<Room> adjacentRooms = new ArrayList<>();
	    
	    while (adjacentRooms.size() <= random.nextInt(6)+3 ) {
	        for (int row = 0; row < map.length; row++) {
	            for (int col = 0; col < map[row].length; col++) {
	                Room currentRoom = map[row][col];
	                
	                if (currentRoom != null) {
	                    if (random.nextDouble() < 0.4) {
	                        int adjacentRow = row;
	                        int adjacentCol = col;
	                        
	                        int direction = random.nextInt(4);
	                        
	                        switch (direction) {
	                            case 0: 
	                                adjacentRow--;
	                                break;
	                            case 1: 
	                                adjacentCol++;
	                                break;
	                            case 2: 
	                                adjacentRow++;
	                                break;
	                            case 3:
	                                adjacentCol--;
	                                break;
	                        }
	                        
	                        if (isValidCoordinates(adjacentRow, adjacentCol, map.length, map[row].length, map)
	                                && (adjacentRow != startRow || adjacentCol != startCol)
	                                && (adjacentRow != endRow || adjacentCol != endCol)) {
	                        	Room adjacentRoom = new Room(generateIUD(floor, adjacentRow, adjacentCol), Openings.UN_UN_UN_UN, 0, false);
	                            map[adjacentRow][adjacentCol] = adjacentRoom;
	                            adjacentRooms.add(map[adjacentRow][adjacentCol]);
	                        }
	                    }
	                }
	            }
	        }
	    }

	    generateExtrasApparitions(map, adjacentRooms, startRow, startCol, endRow, endCol);
    }


	/**
	 * Verifie si il est possible de créer des pièces adjacentes
	 * @param row
	 * @param col
	 * @param numRows
	 * @param numCols
	 * @param map
	 * @return
	 */
	private boolean isValidCoordinates(int row, int col, int numRows, int numCols, Room[][] map) {
	    if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
	        return false;
	    }
	    int adjacentRooms = 0;
	    if (row - 1 >= 0 && map[row - 1][col] != null) {
	        adjacentRooms++;
	    }
	    if (row + 1 < numRows && map[row + 1][col] != null) {
	        adjacentRooms++;
	    }
	    if (col - 1 >= 0 && map[row][col - 1] != null) {
	        adjacentRooms++;
	    }
	    if (col + 1 < numCols && map[row][col + 1] != null) {
	        adjacentRooms++;
	    }
	    return adjacentRooms < 2;
	}
    
    /**
     * Genère l'IUD des room
     * @param floor
     * @param row
     * @param col
     * @return
     */
    private String generateIUD(int floor, int row, int col) {
        return String.format("%d%d%d", floor, row, col);
    }
	
    /**
     * Place le guérisseur et le marchand
     * @param map
     * @param adjacentRooms
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     */
    private void generateExtrasApparitions(Room[][] map, ArrayList<Room> adjacentRooms, int startRow, int startCol, int endRow, int endCol) {
        Random random = new Random();

        int randomIndexHealer;
        Room roomHealer;

        do {
            randomIndexHealer = random.nextInt(adjacentRooms.size());
            roomHealer = adjacentRooms.get(randomIndexHealer);
        } while (roomHealer.getApparition() != 0 || roomHealer == map[endRow][endCol] || roomHealer == map[startRow][startCol]);
        roomHealer.setApparition(3);
        roomHealer.setCanGoThrough(false);

        int randomIndexTrader;
        Room roomTrader;

        do {
            randomIndexTrader = random.nextInt(adjacentRooms.size());
            roomTrader = adjacentRooms.get(randomIndexTrader);
        } while (roomTrader.getApparition() != 0 || roomTrader == map[endRow][endCol] || roomTrader == map[startRow][startCol]);
        roomTrader.setApparition(4);
        roomTrader.setCanGoThrough(false);
        
    }

    /**
     * Place les ennemis et les coffres
     * @param map
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     */
    private void generateApparitions(Room[][] map, int startRow, int startCol, int endRow, int endCol) {
        int countMob = 3; // CHOIX 1
        int countChest = 2; // CHOIX 2
        int[] apparitionCounters = new int[]{countMob, countChest};
        Random random = new Random();

        int emptyRoomCount = 0;

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                Room currentRoom = map[row][col];
                if (currentRoom != null && currentRoom.getApparition() == 0 && (row != startRow && col != startCol)) {
                    emptyRoomCount++;
                }
            }
        }

        for (int apparition = 1; apparition <= 2; apparition++) {
            int count = apparitionCounters[apparition - 1];
            while (count > 0 && emptyRoomCount > 0) {
                int row = random.nextInt(map.length);
                int col = random.nextInt(map[row].length);
                Room currentRoom = map[row][col];
                if (currentRoom != null && currentRoom.getApparition() == 0 && currentRoom != map[startRow][startCol] && currentRoom != map[endRow][endCol]) {
                    currentRoom.setApparition(apparition);
                    currentRoom.setCanGoThrough(false);
                    apparitionCounters[apparition - 1]--;
                    emptyRoomCount--;
                    count--;
                }
            }
        }
    }


    // FONCTION AUTRES ---------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * 
	 */
	public void afficherCarte() {
		for (Room[] ligne : carte) {
            for (Room salle : ligne) {
                if (salle == null) {
                    System.out.print("______\t");
                } else {
                    System.out.print(salle.getRoomName() + " " + salle.getApparition() + "\t");
                }
            }
            System.out.println();
        }
    }
	
	/**
	 * @return carte
	 */
	public Room[][] getCarte() {
		return carte;
	}
	
	/**
	 * @return pièce du début
	 */
	public String getStartPosition() {
		return begin;
	}
	
	/**
	 * @param room
	 * @return Position X de la pièce
	 */
	public int getRoomPosX(Room room) {
        if (room != null) {
        for (int i = 0; i < this.getCarte().length; i++) {
            for (int j = 0; j < this.getCarte()[i].length; j++) {
                if (this.getCarte()[i][j] == null) {
                    continue;
                }
                else if (this.getCarte()[i][j].getRoomName().equals(room.getRoomName())) {
                    return i;
                }
            }
        }}
        return 0;
    }

    /**
     * @param room
     * @return Position Y de la pièce
     */
    public int getRoomPosY(Room room) {
        if (room != null) {
        for (int i = 0; i < this.getCarte().length; i++) {
            for (int j = 0; j < this.getCarte()[i].length; j++) {
                if (this.getCarte()[i][j] == null) {
                    continue;
                }
                else if (this.getCarte()[i][j].getRoomName().equals(room.getRoomName())) {
                    return j;
                }
            }
        }}
        return 0;
    }
	
	/**
	 * @param name
	 * @return Room avec le nom de pièce name
	 */
	public Room getRoom(String name) {
        for (int i = 0; i < this.getCarte().length; i++) {
            for (int j = 0; j < this.getCarte()[i].length; j++) {
                if (this.getCarte()[i][j] == null) {
                    continue;
                }
                else if (this.getCarte()[i][j].getRoomName().equals(name)) {
                    return this.getCarte()[i][j];
                }
            }
        }
        return null;
    }

	/**
	 * @param x
	 * @param y
	 * @return room
	 */
	public Room getRoomWithCoordonates(int x, int y) {
		    int row = (y - 50) / 75;
		    int col = (x - (1920/2 - 11*75/2))  / 75;
		    if (row < 0 || row >= 5 || col < 0 || col >= 11) {
		        return null;
		    }
		    return getCarte()[row][col];
	}

	/**
	 * vérifie si le chemin pour se déplacer est valide
	 * @param startRow
	 * @param startCol
	 * @param endRow
	 * @param endCol
	 * @return
	 */
	public boolean isValidPath(int startRow, int startCol, int endRow, int endCol) {
	    if (!isRoomValid(startRow, startCol) || !isRoomValid(endRow, endCol)) {
	        return false;
	    }
	    if (startRow == endRow && startCol == endCol) {
	        return true;
	    }
	    boolean[][] visited = new boolean[carte.length][carte[0].length];
	    return isValidRecursive(startRow, startCol, endRow, endCol, visited);
	}

	/**
	 * fonction récursive pour isValidPath
	 * @param currRow
	 * @param currCol
	 * @param endRow
	 * @param endCol
	 * @param visited
	 * @return
	 */
	private boolean isValidRecursive(int currRow, int currCol, int endRow, int endCol, boolean[][] visited) {
	    if (currRow == endRow && currCol == endCol) {
	        return true;
	    }
	    visited[currRow][currCol] = true;
	    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

	    for (int[] direction : directions) {
	        int nextRow = currRow + direction[0];
	        int nextCol = currCol + direction[1];

	        if (isRoomValid(nextRow, nextCol) && (isPathValid(nextRow, nextCol) || (nextRow == endRow && nextCol == endCol)) && !visited[nextRow][nextCol]) {
	            if (isValidRecursive(nextRow, nextCol, endRow, endCol, visited)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}


	/**
	 * vérifie si la room existe
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isRoomValid(int row, int col) {
	    int numRows = carte.length;
	    int numCols = carte[0].length;
	    return row >= 0 && row < numRows && col >= 0 && col < numCols;
	}

	/**
	 * vérifie si on peut passer
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isPathValid(int row, int col) {
	    if (!isRoomValid(row, col)) {
	        return false;
	    }

	    Room room = carte[row][col];
	    if (room != null) {
	    }
	    return room != null && room.getCanGoThrough();
	}


}
	