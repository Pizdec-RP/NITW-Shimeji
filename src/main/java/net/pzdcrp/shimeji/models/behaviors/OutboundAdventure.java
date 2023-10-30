package net.pzdcrp.shimeji.models.behaviors;

import net.pzdcrp.shimeji.Main;
import net.pzdcrp.shimeji.models.AABB;
import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class OutboundAdventure extends Behavior {
	public int stage = 0;
	public OutboundAdventure(Model host) {
		super(host);
	}
	
	private int frameindex = 0, nextframekd = 20;
	private boolean increasing = true, totallyended = true;
	@Override
	public void tick() {
		host.ignoredbb = 1;
		//GameU.log(host.source.replace("imgs/", "")+" "+stage);
		/*if (host.y > host.world.miny) {
			ended = true;
		}*/
		if (host.getid() == 1) {//gregg
			if (!Main.mae.onGround) {
				ended = true;
				totallyended = false;
			}
			if (stage == 0) {//walk to border
				if (host.x >= 0 && host.x <= Model.halfmodelwidth) {
					stage = 1;
					frameindex = 0;
					nextframekd = 0;
					increasing = true;
				} else {
					if (--nextframekd <= 0) {
						nextframekd = 15;
						if (increasing) frameindex++;
				        else frameindex--;
				        if (frameindex == 2) increasing = false;
				        else if (frameindex == 0) increasing = true;
					}
					
					host.setFrame("run",frameindex);
					
					host.direction = -1;
					host.velx += host.getRunSpeed();
				}
			} else if (stage == 1) {//call mae
				Main.mae.setBehavior(new OutboundAdventure(Main.mae));
				host.setFrame("stand",0);
				stage = 3;
				host.direction = 1;
			} else if (stage == 3) {//wait for it
				host.direction = 1;
				host.setFrame("stand",0);
				if (Main.mae.getHitbox().collide(host.getHitbox())) {
					stage = 4;
					frameindex = 0;
					nextframekd = 0;
					increasing = true;
				}
			} else if (stage == 4) {//go out
				if (--nextframekd <= 0) {
					nextframekd = 20;
					if (increasing) frameindex++;
			        else frameindex--;
			        if (frameindex == 2) increasing = false;
			        else if (frameindex == 0) increasing = true;
				}
				
				host.setFrame("walk", frameindex);
				
				host.direction = -1;
				host.velx += host.getWalkSpeed();
				
				if (host.x < -(Model.modelwidth*2)) {
					stage = 5;//MathU.rndi(5, 6);
				}
			} else if (stage == 5) {//throw out
				((OutboundAdventure)Main.mae.beh).stage = 2;
				host.setFrame("fall",0);
				host.direction = 1;
	            host.velx = MathU.rndi(16, 32) * host.direction;
				host.vely = MathU.rndi(15, 25);
				stage = 9;
			} else if (stage == 6) {//climbup random
				
			} else if (stage == 9) {//fly
				host.setFrame("fall",0);
				if (host.onGround) {
					stage = 10;
					((OutboundAdventure)Main.mae.beh).stage = 3;
				}
			} else if (stage == 10) {//stay fallen
				host.setFrame("layFromFall",0);
				host.velx = 0;
				host.vely = 0;
				if (Main.mae.getHitbox().collide(host.getHitbox())) {
					ended = true;
				}
			}
		} else if (host.getid() == 0) {
			if (!(Main.gregg.beh instanceof OutboundAdventure)) {
				ended = true;
			}
			if (stage == 0) {//walk to gregg
				if (Main.gregg.getHitbox().collide(host.getHitbox())) {
					stage = 1;
				} else {
					if (--nextframekd <= 0) {
						nextframekd = 20;
						if (increasing) frameindex++;
				        else frameindex--;
				        if (frameindex == 2) increasing = false;
				        else if (frameindex == 0) increasing = true;
					}
					
					host.setFrame("walk", frameindex);
					
					host.direction = -1;
					host.velx += host.getWalkSpeed();
				}
			} else if (stage == 1) {
				host.setFrame("borred", 0);
			} else if (stage == 2) {//sets from gregg | face to gregg
				AABB mnm = Main.gregg.getHitbox().clone();
				AABB hostbb = host.getHitbox();
				mnm.setMinY(hostbb.minY);
				mnm.setMaxY(hostbb.maxY);
				
				if (mnm.collide(hostbb)) {
					host.setFrame("lookup",0);
				} else {
					host.setFrame("stand",0);
				}
				
				if (Main.gregg.x <= host.x) {
					host.direction = -1;
				} else if (Main.gregg.x > host.x) {
					host.direction = 1;
				}
			} else if (stage == 3) {
				stage = 4;
				frameindex = 0;
				nextframekd = 0;
				increasing = true;
			} else if (stage == 4) {
				host.direction = 1;
				if (--nextframekd <= 0) {
					nextframekd = 15;
					if (increasing) frameindex++;
			        else frameindex--;
			        if (frameindex == 2) increasing = false;
			        else if (frameindex == 0) increasing = true;
				}
				
				host.setFrame("run",frameindex);
				
				host.velx += host.getRunSpeed();
				
				if (Main.gregg.getHitbox().collide(host.getHitbox())) {
					ended = true;
				}
			}
		}
	}
	
	@Override
	public Behavior nextBeh() {
		if (host.getid() == 1) {
			host.swingdirection();
			host.ignoredbb = -2;
			if (totallyended) {
				return new LayFromFall(host);
			} else {
				return new Stand(host);
			}
		} else if (host.getid() == 0) {
			host.direction = 1;
			host.ignoredbb = -2;
			if (MathU.rndb()) {
				return new MaeBorred(host);
			} else {
				return new Sit(host);
			}
		}
		return null;
	}
	
	@Override
	public boolean captureFall() {
		return true;
	}
	
	@Override
	public boolean captureDrag() {
		return true;
	}
	
	@Override
	public int[] getDisallowed() {
		return null;
	}
}
