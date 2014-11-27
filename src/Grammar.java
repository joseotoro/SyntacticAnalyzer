import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("serial")
public class Grammar extends ArrayList<Rule> {
	private SymbolSet noTerminals;
	private SymbolSet terminals;
	private NoTerminalSymbol axiom;

	public Grammar() {

	}

	public SymbolSet noTerminals() {
		return noTerminals;
	}

	public SymbolSet terminals() {
		return terminals;
	}

	public NoTerminalSymbol axiom() {
		return axiom;
	}

	public void readGRA(FileReader fr) {
		this.clear();
		noTerminals = new SymbolSet();
		terminals = new SymbolSet();
		axiom = new NoTerminalSymbol();
		Scanner sc = new Scanner(fr);
		Scanner sc2;
		char[] temp;

		while (!sc.nextLine().trim().equals("TERMINALS"))
			;
		temp = sc.nextLine().trim().toCharArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != ' ' && temp[i] != ';') {
				TerminalSymbol s = new TerminalSymbol();
				s.lexem = new Character(temp[i]).toString();
				terminals.add(s);
			}
		}

		while (!sc.nextLine().trim().equals("NOTERMINALS"))
			;
		temp = sc.nextLine().trim().toCharArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != ' ' && temp[i] != ';') {
				NoTerminalSymbol s = new NoTerminalSymbol();
				s.lexem = new Character(temp[i]).toString();
				noTerminals.add(s);
			}
		}

		while (!sc.nextLine().trim().equals("AXIOM"))
			;
		temp = sc.nextLine().trim().toCharArray();
		// If there is not axiom indicated
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != ' ' && temp[i] != ';') {
				axiom.lexem = new Character(temp[i]).toString();
			}
		}

		while (!sc.nextLine().trim().equals("RULES"))
			;
		while (sc.hasNextLine()) {
			String rule = sc.nextLine().trim();
			String[] rr = rule.split("->");
			NoTerminalSymbol pre = new NoTerminalSymbol();
			pre.lexem = rr[0].trim().toUpperCase();
			SymbolString sm = new SymbolString();

			if (rr.length == 2) {
				sc2 = new Scanner(rr[1]);
				do {
					String a = sc2.next();
					char c;
					if (a.length() == 3)
						c = a.toCharArray()[1];
					else
						c = a.toCharArray()[0];
					if (Character.isAlphabetic(c) && Character.isUpperCase(c)) {
						NoTerminalSymbol s = new NoTerminalSymbol();
						s.lexem = new Character(c).toString();
						sm.add(s);
					} else {
						TerminalSymbol s = new TerminalSymbol();
						s.lexem = new Character(c).toString();
						sm.add(s);
					}
				} while (sc2.hasNext());
				sc2.close();
			}
			Rule r = new Rule(pre, sm);
			this.add(r);

			rule = sc.nextLine();
			while (!rule.trim().equals(";")) {
				sm = new SymbolString();
				sc2 = new Scanner(rule.trim().substring(1).trim());
				while (sc2.hasNext()) {
					String a = sc2.next();
					char c;
					if (a.length() == 3)
						c = a.toCharArray()[1];
					else
						c = a.toCharArray()[0];
					if (Character.isAlphabetic(c) && Character.isUpperCase(c)) {
						NoTerminalSymbol s = new NoTerminalSymbol();
						s.lexem = new Character(c).toString();
						sm.add(s);
					} else {
						TerminalSymbol s = new TerminalSymbol();
						s.lexem = new Character(c).toString();
						sm.add(s);
					}
				}
				sc2.close();

				r = new Rule(pre, sm);
				this.add(r);
				rule = sc.nextLine();
			}
		}
		sc.close();
	}

	public void readDAT(FileReader fr) {
		this.clear();
		noTerminals = new SymbolSet();
		terminals = new SymbolSet();
		axiom = new NoTerminalSymbol();

		Scanner sc = new Scanner(fr);
		while (sc.hasNextLine()) {
			String rule = sc.nextLine();
			String[] rr = rule.split("->");

			// If found one with bad format, end
			if (rr[0].equals(rule))
				break;

			NoTerminalSymbol nt = new NoTerminalSymbol();
			nt.lexem = rr[0].toUpperCase();
			noTerminals.add(nt);
			if (axiom.lexem == null)
				axiom.lexem = rr[0];

			SymbolString sm = new SymbolString();

			// Consequent not equal to epsilon
			if (rr.length == 2) {
				char[] consequent = rr[1].toCharArray();
				for (char c : consequent) {
					if (Character.isAlphabetic(c) && Character.isUpperCase(c)) {
						NoTerminalSymbol s = new NoTerminalSymbol();
						s.lexem = new Character(c).toString();
						sm.add(s);
						noTerminals.add(s);
					} else {
						TerminalSymbol s = new TerminalSymbol();
						s.lexem = new Character(c).toString();
						sm.add(s);
						if (!s.lexem.equals("$"))
							terminals.add(s);
					}
				}
			}

			Rule r = new Rule(nt, sm);
			this.add(r);
		}
		sc.close();
	}

	public void writeGRA(PrintWriter pw) {
		pw.println("TERMINALS");
		pw.print("   ");
		for (Symbol s : terminals) {
			pw.print(s + "; ");
		}
		pw.println("\n");

		pw.println("NOTERMINALS");
		pw.print("   ");
		for (Symbol s : noTerminals) {
			pw.print(s + "; ");
		}
		pw.println("\n");

		pw.println("AXIOM");
		pw.print("   ");
		pw.println(axiom + ";" + "\n");

		pw.println("RULES");
		Rule r = this.get(0);
		pw.print("   " + r.antecedent() + " ->");
		for (Symbol s : r.consequent()) {
			if (s instanceof TerminalSymbol) {
				pw.print(" '" + s + "'");
			} else {
				pw.print(" " + s);
			}
		}
		pw.println();
		for (int i = 1; i < this.size(); i++) {
			r = this.get(i);
			if (r.antecedent().equals(this.get(i - 1).antecedent())) {
				pw.print("      |");
				for (Symbol s : r.consequent()) {
					if (s instanceof TerminalSymbol) {
						pw.print(" '" + s + "'");
					} else {
						pw.print(" " + s);
					}
				}
				pw.println();
			} else {
				pw.println("      ;");
				pw.print("   " + r.antecedent() + " ->");
				for (Symbol s : r.consequent()) {
					if (s instanceof TerminalSymbol) {
						pw.print(" '" + s + "'");
					} else {
						pw.print(" " + s);
					}
				}
				pw.println();
			}
		}
		pw.print("      ;");
	}

	public void writeDAT(PrintWriter pw) {
		for (Rule g : this) {
			pw.println(g.toString().replaceAll("EPSILON", "")
					.replaceAll(" ", ""));
		}
	}

	public SymbolSet header(SymbolString alfa) {
		SymbolSet examined = new SymbolSet();
		return headerAux(alfa, examined);
	}

	private SymbolSet headerAux(SymbolString alfa, SymbolSet examined) {
		boolean end = false;
		SymbolSet res = new SymbolSet();

		int i = 0;
		while (!end && i != alfa.size()) {
			if (alfa.isEmpty()) {
				res.add(MetaSymbol.EPSILON);
				end = true;
			} else if (alfa.get(i) instanceof TerminalSymbol) {
				res.add(alfa.get(i));
				end = true;
			} else {
				for (Rule r : this) {
					if (r.antecedent().equals(alfa.get(i))
							&& !examined.contains(alfa.get(i))) {
						SymbolSet c = new SymbolSet();
						for (Symbol s : examined)
							c.add(s);
						c.add(alfa.get(i));
						res.addAll(headerAux(r.consequent(), c));
					}
				}
				if (res.contains(MetaSymbol.EPSILON)) {
					res.remove(MetaSymbol.EPSILON);
					i++;
				} else {
					end = true;
				}
			}
		}
		if (i == alfa.size()) {
			res.add(MetaSymbol.EPSILON);
		}
		return res;
	}

	public SymbolSet next(NoTerminalSymbol s) {
		SymbolSet examined = new SymbolSet();
		examined.add(s);

		return nextAux(s, examined);
	}

	public SymbolSet nextAux(NoTerminalSymbol s, SymbolSet examined) {
		SymbolSet res = new SymbolSet();
		for (Rule r : this) {
			int index;
			if ((index = r.consequent().indexOf(s)) >= 0) {
				if (index == r.consequent().length() - 1
						&& !examined.contains(r.antecedent())) {
					examined.add(r.antecedent());
					res.addAll(nextAux(r.antecedent(), examined));
				} else {
					SymbolString c = new SymbolString();
					for (int i = index + 1; i < r.consequent().size(); i++)
						c.add(r.consequent().get(i));
					res.addAll(header(c));
					if (res.contains(MetaSymbol.EPSILON)) {
						res.remove(MetaSymbol.EPSILON);
						if (!examined.contains(r.antecedent())) {
							examined.add(r.antecedent());
							res.addAll(nextAux(r.antecedent(), examined));
						}
					}
				}
			}
		}

		return res;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size(); i++) {
			sb.append(get(i) + "\n");
		}
		return sb.toString();
	}
}
