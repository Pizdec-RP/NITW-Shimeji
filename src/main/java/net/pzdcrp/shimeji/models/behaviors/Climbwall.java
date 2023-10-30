package net.pzdcrp.shimeji.models.behaviors;

import java.util.ArrayList;
import java.util.List;

import net.pzdcrp.shimeji.models.AABB;
import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

/**
 * @author Pizdec RP
 * I hate this shitass code
 */
public class Climbwall extends Behavior {
	private static final List<int[]> offsets = new ArrayList<>() {{
		add(new int[] {-45,0});//gregg left
		add(new int[] {45,0});//gregg right
		add(new int[] {-40,0});//mae left
		add(new int[] {40,0});//mae right
	}};
	private boolean up;
	public Climbwall(Model host, AABB wall, boolean up) {
		super(host);
		this.up = up;
		if (host.direction == 1) {
			host.x = wall.minX - Model.oneandhalf;
		} else {
			host.x = wall.maxX - Model.halfmodelwidth;
		}
		GameU.log(host.x+" "+host.world.walls.get(1).minX);
	}
	
	private int frameindex = 0, nextframekd = 20, restticks = 0;
	
	@Override
	public void tick() {
		if (host.coly || MathU.rndi(0, 1000) == 55) {
			ended = true;
		}
		//host.onGround = false;
		host.velx = 0;
		
		if (--restticks > 0) {
			host.setFrame("climb", 0);
			host.vely = 0;
		} else { 
			if (--nextframekd == 0) {
				nextframekd = 20;
				if (frameindex == 0) frameindex = 1;
				else if (frameindex == 1) frameindex = 0;
		        
		        if (frameindex == 0) {
		        	restticks = 50;
		        }
			}
			host.setFrame("climb", frameindex);
			
			if (up) host.y += 0.3f;
			else host.y -= 0.3f;
		}
	}
	
	@Override
	public int[] getOffset() {
		if (host.getid() == 1) {
			if (host.direction == 1) {//right
				return offsets.get(1);
			} else {//left
				return offsets.get(0);
			}
		} else if (host.getid() == 0) {
			if (host.direction == 1) {//right
				return offsets.get(3);
			} else {//left
				return offsets.get(2);
			}
		}
		return null;
	}
	
	@Override
	public Behavior nextBeh() {
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
