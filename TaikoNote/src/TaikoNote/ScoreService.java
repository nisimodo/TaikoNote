package TaikoNote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//リザルトの登録・一覧・更新・削除・絞り込みを行うServiceクラス
public class ScoreService {

	private String filePath;

	public ScoreService(String filePath) {
		this.filePath = filePath;
	}

	//CSVファイルを1行ずつ全て読み込んでリストにして返す（ヘッダー行も含む 生の文字列のまま）
	private ArrayList<String> readAllLines() throws IOException {
		ArrayList<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (FileNotFoundException e) {
			//ファイルが無い場合はヘッダー行のみのリストを作成
			lines.add("ID,曲名,ジャンル,難易度,レベル,スコア,良,可,不可,クリア状況");
		}
		return lines;
	}

	//リストの内容を1行ずつCSVファイルに書き込む（上書き）
	private void writeAllLines(ArrayList<String> lines) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			for (String line : lines) {
				bw.write(line);
				bw.newLine();
			}
		}
	}

	//ヘッダー行を除いた全リザルトをScoreのリストにして返す
	public ArrayList<Score> findAll() throws IOException {
		ArrayList<Score> scores = new ArrayList<>();
		ArrayList<String> lines = readAllLines();
		for (int i = 1; i < lines.size(); i++) { //0はヘッダー行のため1から
			String line = lines.get(i);
			if (line.isEmpty()) {
				continue;
			}
			try {
				scores.add(Score.fromCsvLine(line));
			} catch (NumberFormatException e) {
				//数字であるべき項目が壊れている行は読み飛ばす
				continue;
			}
		}
		return scores;
	}

	//同じ曲名・同じ難易度のリザルトが既に登録されているか調べる（無ければnull）
	public Score findBySongAndDifficulty(String songName, int difficulty) throws IOException {
		for (Score s : findAll()) {
			if (s.getSongName().equals(songName) && s.getDifficulty() == difficulty) {
				return s;
			}
		}
		return null;
	}

	//IDを指定して1件取得（無ければnull）
	public Score findById(int id) throws IOException {
		for (Score s : findAll()) {
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}

	//クリア状況（未クリア／クリア／フルコンボ／ドンだフルコンボ）で絞り込む
	public ArrayList<Score> findByClearStatus(String status) throws IOException {
		ArrayList<Score> result = new ArrayList<>();
		for (Score s : findAll()) {
			if (s.getClearStatusText().equals(status)) {
				result.add(s);
			}
		}
		return result;
	}

	//難易度で絞り込む
	public ArrayList<Score> findByDifficulty(int difficulty) throws IOException {
		ArrayList<Score> result = new ArrayList<>();
		for (Score s : findAll()) {
			if (s.getDifficulty() == difficulty) {
				result.add(s);
			}
		}
		return result;
	}

	//ジャンルで絞り込む
	public ArrayList<Score> findByGenre(int genre) throws IOException {
		ArrayList<Score> result = new ArrayList<>();
		for (Score s : findAll()) {
			if (s.getGenre() == genre) {
				result.add(s);
			}
		}
		return result;
	}

	//達成率が指定範囲内のリザルトを達成率の高い順に並べて返す
	public ArrayList<Score> findByAchievementRateRange(int lowerRate, int upperRate) throws IOException {
		ArrayList<Score> filtered = new ArrayList<>();
		ArrayList<Double> rates = new ArrayList<>();

		for (Score s : findAll()) {
			if (s.getYoi() + s.getKa() + s.getFuka() == 0) {
				continue;
			}
			double rate = s.getAchievementRate();
			if (rate < lowerRate || rate > upperRate) {
				continue;
			}
			filtered.add(s);
			rates.add(rate);
		}

		//達成率が高い順に並び替える（選択ソート）
		for (int i = 0; i < rates.size() - 1; i++) {
			int maxIndex = i;
			for (int j = i + 1; j < rates.size(); j++) {
				if (rates.get(j) > rates.get(maxIndex)) {
					maxIndex = j;
				}
			}
			if (maxIndex != i) {
				double tempRate = rates.get(i);
				rates.set(i, rates.get(maxIndex));
				rates.set(maxIndex, tempRate);

				Score tempScore = filtered.get(i);
				filtered.set(i, filtered.get(maxIndex));
				filtered.set(maxIndex, tempScore);
			}
		}

		return filtered;
	}

	//新しいリザルトを登録（IDは自動採番）
	public void register(Score score) throws IOException {
		ArrayList<String> lines = readAllLines();
		score.setId(lines.size()); //ヘッダー行を除いた件数+1をIDにする
		lines.add(score.toCsvLine());
		writeAllLines(lines);
	}

	//既存のリザルトを更新（成功したらtrue）
	public boolean update(Score score) throws IOException {
		ArrayList<String> lines = readAllLines();
		for (int i = 1; i < lines.size(); i++) {
			Score current;
			try {
				current = Score.fromCsvLine(lines.get(i));
			} catch (NumberFormatException e) {
				continue;
			}
			if (current.getId() == score.getId()) {
				lines.set(i, score.toCsvLine());
				writeAllLines(lines);
				return true;
			}
		}
		return false;
	}

	//IDを指定してリザルトを削除（成功したらtrue）
	public boolean delete(int id) throws IOException {
		ArrayList<String> lines = readAllLines();
		for (int i = 1; i < lines.size(); i++) {
			Score current;
			try {
				current = Score.fromCsvLine(lines.get(i));
			} catch (NumberFormatException e) {
				continue;
			}
			if (current.getId() == id) {
				lines.remove(i);
				writeAllLines(lines);
				return true;
			}
		}
		return false;
	}
}