package TaikoNote;

import java.util.ArrayList;
import java.util.List;

/**
 * CSVの1フィールドのエスケープ／1行のパースを行うユーティリティ。
 * 曲名にカンマやダブルクォートが含まれていても壊れないように、
 * 簡易的なRFC4180風のクォート処理を行う。
 */
public class CsvUtil {

	// フィールドをCSV出力用にエスケープする。
	// カンマ・ダブルクォート・改行を含む場合はダブルクォートで囲む。
	public static String escape(String field) {
		if (field == null) {
			return "";
		}
		boolean needQuote = field.contains(",") || field.contains("\"") || field.contains("\n");
		String escaped = field.replace("\"", "\"\"");
		if (needQuote) {
			return "\"" + escaped + "\"";
		}
		return escaped;
	}

	// CSVの1行をフィールドのリストに分解する。
	public static List<String> parseLine(String line) {
		List<String> result = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		boolean inQuotes = false;

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (inQuotes) {
				if (c == '"') {
					if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
						sb.append('"');
						i++;
					} else {
						inQuotes = false;
					}
				} else {
					sb.append(c);
				}
			} else {
				if (c == '"') {
					inQuotes = true;
				} else if (c == ',') {
					result.add(sb.toString());
					sb.setLength(0);
				} else {
					sb.append(c);
				}
			}
		}
		result.add(sb.toString());
		return result;
	}
}
