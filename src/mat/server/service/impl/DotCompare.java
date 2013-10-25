package mat.server.service.impl;

/**
 * The Class DotCompare.
 */
public class DotCompare{
	//TODO revisit this logic an NFE exception occurs in some test data where an oid contains "09"
	/**
	 * Decode helper.
	 * 
	 * @param n
	 *            the n
	 * @return the int
	 */
	int decodeHelper(String n){
		if(n.length()==0)
			return 0;
		try{
			int i= Integer.decode(n);
			return i;
		}catch(NumberFormatException nfe){
			return 0;
		}
	}
	
	 /**
	 * Dot notation compare.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the int
	 */
 	public int dotNotationCompare(String a, String b){
		 boolean aIsNum;
		 boolean bIsNum;
		 
		 if(a.compareTo(b)==0)
			 return 0;
		 if(a.equalsIgnoreCase(""))
			 return -1;
		 if(b.equalsIgnoreCase(""))
			 return 1;
		
			String[] aArr = a.split("\\.");
			String[] bArr = b.split("\\.");

			int parsedLengthCompare = aArr.length - bArr.length;
			int min = Math.min(aArr.length,bArr.length);
			
			for(int i=0;i<min;i++){
	
				aIsNum = aArr[i].matches("[0-9\\.]+");
				bIsNum = bArr[i].matches("[0-9\\.]+");

				 if(aIsNum!=bIsNum){
					 if(aIsNum)
						 return -1;
					 return 1;
				 }
				if(aIsNum){
					boolean sameLength;
					sameLength = aArr[i].length() == bArr[i].length();
					// See if they are the same length (detect leading zeros
					//Check to see if the integers they evaluate to are the same
					if(decodeHelper(aArr[i])- decodeHelper(bArr[i]) != 0 )
						return  decodeHelper(aArr[i])- decodeHelper(bArr[i]);
					else if(!sameLength)
						return  bArr[i].length()-aArr[i].length();
				}
				else{
					if(aArr[i].compareTo(bArr[i]) !=0 )
						return aArr[i].compareTo(bArr[i]);
				}
			}
			//TODO split seems to eat empty last fields like "1.2."
			return parsedLengthCompare;
		}
}
