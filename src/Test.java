import java.io.FileNotFoundException;
import java.io.FileReader;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		FileReader fr = new FileReader("grammars/test2.dat");
		Grammar g = new Grammar();
		g.readDAT(fr);
		SLR analysis = new SLR(g);

		System.out.println("States collection:");
		System.out.println(analysis.itemsSet());

		System.out.println("Action / Transition table");
		System.out.println(analysis.atTable());

		SymbolString string = new SymbolString();
		TerminalSymbol s = new TerminalSymbol();
		s.lexem = "n";
		string.add(s);
		s = new TerminalSymbol();
		s.lexem = "+";
		// cad.add(s);
		s = new TerminalSymbol();
		s.lexem = "n";
		// cad.add(s);
		s = new TerminalSymbol();
		s.lexem = "$";
		string.add(s);

		if (analysis.SLRAnalysis(string)) {
			System.out.println("Accepts the string " + string);
		} else {
			System.out.println("Rejects the string " + string);
		}
	}
}
