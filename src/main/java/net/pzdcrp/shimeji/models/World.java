package net.pzdcrp.shimeji.models;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import net.pzdcrp.shimeji.utils.GameU;

public class World {
	public List<AABB> windowBorder = new ArrayList<>();
	public List<AABB> walls = new ArrayList<>();
	public List<AABB> otherwindows = new ArrayList<>();
	public int miny, screenheight, screenwidth;
	public AABB roof;
	public int offset;

	public World() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		miny = dim.height - winSize.height;
		offset = miny/2;
		//GameU.log(miny);
		miny -= offset;
		screenheight = dim.height;
		screenwidth = dim.width;
		
		AABB floor = new AABB(-999999, 0, 999999, miny);
		floor.setid(2);
		windowBorder.add(floor);
		
		roof = new AABB(-999999, winSize.height+offset, 999999, 999999);
		roof.setid(3);
		windowBorder.add(roof);
		
		AABB leftborder = new AABB(-999999, -999999, 0, 999999);
		leftborder.setid(1);
		windowBorder.add(leftborder);
		walls.add(leftborder);
		
		AABB rightborder = new AABB(screenwidth, -999999, 999999, 999999);
		rightborder.setid(0);
		windowBorder.add(rightborder);
		walls.add(rightborder);
		
		
		//winthread();
	}
	
	public void winthread() {
		new Thread(()->{
			while (true) {
				/*List<AABB> windowSizes = new ArrayList<>();
	
		        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		        GraphicsDevice[] screens = ge.getScreenDevices();
	
		        for (GraphicsDevice screen : screens) {
		            GraphicsConfiguration[] configurations = screen.getConfigurations();
	
		            for (GraphicsConfiguration config : configurations) {
		                Rectangle bounds = config.getBounds();
		                //Window window = screen.getFullScreenWindow();
		                windowSizes.add(new AABB(bounds));
		                /*if (window == null || !window.getBounds().equals(bounds)) {
		                    boolean isOverlapped = false;
	
		                    for (Window otherWindow : Window.getWindows()) {
		                        if (otherWindow != window && otherWindow.isVisible() && otherWindow.getBounds().intersects(bounds)) {
		                            isOverlapped = true;
		                            break;
		                        }
		                    }
	
		                    if (!isOverlapped) {
		                        windowSizes.add(new AABB(bounds));
		                    }
		                    
		                }
		            }
		        }
		        otherwindows = windowSizes;
		        GameU.arrayPrint(windowSizes);*/
		        
				/*User32 user32 = User32.INSTANCE;
		        WinDef.HWND hwnd = user32.GetTopWindow(null);
		        WinDef.RECT rect = new WinDef.RECT();
		        Pointer zero = Pointer.NULL;

		        List<AABB> windowBounds = new ArrayList<>();

		        while (hwnd != null) {
		            user32.GetWindowRect(hwnd, rect);

		            int left = rect.left;
		            int top = rect.top;
		            int right = rect.right;
		            int bottom = rect.bottom;

		            AABB aabb = new AABB(left, top, right, bottom);
		            windowBounds.add(aabb);

		            hwnd = user32.GetWindow(hwnd, WinUser.GW_HWNDNEXT);
		        }*/
		        
		        GameU.sleep(100);
			}
		}).start();
	}
}
