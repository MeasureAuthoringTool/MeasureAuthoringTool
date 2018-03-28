/**
 * 
 */
package mat.util;

/**
 * @author vandavar
 *
 */
public class StringUtilityTest {
	 public static void main(String[] av) {
	     String name = "_population18";
	     String newName = stripOffNumber(name);
	     int value = getPos(name);
	     System.out.println(value);
	     System.out.println("New Name:" +newName);
	 }
	 
	 private static int getPos(String name) {
			int val = 0 ;
			try {
				int offset = 0;
				while(isInt(name.charAt(name.length()-(offset+1))))
					offset++;
				String  numVal = name.substring(name.length()-offset);
				val = (new Integer(numVal)).intValue();
			} catch (Exception e) {
				e.printStackTrace();
				return -1;//default
			}
			return val-1;
		}
	 
	 /*
		 * would like to use something like 
		 * Pattern.matches("\\d*",input)
		 * regex import is not resolved (not translatable to JavaScript?)*/
		 
		/*private static boolean isInt(String s){
			char[] charArray = s.toCharArray();
			for(int i=0;i<charArray.length; i++){
				if(Character.isDigit(charArray[i]))
					return true;
				else
					return false; //This will make sure the string does not have any characters.
			}
			return false;
		}
		*/
	 
	 /* private static boolean isInt(String s){
			try{
				Integer.parseInt(s);
				return true;
			}catch (NumberFormatException e){
				return false;
			}
		}*/
		
	    private static boolean isInt(char a){
			return Character.isDigit(a);
		}
		
		private static String stripOffNumber(String s){
			boolean flag = true;
			int strLen = s.length();
			while(flag){
				char c = s.charAt(strLen-1);
				if(Character.isDigit(c)){
					strLen--;
					continue;
				}else{
					flag = false;
				}
			}
			return s.substring(0,strLen);
		}
}
