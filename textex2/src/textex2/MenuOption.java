package textex2;

/**
 * メニュー選択肢を表す列挙型。
 */
public enum MenuOption {
    INPUT_SALES("1", "売上入力"),
    SHOW_TOTAL_AND_AVERAGE("2", "総合計・平均表示"),
    SHOW_DEPARTMENT_TOTALS("3", "部門別集計表示"),
    EXIT("4", "終了");

    private final String code;   // ユーザー入力用の識別コード
    private final String label;  // 表示用ラベル

    MenuOption(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    /**
     * 入力コードから対応する MenuOption を取得。
     * 不正なコードの場合は null を返す。
     */
    public static MenuOption fromCode(String code) {
        for (MenuOption option : values()) {
            if (option.code.equals(code)) {
                return option;
            }
        }
        return null;
    }
}
