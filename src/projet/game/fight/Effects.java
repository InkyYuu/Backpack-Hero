package projet.game.fight;

import java.util.HashMap;

public interface Effects{
	public int getHaste();

    public int getRage();

    public int getSlow();

    public int getWeak();

    public int getPoison();

    public int getBurn();

    public int getFreeze();

    public int getRegen();

    public boolean isZombie();

    public int getCharm();

    public int getDodge();

    public int getRoughSide();

    public int getSleep();
    
    public void setHaste(int h);
    
    public void setRage(int rage);
    
    public void setSlow(int slow);
    
    public void setWeak(int weak);
    
    public void setPoison(int poison);

    public void setBurn(int burn);

    public void setFreeze(int freeze);

    public void setRegen(int regen);
    
    public void setZombie(boolean zombie);

    public void setCharm(int charm);

    public void setDodge(int dodge);

    public void setRoughSide(int roughSide);

    public void setSleep(int sleep);
    
    public HashMap<String, Integer> getEffects();
}
