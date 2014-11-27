import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {
	static Grammar g;
	static LL ll;
	static SLR slr;
	static Boolean llCond;

	static final byte MAIN_MENU = 0;
	static final byte LOAD_GRAMMAR = 1;
	static final byte SAVE_GRAMMAR = 2;
	static final byte LL = 3;
	static final byte SLR = 4;
	static final String os = System.getProperty("os.name");

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		boolean end = false;
		byte menu = MAIN_MENU;
		String line;
		char opt;
		while (!end) {
			/*
			if (os.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
			*/
			switch (menu) {
				case MAIN_MENU:
				System.out.println("1 - Load Grammar");
				System.out.println("2 - Save Grammar");
				System.out.println("3 - Print Grammar");
				System.out.println("4 - LL Analysis");
				System.out.println("5 - SLR Analysis");
				System.out.println("6 - Exit\n");
				System.out.print("Select option: ");
				line = sc.nextLine();
				opt = (line.length() > 0) ? line.charAt(0) : '?';
				switch (opt) {
					case '1':
					menu = LOAD_GRAMMAR;
					break;
					case '2':
					if (g == null) {
						System.out.println("You need to load a grammar first!\n");
					} else {
						menu = SAVE_GRAMMAR;
					}
					break;
					case '3':
					if (g == null) {
						System.out.println("You need to load a grammar first!\n");
					} else {
						System.out.println("\n /=====================\\");
						System.out.println("|  Grammar description  |");
						System.out.println(" \\=====================/");
						PrintWriter pw = new PrintWriter(System.out);
						g.writeGRA(pw);
						pw.flush();
						System.out.println();
					}
					break;
					case '4':
					if (g == null) {
						System.out.println("You need to load a grammar first!\n");
					} else {
						menu = LL;
					}
					break;
					case '5':
					if (g == null) {
						System.out.println("You need to load a grammar first!\n");
					} else {
						menu = SLR;
					}
					break;
					case '6':
					end = true;
				}
				break;
				case LOAD_GRAMMAR:
				System.out.println("1 - GRA Format");
				System.out.println("2 - DAT Format");
				System.out.println("3 - Return\n");
				System.out.print("Select option: ");
				line = sc.nextLine();
				opt = (line.length() > 0) ? line.charAt(0) : '?';
				switch (opt) {
					case '1':
					ll = null;
					slr = null;
					System.out.println();
					System.out.print("File: ");
					try {
						g = new Grammar();
						g.readGRA(new FileReader(sc.nextLine()));
						System.out.println("Done!!");
					} catch (Exception e) {
						System.out.println("Error reading file!");
						g = null;
					}
					break;
					case '2':
					ll = null;
					slr = null;
					System.out.println();
					System.out.print("File: ");
					try {
						g = new Grammar();
						g.readDAT(new FileReader(sc.nextLine()));
						System.out.println("Done!!");
					} catch (Exception e) {
						System.out.println("Error reading file!");
						g = null;
					}
					break;
					case '3':
					menu = MAIN_MENU;
					break;
				}
				break;
				case SAVE_GRAMMAR:
				System.out.println("1 - GRA Format");
				System.out.println("2 - DAT Format");
				System.out.println("3 - Return\n");
				System.out.print("Select option: ");
				line = sc.nextLine();
				opt = (line.length() > 0) ? line.charAt(0) : '?';
				switch (opt) {
					case '1':
					System.out.println();
					System.out.print("File: ");
					try {
						PrintWriter pw = new PrintWriter(new FileWriter(sc.nextLine()));
						g.writeGRA(pw);
						pw.close();
						System.out.println("Done!!");
					} catch (Exception e) {
						System.out.println("Error writing file!");
					}
					break;
					case '2':
					System.out.println();
					System.out.print("File: ");
					try {
						PrintWriter pw = new PrintWriter(new FileWriter(sc.nextLine()));
						g.writeDAT(pw);
						pw.close();
						System.out.println("Done!!");
					} catch (Exception e) {
						System.out.println("Error writing file!");
					}
					break;
					case '3':
					menu = MAIN_MENU;
					break;
				}
				break;
				case LL:
					if(ll == null) {
						System.out.println("Processing grammar...");
						ll = new LL(g);
						llCond = ll.LL1condition();
						System.out.println("Done!\n");
					}
					if(!llCond) {
						System.out.println("!!! Grammar with no LL1 Condition !!!");
					}
					System.out.println("1 - Director Symbols");
					System.out.println("2 - Test String");
					System.out.println("3 - Return\n");
					System.out.print("Select option: ");
					line = sc.nextLine();
					opt = (line.length() > 0) ? line.charAt(0) : '?';
					switch(opt) {
						case '1':
						if(llCond) {
							System.out.println("\n /=================\\");
							System.out.println("|  Director symbols |");
							System.out.println(" \\=================/");
							for (Rule r : g) {
								System.out.println("Rule: " + r.antecedent() + " -> " + r.consequent());
								System.out.println("      " + ll.directorSymbols(r));
							}
						}
						else {
							System.out.println("Can't perform analysis!!! Grammar with no LL1 Condition!!!");
						}
						break;
						case '2':
						System.out.println();
						if(llCond) {
							System.out.print("Input string: ");
							SymbolString ss = new SymbolString(sc.nextLine());
							System.out.println("\nParsing process:");
							if(ll.LLAnalysis(ss)) {
								System.out.println("\nThe grammar accepts the string");
							}
							else {
								System.out.println("\nThe grammar rejects the string");
							}
						}
						else {
							System.out.println("Can't perform analysis!!! Grammar with no LL1 Condition!!!");
						}
						break;
						case '3':
						menu = MAIN_MENU;
						break;
					}
					break;
				case SLR:
					if(slr == null) {
						System.out.println("Processing grammar...");
						slr = new SLR(g);
						System.out.println("Done!\n");
					}
					System.out.println("1 - Items Set");
					System.out.println("2 - Action / Transition Table");
					System.out.println("3 - Test String");
					System.out.println("4 - Return\n");
					System.out.print("Select option: ");
					line = sc.nextLine();
					opt = (line.length() > 0) ? line.charAt(0) : '?';
					switch(opt) {
						case '1':
							System.out.println("\n /==================\\");
							System.out.println("|  States collection |");
							System.out.println(" \\==================/");
							System.out.print(slr.itemsSet());
							break;
						case '2':
							System.out.println("\n /==========================\\");
							System.out.println("|  Action / Transition table |");
							System.out.println(" \\==========================/");
							System.out.println("\nAction / Transition table:");
							System.out.println(slr.atTable());
							System.out.println("Legend:\n\tD - Displacement\n\tR - Reduction\n\tA - Accept\n\tX - Error");
							break;
						case '3':
							System.out.print("Input string: ");
							SymbolString ss = new SymbolString(sc.nextLine());
							System.out.println("\nParsing process:");
							if(slr.SLRAnalysis(ss)) {
								System.out.println("\nThe grammar accepts the string");
							}
							else {
								System.out.println("\nThe grammar rejects the string");
							}
							break;
						case '4':
							menu = MAIN_MENU;
							break;
					}
				break;
			}
			System.out.println();
		}
		sc.close();
	}
}
