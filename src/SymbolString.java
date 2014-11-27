import java.util.ArrayList;

@SuppressWarnings("serial")
public class SymbolString extends ArrayList<Symbol> {

	public SymbolString() {
		super();
	}

	public SymbolString(String string) {
		for(int i = 0; i < string.length(); i++) {
			NoTerminalSymbol s = new NoTerminalSymbol();
			s.lexem = string.substring(i, i + 1);
			this.add(s);
		}
		NoTerminalSymbol s = new NoTerminalSymbol();
		s.lexem = "$";
		this.add(s);
	}

	public boolean isEpsilon() {
		return isEmpty();
	}

	public int length() {
		return size();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (!isEpsilon()) {
			for (int i = 0; i < size(); i++) {
				sb.append(get(i));
			}
		} else {
			sb.append(MetaSymbol.EPSILON);
		}
		return sb.toString();
	}

}
