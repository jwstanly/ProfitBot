import java.util.*;
import java.io.*;

public class Driver{
	
	public static void main(String args[]){
		//argument processor
		ArrayList<String> arguments = new ArrayList<String>();
		for (String elem : args) 
            		arguments.add(elem);

		ProfitBot Boeing = new ProfitBot(arguments.get(0));
		Boeing.simulate(Double.parseDouble(arguments.get(1)), Double.parseDouble(arguments.get(2)));

		
		System.out.println("");System.out.println("");
	
	}
}





































	
/*

StockAnalyzer[] BA2019 = new StockAnalyzer[12];
		for(int x=0;x<12;x++){
			BA2019[x] = new StockAnalyzer("BA", 20190101+100*x, 20190201+100*x);
			System.out.println((x+1)+" AVERAGE: \t\t"+BA2019[x].generateAverage());
			System.out.println((x+1)+" SIGMA: \t\t"+BA2019[x].generateStandardDeviation());
			BA2019[x].printZScores();
		}




Boeing.printZScores();
Boeing.printCounts();
System.out.println("AVERAGE: "+Boeing.generateAverage());
System.out.println("SIGMA: "+Boeing.generaBoeingandardDeviation());
double[] BoeingData = Boeing.generateDifferences();
	for(int x=0;x<BoeingData.length;x++) System.out.println("DIFFERENCE: "+BoeingData[x]);
double[] BoeingData2 = Boeing.generateConcavity();
	for(int x=0;x<BoeingData2.length;x++) System.out.println("CONCAVITY: "+BoeingData2[x]);
	
	
//*/