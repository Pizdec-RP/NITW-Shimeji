package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;

public class Behavior {
	protected Model host;
	public int timeout = 100;
	public boolean ended = false;

	public Behavior(Model host) {
		this.host = host;
	}
	
	public void tick() {
		
	}
	
	public Behavior nextBeh() {
		return null;
	}
	
	public int[] getOffset() {
		return null;
	}
	
	public boolean captureFall() {
		return false;
	}
	
	public boolean captureDrag() {
		return false;
	}
	
	public int[] getDisallowed() {
		return null;
	}

	public boolean captureGravity() {
		return false;
	}
}
