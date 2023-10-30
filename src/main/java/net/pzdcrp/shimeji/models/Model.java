package net.pzdcrp.shimeji.models;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import net.pzdcrp.shimeji.Main;
import net.pzdcrp.shimeji.models.behaviors.*;
import net.pzdcrp.shimeji.utils.GameU;
import net.pzdcrp.shimeji.utils.MathU;

public class Model {
	public static float scale = 0.8f;
	public float x = 0, y = 0, velx = 0, vely = 0;
	public BufferedImage image = null;
    private User32 user32;
    private WinDef.HWND hwnd;
    public String source;
    public int taskbarHeight;
    public AABB boundingbox;
    public World world;
    public int ignoredbb = -2;
    
    public int direction = Math.random() > 0.5d ? 1 : -1;// -1 - left 1 - right
    public Behavior beh;
	private JFrame frame;
	private JComponent imageComponent;
	
	public Map<String, List<BufferedImage>> frames = new HashMap<>();
	private Thread tickthread;
	
	public static int modelwidth = (int)(128*scale), halfmodelwidth = modelwidth/2, oneandhalf = modelwidth+halfmodelwidth;
	
	public void kill() {
		tickthread.interrupt();
		if (getid() == 0) {
			Main.mae = null;
		} else if (getid() == 1) {
			Main.gregg = null;
		}
		frame.dispose();
	}
    
    static int tickrate = 10, i = 0;
	public void startTickLoop() {
		tickthread = new Thread(() -> {
			long timeone;
			while (true) {
	        	timeone = System.nanoTime();
	        	try {
	        		tick();
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        		GameU.end("err trig");
	        	}
	        	/*GameU.log(i);
	        	if (i++ == 400) {
	        		models.add(new MaeModel());
	        	}*/
	    	    long two = System.nanoTime();
	    	    int elapsed = (int)(two - timeone);
	    	    int normaled = elapsed/1_000_000;
	    	    int additional = elapsed/100_000-normaled*10;
	    	    int itog = normaled + (additional >= 5 ? 1 : 0);
	    	    int tosleep = tickrate - itog;
	    	    if (tosleep > 0) {
	    	    	GameU.sleep(tosleep);
	    	    }
			}
		});
		tickthread.start();
	}
    
	private int mouseX, mouseY;
	@SuppressWarnings("serial")
	public Model(String imgsource, World world) {
		this.source = imgsource;
		this.world = world;
		boundingbox = new AABB(0,0,modelwidth,modelwidth);
		//netThread();
		//screenwidth = dim.width;
		//screenheight = dim.height;
	    user32 = User32.INSTANCE;
	    frame = new JFrame(source.replace("imgs/", ""));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(modelwidth, modelwidth);
	    frame.setUndecorated(true);
	    frame.setBackground(new Color(0, 0, 0, 0));
	    frame.setAlwaysOnTop(true);
	    frame.setLocationByPlatform(true);
	    //frame.setVisible(true);
	    
	    /*try {
	        image = ImageIO.read(new File(source+"stand_0.png"));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }*/
	    imageComponent = new JComponent() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            if (direction == 1) {
	            	/*Graphics2D g2d = (Graphics2D) g.create();
	                AffineTransform transform = new AffineTransform();
	                transform.scale(-1, 1);
	                transform.translate(-image.getWidth(), 0);
	                g2d.drawImage(image, transform, this);*/
	                g.drawImage(image, modelwidth, 0, 0, modelwidth, 0, 0, modelwidth, modelwidth, this);
	                //g2d.dispose();
	            } else {
	            	g.drawImage(image, 0, 0, this);
	            }
	        }
	    };
	    
	    frame.addMouseMotionListener(new MouseAdapter() {
	    	@Override
            public void mouseDragged(MouseEvent e) {
	    		if (beh.captureDrag()) return;
	    		int button = e.getModifiersEx();
	    		if (button == 1024) {
	                int x = e.getXOnScreen() - mouseX;
	                int y = e.getYOnScreen() - mouseY;
	                frame.setLocation(x, y);
	                setPos(x,y);
	                dragged = true;
	    		}
            }
        });

	    frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	if (beh.captureDrag()) return;
            	if (e.getButton() == 1) {
            		dragcounter+=2;
	                mouseX = e.getX();
	                mouseY = e.getY();
	                dragged = true;
            	}
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
            	dragged = false;
            }
        });
	    
	    frame.setContentPane(imageComponent);
	    
	    if (Main.debug) setButtons();
	    
	    x = MathU.rndi(200, world.screenwidth-200);
	    y = world.screenheight-(modelwidth*2);
	    
	    frame.setVisible(true);
	    
	    hwnd = new WinDef.HWND(Native.getWindowPointer(frame));
	    loadimgs();
	    
	    beh = new Stand(this);
	    
		startTickLoop();
		
		//frame.setIconImage(frames.get("stand").get(0));
	}
	
	private String beforetag = "";
	private int beforeindex = 0;
	public void setFrame(String tag, int index) {
		if (beforetag.equals(tag) && beforeindex == index) return;
		beforetag = tag;
		beforeindex = index;
		this.image = getFrames().get(tag).get(index);
		//frame.setimage(image);
		imageComponent.repaint();
		//updateimg();
		//imageComponent.renderImage(image, direction);
	}
	public Map<String, List<BufferedImage>> getFrames() {
		GameU.end("unsetted method");
		return null;
	}
	
	public void loadimgs() {
		try {
			Map<String, BufferedImage> files = new LinkedHashMap<>();
			
			//low-ass coding nigga there is 1:24 am, ima tired of it!
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	        String directoryPath = source;
	        URL directoryUrl = classLoader.getResource(directoryPath);
	        if (directoryUrl == null) {
	            GameU.end("Директория не найдена: " + directoryPath);
	        }
	        
	        File directory = new File(directoryUrl.getFile());

	        if (!directory.isDirectory()) {
	            classLoader = getClass().getClassLoader();
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
		                            BufferedImage image = ImageIO.read(jarFile.getInputStream(entry));
		                            files.put(entry.getName().replace(source, ""), image);
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
		                try {
		                    BufferedImage image = ImageIO.read(file);
		                    files.put(file.getName(), image);
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }
		            }
		        }
	        }
	        
	        Map<String, BufferedImage> sortedImageMap = new TreeMap<>(files);
	        
		    for (Entry<String, BufferedImage> file : sortedImageMap.entrySet()) {
		    	String fn = file.getKey();
		    	/*Image img = new Image(frame.display, loader.load(source+fn)[0]);
		    	img.setBackground(new Color(0,0,0,0));*/
		    	String[] splited = fn.split("_");
		    	String type = splited[0];
		    	//GameU.log(fn);
		    	int id = Integer.parseInt(splited[1]
		    			.split("\\.")[0]);
		    	if (!getFrames().containsKey(type)) {
		    		getFrames().put(type, new ArrayList<BufferedImage>());
		    	}
		    	BufferedImage resized = GameU.scaleImage(file.getValue(), scale);
		    	getFrames().get(type).add(id, resized);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			GameU.end("err");
		}
	}
	
	//float bx = 0, by = 0;
	public void setPos(float xx, float yy) {
		//this.velx = xx - bx;
		//this.vely = yy - by;
		this.x = xx;
		this.y = world.screenheight - yy - modelwidth;
		//bx = x;
		//by = y;
	}
	
	public void updatepos() {
		if (dragged) return;
		int[] offfset = beh.getOffset();
		int nx = 0, ny = world.miny;
		if (offfset != null) {
			nx += offfset[0];
			ny += offfset[1];
		}
		int lx = (int)((nx+x) + modelwidth/2), ly = (int)(world.screenheight - (ny+y) - modelwidth);
		
		frame.setLocation(lx, ly);
	}
	
	long beforetime = System.currentTimeMillis();
	long now;
	public long deltaTime() {
		now = System.currentTimeMillis();
		long i = now - beforetime;
		beforetime = now;
		return i;
	}
	
	public void setBehavior(Behavior nbeh) {
		int[] dwd = nbeh.getDisallowed();
		if (dwd != null) {
			for (int i : dwd) {
				if (i == getid()) return;
			}
		}
		if (dragged && !nbeh.captureDrag()) return;
		if (!onGround && !nbeh.captureFall()) return;
		beh = nbeh;
	}
	
	public void changeDirRandom() {
		direction = Math.random() > 0.5d ? 1 : -1;
	}
	
	public void swingdirection() {
		direction = direction == 1 ? -1 : 1;
        imageComponent.repaint();
	}
	
	public float getRunSpeed() {
		return 0.794565f * Model.scale * direction;
	}
	
	public float getWalkSpeed() {
		return 0.3f * Model.scale * direction;
	}
	
	public void applyMovement() {
	    if (velx != 0 || vely != 0) {
	        List<AABB> nb = new CopyOnWriteArrayList<>();
	        nb.addAll(world.windowBorder);
	        nb.addAll(world.otherwindows);
	        for (AABB b : nb) {
	        	if (b.id == ignoredbb) {
	        		nb.remove(b);
	        	}
	        }
	        boolean wasMovingDown = vely < 0, colx = false, coly = false;
	        float bx, by;
	        for (AABB collidedBB : nb) {
	        	by = vely;
	            vely = collidedBB.calculateYOffset(this.getHitbox(), vely);
	            if (by != vely) {
	                coly = true;
	            }
	        }
	        
	        if (wasMovingDown && vely == 0) {
	            onGround = true;
	        } else {
	            onGround = false;
	        }
	        
	        
	        this.y += vely;
	        
	        for (AABB collidedBB : nb) {
	        	bx = velx;
	            velx = collidedBB.calculateXOffset(this.getHitbox(), velx);
	            if (bx != velx) {
	                colx = true;
	            }
	        }
	        
	        this.colx = colx;
	        this.coly = coly;
	        
	        this.x += velx;
	        
            if (this.onGround) {
                velx *= 0.6;
            } else {
                velx *= 0.98;
            }
	        
	        if (Math.abs(velx) < 0.02f) velx = 0;
	        if (Math.abs(vely) < 0.02f) vely = 0;
	        
	    }
	}
	
	/*public int nextframekd = 0, nextbehaviorkd = 100, frameindex = 0;
	private boolean increasing = true;*/
	public int dragcounter = 0;
	private JPopupMenu popupMenu;
	public boolean onGround = false, dragged = false, colx = false, coly = false;
	public void tick() {
		//GameU.log("drag count: "+dragcounter);
		//user32.SetWindowPos(hwnd, new WinDef.HWND(Pointer.NULL), 0, 0, 0, 0, WinUser.SWP_NOMOVE | WinUser.SWP_NOSIZE | WinUser.SWP_NOZORDER | WinUser.SWP_SHOWWINDOW | WinUser.SWP_NOACTIVATE);
		frame.setSize(modelwidth, modelwidth);
		
		if (popupMenu != null && popupMenu.isVisible()) return;
		
		if (dragged && !beh.captureDrag()) {
			if (!(beh instanceof Dragged)) {
				beh = new Dragged(this);
			}
		} else {
			if (!beh.captureGravity()) {
				vely -= (0.3*scale);
				vely *= 0.9800000190734863D;
			}
			
			applyMovement();
			AABB wch;
			//GameU.log(beh.getClass().getName());
			
			//приоритет на зацепку за стену, потоm за потолок
			boolean captwall = beh instanceof Climbwall;
			
			if (colx && !captwall) {
				if (MathU.rndi(0, 5) == 0) {
					wch = getHitbox().grow(10, 0);
				} else {
					swingdirection();
					wch = getHitbox();
				}
				coly = false;
	        } else {
	        	wch = getHitbox();
	        }
			
			if (!captwall) {
				for (AABB wall : world.walls) {
					if (wall.id == this.ignoredbb) continue;
					if (wall.collide(wch)) {
						
						captwall = true;
						if (wch.getCenterX() >= wall.getCenterX()) {//capt right side of wall
							GameU.log("true -1");
							direction = -1;//face on wall
							beh = new Climbwall(this, wall, true);
						} else {//left
							GameU.log("true 1");
							direction = 1;
							beh = new Climbwall(this, wall, true);
						}
					}
				}
			}
			
			if (captwall) {
				if (world.roof.collide(getHitbox())) {
					if (!(beh instanceof Holdontop)) {
						beh = new Holdontop(this, true);
						swingdirection();
					}
				}
			} else if (world.roof.collide(getHitbox())) {
				if (!(beh instanceof Holdontop)) {
					beh = new Holdontop(this);
				}
			} else {
				if (!onGround && !beh.captureFall()) {
					if (!(beh instanceof Fall)) {
						beh = new Fall(this);
					}
				}
			}
			
			if (beh.ended) {
				beh = beh.nextBeh();
				if (dragcounter > 0) dragcounter--;
			}
		}
		beh.tick();
		updatepos();
	}
	
	public AABB getHitbox() {
		return boundingbox.clone().offset(x+halfmodelwidth,y);
	}
	
	public int getid() {
		GameU.end("");
		return -1;
	}
	
	public void setButtons() {
		popupMenu = new JPopupMenu();
		frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) {
                    popupMenu.setVisible(false);
                }
            }
        });
        
        JMenuItem skipbehavior = new JMenuItem("skip behavior");
        popupMenu.add(skipbehavior);
        skipbehavior.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            beh = beh.nextBeh();
        }});
        
        JMenuItem sd = new JMenuItem("swing direction");
        popupMenu.add(sd);
        sd.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            swingdirection();
        }});
        
        JMenuItem throwtest = new JMenuItem("throwtest");
        popupMenu.add(throwtest);
        throwtest.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            velx = 20*direction;
			vely = 15;
        }});
        
        //set behaviors menu
        JMenu behlist = new JMenu("behavior list");
        popupMenu.add(behlist);
        
        Model m = this;//fuck java
        
        JMenuItem dragged = new JMenuItem("Dragged");
        behlist.add(dragged);
        dragged.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new Dragged(m));
        }});
        JMenuItem fall = new JMenuItem("fall");
        behlist.add(fall);
        fall.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new Fall(m));
        }});
        JMenuItem greggrage = new JMenuItem("greggrage");
        behlist.add(greggrage);
        greggrage.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new GreggRage(m));
        }});
        JMenuItem lay = new JMenuItem("lay");
        behlist.add(lay);
        lay.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new Lay(m));
        }});
        JMenuItem layfromfall = new JMenuItem("layfromfall");
        behlist.add(layfromfall);
        layfromfall.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new LayFromFall(m));
        }});
        JMenuItem lookup = new JMenuItem("lookup");
        behlist.add(lookup);
        lookup.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new Lookup(m));
        }});
        JMenuItem maeborred = new JMenuItem("maeborred");
        behlist.add(maeborred);
        maeborred.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new MaeBorred(m));
        }});
        JMenuItem run = new JMenuItem("run");
        behlist.add(run);
        run.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new Run(m));
        }});
        JMenuItem sit = new JMenuItem("sit");
        behlist.add(sit);
        sit.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new Sit(m));
        }});
        JMenuItem sleep = new JMenuItem("sleep");
        behlist.add(sleep);
        sleep.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new Sleep(m));
        }});
        JMenuItem sleepy = new JMenuItem("sleepy");
        behlist.add(sleepy);
        sleepy.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new Sleepy(m));
        }});
        JMenuItem stand = new JMenuItem("stand");
        behlist.add(stand);
        stand.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new Stand(m));
        }});
        JMenuItem walk = new JMenuItem("walk");
        behlist.add(walk);
        walk.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new Walk(m));
        }});
        JMenuItem obadv = new JMenuItem("outboundAdv");
        behlist.add(obadv);
        obadv.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        	setBehavior(new OutboundAdventure(m));
        }});
	}
}