package TaikoNote;

import java.util.Scanner;

//入力受付と入力チェックを担当するUtilクラス
public class InputUtil {
	private Scanner scanner;

	public InputUtil() {
		scanner = new Scanner(System.in);
	}

	//プロンプト表示後 空白区切りの文字列を受け取る
	public String next(String prompt) {
		System.out.print(prompt);
		return scanner.next();
	}

	//プロンプト表示済みの状態で空白区切りの文字列を受け取る
	public String next() {
		return scanner.next();
	}

	//プロンプト表示後 整数の入力を受け取る
	public int nextInt(String prompt) {
		System.out.print(prompt);
		return scanner.nextInt();
	}

	//プロンプト表示済みの状態で整数の入力を受け取る
	public int nextInt() {
		return scanner.nextInt();
	}

	//プロンプト表示後 1行分の文字列を受け取る（空欄ならそのまま空文字列）
	public String nextLine(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine();
	}

	//nextInt()やnext()の後に残る改行を読み捨てる（nextLine()に切り替える前に呼ぶ）
	public void skipNewLine() {
		scanner.nextLine();
	}

	//プロンプト表示後 有効な整数が入るまで再入力（数字以外でも落ちない）
	public int nextIntSafe(String prompt) {
		while (true) {
			System.out.print(prompt);
			String line = scanner.nextLine();
			Integer value = parseIntSafe(line.trim());
			if (value != null) {
				return value;
			}
			System.out.println("数字を入力してください");
		}
	}

	//プロンプト表示後 min~max範囲の整数が入るまで再入力（メニュー選択用）
	public int nextIntInRange(String prompt, int min, int max) {
		while (true) {
			int value = nextIntSafe(prompt);
			if (value >= min && value <= max) {
				return value;
			}
			System.out.println(min + "~" + max + "で入力してください");
		}
	}

	//プロンプト表示後 空欄（変更なし）か有効な整数が入るまで再入力 空欄ならnull
	public Integer nextIntOrEmpty(String prompt) {
		while (true) {
			System.out.print(prompt);
			String line = scanner.nextLine().trim();
			if (line.isEmpty()) {
				return null;
			}
			Integer value = parseIntSafe(line);
			if (value != null) {
				return value;
			}
			System.out.println("数字を入力してください（空欄で変更なし）");
		}
	}

	//プロンプト表示後 空欄（変更なし）かmin~max範囲の整数が入るまで再入力 空欄ならnull
	public Integer nextIntInRangeOrEmpty(String prompt, int min, int max) {
		while (true) {
			Integer value = nextIntOrEmpty(prompt);
			if (value == null) {
				return null;
			}
			if (value >= min && value <= max) {
				return value;
			}
			System.out.println(min + "~" + max + "で入力してください（空欄で変更なし）");
		}
	}

	//プロンプト表示後 "y"か"n"が入るまで再入力
	public boolean nextYesNoSafe(String prompt) {
		while (true) {
			System.out.print(prompt);
			String line = scanner.nextLine().trim();
			if (line.equals("y") || line.equals("n")) {
				return line.equals("y");
			}
			System.out.println("yかnを入力してください");
		}
	}

	//プロンプト表示後 空欄（変更なし）か"y"/"n"が入るまで再入力 空欄ならnull
	public Boolean nextYesNoOrEmpty(String prompt) {
		while (true) {
			System.out.print(prompt);
			String line = scanner.nextLine().trim();
			if (line.isEmpty()) {
				return null;
			}
			if (line.equals("y") || line.equals("n")) {
				return line.equals("y");
			}
			System.out.println("yかnを入力してください（空欄で変更なし）");
		}
	}

	//文字列が数字として正しく変換できるかチェック 失敗時はnull
	public Integer parseIntSafe(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public void close() {
		scanner.close();
	}
}