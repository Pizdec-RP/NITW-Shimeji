package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class Sleepy extends Behavior {
	public Sleepy(Model host) {
		super(host);
		this.timeout = MathU.rndi(200, 400);
	}
	
	@Override
	public void tick() {
		host.setFrame("sleepy", 0);
		timeout--;
		if (timeout <= 0) {
			ended = true;
		}
	}
	
	@Override
	public Behavior nextBeh() {
		if (host.getid() == 0) {//mae
			int rnd = MathU.rndi(0, 5);
			if (rnd == 0 || rnd == 1) {
				return new Stand(host);
			} else if (rnd == 2) {
				return new Lay(host);
			} else if (rnd == 3) {
				return new Walk(host);
			} else if (rnd == 4) {
				return new MaeBorred(host);
			} else if (rnd == 5) {
				return new Lookup(host);
			}
		} else if (host.getid() == 1) {//greg
			int rnd = MathU.rndi(0, 5);
			if (rnd == 0 || rnd == 1) {
				return new Stand(host);
			} else if (rnd == 2) {
				return new Lay(host);
			} else if (rnd == 3) {
				return new Walk(host);
			} else if (rnd == 4) {
				return new Run(host);
			} else if (rnd == 5) {
				return new Lookup(host);
			}
		}
		GameU.end("nobehavior");
		return null;
	}
}
