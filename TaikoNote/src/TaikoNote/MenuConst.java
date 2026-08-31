package TaikoNote;

// メニュー表示・各種定数を集約するクラス。
public class MenuConst {

	// CSVファイルの保存先（src/TaikoScoreManager フォルダ内。実行時のカレントディレクトリが
	// プロジェクトルートであることを前提としたパス）
	public static final String CSV_FILE_PATH = "src/TaikoScoreManager/scores.csv";

	// CSVヘッダー
	public static final String CSV_HEADER = "id,title,difficulty,starLevel,score,good,ok,bad,maxCombo,crown";

	// 星レベルの範囲
	public static final int MIN_STAR_LEVEL = 1;
	public static final int MAX_STAR_LEVEL = 10;

	// メニュー番号
	public static final int MENU_CREATE = 1;
	public static final int MENU_LIST_ALL = 2;
	public static final int MENU_LIST_UNFULLCOMBO = 3;
	public static final int MENU_UPDATE = 4;
	public static final int MENU_DELETE = 5;
	public static final int MENU_SEARCH_DIFFICULTY = 6;
	public static final int MENU_SEARCH_ACHIEVEMENT_RATE = 7;
	public static final int MENU_EXIT = 0;

	public static final String MENU_TEXT = "\n===== Taiko Score Manager =====\n" +
			MENU_CREATE + ". スコア登録\n" +
			MENU_LIST_ALL + ". スコア一覧表示\n" +
			MENU_LIST_UNFULLCOMBO + ". 未フルコンボ曲一覧表示\n" +
			MENU_UPDATE + ". スコア・判定データの更新\n" +
			MENU_DELETE + ". 曲データの削除\n" +
			MENU_SEARCH_DIFFICULTY + ". 難易度・星レベルで検索\n" +
			MENU_SEARCH_ACHIEVEMENT_RATE + ". 達成率で検索\n" +
			MENU_EXIT + ". 終了\n" +
			"選択してください: ";

	private MenuConst() {
		// インスタンス化禁止
	}
}
