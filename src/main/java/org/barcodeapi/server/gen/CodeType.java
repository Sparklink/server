package org.barcodeapi.server.gen;

public enum CodeType {

	/**
	 * UPC-E type UPC code;
	 */
	UPC_E(new String[] { "e", "upc-e" }, //
			"^(?=.*0)[0-9]{8}$", //
			"^(?=.*0)[0-9]{7,8}$"),

	/**
	 * UPC-A type UPC code;
	 */
	UPC_A(new String[] { "a", "upc-a", "upc" }, //
			"^(?=.*0)[0-9]{12}$", //
			"^(?=.*0)[0-9]{11,12}$"),

	/**
	 * EAN-8 type UPC code;
	 * 
	 * 7 numerical digits followed by a single checksum digit.
	 */
	EAN8(new String[] { "8", "ean8" }, //
			"^[0-9]{8}$", //
			"^[0-9]{7,8}$"),

	/**
	 * EAN-13 type UPC code;
	 * 
	 * 12 numerical digits followed by a single checksum digit.
	 */
	EAN13(new String[] { "13", "ean13" }, //
			"^[0-9]{13}$", //
			"^[0-9]{12,13}$"),

	/**
	 * Codabar type code;
	 */
	CODABAR(new String[] { "codabar" }, //
			"^[0-9:$]{1,16}$", //
			"^[0-9-:$\\/.+]+$"), //

	/**
	 * Code39 type code;
	 * 
	 * Variable length consisting of only numbers and upper-case characters.
	 */
	Code39(new String[] { "39", "code39" }, //
			"^[A-Z0-9 $.\\/]{1,20}$", //
			"^[A-Z*0-9 -$%.\\/+]+$"),

	/**
	 * Code128 type code;
	 * 
	 * Variable length consisting of numbers, letters, and symbols.
	 */
	Code128(new String[] { "128", "code128" }, //
			"^[ !#$()*.\\/0-9=?A-Z_a-z~]{1,24}$", //
			"^[ !\"#$%&'()*+,-.\\/0-9:;<=>?@A-Z\\[\\\\\\]^_`a-z{|}~]+$"),

	/**
	 * QR type code;
	 * 
	 * A high density data code with error correction.
	 */
	QRCode(new String[] { "qr", "qrcode" }, //
			"^.{1,64}$", //
			"^.{1,65535}$"),

	/**
	 * Data Matrix type code;
	 * 
	 * A high density data code with error correction.
	 */
	DataMatrix(new String[] { "matrix", "datamatrix", "data" }, //
			"^[ !\"#$%&'()*+,-.\\/0-9:;<=>?@A-Z\\[\\\\\\]^_`a-z{|}~]{1,2335}$", //
			"^[ !\"#$%&'()*+,-.\\/0-9:;<=>?@A-Z\\[\\\\\\]^_`a-z{|}~]{1,2335}$");

	/**
	 * Local Variables
	 */
	private final String[] types;

	private final String autoPattern;
	private final String formatPattern;

	/**
	 * Create a new CodeType with its pattern and list of associated IDs.
	 * 
	 * @param typeStrings
	 */
	CodeType(String[] typeStrings, String automatchPattern, String extendedPattern) {

		this.types = typeStrings;

		this.autoPattern = automatchPattern;
		this.formatPattern = extendedPattern;
	}

	/**
	 * Get a list of all IDs associated with a CodeType.
	 * 
	 * @return
	 */
	public String[] getTypeStrings() {

		return types;
	}

	/**
	 * Get the regular expression that matches on auto-typing.
	 * 
	 * @return
	 */
	public String getAutomatchPattern() {

		return autoPattern;
	}

	/**
	 * Get the regular expression that validates the data for the code type.
	 * 
	 * @return
	 */
	public String getFormatPattern() {

		return formatPattern;
	}
}