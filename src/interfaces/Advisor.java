package interfaces;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface Advisor {

	String genPassword(final String alph, final String passwordLength) throws IOException, NoSuchAlgorithmException, InterruptedException;
	String genAlphabet(final String alph);
	String getReallyAlphabet(final String alph);
}
