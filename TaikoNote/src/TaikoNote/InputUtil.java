package TaikoNote;

import java.util.Scanner;

// ターミナルからの入力受付・チェックを行うユーティリティクラス。
public class InputUtil {

	// 空文字を許可しない文字列入力を受け付ける。
	public static String readNonEmptyString(Scanner sc, String prompt) {
		while (true) {
			System.out.print(prompt);
			String input = sc.nextLine().trim();
			if (!input.isEmpty()) {
				return input;
			}
			System.out.println("※ 空欄では登録できません。もう一度入力してください。");
		}
	}

	// 0以上の整数を受け付ける。
	public static int readNonNegativeInt(Scanner sc, String prompt) {
		while (true) {
			System.out.print(prompt);
			String input = sc.nextLine().trim();
			try {
				int value = Integer.parseInt(input);
				if (value < 0) {
					System.out.println("※ 0以上の整数を入力してください。");
					continue;
				}
				return value;
			} catch (NumberFormatException e) {
				System.out.println("※ 数値を入力してください。");
			}
		}
	}

	// 指定した範囲(min〜max)の整数を受け付ける。
	public static int readIntInRange(Scanner sc, String prompt, int min, int max) {
		while (true) {
			System.out.print(prompt);
			String input = sc.nextLine().trim();
			try {
				int value = Integer.parseInt(input);
				if (value < min || value > max) {
					System.out.println("※ " + min + "〜" + max + " の範囲で入力してください。");
					continue;
				}
				return value;
			} catch (NumberFormatException e) {
				System.out.println("※ 数値を入力してください。");
			}
		}
	}

	// 0.0〜100.0の範囲のパーセント値（達成率）を受け付け
	public static double readPercent(Scanner sc, String prompt) {
		while (true) {
			System.out.print(prompt);
			String input = sc.nextLine().trim();
			try {
				double value = Double.parseDouble(input);
				if (value < 0.0 || value > 100.0) {
					System.out.println("※ 0〜100 の範囲で入力してください。");
					continue;
				}
				return value;
			} catch (NumberFormatException e) {
				System.out.println("※ 数値を入力してください。");
			}
		}
	}

	//更新処理用：空欄のままEnterされた場合は現在値を維持する整数入力。
	public static int readOptionalInt(Scanner sc, String prompt, int currentValue) {
		while (true) {
			System.out.print(prompt);
			String input = sc.nextLine().trim();
			if (input.isEmpty()) {
				return currentValue;
			}
			try {
				int value = Integer.parseInt(input);
				if (value < 0) {
					System.out.println("※ 0以上の整数を入力してください。");
					continue;
				}
				return value;
			} catch (NumberFormatException e) {
				System.out.println("※ 数値を入力してください。");
			}
		}
	}

	//難易度分類を選択
	public static Difficulty readDifficulty(Scanner sc) {
		Difficulty[] values = Difficulty.values();
		while (true) {
			System.out.println("難易度を選択してください:");
			for (int i = 0; i < values.length; i++) {
				System.out.println((i + 1) + ". " + values[i].getLabel());
			}
			System.out.print("番号を入力: ");
			String input = sc.nextLine().trim();
			try {
				int num = Integer.parseInt(input);
				if (num >= 1 && num <= values.length) {
					return values[num - 1];
				}
			} catch (NumberFormatException e) {
				// 下のメッセージ
			}
			System.out.println("※ 1〜" + values.length + " の番号で選択してください。");
		}
	}

	//星レベル(1〜10)を受け付け
	public static int readStarLevel(Scanner sc) {
		return readIntInRange(sc,
				"星レベル(" + MenuConst.MIN_STAR_LEVEL + "〜" + MenuConst.MAX_STAR_LEVEL + ")を入力: ",
				MenuConst.MIN_STAR_LEVEL, MenuConst.MAX_STAR_LEVEL);
	}

	//王冠（クリア状況）を選択

	public static Crown readCrown(Scanner sc) {
		Crown[] values = Crown.values();
		while (true) {
			System.out.println("クリア状況（王冠）を選択してください:");
			for (int i = 0; i < values.length; i++) {
				System.out.println((i + 1) + ". " + values[i].getLabel());
			}
			System.out.print("番号を入力: ");
			String input = sc.nextLine().trim();
			try {
				int num = Integer.parseInt(input);
				if (num >= 1 && num <= values.length) {
					return values[num - 1];
				}
			} catch (NumberFormatException e) {
				// 下の警告メッセージへ
			}
			System.out.println("※ 1〜" + values.length + " の番号で選択してください。");
		}
	}

	// y/n の確認入力を受け付け。yの場合trueを返す。
	public static boolean confirm(Scanner sc, String prompt) {
		while (true) {
			System.out.print(prompt + " (y/n): ");
			String input = sc.nextLine().trim().toLowerCase();
			if (input.equals("y")) {
				return true;
			} else if (input.equals("n")) {
				return false;
			}
			System.out.println("※ y または n を入力してください。");
		}
	}

	// 入力を空のまま Enter した場合は現在値を維持するための文字列入力（更新処理用）。
	// 空欄ならnullを返す。
	public static String readOptionalString(Scanner sc, String prompt) {
		System.out.print(prompt);
		String input = sc.nextLine().trim();
		return input.isEmpty() ? null : input;
	}
}
