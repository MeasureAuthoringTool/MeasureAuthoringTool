package mat.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/* Data generated from sucessful logins in LoginCredentialServiceImpl.java
 * Copy log file from server to  workspace/mat/log.txt
 * Copy output to your favorite web browser.
 */

public class LogToChart {

	static String header =
		"<html>\n"+
		"<head>\n"+
    "<script type='text/javascript' src='http://www.google.com/jsapi'></script>\n"+
    "<script type='text/javascript'>\n"+
    "google.load('visualization', '1', {'packages':['annotatedtimeline']});\n"+
    "google.setOnLoadCallback(drawChart);\n"+
    "function drawChart() {\n"+
        "var data = new google.visualization.DataTable();\n"+
        "data.addColumn('date', 'Date');\n"+
		"data.addColumn('number', 'PermGen')\n"+
		"data.addColumn('number', 'Heap')\n"+
		"data.addColumn('number', 'Threads')\n"+
		"data.addRows([\n";
	
  
	static String tmpMiddle =
			"[new Date(2008,1,5,11,33,0), 33322,  39463,46],\n"+
			  "[new Date(2008,1,5,13,33,0), 40000,  39463,17],\n"+
			  "[new Date(2008,1,5,14,33,0), 30000,   39463,66]\n";
			
			
   static String footer ="]);\n"+
        "var chart = new google.visualization.AnnotatedTimeLine(document.getElementById('chart_div'));\n"+
        "chart.draw(data, {displayAnnotations: true});\n"+
      "}\n"+
    "</script>\n"+
    "</head>\n"+
    "<body>\n"+
    "eMeasure MAT SERVER USAGE\n"+
    "<div id='chart_div' style='width: 700px; height: 240px;'></div>\n"+
    "</body>\n"+ 
    "</html>\n";
	

   static public String appendLogItem(String time, String permGen, String heap, String threads){
	   String s = "[new Date("+time+"), "+permGen+", "+heap+", "+threads+"]";
	   return s;
   }


   public static void main(String[] av) {
	   ArrayList<String> logItems = new ArrayList<String>();



	   try {
		   File f = new File("log.txt");
		   // System.out.println(f.getCanonicalPath());
		   Scanner fileScanner = new Scanner(f);


		   String line;
		   while(fileScanner.hasNext()){
			   line = fileScanner.nextLine();

			   Pattern chartPattern = Pattern.compile("CHARTREPORT");
			   Matcher chartMatcher = chartPattern.matcher(line);

			   if (chartMatcher.find()) {

				   //    String match = chartMatcher.group();
				   // System.out.println(chartMatcher.end()+" hit "+match);
				   String logLine = line.substring(chartMatcher.end());
				   // System.out.println(logLine);

				   Scanner scan = new Scanner(logLine);
				   //System.out.println("* "+logLine);

				   String permGen ="";
				   String heap = "";
				   String threads = "";
				   String time = "";

				   BigInteger bigBoy;
				   bigBoy = scan.nextBigInteger();
				   permGen = bigBoy.toString();
				   bigBoy = scan.nextBigInteger();
				   heap = bigBoy.toString();
				   bigBoy = scan.nextBigInteger();
				   threads = bigBoy.toString();
				   bigBoy = scan.nextBigInteger();
				   time = bigBoy.toString();

				   logItems.add(appendLogItem(time, permGen, heap, threads));

				   scan.close();
			   }

			   fileScanner.close();
		   }

	   } catch (FileNotFoundException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }

		
		
    	/*
    	String corpus ="Threads running:   	40\n"+
    	"/login_success\n"+
    	"CHARTREPORT 48905736 78286392 40 49484632\n"+
    	"CHARTREPORT 444 744 44 4942\n"+
    	"11:54:58,717  INFO btpool0-5 server.PreventCachingFilter:41 - PreventCachingFilter\n"+
    	"11:54:58,811  INFO btpool0-5 server.HibernateStatisticsFilter:35 - Requesting /Mat.html in session 1wxw9q49bilab\n"+
    	"11:54:58,873  INFO btpool0-5 server.HibernateStatisticsFilter:35 - Requesting /Mat.css in session 1wxw9q49bilab\n";
    	
    	System.out.println(corpus);
    	*/
    	
    
    	    
    	    System.out.print(header);
    	    for(int i=0; i<logItems.size()-1;i++){
    	    	System.out.println(logItems.get(i)+",");
    	    }
    	    System.out.println(logItems.get(logItems.size()-1));
    	    System.out.print(footer);
    	  

 }
	
	
}
