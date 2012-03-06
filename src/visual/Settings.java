package visual;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import util.Period;

import IO.SettingsIO;

public class Settings extends Frame {

	private static final long serialVersionUID = -498468767736919766L;
	private static final String KEY_POSITION="Position";
	private static final String KEY_SIZE="Size";
	private static final String KEY_URL="URLadress";
	private static final String KEY_INTERVAL="Intervall";
	private static final String KEY_POPTIME="Popuptime";
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
	
	private JSlider intervalChoose,popuptimeChoose;
	private JTextField urlChoose;
	private JButton positionChoose,save;
	private JPanel back;
	private Main owner;
	
	private Actions actions;
	
	Settings(Frame owner,Image icon){
		super("Real Life Achievments Settings");
		this.owner=(Main)owner;
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
		
		actions=new Actions();
		back=new JPanel();
		
		save=new JButton("Save Properties");
		positionChoose=new JButton("Change Position");
		
		positionChoose.addActionListener(actions);
		save.addActionListener(actions);
		
		this.add(back);
		
		back.add(positionChoose);
		back.add(save);
	}
	
	public long getSetPopuptime(){
		return ((Period)settings.get(Settings.KEY_POPTIME)).getTime();
	}
	public long getSetIntervall(){
		return ((Period)settings.get(Settings.KEY_INTERVAL)).getTime();
	}
	public Dimension getSetSize(){
		return ((Dimension)settings.get(Settings.KEY_SIZE));
	}
	public Point getSetPosition(){
		return ((Point)settings.get(Settings.KEY_POSITION));
	}
	public String getSetURL(){
		return (String)settings.get(Settings.KEY_URL);
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
		settings.put("URLadress","http://l4desu.bytezero.de/RLA/get");
		settings.put("Intervall", new Period(5,Period.SEC));
		settings.put("Popuptime", new Period(6,Period.SEC));
	}
	
	private class Actions implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource().equals(positionChoose)){
				owner.getToast().setRepositioning(!owner.getToast().getRepositioning());
				if(owner.getToast().getRepositioning())
					owner.getToast().setVisible(true);
				else
					owner.getToast().pop();
			}
			else if(e.getSource().equals(save)){
				saveSettings();
			}
				
		}
	}
	
}
