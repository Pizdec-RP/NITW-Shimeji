
package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class Run extends Behavior {
	public Run(Model host) {
		super(host);
		this.timeout = MathU.rndi(200, 1000);
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
			nextframekd = 15;
			if (increasing) frameindex++;
	        else frameindex--;
	        if (frameindex == 2) increasing = false;
	        else if (frameindex == 0) increasing = true;
		}
		
		host.setFrame("run",frameindex);
		
		host.velx += host.getRunSpeed();
	}
	
	@Override
	public Behavior nextBeh() {
		int rnd = MathU.rndi(0, 2);
		if (rnd == 0) {
			return new Stand(host);
		} else if (rnd == 1) {
			return new Walk(host);
		} else if (rnd == 2) {
			return new Lookup(host);
		}
		GameU.end("nobehavior");
		return null;
	}
}
