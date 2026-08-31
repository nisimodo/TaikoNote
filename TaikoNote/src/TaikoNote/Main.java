package TaikoNote;

import java.util.List;
import java.util.Scanner;

// Taiko Note のエントリポイント。
// メニュー表示と各処理への分岐を担う。
public class Main {

	private static final Scanner sc = new Scanner(System.in);
	private static final ScoreService scoreService = new ScoreService();

	public static void main(String[] args) {
		System.out.println("Taiko Note へようこそ！");

		boolean running = true;
		while (running) {
			System.out.print(MenuConst.MENU_TEXT);
			String input = sc.nextLine().trim();
			int choice;
			try {
				choice = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("※ 数字でメニュー番号を入力してください。");
				continue;
			}

			switch (choice) {
			case MenuConst.MENU_CREATE:
				createScore();
				break;
			case MenuConst.MENU_LIST_ALL:
				listAll();
				break;
			case MenuConst.MENU_LIST_UNFULLCOMBO:
				listUnfullCombo();
				break;
			case MenuConst.MENU_UPDATE:
				updateScore();
				break;
			case MenuConst.MENU_DELETE:
				deleteScore();
				break;
			case MenuConst.MENU_SEARCH_DIFFICULTY:
				searchByDifficulty();
				break;
			case MenuConst.MENU_SEARCH_ACHIEVEMENT_RATE:
				searchByAchievementRate();
				break;
			case MenuConst.MENU_EXIT:
				System.out.println("アプリを終了します。お疲れ様でした！");
				running = false;
				break;
			default:
				System.out.println("※ 該当するメニューがありません。番号を確認してください。");
			}
		}
		sc.close();
	}

	// ---------- Create ----------

	private static void createScore() {
		System.out.println("\n--- スコア登録 ---");
		String title = InputUtil.readNonEmptyString(sc, "曲名: ");
		Difficulty difficulty = InputUtil.readDifficulty(sc);

		Score duplicate = scoreService.findByTitleAndDifficulty(title, difficulty);
		if (duplicate != null) {
			System.out.println("※ 「" + title + "」の" + difficulty.getLabel() + "は既に登録されています。");
			System.out.println(duplicate);
			boolean goToUpdate = InputUtil.confirm(sc, "このデータを更新しますか？（nを選ぶとメインメニューに戻ります）");
			if (goToUpdate) {
				updateScoreById(duplicate.getId());
			} else {
				System.out.println("登録をキャンセルし、メインメニューに戻ります。");
			}
			return;
		}

		int starLevel = InputUtil.readStarLevel(sc);
		int score = InputUtil.readNonNegativeInt(sc, "ハイスコア: ");
		int good = InputUtil.readNonNegativeInt(sc, "良の数: ");
		int ok = InputUtil.readNonNegativeInt(sc, "可の数: ");
		int bad = InputUtil.readNonNegativeInt(sc, "不可の数: ");
		int maxCombo = InputUtil.readNonNegativeInt(sc, "最大コンボ数: ");
		Crown crown = InputUtil.readCrown(sc);

		Score created = scoreService.create(title, difficulty, starLevel, score, good, ok, bad, maxCombo, crown);
		System.out.println("登録しました！");
		System.out.println(created);
	}

	// ---------- Read ----------

	private static void listAll() {
		System.out.println("\n--- スコア一覧 ---");
		List<Score> list = scoreService.getAll();
		printList(list);
	}

	private static void listUnfullCombo() {
		System.out.println("\n--- 未フルコンボ曲一覧 ---");
		List<Score> list = scoreService.getUnfullComboList();
		printList(list);
	}

	private static void searchByDifficulty() {
		System.out.println("\n--- 難易度・星レベルで検索 ---");
		Difficulty difficulty = InputUtil.readDifficulty(sc);
		boolean filterStar = InputUtil.confirm(sc, "星レベルでも絞り込みますか？");
		Integer starLevel = null;
		if (filterStar) {
			starLevel = InputUtil.readStarLevel(sc);
		}
		List<Score> list = scoreService.searchByDifficulty(difficulty, starLevel);
		printList(list);
	}

	private static void searchByAchievementRate() {
		System.out.println("\n--- 達成率で検索 ---");
		System.out.println("達成率 = ((良の数 × 1 + 可の数 × 0.5) ÷ (良の数+可の数+不可の数)) × 100 [%]");
		double minRate = InputUtil.readPercent(sc, "検索する達成率の下限(%) [0〜100]: ");
		double maxRate = InputUtil.readPercent(sc, "検索する達成率の上限(%) [0〜100]: ");
		if (minRate > maxRate) {
			double tmp = minRate;
			minRate = maxRate;
			maxRate = tmp;
		}
		List<Score> list = scoreService.searchByAchievementRate(minRate, maxRate);
		printList(list);
	}

	private static void printList(List<Score> list) {
		if (list.isEmpty()) {
			System.out.println("該当するデータはありません。");
			return;
		}
		for (Score s : list) {
			System.out.println(s);
		}
		System.out.println("該当件数: " + list.size() + "件");
	}

	// ---------- Update ----------

	private static void updateScore() {
		System.out.println("\n--- スコア・判定データの更新 ---");
		int id = InputUtil.readNonNegativeInt(sc, "更新する曲のID: ");
		updateScoreById(id);
	}

	private static void updateScoreById(int id) {
		Score existing = scoreService.findById(id);
		if (existing == null) {
			System.out.println("※ ID " + id + " のデータは見つかりませんでした。");
			return;
		}

		System.out.println("現在のデータ:");
		System.out.println(existing);
		System.out.println("変更しない項目はそのままEnterを押してください。");

		String newTitle = InputUtil.readOptionalString(sc,
				"曲名 (現在: " + existing.getTitle() + "): ");
		if (newTitle != null) {
			existing.setTitle(newTitle);
		}

		if (InputUtil.confirm(sc, "難易度を変更しますか？ (現在: " + existing.getDifficulty().getLabel() + ")")) {
			existing.setDifficulty(InputUtil.readDifficulty(sc));
		}

		if (InputUtil.confirm(sc, "星レベルを変更しますか？ (現在: " + existing.getStarLevel() + ")")) {
			existing.setStarLevel(InputUtil.readStarLevel(sc));
		}

		existing.setScore(InputUtil.readOptionalInt(sc,
				"ハイスコア (現在: " + existing.getScore() + "): ", existing.getScore()));
		existing.setGood(InputUtil.readOptionalInt(sc,
				"良の数 (現在: " + existing.getGood() + "): ", existing.getGood()));
		existing.setOk(InputUtil.readOptionalInt(sc,
				"可の数 (現在: " + existing.getOk() + "): ", existing.getOk()));
		existing.setBad(InputUtil.readOptionalInt(sc,
				"不可の数 (現在: " + existing.getBad() + "): ", existing.getBad()));
		existing.setMaxCombo(InputUtil.readOptionalInt(sc,
				"最大コンボ数 (現在: " + existing.getMaxCombo() + "): ", existing.getMaxCombo()));

		if (InputUtil.confirm(sc, "王冠（クリア状況）を変更しますか？ (現在: " + existing.getCrown().getLabel() + ")")) {
			existing.setCrown(InputUtil.readCrown(sc));
		}

		boolean success = scoreService.update(existing);
		if (success) {
			System.out.println("更新しました！");
			System.out.println(existing);
		} else {
			System.out.println("※ 更新に失敗しました。");
		}
	}

	// ---------- Delete ----------

	private static void deleteScore() {
		System.out.println("\n--- 曲データの削除 ---");
		int id = InputUtil.readNonNegativeInt(sc, "削除する曲のID: ");
		Score existing = scoreService.findById(id);
		if (existing == null) {
			System.out.println("※ ID " + id + " のデータは見つかりませんでした。");
			return;
		}
		System.out.println(existing);
		boolean confirmed = InputUtil.confirm(sc, "本当に削除しますか？");
		if (!confirmed) {
			System.out.println("削除をキャンセルしました。");
			return;
		}
		boolean success = scoreService.delete(id);
		if (success) {
			System.out.println("削除しました。");
		} else {
			System.out.println("※ 削除に失敗しました。");
		}
	}
}
