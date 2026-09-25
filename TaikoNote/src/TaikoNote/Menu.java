package TaikoNote;

//メニューや選択肢の表示を担当するクラス
public class Menu {

	public void showTitle() {
		System.out.println("ーーーーーー　TaikoNote　ーーーーーー");
	}

	public void showMainMenu() {
		System.out.println("１：リザルトの登録");
		System.out.println("２：リザルト更新");
		System.out.println("３：一覧出力");
		System.out.println("４：クリア状況絞り込み");
		System.out.println("５：難易度絞り込み");
		System.out.println("６：達成率絞り込み");
		System.out.println("７：ジャンル絞り込み");
		System.out.println("８：データ削除");
		System.out.println("９：終了");
	}

	public void showGenreOptions() {
		System.out.println("ジャンルを選択してください");
		System.out.print("(1:ポップス 2:アニメ 3:キッズ 4:ゲーム/バラエティ 5:ナムコオリジナル　6:クラシック 7:ボーカロイド)");
	}

	public void showDifficultyOptions() {
		System.out.println("難易度を選択してください");
		System.out.print("(1:かんたん 2:ふつう 3:むずかしい 4:おに 5:おに裏)");
	}

	public void showClearStatusOptions() {
		System.out.println("絞り込むクリア状況を選択してください");
		System.out.print("(1:未クリア 2:クリア 3:フルコンボ 4:ドンだフルコンボ)");
	}
}