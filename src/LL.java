import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LL {
	private Grammar g;
	private SymbolString[][] table;

	private List<NoTerminalSymbol> noTerminals;
	private List<TerminalSymbol> terminals;

	public LL(Grammar g) {
		this.g = g;
		noTerminals = new ArrayList<NoTerminalSymbol>();
		terminals = new ArrayList<TerminalSymbol>();

		if (LL1condition()) {
			for (Symbol s : g.noTerminals()) {
				noTerminals.add((NoTerminalSymbol) s);
			}
			for (Symbol s : g.terminals()) {
				terminals.add((TerminalSymbol) s);
			}
			TerminalSymbol dolar = new TerminalSymbol();
			dolar.lexem = "$";
			terminals.add(dolar);

			table = new SymbolString[noTerminals.size()][terminals.size()];
			for (Rule r : g) {
				SymbolSet c = directorSymbols(r);
				for (Symbol s : c) {
					table[noTerminals.indexOf(r.antecedent())][terminals
					                                           .indexOf(s)] = r.consequent();
				}
			}
		}
	}

	public boolean LLAnalysis(SymbolString input) {
		boolean error = false;
		SymbolString alfa = new SymbolString();
		Stack<Symbol> stack = new Stack<Symbol>();
		List<Symbol> print = new ArrayList<Symbol>();
		stack.add(g.axiom());
		for (Symbol s : input) {
			alfa.add(s);
		}
		System.out.print(g.axiom());
		do {
			Symbol x = stack.peek();
			Symbol a = alfa.get(0);
			SymbolString c;
			if (noTerminals.contains(x)
					&& (c = table[noTerminals.indexOf(x)][terminals.indexOf(a)]) != null) {
				stack.pop();
				for (int i = c.size() - 1; i >= 0; i--) {
					stack.add(c.get(i));
				}
				System.out.print(" -> ");
				for (Symbol s : print) {
					System.out.print(s);
				}
				for (int i = stack.size() - 1; i >= 0; i--) {
					System.out.print(stack.get(i));
				}
			} else if (terminals.contains(x) && x.equals(a)) {
				alfa.remove(0);
				stack.pop();
				print.add(a);
			} else {
				error = true;
			}
		} while (!error && !stack.isEmpty() && !alfa.isEmpty());

		System.out.println();
		return !error && stack.isEmpty();
	}

	public SymbolSet directorSymbols(Rule r) {
		SymbolSet res = g.header(r.consequent());

		if (res.contains(MetaSymbol.EPSILON)) {
			res.remove(MetaSymbol.EPSILON);
			res.addAll(g.next(r.antecedent()));
		}

		return res;
	}

	public boolean LL1condition() {
		for (Symbol s : g.noTerminals()) {
			SymbolSet c1 = new SymbolSet();
			for (Rule r : g) {
				if (r.antecedent().equals(s)) {
					SymbolSet c2 = directorSymbols(r);
					for (Symbol ss : c2) {
						if (c1.contains(ss))
							return false;
						c1.add(ss);
					}
				}
			}
		}

		return true;
	}
}