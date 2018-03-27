package mat.util;

public class SimpleSelenTest {

    /**
     * @param args
     */
	
//	public static void takeScreenshot( WebDriver driver, String path){
//	
//	WebDriver augmentedDriver = new Augmenter().augment(driver);
//    File screenshot = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
//    try {
//		FileUtils.copyFile(screenshot, new File(path));
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	
//	}
//    public static void main(String[] args) {
//        System.out.println(System.currentTimeMillis());
//        // TODO Auto-generated method stub
//        // Create a new instance of the Firefox driver
//        // Notice that the remainder of the code relies on the interface,
//        // not the implementation.
//     
//        while (System.currentTimeMillis()!=77){
//        WebDriver driver = new FirefoxDriver();
//
//        // And now use this to visit Google
//        driver.get("https://dev.ifmc.org/Login.html");
//        // Alternatively the same thing can be done like this
//        // driver.navigate().to("http://www.google.com");
//
//        // Find the text input element by its name
//        WebElement userName = driver.findElement(By.id("E-mail Address"));
//
//        // Enter something to search for
//       userName.sendKeys("cbrewbak@ifmc.org");
//
//        // Now submit the form. WebDriver will find the form for us from the element
//       
//      
//       WebElement password =  driver.findElement(By.id("Password"));
//       password.sendKeys("Welcome1#\n");
//       
//       (new WebDriverWait(driver, 10)).until(new ExpectedCondition<WebElement>() {
//    	   public WebElement apply(WebDriver d) {
//    		   return d.findElement(By.id("Search for a Measure"));
//    	   }
//    	  });
//       
//       
//       
//        // Check the title of the page
//        System.out.println("Page title is: " + driver.getTitle());
//        
//        driver.quit();
//        
//        }
        
       // WebElement mSearch = driver.findElement(By.id("Search for a Measure"));
       // 
      //  mSearch.sendKeys("borkbork\n");
        
      //  takeScreenshot(driver,"c:\\screenshot.png" );
        
        
        
        // FileWriter fstream = new FileWriter("out.txt");
       // BufferedWriter out = new BufferedWriter(fstream);
       // out.write(screenshot);
        
        // Should see: "cheese! - Google Search"
       // System.out.println("Page title is: " + driver.getTitle());
        
        //Close the browser
      //  driver.quit();
      //  System.out.println(System.currentTimeMillis());
//    }
}