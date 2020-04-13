import java.io.*;
import java.util.*;
import java.text.*;

public class ProfitBot{

//INSTANCE VARIABLES
	
	private File database;
	private Scanner kb;
	
	private StockPoint[] data;
	private int size; //will be data.length
	
	private double average;
	private double sigma;
	private double differenceAverage;
	private double differenceSigma;
	private double concavityAverage;
	private double concavitySigma;
	
	private int minDate, maxDate;
	private boolean interval = false;
	
	public double totalMoney;
	
//CONSTRUCTORS	
	
	public ProfitBot(String name){
		database = new File(name+".csv");
		MASTER();
	}
	
	public ProfitBot(String name, int minDate, int maxDate){
		database = new File(name+".csv");
		this.minDate = minDate;
		this.maxDate = maxDate;	
		interval = true;
		MASTER();
	}
	
//MASTER	
	
	private void MASTER(){
		generateData();
		generateDifferences();
		generateConcavity();
		generateAverage();
		generateDifferenceAverage();
		generateConcavityAverage();
		generateStandardDeviation();
		generateDifferenceStandardDeviation();
		generateConcavityStandardDeviation();
		generateZScores();
		generateDifferenceZScores();
		generateConcavityZScores();
		generateChaosIndex();
		for(int x=0;x<size;x++) data[x].round();
	}
	
//GENERATION	
	
	public void generateData(){
		try{
			kb = new Scanner(database);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		ArrayList<StockPoint> tempData = new ArrayList<StockPoint>();	
		kb.nextLine(); //pass headers
		while(kb.hasNextLine()){
			String[] tempLineSplitted = kb.nextLine().split(",");
			StockPoint temp = new StockPoint(tempLineSplitted[0], Double.parseDouble(tempLineSplitted[5]));
			tempData.add(temp);
		}	
		StockPoint[] returnData = new StockPoint[tempData.size()];
		for(int x=0;x<tempData.size();x++) returnData[x] = tempData.get(x);		
		kb.close();
		if(interval){
			ArrayList<StockPoint> intervalData = new ArrayList<StockPoint>();
			for(int x=0;x<returnData.length;x++){	
				if(returnData[x].getDateAsNumber() >= minDate && returnData[x].getDateAsNumber() <= maxDate){
					intervalData.add(returnData[x]);
				}
			}
			StockPoint[] intervalReturnData = new StockPoint[intervalData.size()];
			for(int x=0;x<intervalData.size();x++){
				intervalReturnData[x] = intervalData.get(x);
			}
			data = intervalReturnData;
		}
		else{
			data = returnData;
		}
		size = data.length;
		for(int x=0;x<size;x++){
			data[x].setID(x);
		}	
	}
	
	public void generateDifferences(){
		for(int x=1;x<data.length;x++){
			data[x].setDifference(data[x].getValue() - data[x-1].getValue());
		}	
	}

	public void generateConcavity(){
		for(int x=1;x<data.length;x++){
			data[x].setConcavity(data[x].getDifference() - data[x-1].getDifference());
		}
		data[1].setConcavity(0);
	}

	public void generateAverage(){
		double sum=0;
		for(int x=0;x<data.length;x++){
			sum+=data[x].getValue();
		}
		average = sum/data.length;
	}
	
	public void generateDifferenceAverage(){
		generateDifferences();
		double sum=0;
		for(int x=1;x<data.length;x++){
			sum+=data[x].getDifference();
		}
		differenceAverage = sum/data.length;
	}
	
	public void generateConcavityAverage(){
		generateConcavity();
		double sum=0;
		for(int x=1;x<data.length;x++){
			sum+=data[x].getConcavity();
		}
		concavityAverage = sum/data.length;
	}
	
	public void generateStandardDeviation(){
		generateAverage();	
		double varianceSum = 0;	
		for(int x=0;x<data.length;x++) varianceSum+= Math.pow((data[x].getValue()-average),2);	
		sigma = Math.sqrt(varianceSum/data.length);
	}
	
	public void generateDifferenceStandardDeviation(){
		generateDifferenceAverage();	
		double varianceSum = 0;	
		for(int x=1;x<data.length;x++) varianceSum+= Math.pow((data[x].getDifference()-differenceAverage),2);	
		differenceSigma = Math.sqrt(varianceSum/(data.length-1));
	}
	
	public void generateConcavityStandardDeviation(){
		generateConcavityAverage();	
		double varianceSum = 0;	
		for(int x=1;x<data.length;x++) varianceSum+= Math.pow((data[x].getConcavity()-concavityAverage),2);	
		concavitySigma = Math.sqrt(varianceSum/(data.length-1));
	}
	
	public void generateZScores(){
		for(int x=0;x<size;x++){
			data[x].setZScore((data[x].getValue() - average)/sigma);
		}
	}
	
	public void generateDifferenceZScores(){
		for(int x=1;x<size;x++){
			data[x].setDifferenceZScore((data[x].getDifference() - differenceAverage)/differenceSigma);
		}
	}
	
	public void generateConcavityZScores(){
		for(int x=1;x<size;x++){
			data[x].setConcavityZScore((data[x].getConcavity() - concavityAverage)/concavitySigma);
		}
	}

//UTIL

	public double averageDiffZScore(){
		double sum = 0;
		for(int x=0;x<size;x++){
			sum+=Math.abs(data[x].getDifferenceZScore());
		}
		return sum/size;
	}

//CHAOS INDEX CREATORS
	
	public double createChaosIndexMethodOne(int ID){
		
		double original = data[ID].getDifferenceZScore();
		double value = Math.exp(0.4*(Math.abs(original)-3))+1;
		
		if(original>0||ID==0){
			//System.out.print("1 = ");
			return 1;
		}
		else{
			//System.out.print("("+original+")"+value+" * ");
			return value * 
				createChaosIndexMethodOne(ID-1);
		}
	}
	
	public void generateChaosIndex(){
		for(int x=0;x<size;x++){
			data[x].setChaosIndex(createChaosIndexMethodOne(x));
		}
	}
	
	public void simulate(double depositCash, double tolerance){
		
		totalMoney = depositCash;
		ArrayList<Double> shares = new ArrayList<Double>();

		NumberFormat nf = NumberFormat.getCurrencyInstance();
		final Object[][] table = new String[data.length+1][5];
		table[0] = new String[] {"Date", "Price", "Chaos #", "Total $$", "Comments"};
		
		for(int x=0;x<size;x++){
			String comments="";
			if(data[x].getChaosIndex() > tolerance){
				double price = data[x].getValue();
				shares.add(price);
				totalMoney-=price;
				comments+="BOUGHT "+nf.format(price)+"  ";
			}
			for(int y=0;y<shares.size();y++){
				if(data[x].getValue()>shares.get(y)){
					double price = data[x].getValue();
					totalMoney+=price;
					comments+="SOLD "+nf.format(price)+" ("+nf.format(price-shares.get(y))+")  ";
					shares.remove(y);
				}
			}
			
			totalMoney = Math.round(totalMoney*10000.0)/10000.0;
			table[x+1] = new String[] {data[x].getDate(), ""+data[x].getValue(), 
				""+data[x].getChaosIndex(), nf.format(totalMoney), comments};
		}
		for (final Object[] row : table) {
		    System.out.format("%-15.15s%-15.15s%-15.15s%-15.15s%-120.120s\n", row);
		}
		
		System.out.println("\nSHARES STILL OWNED: "+shares.size());
		double totalShareValue = 0;
		for(int x=0;x<shares.size();x++) {
			System.out.println("\t"+nf.format(shares.get(x)));
			totalShareValue+=shares.get(x);
		}
		
		double bestCase = totalMoney+totalShareValue, worseCase = totalMoney+data[size-1].getValue()*shares.size();

		System.out.println("TOTAL $$$$$$ OWNED:            "+nf.format(totalShareValue));
		System.out.println("TOTAL CURRENT CASH:            "+nf.format(totalMoney));
		System.out.println("\nWORSE CASE, IF YOU SELL ALL:   "+nf.format(totalMoney+data[size-1].getValue()*shares.size()));		
		System.out.println("BEST CASE, IF YOU BREAK EVEN:  "+nf.format(totalMoney+totalShareValue));
		System.out.println("\nPRICE TO BEAT:                 "+nf.format(depositCash*(data[size-1].getValue()/data[0].getValue())));
		System.out.println("WORSE CASE YOU MADE/SAVED:     "+nf.format((totalMoney+data[size-1].getValue()*shares.size())-(depositCash*(data[size-1].getValue()/data[0].getValue()))));		
		System.out.println("BEST CASE YOU MADE/SAVED:      "+nf.format((totalMoney+totalShareValue)-(depositCash*(data[size-1].getValue()/data[0].getValue()))));
		System.out.println("\nWORSE CASE PERCENT CHANGE:     %"+(Math.round((worseCase-depositCash)/depositCash*100000.0)/1000.0));		
		System.out.println("BEST CASE PERCENT CHANGE:      %"+(Math.round((bestCase-depositCash)/depositCash*100000.0)/1000.0));		
		
	}
	
//PRINTS	
	
	public void printData(){
		for(int x=0;x<data.length;x++) System.out.println(data[x].getValue());
	}
	
	public void printDates(){
		for(int x=0;x<data.length;x++) System.out.println(data[x].getDate());
	}
	
	public void printIDs(){		
		for(int x=0;x<data.length;x++) System.out.println(data[x].getID());
	}
	
	public void printAverage(){
		System.out.println("AVERAGE:        "+average);
		System.out.println("DIFF AVERAGE:   "+differenceAverage);
		System.out.println("CONC AVERAGE:   "+concavityAverage);
	}
	
	public void printStandardDeviation(){
		System.out.println("SIGMA:          "+sigma);
		System.out.println("DIFF SIGMA:     "+differenceSigma);
		System.out.println("CONC SIGMA:     "+concavitySigma);
	}
	
	public void printDifferences(){
		for(int x=0;x<size-1;x++) System.out.println("DIFFERENCE: "+data[x].getDifference());
	}
	
	public void printConcavities(){
		for(int x=0;x<size-1;x++) System.out.println("CONCAVITY: "+data[x].getConcavity());
	}
	
	public void printZScores(){
		for(int x=0;x<data.length;x++){
			System.out.println(data[x].getDate()+"\t\t"+data[x].getZScore()+"\t\t"+data[x].getDifferenceZScore()+"\t\t"+data[x].getConcavityZScore());
		}
	}
	
	public void printChaosIndexes(){
		for(int x=0;x<size;x++) System.out.println(data[x].getChaosIndex());
	}
	
	public void printTable(){
		MASTER();
		
		final Object[][] table = new String[data.length][8];
		table[0] = new String[] {"Date", "Adj Close", "Difference", "Concavity", 
								"Z Score", "Diff Z Score", "Conc Z Score", "Chaos Index"};
		for(int x=1;x<data.length;x++){
			table[x] = data[x].toStringArray();
		}
		for (final Object[] row : table) {
		    System.out.format("%15s%15s%15s%15s%15s%15s%15s%15s\n", row);
		}
	}
	
	public void printTableWithIDs(){
		final Object[][] table = new String[data.length][9];
		table[0] = new String[] {"ID", "Date", "Adj Close", "Difference", "Concavity", 
								"Z Score", "Diff Z Score", "Conc Z Score", "Chaos Index"};
		for(int x=1;x<data.length;x++){
			table[x] = data[x].toStringArrayWithIDs();
		}
		for (final Object[] row : table) {
		    System.out.format("%15s%15s%15s%15s%15s%15s%15s%15s%15s\n", row);
		}
	}
	
	public void printToStrings(){
		for(int x=0;x<data.length;x++){
			System.out.println(data[x].toString());
		}
	}
}