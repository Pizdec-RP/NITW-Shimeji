package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class MaeBorred extends Behavior {
	public MaeBorred(Model host) {
		super(host);
		this.timeout = MathU.rndi(200, 600);
	}
	
	@Override
	public void tick() {
		host.setFrame("borred", 0);
		timeout--;
		if (timeout <= 0) {
			ended = true;
			host.changeDirRandom();
		}
	}
	
	@Override
	public Behavior nextBeh() {
		if (host.getid() == 0) {//mae
			int rnd = MathU.rndi(0, 4);
			if (rnd == 0) {
				return new Sit(host);
			} else if (rnd == 1) {
				return new Walk(host);
			} else if (rnd == 2) {
				return new Stand(host);
			} else if (rnd == 3) {
				return new Sleepy(host);
			} else if (rnd == 4) {
				return new Lookup(host);
			}
		}
		GameU.end("nobehavior");
		return null;
	}
	
	@Override
	public int[] getDisallowed() {
		return new int[] {1};
	}
}
