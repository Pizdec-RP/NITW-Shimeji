package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.MathU;

public class Holdontop extends Behavior {
	private static final int[] offsetg = new int[] {0, 21};//gregg
	private static final int[] offsetm = new int[] {0, 13};//mae
	private boolean b1;
	public Holdontop(Model host) {
		super(host);
		host.y = host.world.roof.minY-Model.modelwidth;
	}
	
	public Holdontop(Model host, boolean waitforuncollidexbeforeends) {
		super(host);
		this.b1 = waitforuncollidexbeforeends;
		host.y = host.world.roof.minY-Model.modelwidth;
	}
	
	private int frameindex = 0, nextframekd = 20, restticks = 0;
	private boolean increasing = true;
	
	@Override
	public void tick() {
		if ((host.colx && !b1) || MathU.rndi(0, 200) == 55) {
			ended = true;
		} else {
			b1 = false;
		}
		//host.onGround = false;
		host.vely = 0;
		if (--restticks > 0) {
			host.setFrame("hold", 1);
			host.velx = 0;
		} else { 
			if (--nextframekd == 0) {
				nextframekd = 20;
				if (increasing) frameindex++;
		        else frameindex--;
		        if (frameindex == 2) increasing = false;
		        else if (frameindex == 0) increasing = true;
		        
		        if (frameindex == 1 && MathU.rndi(0, 4) == 0) {
		        	restticks = MathU.rndi(50, 200);
		        }
			}
			host.setFrame("hold", frameindex);
			
			host.velx = 0.3f * host.direction;
			
		}
	}
	
	@Override
	public int[] getOffset() {
		if (host.getid() == 1) {
			return offsetg;
		} else if (host.getid() == 0) {
			return offsetm;
		}
		return null;
	}
	
	@Override
	public Behavior nextBeh() {
		if (ended) {
			//return new Climbwall(host, false);
		} else {
			return new Fall(host);
		}
		return new Fall(host);
	}
	
	@Override
	public boolean captureGravity() {
		return true;
	}
	
	@Override
	public boolean captureFall() {
		return true;
	}
}
