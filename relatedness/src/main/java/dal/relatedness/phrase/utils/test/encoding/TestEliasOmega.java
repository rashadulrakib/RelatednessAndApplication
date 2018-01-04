package dal.relatedness.phrase.utils.test.encoding;

import java.util.Arrays;

import org.junit.Test;

import dal.relatedness.phrase.utils.encoding.EliasOmega;
import dal.relatedness.phrase.utils.encoding.EliasOmega.EliasOmegaDecoder;
import dal.relatedness.phrase.utils.encoding.EliasOmega.EliasOmegaEncoder;

public class TestEliasOmega {

	@Test
	public void TestEncodeDecode() throws Exception{
		
		EliasOmega.EliasOmegaEncoder encodeobj = new EliasOmegaEncoder();
		
		byte [] barr = encodeobj.EncodeInterpolate(Arrays.asList(Integer.MAX_VALUE, 100, 200, 100000, 400));
		
		EliasOmega.EliasOmegaDecoder decodeobj = new EliasOmegaDecoder(barr);
		long a = decodeobj.ReadValue();
		a = decodeobj.ReadValue();
		a = decodeobj.ReadValue();
		a = decodeobj.ReadValue();
		a = decodeobj.ReadValue();
		
		System.out.println("test");
		
	}
}
