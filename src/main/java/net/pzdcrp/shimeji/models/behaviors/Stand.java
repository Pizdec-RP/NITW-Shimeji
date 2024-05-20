package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class Stand extends Behavior {
	public Stand(Model host) {
		super(host);
		this.timeout = MathU.rndi(200, 1000);
	}
	
	@Override
	public void tick() {
		host.setFrame("stand", 0);
		timeout--;
		if (timeout <= 0) {
			ended = true;
			host.changeDirRandom();
		}
	}
	
	@Override
	public Behavior nextBeh() {
		if (host.getid() == 0) {//mae
			int rnd = MathU.rndi(0, 5);
			if (rnd == 0) {
				return new Sit(host);
			} else if (rnd == 1) {
				return new Walk(host);
			} else if (rnd == 2) {
				return new Run(host);
			} else if (rnd == 3) {
				return new Sleepy(host);
			} else if (rnd == 4) {
				return new MaeBorred(host);
			} else if (rnd == 5) {
				return new Lookup(host);
			}
		} else if (host.getid() == 1) {//greg
			if (MathU.rndi(0, 15) == 15) {
				return new Adventure(host);
			} else {
				int rnd = MathU.rndi(0, 7);
				if (rnd == 0) {
					return new Sit(host);
				} else if (rnd == 1 || rnd == 2) {
					return new Walk(host);
				} else if (rnd == 3 || rnd == 4) {
					return new Run(host);
				} else if (rnd == 5) {
					return new Sleepy(host);
				} else if (rnd == 6) {
					return new GreggRage(host);
				} else if (rnd == 7) {
					return new Lookup(host);
				}
			}
		}
		GameU.end("nobehavior");
		return null;
	}
}
