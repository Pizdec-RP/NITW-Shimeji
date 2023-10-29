package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class Sit extends Behavior {
	private static final int[] offset = new int[] {0, -15};
	public Sit(Model host) {
		super(host);
		this.timeout = MathU.rndi(200, 1000);
	}
	
	@Override
	public void tick() {
		host.setFrame("sit", 0);
		timeout--;
		if (timeout <= 0) {
			ended = true;
		}
	}
	
	@Override
	public Behavior nextBeh() {
		int rnd = MathU.rndi(0, 2);
		if (rnd == 0) {
			return new Stand(host);
		} else if (rnd == 1) {
			return new Lay(host);
		} else if (rnd == 2) {
			return new Lookup(host);
		}
		GameU.end("nobehavior");
		return null;
	}
	
	@Override
	public int[] getOffset() {
		if (host.getid() == 0) {
			return offset;
		}
		return null;
	}
}
