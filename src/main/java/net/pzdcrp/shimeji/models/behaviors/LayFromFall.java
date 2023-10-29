package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class LayFromFall extends Behavior {
	boolean agr = false;
	public LayFromFall(Model host) {
		super(host);
		if (host.getid() == 0) {
			timeout = MathU.rndi(300, 700);
		} else {
			timeout = MathU.rndi(100, 200);
		}
	}
	
	public LayFromFall(Model host, boolean b) {
		super(host);
		agr = true;
		if (host.getid() == 1) {
			timeout = MathU.rndi(70, 120);
		} else {
			if (host.getid() == 0) {
				timeout = MathU.rndi(300, 700);
			} else {
				timeout = MathU.rndi(100, 200);
			}
		}
	}
	
	@Override
	public void tick() {
		host.setFrame("layFromFall", 0);
		timeout--;
		if (timeout <= 0) {
			ended = true;
		}
	}
	
	@Override
	public Behavior nextBeh() {
		if (agr && host.getid() == 1) return new GreggRage(host); 
		int rnd = MathU.rndi(0, 1);
		if (rnd == 0) {
			return new Sit(host);
		} else if (rnd == 1) {
			return new Stand(host);
		}
		GameU.end("nobehavior");
		return null;
	}
}
