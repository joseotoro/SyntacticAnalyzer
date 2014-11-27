public final class MetaSymbol extends Symbol {

	public final static MetaSymbol EPSILON = new MetaSymbol("EPSILON");
	public final static MetaSymbol DOLAR = new MetaSymbol("$");
	public final static MetaSymbol DOT = new MetaSymbol(".");

	private MetaSymbol(String lexem) {
		this.lexem = lexem;
	}

}
