import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestSLR {
	public static void main(String[] args) throws FileNotFoundException {
		FileReader fr = new FileReader("grammars/gra.dat");
		Grammar g = new Grammar();
		g.readDAT(fr);
		SLR analisis = new SLR(g);

		System.out.println("Rules:");
		for (int i = 0; i < g.size(); i++) {
			System.out.println("R" + i + ": " + g.get(i).antecedent() + " -> "
					+ g.get(i).consequent());
		}
		System.out.println();

		System.out.println("States collection:");
		System.out.println(analisis.itemsSet());

		System.out.println("Action / Transition table:");
		System.out.println(analisis.atTable());

		SymbolString string = new SymbolString();
		TerminalSymbol s = new TerminalSymbol();
		s.lexem = "n";
		string.add(s);
		s = new TerminalSymbol();
		s.lexem = "+";
		string.add(s);
		s = new TerminalSymbol();
		s.lexem = "n";
		string.add(s);
		s = new TerminalSymbol();
		s.lexem = "$";
		string.add(s);

		if (analisis.SLRAnalysis(string)) {
			System.out.println("Accepts the string " + string);
		} else {
			System.out.println("Rejects the string " + string);
		}
	}
}
