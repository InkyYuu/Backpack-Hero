package projet.game.items;

import java.util.HashMap;

public interface Items {
		int getUID();
		void setUID(int UID);
		int[] getXY();
		void setXY(int X, int Y);
		/**
		 * échange les tailles maximales de l'objet
		 */
		void switchLenght();
		void setSize(int[][] matrix);
		String getDescription();
	    String getName();
	    String getImgPath();
	    int getAction();
		void setAction(int I);
		void resetAction();
	    HashMap<String, Integer> getCost();
	    int[][] getSize();
	    String getRarety();
	    double getRotation();
	    void setRotation();
		/**
		 * renvoi une copie de l'item
		 * @return
		 */
		Items copie();

		
	    /**
	     * permet de tourner l'objet de 90°
	     * @param matrix
	     * @return
	     */
	    public static int[][] rotateItem(int[][] matrix) {
	        int m = matrix.length;
	        int n = matrix[0].length;
	        int[][] rotatedMatrix = new int[n][m];
	        
	        for (int i = 0; i < m; i++) {
	            for (int j = 0; j < n; j++) {
	                rotatedMatrix[j][m - 1 - i] = matrix[i][j];
	            }
	        }
	        
	        return rotatedMatrix;
	    }
		HashMap<String, Integer> getOnUseInteraction();
		HashMap<String, Integer> getEachTurnInteraction();
		HashMap<String, Integer> getPassifInteraction();



}

