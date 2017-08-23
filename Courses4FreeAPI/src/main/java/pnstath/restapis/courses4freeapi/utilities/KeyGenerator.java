package pnstath.restapis.courses4freeapi.utilities;

import java.security.Key;

// KeyGenerator Interface that exposes the generateKey method, which is implemented by RestKeyGenerator class
public interface KeyGenerator {

	 public Key generateKey();
}
