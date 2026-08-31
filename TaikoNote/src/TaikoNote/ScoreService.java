package TaikoNote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// スコアデータのCRUD処理・検索処理・CSVファイルへの永続化を担うServiceクラス。
public class ScoreService {

	private final List<Score> scoreList = new ArrayList<>();
	private final String filePath;

	public ScoreService() {
		this(MenuConst.CSV_FILE_PATH);
	}

	public ScoreService(String filePath) {
		this.filePath = filePath;
		load();
	}

	// ---------- CSV入出力 ----------

	// CSVファイルからデータを読み込む。ファイルが存在しない場合は新規作成
	public void load() {
		scoreList.clear();
		File file = new File(filePath);

		try {
			File parentDir = file.getParentFile();
			if (parentDir != null && !parentDir.exists()) {
				parentDir.mkdirs();
			}

			if (!file.exists()) {
				// 初回起動時はヘッダーのみのファイルを作成
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
					bw.write(MenuConst.CSV_HEADER);
					bw.newLine();
				}
				return;
			}

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				boolean isFirstLine = true;
				while ((line = br.readLine()) != null) {
					if (isFirstLine) {
						// ヘッダー行はスキップ
						isFirstLine = false;
						continue;
					}
					if (line.trim().isEmpty()) {
						continue;
					}
					try {
						scoreList.add(Score.fromCsvLine(line));
					} catch (Exception e) {
						System.out.println("※ CSVの読み込みに失敗した行をスキップしました: " + line);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("※ CSVファイルの読み込み中にエラーが発生しました: " + e.getMessage());
		}
	}

	// 現在のメモリ上のデータをCSVファイルに書き込む。
	public void save() {
		File file = new File(filePath);
		File parentDir = file.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			bw.write(MenuConst.CSV_HEADER);
			bw.newLine();
			for (Score s : scoreList) {
				bw.write(s.toCsvLine());
				bw.newLine();
			}
		} catch (IOException e) {
			System.out.println("※ CSVファイルの書き込み中にエラーが発生しました: " + e.getMessage());
		}
	}

	// ---------- Create ----------

	// 新しいスコアを登録。IDは自動採番。
	public Score create(String title, Difficulty difficulty, int starLevel, int score,
			int good, int ok, int bad, int maxCombo, Crown crown) {
		int newId = getNextId();
		Score s = new Score(newId, title, difficulty, starLevel, score, good, ok, bad, maxCombo, crown);
		scoreList.add(s);
		save();
		return s;
	}

	private int getNextId() {
		int maxId = 0;
		for (Score s : scoreList) {
			if (s.getId() > maxId) {
				maxId = s.getId();
			}
		}
		return maxId + 1;
	}

	// ---------- Read ----------

	public List<Score> getAll() {
		return new ArrayList<>(scoreList);
	}

	public Score findById(int id) {
		for (Score s : scoreList) {
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}

	// 曲名・難易度分類が完全一致するデータを検索（登録時の重複チェック用）
	// 該当がなければnullを返す
	public Score findByTitleAndDifficulty(String title, Difficulty difficulty) {
		for (Score s : scoreList) {
			if (s.getTitle().equals(title) && s.getDifficulty() == difficulty) {
				return s;
			}
		}
		return null;
	}

	// 未フルコンボ（フルコンボ・ドンダフルコンボ以外）の曲一覧を取得
	public List<Score> getUnfullComboList() {
		List<Score> result = new ArrayList<>();
		for (Score s : scoreList) {
			if (!s.getCrown().isFullComboAchieved()) {
				result.add(s);
			}
		}
		return result;
	}

	// 難易度分類・星レベルで検索する。starLevelがnullの場合は難易度分類のみで絞り込む
	public List<Score> searchByDifficulty(Difficulty difficulty, Integer starLevel) {
		List<Score> result = new ArrayList<>();
		for (Score s : scoreList) {
			if (s.getDifficulty() != difficulty) {
				continue;
			}
			if (starLevel != null && s.getStarLevel() != starLevel) {
				continue;
			}
			result.add(s);
		}
		return result;
	}

	// 達成率(%)の範囲で検索する。結果は達成率の高い順（降順）で返す。
	// 達成率 = ((良の数 × 1 + 可の数 × 0.5) / (良の数+可の数+不可の数)) * 100
	public List<Score> searchByAchievementRate(double minRate, double maxRate) {
		List<Score> result = new ArrayList<>();
		for (Score s : scoreList) {
			double rate = s.calcAchievementRate();
			if (rate >= minRate && rate <= maxRate) {
				result.add(s);
			}
		}
		result.sort((a, b) -> Double.compare(b.calcAchievementRate(), a.calcAchievementRate()));
		return result;
	}

	// ---------- Update ----------

	// 既存のスコアを更新する。該当IDが存在しない場合はfalseを返す。
	public boolean update(Score updated) {
		for (int i = 0; i < scoreList.size(); i++) {
			if (scoreList.get(i).getId() == updated.getId()) {
				scoreList.set(i, updated);
				save();
				return true;
			}
		}
		return false;
	}

	// ---------- Delete ----------

	// 指定IDのスコアを削除する。削除できた場合trueを返す。
	public boolean delete(int id) {
		boolean removed = scoreList.removeIf(s -> s.getId() == id);
		if (removed) {
			save();
		}
		return removed;
	}
}
