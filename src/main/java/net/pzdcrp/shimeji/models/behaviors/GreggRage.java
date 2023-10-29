package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class GreggRage extends Behavior {
	public GreggRage(Model host) {
		super(host);
		this.timeout = MathU.rndi(150, 400);
	}
	
	private int frameindex = 0, nextframekd = 15;
	private boolean increasing = true;
	
	@Override
	public void tick() {
		timeout--;
		if (timeout <= 0) {
			ended = true;
			host.changeDirRandom();
		}
		if (--nextframekd == 0) {
			nextframekd = 10;
			if (increasing) frameindex++;
	        else frameindex--;
	        if (frameindex == 3) increasing = false;
	        else if (frameindex == 0) increasing = true;
		}
		
		host.setFrame("rage",frameindex);
	}
	
	@Override
	public Behavior nextBeh() {
		int rnd = MathU.rndi(0, 1);
		if (rnd == 0) {
			return new Stand(host);
		} else if (rnd == 1) {
			return new Lookup(host);
		}
		GameU.end("nobehavior");
		return null;
	}
	
	@Override
	public int[] getDisallowed() {
		return new int[] {0};
	}
}
