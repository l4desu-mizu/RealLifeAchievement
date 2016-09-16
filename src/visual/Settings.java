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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Period;

import IO.SettingsIO;

public class Settings extends Frame {

	private static final long serialVersionUID = -498468767736919766L;
	private static final String KEY_POSITION="Position";
	private static final String KEY_SIZE="Size";
	private static final String KEY_URL="URLaddress";
	private static final String KEY_INTERVAL="Interval";
	private static final String KEY_POPTIME="Popuptime";

	//private static final String KEY_PASSWORD="Password";
	//private static final String KEY_URLSET="URLsetAddress";
	/**
	 * Settings: (5)
	 * 
	 * 	Position
	 *  Size
	 * 	URLadress
	 * 	Interval
	 * 	Popuptime
	 */

	//implementation of setting address: urladress+"/set" (changing get address to urladress+"/get"

	private SettingsIO io;
	private HashMap<String,Object> settings;
	private int anzahl;

	private JSlider intervalChoose,popuptimeChoose;
	private JLabel intervalDescription, popupDescription;
	private JTextField urlChoose;
	private JButton positionChoose,save;
	private JPanel back;
	private JCheckBox useSettings;
	private Main owner;

	private Actions actions;
	private SlideListener changy;

	private Dimension minSize;

	Settings(Frame owner,Image icon){
		super("Real Life Achievments Settings");
		this.owner=(Main)owner;
		this.setVisible(false);
		this.setIconImage(icon);
		minSize=new Dimension(320,200);
		this.setMinimumSize(minSize);
		this.setPreferredSize(minSize);
		this.setSize(minSize);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-this.getWidth()/2,Toolkit.getDefaultToolkit().getScreenSize().height/2-this.getHeight()/2);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				setVisible(false);
			}
		});

		//configsobject load
		anzahl=5;//anzahl der inhalte?
		settings=new HashMap<String, Object>(anzahl);
		io=new SettingsIO();
		loadSettings();


		//GUI Components
			//listeners and back
		actions=new Actions();
		changy=new SlideListener();
		back=new JPanel();

			//init component
		useSettings=new JCheckBox("Use Settings",true);
		save=new JButton("Save Properties");
		positionChoose=new JButton("Change Position");
		urlChoose=new JTextField(this.getSetURL());
		intervalChoose=new JSlider(5,120,5);//5 sec up to 2 mins catch time
		popuptimeChoose=new JSlider(3,20,6);//3 sec up to 20 sec popuptime
		intervalDescription=new javax.swing.JLabel("When should I get new information: "+intervalChoose.getValue()+"s");
		popupDescription=new javax.swing.JLabel("How long shall I show you them: "+popuptimeChoose.getValue()+"s");

			//disable non implemented features
			//atm disable Textfield, no possibility of changing this.
		urlChoose.setEditable(false);
		useSettings.setEnabled(false);

			//add listeners
		positionChoose.addActionListener(actions);
		save.addActionListener(actions);
		popuptimeChoose.addChangeListener(changy);
		intervalChoose.addChangeListener(changy);

			//add background
		this.add(back);

			//add components
		back.add(positionChoose);
		back.add(useSettings);
		back.add(urlChoose);
		back.add(intervalDescription);
		back.add(intervalChoose);
		back.add(popupDescription);
		back.add(popuptimeChoose);
		back.add(save);
		pack(); //hilfts?
	}

	public long getSetPopuptime(){
		return ((Period)settings.get(Settings.KEY_POPTIME)).getTime();
	}
	public long getSetInterval(){
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
				System.out.println("Reading settings");
				settings=io.readSettings();
				if(settings==null){
					loadDefaults();
					System.out.println("RLA.settings broken?, loading defaults");
				}
				System.out.println("Settings read");
			}catch(Exception e){
				System.out.println("Reading error");
				e.printStackTrace();
				System.out.println("Loading defaults");
				loadDefaults();
			}
		}
	}

	public void saveSettings(){
		//change settings
		try{
			settings.put(Settings.KEY_POSITION, owner.getToast().getLocation());
			settings.put(Settings.KEY_SIZE, owner.getToast().getSize());
			settings.put(Settings.KEY_URL, urlChoose.getText());
			settings.put(Settings.KEY_INTERVAL, new Period(this.intervalChoose.getValue(),Period.SEC));
			settings.put(Settings.KEY_POPTIME, new Period(this.intervalChoose.getValue(),Period.SEC));
		}catch(NullPointerException e){
			System.out.println("No settings yet, initializing defaults for RLA usage");//first init (there is no toast, the toast needs the settings)
		}

		//write settings
		try{
			System.out.println("writing settings");
			io.writeSettings(settings);
			System.out.println("Settings written");
		}catch(Exception e){
			System.out.println("Writing error");
			e.printStackTrace();
		}
	}

	private void loadDefaults(){
		settings.put(Settings.KEY_POSITION, new Point(0,0));
		settings.put(Settings.KEY_SIZE, new Dimension(290,90));
		settings.put(Settings.KEY_URL,"http://l4desu.bytezero.de/RLA/get");
		settings.put(Settings.KEY_INTERVAL, new Period(5,Period.SEC));
		settings.put(Settings.KEY_POPTIME, new Period(6,Period.SEC));
	}

	private void togglePosChooser(boolean disabled){
		if(disabled)
			positionChoose.setForeground(new java.awt.Color(0x10Af00));
		else
			positionChoose.setForeground(new java.awt.Color(0x0));
	}

	private void acceptSettings(){
		saveSettings();
		owner.getToast().setRepositioning(false);
		togglePosChooser(owner.getToast().getRepositioning());
		reloadSettings();
	}

	private void reloadSettings() {
		owner.reloadSettings();
	}

	private class Actions implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource().equals(positionChoose)){
				owner.getToast().setRepositioning(!owner.getToast().getRepositioning());
				if(owner.getToast().getRepositioning()){
					owner.getToast().setVisible(true);
					togglePosChooser(owner.getToast().getRepositioning());
				}
				else{
					owner.getToast().pop();
					togglePosChooser(owner.getToast().getRepositioning());
				}
			}
			else if(e.getSource().equals(save)){
				acceptSettings();
			}
		}
	}

	private class SlideListener implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if(e.getSource().equals(intervalChoose))
				intervalDescription.setText("When should I get new information: "+intervalChoose.getValue()+" s");
			else if(e.getSource().equals(popuptimeChoose))
				popupDescription.setText("When should I show you them: "+popuptimeChoose.getValue()+"s");
			else {
				intervalDescription.setText("When should I get new information: "+intervalChoose.getValue()+"s");
				popupDescription.setText("When should I show you them: "+popuptimeChoose.getValue()+"s");
			}
		}
	}
}
