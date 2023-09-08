package projet.game.map;

public class Room {
    private final String iud;
    private final Openings openings;
    private int choix;
    private final String imgPath;
    private boolean canGoThrough;
    private boolean isFinal;

    public Room(String iud, Openings openings, int choix ,boolean isFinal) {
        this.iud = iud;
        this.openings = openings;
        this.choix =  choix;
        StringBuilder sb = new StringBuilder();
        for (int coord : openings.getCoords()) {
            sb.append(coord);
        }
        sb.append(".png");
        this.imgPath = sb.toString();
        if (this.choix != 0) {
            this.canGoThrough = false;
        } else {
            this.canGoThrough = true;
        }
        this.isFinal = isFinal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Room ID: ").append(iud).append("\n");
        sb.append("Openings: ").append(openings).append("\n");
        sb.append("Image path: ").append(imgPath).append("\n");
        sb.append("Can go through: ").append(canGoThrough).append("\n");
        if (choix != 0) {
            sb.append("Beast: ").append(choix).append("\n");
        }
        return sb.toString();
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getRoomName() {
        return iud;
    }

    public boolean getIsFinal() {
        return isFinal;
    }

    public int[] getXY() {
        return null;
    }
    
    public int getApparition() {
        return choix;
    }
    
    /**
     * @param choix
     */
    public void setApparition(int choix) {
    	this.choix = choix;
    }
    
    /**
     * @param f
     */
    public void setIsFinal(boolean f) {
    	this.isFinal = f;
    }

	public boolean getCanGoThrough() {
		return canGoThrough;
	}
	
	/**
	 * @param i
	 */
	public void setCanGoThrough(boolean i) {
		this.canGoThrough = i ;
	}


}
