package TaikoNote;

// 難易度の分類（星レベルは別フィールド starLevel で管理する）
public enum Difficulty {
	KANTAN("かんたん"), FUTSUU("ふつう"), MUZUKASHII("むずかしい"), ONI("おに"), URA_ONI("裏おに");

	private final String label;

	Difficulty(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	// 表示名から Difficulty を取得する
	public static Difficulty fromLabel(String label) {
		for (Difficulty d : values()) {
			if (d.label.equals(label)) {
				return d;
			}
		}
		throw new IllegalArgumentException("不正な難易度です: " + label);
	}

	// enum名（KANTAN等）から Difficulty を取得する（CSV読込用）
	public static Difficulty fromName(String name) {
		return Difficulty.valueOf(name);
	}
}
