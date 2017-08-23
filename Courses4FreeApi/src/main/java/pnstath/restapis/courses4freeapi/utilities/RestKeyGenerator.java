package pnstath.restapis.courses4freeapi.utilities;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

// Class with implementaion of generateKey method of KeyGenerator interface
public class RestKeyGenerator implements KeyGenerator {
    
	@Override
	public Key generateKey() {
		// A source string for construction of the final secretkey
		String source = "restKey";
        // Final secretKey construction
		Key secretKey = new SecretKeySpec(source.getBytes(), 0, source.getBytes().length, "DES");
		return secretKey;
	}
}

