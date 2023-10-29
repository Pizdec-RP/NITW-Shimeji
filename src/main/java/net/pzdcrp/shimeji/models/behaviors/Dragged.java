package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class Dragged extends Behavior {
	public Dragged(Model host) {
		super(host);
	}
	
	@Override
	public void tick() {
		host.setFrame("dragged", 0);
		if (!host.dragged) {
			ended = true;
		}
	}
	
	@Override
	public Behavior nextBeh() {
		return new Fall(host);
	}
}
