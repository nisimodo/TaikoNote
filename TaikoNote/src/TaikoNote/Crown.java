package TaikoNote;

/**
 * クリア状況（王冠の種類）
 */
public enum Crown {
	NONE("なし"), CLEAR("クリア"), FULL_COMBO("フルコンボ"), DONDA_FULL_COMBO("ドンダフルコンボ");

	private final String label;

	Crown(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	/**
	 * フルコンボ系（フルコンボ or ドンダフルコンボ）かどうか
	 */
	public boolean isFullComboAchieved() {
		return this == FULL_COMBO || this == DONDA_FULL_COMBO;
	}

	public static Crown fromLabel(String label) {
		for (Crown c : values()) {
			if (c.label.equals(label)) {
				return c;
			}
		}
		throw new IllegalArgumentException("不正な王冠種別です: " + label);
	}

	public static Crown fromName(String name) {
		return Crown.valueOf(name);
	}
}
