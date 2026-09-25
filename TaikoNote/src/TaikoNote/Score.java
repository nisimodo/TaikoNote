package TaikoNote;

//太鼓の達人1曲分のリザルトを表すModelクラス
public class Score {
	private int id;
	private String songName;
	private int genre;
	private int difficulty;
	private String level;
	private int score;
	private int yoi;
	private int ka;
	private int fuka;
	private boolean cleared;

	public Score() {
	}

	public Score(int id, String songName, int genre, int difficulty, String level, int score, int yoi, int ka,
			int fuka, boolean cleared) {
		this.id = id;
		this.songName = songName;
		this.genre = genre;
		this.difficulty = difficulty;
		this.level = level;
		this.score = score;
		this.yoi = yoi;
		this.ka = ka;
		this.fuka = fuka;
		this.cleared = cleared;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public int getGenre() {
		return genre;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getYoi() {
		return yoi;
	}

	public void setYoi(int yoi) {
		this.yoi = yoi;
	}

	public int getKa() {
		return ka;
	}

	public void setKa(int ka) {
		this.ka = ka;
	}

	public int getFuka() {
		return fuka;
	}

	public void setFuka(int fuka) {
		this.fuka = fuka;
	}

	public boolean isCleared() {
		return cleared;
	}

	public void setCleared(boolean cleared) {
		this.cleared = cleared;
	}

	//難易度の数字を表示用テキストに変換
	public String getDifficultyText() {
		switch (difficulty) {
		case 1:
			return "かんたん";
		case 2:
			return "ふつう";
		case 3:
			return "むずかしい";
		case 4:
			return "おに";
		case 5:
			return "おに裏";
		default:
			return String.valueOf(difficulty);
		}
	}

	//ジャンルの数字を表示用テキストに変換
	public String getGenreText() {
		switch (genre) {
		case 1:
			return "ポップス";
		case 2:
			return "アニメ";
		case 3:
			return "キッズ";
		case 4:
			return "ゲーム/バラエティ";
		case 5:
			return "ナムコオリジナル";
		case 6:
			return "クラシック";
		case 7:
			return "ボーカロイド";
		default:
			return String.valueOf(genre);
		}
	}

	//可・不可の数とクリアフラグから「未クリア／クリア／フルコンボ／ドンだフルコンボ」を判定
	public String getClearStatusText() {
		if (fuka == 0 && ka == 0) {
			return "ドンだフルコンボ";
		} else if (fuka == 0) {
			return "フルコンボ";
		} else if (cleared) {
			return "クリア";
		} else {
			return "未クリア";
		}
	}

	//達成率を計算：((良*1.0 + 可*0.5) / (良+可+不可)) * 100.0
	public double getAchievementRate() {
		double yoiD = yoi;
		double kaD = ka;
		double fukaD = fuka;
		double total = yoiD + kaD + fukaD;
		if (total == 0) {
			return 0.0;
		}
		return ((yoiD * 1.0 + kaD * 0.5) / total) * 100.0;
	}

	//一覧表示用に1行の文字列へ整形
	public String toDisplayString() {
		return "ID：" + id + ", 曲名：" + songName + ", ジャンル：" + getGenreText() + ", 難易度：" + getDifficultyText()
				+ ", レベル：★" + level + ", スコア：" + score + ", 良：" + yoi + ", 可：" + ka
				+ ", 不可：" + fuka + " クリア状況：" + getClearStatusText();
	}

	//CSVの1行(カンマ区切り)からScoreを作成
	public static Score fromCsvLine(String line) {
		String[] data = line.split(",");
		Score s = new Score();
		s.id = Integer.parseInt(data[0]);
		s.songName = data[1];
		s.genre = Integer.parseInt(data[2]);
		s.difficulty = Integer.parseInt(data[3]);
		s.level = data[4];
		s.score = Integer.parseInt(data[5]);
		s.yoi = Integer.parseInt(data[6]);
		s.ka = Integer.parseInt(data[7]);
		s.fuka = Integer.parseInt(data[8]);
		s.cleared = data[9].equals("y");
		return s;
	}

	//CSVの1行(カンマ区切り)に変換
	public String toCsvLine() {
		String clearedText;
		if (cleared) {
			clearedText = "y";
		} else {
			clearedText = "n";
		}
		return id + "," + songName + "," + genre + "," + difficulty + "," + level + "," + score + "," + yoi + ","
				+ ka + "," + fuka + "," + clearedText;
	}
}