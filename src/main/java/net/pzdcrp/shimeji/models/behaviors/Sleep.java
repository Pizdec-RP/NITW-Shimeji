package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class Sleep extends Behavior {
	private static final int[] offset = new int[] {0, -15};//gregg
	public Sleep(Model host) {
		super(host);
		this.timeout = MathU.rndi(800, 1200);
	}
	
	@Override
	public void tick() {
		host.setFrame("sleep", 0);
		timeout--;
		if (timeout <= 0) {
			ended = true;
		}
	}
	
	@Override
	public int[] getOffset() {
		if (host.getid() == 1) {
			return offset;
		}
		return null;
	}
	
	@Override
	public Behavior nextBeh() {
		int rnd = MathU.rndi(0, 4);
		if (rnd == 0 || rnd == 1) {
			return new Lay(host);
		} else if (rnd == 2 || rnd == 3) {
			return new Sit(host);
		} else if (rnd == 4) {
			return new Stand(host);
		}
		GameU.end("nobehavior");
		return null;
	}
}
