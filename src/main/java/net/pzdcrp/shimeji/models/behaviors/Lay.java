package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class Lay extends Behavior {
	private static final int[] offset = new int[] {0, -15};//gregg
	public Lay(Model host) {
		super(host);
		this.timeout = MathU.rndi(300, 600);
	}
	
	@Override
	public void tick() {
		host.setFrame("lay", 0);
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
		int rnd = MathU.rndi(0, 3);
		if (rnd == 0 || rnd == 1) {
			return new Stand(host);
		} else if (rnd == 2) {
			return new Sit(host);
		} else if (rnd == 3) {
			return new Sleep(host);
		}
		GameU.end("nobehavior");
		return null;
	}
}
