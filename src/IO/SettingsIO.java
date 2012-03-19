package IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SettingsIO {

	private File settingsFile;
	
	public SettingsIO(){
		this("RLA.settings");
	}
	public SettingsIO(String fileName){
		
		try{
			
			try{
				//loading allready existing settings file
				settingsFile = new File(SettingsIO.class.getClassLoader().getResource("./"+fileName).getPath());
				
			}catch(NullPointerException n){//if it doesn't exsist catch the exception and create a new settings file
				
				settingsFile=new File(SettingsIO.class.getClassLoader().getResource("./").getPath()+fileName);
				
				if(!settingsFile.createNewFile()){//if creating fails exit with exception, couldn't generate settings
					throw new Exception("couldn't create Settingsfile");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("no RLA.settings found. System exiting");
			System.exit(0);
		}
	}
	
	public boolean settingsFileExsist(){
		return settingsFile.exists();
	}
	
	public boolean settingsFileIsEmpty(){
		return !(settingsFile.length()>0);
	}
	
	public HashMap<String,Object> readSettings() throws IOException, ClassNotFoundException{
		HashMap<String,Object> out;
		out=readFile();
		return out;
	}
	
	@SuppressWarnings("unchecked")//geht wohl nicht anders das warning los zu werden
	private HashMap<String,Object> readFile() throws IOException, ClassNotFoundException{
		FileInputStream r=new FileInputStream(settingsFile);
		ObjectInputStream o=new ObjectInputStream(r);
		if(settingsFileIsEmpty())
			return null;
		HashMap<?,?> hashMap = (HashMap<?,?>)(o.readObject());
		o.close();
		return (HashMap<String, Object>) hashMap;
	}
	
	public void writeSettings(HashMap<String,Object> h) throws IOException{
		if(settingsFile.canWrite())
			writeFile(h);
		else
			System.out.println("Can't write settings File");
	}
	
	private void writeFile(HashMap<String,Object> out)throws IOException{
		FileOutputStream w=new FileOutputStream(settingsFile);
		ObjectOutputStream o=new ObjectOutputStream(w);
		o.flush();//schadet nicht
		o.writeObject(out);
		o.close();
		return;
	}
}
