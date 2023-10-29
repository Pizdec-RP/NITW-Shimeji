package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class Walk extends Behavior {
	public Walk(Model host) {
		super(host);
		this.timeout = MathU.rndi(200, 1000);
	}
	
	private int frameindex = 0, nextframekd = 20;
	private boolean increasing = true;
	
	@Override
	public void tick() {
		timeout--;
		if (timeout <= 0) {
			ended = true;
			host.changeDirRandom();
		}
		if (--nextframekd == 0) {
			nextframekd = 20;
			if (increasing) frameindex++;
	        else frameindex--;
	        if (frameindex == 2) increasing = false;
	        else if (frameindex == 0) increasing = true;
		}
		
		host.setFrame("walk",frameindex);
		
		host.velx += host.getWalkSpeed();
	}
	
	@Override
	public Behavior nextBeh() {
		if (host.getid() == 0) {//mae
			int rnd = MathU.rndi(0, 2);
			if (rnd == 0 || rnd == 1) {
				return new Stand(host);
			} else if (rnd == 2) {
				return new Lookup(host);
			}
		} else if (host.getid() == 1) {//greg
			int rnd = MathU.rndi(0, 4);
			if (rnd == 0 || rnd == 1) {
				return new Stand(host);
			} else if (rnd == 2) {
				return new Run(host);
			} else if (rnd == 3) {
				return new GreggRage(host);
			} else if (rnd == 4) {
				return new Lookup(host);
			}
		}
		GameU.end("nobehavior");
		return null;
	}
}
