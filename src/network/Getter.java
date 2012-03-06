package network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Getter {
	
	private URL url;
	private BufferedInputStream inputStream;
	private BufferedReader reader;
	private String name;
	private String achievment;
	private String description;
	
	public Getter(String u){
		try{
			this.url=new URL(u);
			inputStream=new BufferedInputStream(url.openStream());
			reader=new BufferedReader(new InputStreamReader(inputStream));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//just some default stuff... for the lulz
			name="John Doe";
			achievment="Existing";
			description="Over 9000, he's everywhere";
		getData();
	}
	
	public void setURL(URL url){
		this.url=url;
	}
	
	public String getName(){
		return name;
	}
	
	public String getAchievment(){
		return achievment;
	}
	
	public String getDescription(){
		return description;
	}
	
	public boolean getData(){
		//returns true if new data, also false if an error occured
		String[] out=new String[3];
		try{
			if(reader.ready())
				out=((String)reader.readLine()).split("/");
			else{
				out[0]=name;
				out[1]=achievment;
				out[2]=description;
			}
			
			// "reset" ... das marken saugt irgendwie...
			inputStream=new BufferedInputStream(url.openStream());
			reader=new BufferedReader(new InputStreamReader(inputStream));
			
			if(!name.equals(out[0])||!achievment.equals(out[1])||!description.equals(out[2])){
				name=out[0];
				achievment=out[1];
				description=out[2];
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Couldn't get Data");
		}
		return false;
	}

//	@Override
//	public void run() {
//		if(getData())
//			return;//dinge tun, events foiern usw (java.util.Timer vs javax.swing.Timer)
//	}

}
