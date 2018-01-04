package dal.test.encoding.startup;

import dal.test.encoding.vbyte.VbyteEncodingTest;

public class AppStartVbyte {

	public static void main(String[] args) {
		try{
			new VbyteEncodingTest().EncodeDecode();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
