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

public class NITW_MaeModel extends Model {
	
	
	@Override
	public Map<String, List<BufferedImage>> getFrames() {
		return frames;
	}

	public NITW_MaeModel(World world) {
		super("imgs/mae/", world);
	}
	
	@Override
	public int getid() {
		return 0;
	}
}
