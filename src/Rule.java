public class Rule {

	private NoTerminalSymbol antecedent;
	private SymbolString consequent;

	public Rule(NoTerminalSymbol antecedent, SymbolString consequent) {
		this.antecedent = antecedent;
		this.consequent = consequent;
	}

	public NoTerminalSymbol antecedent() {
		return this.antecedent;
	}

	public SymbolString consequent() {
		return this.consequent;
	}

	public String toString() {
		return antecedent.toString() + " -> " + consequent.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((antecedent == null) ? 0 : antecedent.hashCode());
		result = prime * result
				+ ((consequent == null) ? 0 : consequent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (antecedent == null) {
			if (other.antecedent != null)
				return false;
		} else if (!antecedent.equals(other.antecedent))
			return false;
		if (consequent == null) {
			if (other.consequent != null)
				return false;
		} else if (!consequent.equals(other.consequent))
			return false;
		return true;
	}
}
