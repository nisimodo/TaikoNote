package TaikoNote;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		/*******************************************/
		/* 初期設定
		/*******************************************/
		InputUtil input = new InputUtil();
		Menu menu = new Menu();
		ScoreService scoreService = new ScoreService("src/TaikoNote/scores.csv");

		/*******************************************/
		/* メイン
		/*******************************************/
		menu.showTitle();
		int number;

		while (true) {
			while (true) {
				menu.showMainMenu();
				number = input.nextIntInRange("番号を半角英数字で入力してください：", 1, 9);

				switch (number) {
				case 1:
					registerScore(input, menu, scoreService);
					break;
				case 2:
					updateScore(input, menu, scoreService);
					break;
				case 3:
					listAllScores(scoreService);
					break;
				case 4:
					filterByClearStatus(input, menu, scoreService);
					break;
				case 5:
					filterByDifficulty(input, menu, scoreService);
					break;
				case 6:
					filterByAchievementRate(input, scoreService);
					break;
				case 7:
					filterByGenre(input, menu, scoreService);
					break;
				case 8:
					deleteScore(input, scoreService);
					break;
				case 9:
					System.out.println("終了します。");
					break;
				default:
					System.out.println("1~9で入力してください");
				}

				if (number == 9) {
					break;
				}
			}
			if (number == 9) {
				break;
			}
		}
		input.close();
	}

	//１：リザルトの登録
	private static void registerScore(InputUtil input, Menu menu, ScoreService scoreService) {
		String songName = input.next("曲名を入力してください：");
		input.skipNewLine(); // next()の後に残る改行を読み捨てる（この後の入力はnextLineベースのため）
		menu.showDifficultyOptions();
		int difficulty = input.nextIntInRange("：", 1, 5);

		Score existing;
		try {
			existing = scoreService.findBySongAndDifficulty(songName, difficulty);
		} catch (IOException e) {
			System.out.println("CSVファイルの読み込みに失敗しました");
			return;
		}

		if (existing != null) {
			System.out.println("同じ曲・同じ難易度のリザルトが既に登録されています");
			System.out.println("１：リザルト更新　２：メニューに戻る");
			int choice = input.nextIntInRange("番号を半角英数字で入力してください：", 1, 2);

			if (choice == 1) {
				updateScoreStats(input, existing);
				saveUpdatedScore(scoreService, existing);
			}
			return;
		}

		menu.showGenreOptions();
		int genre = input.nextIntInRange("：", 1, 7);
		int level = input.nextIntInRange("レベルを入力してください（1~10）：", 1, 10);
		int score = input.nextIntSafe("スコア：");
		int yoi = input.nextIntSafe("良の数：");
		int ka = input.nextIntSafe("可の数：");
		int fuka = input.nextIntSafe("不可の数：");
		boolean cleared = input.nextYesNoSafe("クリアしましたか？(y/n)：");

		Score newScore = new Score(0, songName, genre, difficulty, String.valueOf(level), score, yoi, ka, fuka,
				cleared);

		try {
			scoreService.register(newScore);
			System.out.println("登録が完了しました");
		} catch (IOException e) {
			System.out.println("CSVファイルへの書き込みに失敗しました");
		}
	}

	//２：リザルト更新
	private static void updateScore(InputUtil input, Menu menu, ScoreService scoreService) {
		int targetId = input.nextIntSafe("更新するIDを入力してください：");

		Score target;
		try {
			target = scoreService.findById(targetId);
		} catch (IOException e) {
			System.out.println("CSVファイルの読み込みに失敗しました");
			return;
		}

		if (target == null) {
			System.out.println("該当するIDが見つかりません");
			return;
		}

		String nameInput = input.nextLine("曲名を入力してください（空欄で変更なし）：");
		if (!nameInput.isEmpty()) {
			target.setSongName(nameInput);
		}

		menu.showGenreOptions();
		Integer genre = input.nextIntInRangeOrEmpty("（空欄で変更なし）：", 1, 7);
		if (genre != null) {
			target.setGenre(genre);
		}

		menu.showDifficultyOptions();
		Integer difficulty = input.nextIntInRangeOrEmpty("（空欄で変更なし）：", 1, 5);
		if (difficulty != null) {
			target.setDifficulty(difficulty);
		}

		Integer levelInput = input.nextIntInRangeOrEmpty("レベルを入力してください（1~10）（空欄で変更なし）：", 1, 10);
		if (levelInput != null) {
			target.setLevel(String.valueOf(levelInput));
		}

		updateScoreStats(input, target);
		saveUpdatedScore(scoreService, target);
	}

	//スコア・良・可・不可・クリア状況の入力を受け取り 空欄なら変更なし（targetを直接書き換え）
	private static void updateScoreStats(InputUtil input, Score target) {
		Integer score = input.nextIntOrEmpty("スコア（空欄で変更なし）：");
		if (score != null) {
			target.setScore(score);
		}

		Integer yoi = input.nextIntOrEmpty("良の数（空欄で変更なし）：");
		if (yoi != null) {
			target.setYoi(yoi);
		}

		Integer ka = input.nextIntOrEmpty("可の数（空欄で変更なし）：");
		if (ka != null) {
			target.setKa(ka);
		}

		Integer fuka = input.nextIntOrEmpty("不可の数（空欄で変更なし）：");
		if (fuka != null) {
			target.setFuka(fuka);
		}

		Boolean cleared = input.nextYesNoOrEmpty("クリアしましたか？(y/n)（空欄で変更なし）：");
		if (cleared != null) {
			target.setCleared(cleared);
		}
	}

	//更新後のScoreをCSVへ保存し 結果メッセージを表示
	private static void saveUpdatedScore(ScoreService scoreService, Score target) {
		try {
			boolean success = scoreService.update(target);
			if (success) {
				System.out.println("更新が完了しました");
			} else {
				System.out.println("該当するIDが見つかりません");
			}
		} catch (IOException e) {
			System.out.println("CSVファイルへの書き込みに失敗しました");
		}
	}

	//３：一覧出力
	private static void listAllScores(ScoreService scoreService) {
		try {
			ArrayList<Score> scores = scoreService.findAll();
			if (scores.isEmpty()) {
				System.out.println("データがありません");
			} else {
				for (Score s : scores) {
					System.out.println(s.toDisplayString());
				}
			}
		} catch (IOException e) {
			System.out.println("CSVファイルの読み込みに失敗しました");
		}
	}

	//４：クリア状況絞り込み
	private static void filterByClearStatus(InputUtil input, Menu menu, ScoreService scoreService) {
		menu.showClearStatusOptions();
		int choice = input.nextIntInRange("：", 1, 4);

		String target;
		switch (choice) {
		case 1:
			target = "未クリア";
			break;
		case 2:
			target = "クリア";
			break;
		case 3:
			target = "フルコンボ";
			break;
		default:
			target = "ドンだフルコンボ";
		}

		try {
			printScoresOrEmptyMessage(scoreService.findByClearStatus(target));
		} catch (IOException e) {
			System.out.println("CSVファイルの読み込みに失敗しました");
		}
	}

	//５：難易度絞り込み
	private static void filterByDifficulty(InputUtil input, Menu menu, ScoreService scoreService) {
		menu.showDifficultyOptions();
		int target = input.nextIntInRange("：", 1, 5);

		try {
			printScoresOrEmptyMessage(scoreService.findByDifficulty(target));
		} catch (IOException e) {
			System.out.println("CSVファイルの読み込みに失敗しました");
		}
	}

	//６：達成率絞り込み
	private static void filterByAchievementRate(InputUtil input, ScoreService scoreService) {
		int lowerRate = input.nextIntSafe("達成率の下限値(%)を入力してください：");
		int upperRate = input.nextIntSafe("達成率の上限値(%)を入力してください：");

		try {
			printScoresOrEmptyMessage(scoreService.findByAchievementRateRange(lowerRate, upperRate));
		} catch (IOException e) {
			System.out.println("CSVファイルの読み込みに失敗しました");
		}
	}

	//７：ジャンル絞り込み
	private static void filterByGenre(InputUtil input, Menu menu, ScoreService scoreService) {
		menu.showGenreOptions();
		int target = input.nextIntInRange("：", 1, 7);

		try {
			printScoresOrEmptyMessage(scoreService.findByGenre(target));
		} catch (IOException e) {
			System.out.println("CSVファイルの読み込みに失敗しました");
		}
	}

	//絞り込み結果を出力（0件なら該当なしメッセージ）
	private static void printScoresOrEmptyMessage(ArrayList<Score> scores) {
		if (scores.isEmpty()) {
			System.out.println("該当するデータがありません");
		} else {
			for (Score s : scores) {
				System.out.println(s.toDisplayString());
			}
		}
	}

	//８：データ削除
	private static void deleteScore(InputUtil input, ScoreService scoreService) {
		int targetId = input.nextIntSafe("削除するIDを入力してください：");

		try {
			boolean success = scoreService.delete(targetId);
			if (success) {
				System.out.println("削除が完了しました");
			} else {
				System.out.println("該当するIDが見つかりません");
			}
		} catch (IOException e) {
			System.out.println("CSVファイルへの書き込みに失敗しました");
		}
	}
}