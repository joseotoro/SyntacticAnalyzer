import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestLL {
	public static void main(String[] args) throws FileNotFoundException {
		FileReader fr = new FileReader("grammars/gra.dat");
		Grammar g = new Grammar();
		g.readDAT(fr);
		LL analysis = new LL(g);

		System.out.println("Director symbols:");
		for (Rule r : g) {
			System.out.println(r.antecedent() + " -> " + r.consequent());
			System.out.println(analysis.directorSymbols(r));
			System.out.println();
		}

		SymbolString c = new SymbolString();
		NoTerminalSymbol s = new NoTerminalSymbol();
		s.lexem = "n";
		c.add(s);
		s = new NoTerminalSymbol();
		s.lexem = "+";
		c.add(s);
		s = new NoTerminalSymbol();
		s.lexem = "n";
		c.add(s);
		s = new NoTerminalSymbol();
		s.lexem = "$";
		c.add(s);
		if (analysis.LLAnalysis(c)) {
			System.out.println("Accepts the string " + c);
		} else {
			System.out.println("Rejects the string " + c);
		}

	}
}