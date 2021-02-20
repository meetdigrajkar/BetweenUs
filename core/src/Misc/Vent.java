package Misc;
import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.mmog.Client;
import com.mmog.players.Imposter;

public class Vent {
	private ArrayList<Imposter> imposters;
	private boolean containsPlayer;
	private ArrayList<Integer> connectedVents;
	private Rectangle rec;
	
	public Vent(Rectangle rec) {
		this.setRec(rec);
		setContainsPlayer(false);
		connectedVents = new ArrayList<Integer>();
		imposters = new ArrayList<Imposter>();
	}
	
	public void addConnectedVent(int i) {
		connectedVents.add(i);
	}
	
	public ArrayList<Integer> getConnectedVents(){
		return connectedVents;
	}
	public Imposter getImposter(Imposter imp) {
		Imposter toReturn = null;
		for(Imposter i: imposters) {
			if(i.getPlayerName().equals(imp.getPlayerName())) {
				toReturn = imp;
			}
		}
		return toReturn;
	}
	
	public boolean hasImposter(Imposter imp) {
		for(Imposter i: imposters) {
			if(i.getPlayerName().equals(imp.getPlayerName())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isContainsPlayer() {
		return containsPlayer;
	}

	public void setContainsPlayer(boolean containsPlayer) {
		this.containsPlayer = containsPlayer;
	}
	
	public void addImposter(Imposter imp) {
		imposters.add(imp);
	}
	
	public boolean removeImposter(Imposter imp) {
		return imposters.remove(imp);
	}
	
	public ArrayList<Imposter> getImposters() {
		return imposters;
	}

	public void setImposters(ArrayList<Imposter> imposters) {
		this.imposters = imposters;
	}

	public Rectangle getRec() {
		return rec;
	}

	public void setRec(Rectangle rec) {
		this.rec = rec;
	}
}
