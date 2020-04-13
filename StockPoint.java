import java.util.*;
import java.text.*;

public class StockPoint{
	
	private int year, month, day;
	private int ID;
	private double adjClose;
	private double difference;
	private double concavity;
	private double zScore;
	private double differenceZScore;
	private double concavityZScore;
	private double chaosIndex;
	private double totalMoney;
	
	public StockPoint(String dateString, double adjClose){
		String[] tempTokens = dateString.split("-");
			year = Integer.parseInt(tempTokens[0]);
			month = Integer.parseInt(tempTokens[1]);
			day = Integer.parseInt(tempTokens[2]);
		this.adjClose = adjClose;
	}
	
	public void round(){
		adjClose = Math.round(adjClose*100.0)/100.0;
		difference = Math.round(difference*100.0)/100.0;
		concavity = Math.round(concavity*10000.0)/10000.0;
		zScore = Math.round(zScore*10000.0)/10000.0;
		differenceZScore = Math.round(differenceZScore*10000.0)/10000.0;
		concavityZScore = Math.round(concavityZScore*10000.0)/10000.0;
		chaosIndex = Math.round(chaosIndex*10000.0)/10000.0;
	}
	
	public double getValue(){
		return adjClose;
	}
	
	public void setValue(double value){
		adjClose = value;
	}
	
	public String getDate(){
		return ""+year+"-"+month+"-"+day;
	}
	
	public int getDateAsNumber(){
		String yearString, monthString, dayString;
		yearString=""+year;
		if(month<10) monthString="0"+month;
			else monthString=""+month;
		if(day<10) dayString="0"+day;
			else dayString=""+day;
		return Integer.parseInt(yearString+monthString+dayString);
	}
	
	public void setDate(String dateString){
		String[] tempTokens = dateString.split("-");
			year = Integer.parseInt(tempTokens[0]);
			month = Integer.parseInt(tempTokens[1]);
			day = Integer.parseInt(tempTokens[2]);
	}
	
	public int getID(){
		return ID;
	}
	
	public void setID(int c){
		ID = c;
	}
	
	public void setZScore(double z){
		zScore = z;
	}
	
	public double getZScore(){
		return zScore;
	}
	
	public void setDifference(double diff){
		difference = diff;
	}
	
	public double getDifference(){
		return difference;
	}
	
	public void setConcavity(double conc){
		concavity = conc;
	}
	
	public double getConcavity(){
		return concavity;
	}
	
	public void setDifferenceZScore(double z){
		differenceZScore = z;
	}
	
	public double getDifferenceZScore(){
		return differenceZScore;
	}
	
	public void setConcavityZScore(double z){
		concavityZScore = z;
	}
	
	public double getConcavityZScore(){
		return concavityZScore;
	}
	
	public void setChaosIndex(double index){
		chaosIndex = index;
	}
	
	public double getChaosIndex(){
		return chaosIndex;
	}
	
	public void setTotalMoney(double mon){
		totalMoney = mon;
	}
	
	public double getTotalMoney(){
		return totalMoney;
	}
	
	public String toString(){
		return ""+ ID +" "+ getDate()+" "+adjClose+" "+difference+" "+concavity+" "+
			zScore+" "+differenceZScore+" "+concavityZScore;
	}
	
	public String[] toStringArray(){
		round();
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		
		String[] tokens = new String[8];
		tokens[0]=getDate();		
		tokens[1]=""+nf.format(getValue());
		tokens[2]=""+getDifference();
		tokens[3]=""+getConcavity();
		tokens[4]=""+getZScore();
		tokens[5]=""+getDifferenceZScore();
		tokens[6]=""+getConcavityZScore();
		tokens[7]=""+getChaosIndex();
		return tokens;
	}
	
	public String[] toStringArrayWithIDs(){
		round();
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		
		String[] tokens = new String[9];
		tokens[0]=""+getID();
		tokens[1]=getDate();		
		tokens[2]=""+nf.format(getValue());
		tokens[3]=""+getDifference();
		tokens[4]=""+getConcavity();
		tokens[5]=""+getZScore();
		tokens[6]=""+getDifferenceZScore();
		tokens[7]=""+getConcavityZScore();
		tokens[8]=""+getChaosIndex();
		return tokens;
	}
}