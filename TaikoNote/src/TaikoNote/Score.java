package TaikoNote;

import java.util.List;

// 1曲・1難易度分のプレイ結果を表すModelクラス。
public class Score {

	private int id;
	private String title;
	private Difficulty difficulty;
	private int starLevel; // 星の数（1〜10）
	private int score; // ハイスコア
	private int good; // 良の数
	private int ok; // 可の数
	private int bad; // 不可の数
	private int maxCombo; // 最大コンボ数（達成率算出に使用）
	private Crown crown; // クリア状況

	public Score(int id, String title, Difficulty difficulty, int starLevel,
			int score, int good, int ok, int bad, int maxCombo, Crown crown) {
		this.id = id;
		this.title = title;
		this.difficulty = difficulty;
		this.starLevel = starLevel;
		this.score = score;
		this.good = good;
		this.ok = ok;
		this.bad = bad;
		this.maxCombo = maxCombo;
		this.crown = crown;
	}

	// ---------- getter / setter ----------

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public int getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(int starLevel) {
		this.starLevel = starLevel;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getGood() {
		return good;
	}

	public void setGood(int good) {
		this.good = good;
	}

	public int getOk() {
		return ok;
	}

	public void setOk(int ok) {
		this.ok = ok;
	}

	public int getBad() {
		return bad;
	}

	public void setBad(int bad) {
		this.bad = bad;
	}

	public int getMaxCombo() {
		return maxCombo;
	}

	public void setMaxCombo(int maxCombo) {
		this.maxCombo = maxCombo;
	}

	public Crown getCrown() {
		return crown;
	}

	public void setCrown(Crown crown) {
		this.crown = crown;
	}

	/**
	 * 達成率を計算する。
	 * 計算式: ((良の数 × 1 + 可の数 × 0.5) / (良の数 + 可の数 + 不可の数)) × 100 [%]
	 * 判定数の合計が0（未入力）の場合は0.0を返す。
	 * 理論上あり得ない負の値・100%超になった場合は0〜100の範囲に丸める。
	 */
	public double calcAchievementRate() {
		int totalJudge = good + ok + bad;
		if (totalJudge <= 0) {
			return 0.0;
		}
		double rate = ((good * 1.0 + ok * 0.5) / totalJudge) * 100.0;
		if (rate < 0.0) {
			rate = 0.0;
		}
		if (rate > 100.0) {
			rate = 100.0;
		}
		return rate;
	}

	/**
	 * CSVの1行分の文字列に変換する。
	 * 列順: id,title,difficulty,starLevel,score,good,ok,bad,maxCombo,crown
	 */
	public String toCsvLine() {
		return String.join(",",
				String.valueOf(id),
				CsvUtil.escape(title),
				difficulty.name(),
				String.valueOf(starLevel),
				String.valueOf(score),
				String.valueOf(good),
				String.valueOf(ok),
				String.valueOf(bad),
				String.valueOf(maxCombo),
				crown.name());
	}

	// CSVの1行から Score インスタンスを生成する。
	public static Score fromCsvLine(String line) {
		List<String> f = CsvUtil.parseLine(line);
		int id = Integer.parseInt(f.get(0).trim());
		String title = f.get(1);
		Difficulty difficulty = Difficulty.fromName(f.get(2).trim());
		int starLevel = Integer.parseInt(f.get(3).trim());
		int score = Integer.parseInt(f.get(4).trim());
		int good = Integer.parseInt(f.get(5).trim());
		int ok = Integer.parseInt(f.get(6).trim());
		int bad = Integer.parseInt(f.get(7).trim());
		int maxCombo = Integer.parseInt(f.get(8).trim());
		Crown crown = Crown.fromName(f.get(9).trim());
		return new Score(id, title, difficulty, starLevel, score, good, ok, bad, maxCombo, crown);
	}

	@Override
	public String toString() {
		return String.format(
				"ID:%-4d %-20s [%s★%d] Score:%-8d 良:%d 可:%d 不可:%d 最大コンボ:%d 達成率:%.2f%% 王冠:%s",
				id, title, difficulty.getLabel(), starLevel, score, good, ok, bad, maxCombo,
				calcAchievementRate(), crown.getLabel());
	}
}
