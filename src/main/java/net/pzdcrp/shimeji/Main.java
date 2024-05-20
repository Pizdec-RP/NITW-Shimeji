package net.pzdcrp.shimeji;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import net.pzdcrp.shimeji.models.NITW_GreggModel;
import net.pzdcrp.shimeji.models.NITW_MaeModel;
import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.models.World;
import net.pzdcrp.shimeji.utils.GameU;

public class Main {
	static World world;
	public static Model mae,gregg;
	public static final boolean debug = true;
	
	public static void main(String[] args) throws IOException {
		if (SystemTray.isSupported()) {
            try {
                SystemTray systemTray = SystemTray.getSystemTray();

                Image image = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);

                TrayIcon trayIcon = new TrayIcon(image, "Trayed");
                trayIcon.setImageAutoSize(true);

                PopupMenu popupMenu = new PopupMenu();
                MenuItem exitItem = new MenuItem("Выход");
                
                popupMenu.add(exitItem);
                trayIcon.setPopupMenu(popupMenu);

                exitItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GameU.end("end");
                    }
                });

                systemTray.add(trayIcon);

                boot(args);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Системный трей не поддерживается на вашей платформе.");
            boot(args);
        }
	}
	
	public static void boot(String[] args) {
		GameU.log("./");
		world = new World();
		mae = new NITW_MaeModel(world);
		gregg = new NITW_GreggModel(world);
	}
}
