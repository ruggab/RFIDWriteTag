/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.smart.rfid.tunnel.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author sidious
 * 
 */

public class DateFunction{
	
	private String dateSep; 
	private String timeSep;
	private String dttmSep;
	
	private GregorianCalendar gc;
    
    public DateFunction(){
    	setDefaul();
    	gc = new GregorianCalendar();
    }
    
    public void setDefaul() {
    	dateSep="/";
    	timeSep=":";
    	dttmSep=" ";
    }
    
    public void RefreshDate() {
    	gc = new GregorianCalendar();
    }
    
    public void setDateSeparator(String pValue) {
    	this.dateSep=pValue;
    }
    
    public void setTimeSeparator(String pValue) {
    	this.timeSep=pValue;
    }
    
    public void setDttmSeparator(String pValue) {
    	this.dttmSep=pValue;
    }
    
    //Reperisce Data Corrente
    public String getFullDate(){
    	//GregorianCalendar gc = new GregorianCalendar();
        int anno = gc.get(Calendar.YEAR);
        int mese = gc.get(Calendar.MONTH) + 1;
        int giorno = gc.get(Calendar.DATE);
        int ore = gc.get(Calendar.HOUR);
        if (gc.get(Calendar.AM_PM) != 0)
            ore=ore+12;
        int min = gc.get(Calendar.MINUTE);
        int sec = gc.get(Calendar.SECOND);
        return anno + dateSep + mese + dateSep + giorno + dttmSep + ore + timeSep + min + timeSep + sec;
    }
    
    //Reperisce Data Corrente
    public String getCurrentDate(){
        //GregorianCalendar gc = new GregorianCalendar();
        int anno = gc.get(Calendar.YEAR);
        int mese = gc.get(Calendar.MONTH) + 1;
        int giorno = gc.get(Calendar.DATE);
        return anno + dateSep + mese + dateSep + giorno;
    }
      
    public String getDateIT(String pDate){
        
        String retVal="31"+dateSep+"12"+dateSep+"2099";
        
        if (pDate!=null){ 
            String wAnno=pDate.substring(0, 4);
            String wMese=pDate.substring(5, 7);
            String wGiorno=pDate.substring(8, 10);
            retVal=wGiorno+dateSep+wMese+dateSep+wAnno;
        }
            return retVal; 
    }

    public String getDateEN(String pDate){
        
        String retVal="2099"+dateSep+"12"+dateSep+"31";
        
        if (pDate!=null){
            String wGiorno=pDate.substring(0, 2);
            String wMese=pDate.substring(3, 5);
            String wAnno=pDate.substring(6, 10);
            retVal=wAnno+dateSep+wMese+dateSep+wGiorno;
        }
        return retVal;
    }
    
    public String getYear(){
        //GregorianCalendar gc = new GregorianCalendar();
        return Integer.toString(gc.get(Calendar.YEAR));
    }
    
    public String getMonth(){
        //GregorianCalendar gc = new GregorianCalendar();
        return Integer.toString(gc.get(Calendar.MONTH) + 1);
    }
    
    public String getDay(){
        //GregorianCalendar gc = new GregorianCalendar();
        return Integer.toString(gc.get(Calendar.DATE));
    }
    
    public String getHour(){
        //GregorianCalendar gc = new GregorianCalendar();
        int ore = gc.get(Calendar.HOUR);
        if (gc.get(Calendar.AM_PM) != 0)
            ore=ore+12;
        return Integer.toString(ore);
    }
    
    public String getMinute(){
        //GregorianCalendar gc = new GregorianCalendar();
        return Integer.toString(gc.get(Calendar.MINUTE));
    }
    
    public String getSecond(){
        GregorianCalendar gc = new GregorianCalendar();
        return Integer.toString(gc.get(Calendar.SECOND));
    }
    
    public String getTime() {
    	//GregorianCalendar gc = new GregorianCalendar();
        int ore = gc.get(Calendar.HOUR);
        if (gc.get(Calendar.AM_PM) != 0)
            ore=ore+12;
        return Integer.toString(ore)+timeSep+
        	   Integer.toString(gc.get(Calendar.MINUTE))+timeSep+
               Integer.toString(gc.get(Calendar.SECOND));
    }
}
