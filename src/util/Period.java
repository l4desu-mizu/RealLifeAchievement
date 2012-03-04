package util;

import java.io.Serializable;

public class Period implements Serializable{
	
	private static final long serialVersionUID = 896442342943464714L;
	
	public final static int MIL=1;
	public final static int SEC=1000;
	public final static int MIN=60000;
	public final static int HRS=60000*60000;
	
	private long timeInMil;
	
	public Period(int time,int unit){
		timeInMil=time*unit;
	}
	
	public void setTime(int time,int unit){
		timeInMil=time*unit;
	}
	
	public long getTime(){
		return timeInMil;
	}
	
	public long getTimeInSec(){
		return timeInMil/60;
	}
}
