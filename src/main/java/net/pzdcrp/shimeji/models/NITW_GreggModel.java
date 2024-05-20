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

public class NITW_GreggModel extends Model {
	/*private static final Map<Behavior, int[]> behoffset = new HashMap<>() {{//x,y
		//put(Behavior.sit, new int[] {0, -15});
	}};*/
	
	@Override
	public Map<String, List<BufferedImage>> getFrames() {
		return frames;
	}

	public NITW_GreggModel(World world) {
		super("imgs/gregg/", world);
	}

	@Override
	public int getid() {
		return 1;
	}
}
