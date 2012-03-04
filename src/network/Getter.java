package network;

import java.net.HttpURLConnection;
import java.net.URL;

public class Getter {
	
	private URL url;
	private HttpURLConnection con;
	private String name;
	private String achievment;
	private String description;
	
	public Getter(){
		this("http://l4desu.bytezero.de/RLA");
	}
	
	public Getter(String u){
		try{
			this.url=new URL(u);
			con=(HttpURLConnection) url.openConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
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
		String[] out;
		try{
			con.connect();
			out=((String)con.getContent()).split("/");
			con.disconnect();
		}catch(Exception e){
			System.out.println("Couldn't get Data");
			return false;
		}
		if(name!=out[0]||achievment!=out[1]||description!=out[2]){
			name=out[0];
			achievment=out[1];
			description=out[2];
			return true;
		}
		return false;
	}

//	@Override
//	public void run() {
//		if(getData())
//			return;//dinge tun, events foiern usw (java.util.Timer vs javax.swing.Timer)
//	}

}
