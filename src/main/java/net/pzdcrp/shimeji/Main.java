package net.pzdcrp.shimeji;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
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

import org.apache.commons.io.FileUtils;

import net.pzdcrp.shimeji.models.GreggModel;
import net.pzdcrp.shimeji.models.MaeModel;
import net.pzdcrp.shimeji.models.Model;
import net.pzdcrp.shimeji.models.World;
import net.pzdcrp.shimeji.utils.GameU;

public class Main {
	static World world;
	public static Model mae,gregg;
	
	public static void main(String[] args) throws MalformedURLException {
		Map<String, File> fs = loadFiles("general/");
		File ico = fs.get("icon.ico");
		if (SystemTray.isSupported()) {
            try {
                // Создание SystemTray
                SystemTray systemTray = SystemTray.getSystemTray();

                // Загрузка иконки для трея
                ImageIcon trayImage = new ImageIcon(ico.toURI().toURL());
                Image image = trayImage.getImage();

                // Создание объекта TrayIcon
                TrayIcon trayIcon = new TrayIcon(image, "Trayed");
                trayIcon.setImageAutoSize(true);

                // Создание контекстного меню
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

                // Добавление иконки в трей
                systemTray.add(trayIcon);

                boot(args);

            } catch (AWTException e) {
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
		mae = new MaeModel(world);
		gregg = new GreggModel(world);
	}
	
	public static Map<String, File> loadFiles(String source) {
		Map<String, File> files = new LinkedHashMap<>();
		
		//low-ass coding nigga there is 1:24 am, ima tired of it!
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String directoryPath = source;
        URL directoryUrl = classLoader.getResource(directoryPath);
        if (directoryUrl == null) {
            GameU.end("Директория не найдена: " + directoryPath);
        }
        
        File directory = new File(directoryUrl.getFile());

        if (!directory.isDirectory()) {
            classLoader = new Main().getClass().getClassLoader();
            try {
	            classLoader = Main.class.getClassLoader();
	            Enumeration<URL> resources = classLoader.getResources(directoryPath);

	            while (resources.hasMoreElements()) {
	                directoryUrl = resources.nextElement();

	                try {
	                    JarURLConnection jarConnection = (JarURLConnection) directoryUrl.openConnection();
	                    JarFile jarFile = jarConnection.getJarFile();

	                    Enumeration<JarEntry> entries = jarFile.entries();

	                    while (entries.hasMoreElements()) {
	                        JarEntry entry = entries.nextElement();
	                        if (!entry.isDirectory() && entry.getName().startsWith(directoryPath)) {
	                            File f = new File("temp");
	                            InputStream is = jarFile.getInputStream(entry);
	                            FileUtils.copyInputStreamToFile(is, f);
	                            files.put(entry.getName().replace(source, ""), f);
	                        }
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            
            } catch (Exception e) {
                e.printStackTrace();
                GameU.end("");
            }
            
        } else {
	        for (File file : Objects.requireNonNull(directory.listFiles())) {
	            if (file.isFile()) {
	                files.put(file.getName(), file);
	            }
	        }
        }

	    return files;
	}
}
