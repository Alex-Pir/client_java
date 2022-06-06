package advisor;

import interfaces.Advisor;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.sun.istack.internal.NotNull;

public class AdvisorForPass implements Advisor {

	private static final String LITTLEA = "abcdefghijklmnopqrstuvwxyz";
	private static final String BIGA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMBERS = "0123456789";
	
	private static final String UPPER = "U";
	private static final String LOWWER = "L";
	private static final String NUMBER = "1";
	
	@Override
	public String genPassword(@NotNull final String alph, @NotNull final String passwordLength) throws IOException, NoSuchAlgorithmException, InterruptedException
	{
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		String pass = "";
		
		if (!alph.equals("") && !passwordLength.equals(""))
		{
			final String fullAlphabet = alph;
			char[] alphChar = fullAlphabet.toCharArray();
			for (int i = 0; i < Integer.parseInt(passwordLength); i++)
			{
				pass += alphChar[random.nextInt(alphChar.length)];
			}
		}
		
		return pass;
	}

	@Override
	public String genAlphabet(@NotNull final String alph)
	{
		String result = "";
		final int upper = alph.indexOf(UPPER);
		final int lowwer = alph.indexOf(LOWWER);
		final int number = alph.indexOf(NUMBER);
		
		if (upper != -1)
		{
			result += BIGA;
		}
		
		if (lowwer != -1)
		{
			result +=LITTLEA;
		}
		
		if (number != -1)
		{
			result += NUMBERS;
		}
		
		return result;
	}
	
	@Override
	public String getReallyAlphabet(final String alph)
	{
		String result = "<html> <body>";
		if (alph.indexOf(BIGA) != -1)
		{
			result += "Большие латинские буквы <br> ";
		}
		if (alph.indexOf(LITTLEA) != -1)
		{
			result += "Малые латинские буквы <br> ";
		}
		if (alph.indexOf(NUMBERS) != -1)
		{
			result += "Цифры";
		}
		result += "</body></html>";
		return result;
	}
}
