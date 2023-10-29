package net.pzdcrp.shimeji.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class MaeModel extends Model {
	
	
	@Override
	public Map<String, List<BufferedImage>> getFrames() {
		return frames;
	}

	public MaeModel(World world) {
		super("imgs/mae/", world);
	}

	@Override
	public void tick() {
		super.tick();
		
		
		//GameU.log("kd: "+nextbehaviorkd+" beh: "+behavior.toString()+" pos: "+x+" "+y+" d:"+deltaTime());

	}
	
}
