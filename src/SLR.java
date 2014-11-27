import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class SLR {
	private Grammar g;
	private List<NoTerminalSymbol> noTerminals;
	private List<TerminalSymbol> terminals;
	private List<Symbol> symbols;
	private List<Set<Rule>> collection;
	private String[][] TAT;

	public SLR(Grammar g) {
		this.g = g;
		noTerminals = new ArrayList<NoTerminalSymbol>();
		terminals = new ArrayList<TerminalSymbol>();
		symbols = new ArrayList<Symbol>();
		for (Symbol s : g.noTerminals()) {
			noTerminals.add((NoTerminalSymbol) s);
		}
		for (Symbol s : g.terminals()) {
			terminals.add((TerminalSymbol) s);
		}
		TerminalSymbol dolar = new TerminalSymbol();
		dolar.lexem = "$";
		terminals.add(dolar);

		symbols.addAll(terminals);
		symbols.addAll(noTerminals);

		Set<Rule> ini = new HashSet<Rule>();
		Rule rule0 = new Rule(g.get(0).antecedent(), new SymbolString());
		rule0.consequent().add(MetaSymbol.DOT);
		for (Symbol s : g.get(0).consequent()) {
			rule0.consequent().add(s);
		}
		ini.add(rule0);
		List<Set<Rule>> newCollection = new ArrayList<Set<Rule>>();
		List<Set<Rule>> old = new ArrayList<Set<Rule>>();
		newCollection.add(closure(ini));
		do {
			List<Set<Rule>> dif = new ArrayList<Set<Rule>>();
			for (Set<Rule> set : newCollection) {
				if (!old.contains(set))
					dif.add(set);
			}

			for (Set<Rule> set : dif) {
				for (Symbol s : terminals) {
					Set<Rule> j = delta(set, s);
					if (!j.isEmpty() && !newCollection.contains(j)) {
						newCollection.add(j);
					}
				}
				for (Symbol s : noTerminals) {
					Set<Rule> j = delta(set, s);
					if (!j.isEmpty() && !newCollection.contains(j)) {
						newCollection.add(j);
					}
				}
			}
			old.addAll(dif);
		} while (!newCollection.equals(old));

		collection = newCollection;
		makeTable();

	}

	public Set<Rule> delta(Set<Rule> col, Symbol alpha) {
		Set<Rule> res = new HashSet<Rule>();
		for (Rule r : col) {
			int index = r.consequent().indexOf(MetaSymbol.DOT);
			if (index != r.consequent().size() - 1
					&& r.consequent().get(index + 1).equals(alpha)) {
				Rule nuevaR = new Rule(r.antecedent(), new SymbolString());
				for (int i = 0; i < index; i++) {
					nuevaR.consequent().add(r.consequent().get(i));
				}
				nuevaR.consequent().add(r.consequent().get(index + 1));
				nuevaR.consequent().add(MetaSymbol.DOT);
				for (int i = index + 2; i < r.consequent().size(); i++) {
					nuevaR.consequent().add(r.consequent().get(i));
				}
				res.add(nuevaR);
			}
		}
		return closure(res);
	}

	public Set<Rule> closure(Set<Rule> col) {
		Set<Rule> res = new HashSet<Rule>(col);
		for (Rule r : col) {
			int index = r.consequent().indexOf(MetaSymbol.DOT);
			if (index != r.consequent().size() - 1
					&& noTerminals.contains(r.consequent().get(index + 1))) {
				NoTerminalSymbol s = (NoTerminalSymbol) r.consequent().get(
						index + 1);
				for (Rule rr : g) {
					if (rr.antecedent().equals(s)) {
						Rule nRule = new Rule(s, new SymbolString());
						nRule.consequent().add(MetaSymbol.DOT);
						for (Symbol ss : rr.consequent()) {
							nRule.consequent().add(ss);
						}
						res.add(nRule);
					}
				}
			}
		}
		if (!res.equals(col)) {
			return closure(res);
		}
		return col;
	}

	private void makeTable() {
		// In case of conflict, reduction is applied
		TAT = new String[collection.size()][symbols.size()];
		for (int i = 0; i < collection.size(); i++) {
			for (int j = 0; j < symbols.size(); j++) {
				Set<Rule> set = delta(collection.get(i), symbols.get(j));
				if (!set.isEmpty()) {
					TAT[i][j] = "D" + collection.indexOf(set);
				}
				for (Rule r : collection.get(i)) {
					if (!r.antecedent().equals(g.axiom())
							&& r.consequent().indexOf(MetaSymbol.DOT) == r
							.consequent().size() - 1) {
						if (g.next(r.antecedent()).contains(symbols.get(j))) {
							Rule rr = new Rule(r.antecedent(),
									new SymbolString());
							for (int k = 0; k < r.consequent().size() - 1; k++) {
								rr.consequent().add(r.consequent().get(k));
							}
							TAT[i][j] = "R" + g.indexOf(rr);
						}
					}
				}
				for (Rule r : collection.get(i)) {
					if (r.antecedent().equals(g.axiom())
							&& r.consequent().indexOf(MetaSymbol.DOT) == r
							.consequent().size() - 1) {
						for (int k = 0; k < symbols.size(); k++) {
							TAT[i][k] = "A";
						}
						j = symbols.size();
						break;
					}
				}
				if (j < symbols.size() && TAT[i][j] == null) {
					TAT[i][j] = "X";
				}
			}
		}
	}

	public boolean SLRAnalysis(SymbolString input) {
		Stack<Symbol> stack = new Stack<Symbol>();
		Stack<Integer> states = new Stack<Integer>();
		SymbolString alfa = new SymbolString();
		boolean error = false;
		boolean accept = false;
		states.add(0);
		for (Symbol s : input) {
			alfa.add(s);
		}

		do {
			if (TAT[states.peek()][0] == "A") {
				accept = true;
			} else if (alfa.isEmpty()) {
				error = true;
			} else {
				String action = TAT[states.peek()][symbols.indexOf(alfa.get(0))];

				System.out.print(action + " -> ");

				if (action.charAt(0) == 'D') {
					stack.add(alfa.get(0));
					alfa.remove(0);
					states.add(Integer.parseInt(action.substring(1)));
				} else if (action.charAt(0) == 'R') {
					int numRule = Integer.parseInt(action.substring(1));
					Rule r = g.get(numRule);
					for (int i = 0; i < r.consequent().size(); i++) {
						stack.pop();
						states.pop();
					}
					stack.add(r.antecedent());
					String recomp = TAT[states.peek()][symbols.indexOf(r
							.antecedent())];
					states.add(Integer.parseInt(recomp.substring(1)));
				} else {
					error = true;
				}
			}
		} while (!accept && !error);
		if(accept) System.out.println("A");
		return accept;
	}

	public String itemsSet() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < collection.size(); i++) {
			sb.append("I" + i + ": " + collection.get(i) + "\n");
		}
		return sb.toString();
	}

	public String atTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("   ");
		for (int i = 0; i < symbols.size(); i++) {
			sb.append(String.format("%3s", symbols.get(i)) + " ");
		}
		sb.append("\n");
		for (int i = 0; i < collection.size(); i++) {
			sb.append(String.format("%2d", i) + " ");
			for (int j = 0; j < symbols.size(); j++) {
				sb.append(String.format("%3s", TAT[i][j]) + " ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}