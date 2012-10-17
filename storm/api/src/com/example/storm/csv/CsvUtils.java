package com.example.storm.csv;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {

	public static final char DELIMITER = ',';
	public static final char QUOTE = '"';
	public static final char CR = '\r';
	public static final char LF = '\n';
	public static final char[] CSV_SEARCH_CHARS = new char[] { DELIMITER,
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
		if (str == null)
			return "";
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

	/**
	 * Return values from a CSV String.
	 * 
	 * @param csvRow
	 * @return
	 */
	public static List<String> getValues(String csvRow) {
		List<String> values = new ArrayList<String>();
		StringReader in = new StringReader(csvRow);
		String value;
		try {
			value = nextValue(in);
			while (true) {
				values.add(value);
				value = nextValue(in);
			}
		} catch (IOException e) {
			// TODO handle case of final null value better?
			if (csvRow.lastIndexOf(',') == csvRow.length() - 1)
				values.add(null);
			return values;
		}
	}

	public static String nextValue(StringReader in) throws IOException {
		StringWriter w = new StringWriter();
		boolean inQuotedValue = false;
		boolean openQuote = false;
		int c = in.read();
		if (c == QUOTE)
			inQuotedValue = true;
		else if (c == DELIMITER) {
			return null;
		} else if (c >= 0) {
			w.write(c);
		} else {
			throw new IOException("End of line reached");
		}
		c = in.read();
		while (c >= 0) {
			if (c == QUOTE) {
				if (inQuotedValue) {
					if (openQuote) {
						openQuote = false;
						w.write(QUOTE);
					} else {
						openQuote = true;
					}
				} else
					// invalid
					return w.toString();
			} else if (c == DELIMITER) {
				if (openQuote)
					return w.toString();
				else if (inQuotedValue)
					w.write(c);
				else
					// invalid
					return w.toString();
			} else
				w.write(c);
			c = in.read();
		}
		return w.toString();
	}

}