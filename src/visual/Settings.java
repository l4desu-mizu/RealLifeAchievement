package visual;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.HashMap;

import util.Period;

import IO.SettingsIO;

public class Settings extends Frame {

	private static final long serialVersionUID = -498468767736919766L;
	/**
	 * Settings: (5)
	 * 
	 * 	Position
	 *  Size
	 * 	URLadress
	 * 	Intervall
	 * 	Popuptime
	 */
	
	private SettingsIO io;
	private HashMap<String,Object> settings;
	private int anzahl;
	
	Settings(Image icon){
		super("Real Life Achievments Settings");
		this.setVisible(false);
		this.setIconImage(icon);
		this.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2-this.getWidth()/2,Toolkit.getDefaultToolkit().getScreenSize().height/2-this.getHeight()/2,300,500);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				setVisible(false);
			}
		});
		anzahl=5;//anzahl der inhalte?
		settings=new HashMap<String, Object>(anzahl);
		io=new SettingsIO();
		loadSettings();
	}
	
	public long getSetPopuptime(){
		return ((Period)settings.get("Popuptime")).getTime();
	}
	public long getSetIntervall(){
		return ((Period)settings.get("Intervall")).getTime();
	}
	public Dimension getSetSize(){
		return ((Dimension)settings.get("Size"));
	}
	public Point getSetPosition(){
		return ((Point)settings.get("Position"));
	}
	
	public void loadSettings(){
		if(!io.settingsFileExsist()||io.settingsFileIsEmpty()){
			loadDefaults();
			saveSettings();
		}
		else{
			try{
				settings=io.readSettings();
				if(settings==null){
					loadDefaults();
					System.out.println("RLA.settings broken?, loading defaults");
				}
			}catch(Exception e){
				System.out.println("Reading error");
				e.printStackTrace();
				System.out.println("Loading defaults");
				loadDefaults();
			}
		}
	}
	
	public void saveSettings(){
		try{
			io.writeSettings(settings);
		}catch(Exception e){
			System.out.println("Writing error");
			e.printStackTrace();
		}
	}
	
	private void loadDefaults(){
		settings.put("Position", new Point(0,0));
		settings.put("Size", new Dimension(290,90));
		settings.put("URLadress","http://l4desu.bytezero.de/RLA");
		settings.put("Intervall", new Period(5,Period.SEC));
		settings.put("Popuptime", new Period(6,Period.SEC));
	}
	
}
