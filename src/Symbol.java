public abstract class Symbol {

	protected String lexem;

	public String toString() {
		return lexem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lexem == null) ? 0 : lexem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return ((Symbol) (obj)).lexem.equals(lexem);
	}
}
