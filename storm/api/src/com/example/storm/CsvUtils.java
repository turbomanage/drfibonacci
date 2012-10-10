package com.example.storm;

import java.io.StringWriter;

public class CsvUtils {

	public static final char CSV_DELIMITER = ',';
	public static final char QUOTE = '"';
	public static final char CR = '\r';
	public static final char LF = '\n';
	public static final char[] CSV_SEARCH_CHARS = new char[] { CSV_DELIMITER,
			QUOTE, CR, LF };
	public static final String QUOTE_STR = String.valueOf(QUOTE);

	/**
	 * Returns a {@link String} for a CSV column enclosed in double
	 * quotes, if required.
	 * 
	 * see <a
	 * href="http://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia</a>
	 * and <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
	 * 
	 * @param str the input String, may be null
	 * @return the input String enclosed in double quotes if required, or null
	 */
	public static String escapeCsv(String str) {
		if (containsNone(str, CSV_SEARCH_CHARS))
			return str;
		StringWriter out = new StringWriter();
		out.write(QUOTE);
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == QUOTE)
				out.write(QUOTE);
			out.write(c);
		}
		out.write(QUOTE);
		return out.toString();
	}

	public static String unescapeCsv(String str) {
		if (str == null)
			return null;
		if (!(str.charAt(0) == QUOTE && str.charAt(str.length() - 1) == QUOTE))
			return str;
		String quoteless = str.substring(1, str.length() - 1);
		return quoteless.replace(QUOTE_STR + QUOTE_STR, QUOTE_STR);
	}

	private static boolean containsNone(String str, char[] csvSearchChars) {
		if (str == null)
			return true;
		for (int i = 0; i < str.length(); i++)
			for (int j = 0; j < csvSearchChars.length; j++)
				if (str.charAt(i) == csvSearchChars[j])
					return false;
		return true;
	}

}