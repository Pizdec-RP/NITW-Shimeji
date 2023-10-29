package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.MathU;

public class Fall extends Behavior {
	boolean agr = false;
	public Fall(Model host) {
		super(host);
	}

	@Override
	public void tick() {
		host.setFrame("fall", 0);
		if (host.onGround) {
			ended = true;
		}
	}
	
	@Override
	public Behavior nextBeh() {
		if (host.getid() == 1) {
			if (host.dragcounter >= 3) {
				return new LayFromFall(host, true);
			}
		}
		return new LayFromFall(host);
	}
}
