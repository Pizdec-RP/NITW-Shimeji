package test123;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinDef.HWND;

import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class GraphicsTestWindow {
	public JFrame frame;
	private JComponent canvas;
	private BufferedImage image;
	private BufferedImage i1, i2;
	private HWND hwnd;
	private User32 user32;
	
	public GraphicsTestWindow() {
		try {
			user32 = User32.INSTANCE;
			frame = new JFrame("Image Overlay");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setSize(200, 200);
		    frame.setUndecorated(true);
		    frame.setBackground(new Color(0, 0, 0, 0)); // Прозрачный фон
		    frame.setAlwaysOnTop(true);
		    
		    canvas = new JComponent() {
		        @Override
		        protected void paintComponent(Graphics g) {
		            super.paintComponent(g);
		            g.drawImage(image, 0, 0, this);
		        }
		    };
		    frame.setContentPane(canvas);
		    
		    frame.setVisible(true);
	    
	    
			i1 = ImageIO.read(new File("C:/Users/qm080/Pictures/pisuncity.jpg"));
			i2 = ImageIO.read(new File("C:/Users/qm080/Pictures/unknown.png"));
			hwnd = new WinDef.HWND(Native.getWindowPointer(frame));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	boolean b = false;
	public GraphicsTestWindow start() {
		new Thread(()->{
			while (true) {
				if (b) {
					image = i1;
				} else {
					image = i2;
				}
				b = !b;
				user32.SetWindowPos(hwnd, new WinDef.HWND(Pointer.NULL), 0, 0, 0, 0, WinUser.SWP_NOMOVE | WinUser.SWP_NOSIZE | WinUser.SWP_NOZORDER | WinUser.SWP_SHOWWINDOW | WinUser.SWP_NOACTIVATE);
				canvas.repaint();
				GameU.log("repaint!");
				GameU.sleep(500);
			}
		}).start();
		return this;
	}

}
