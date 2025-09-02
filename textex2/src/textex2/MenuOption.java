package textex2;

/**
 * メニュー選択肢を表す列挙型。
 * 各選択肢は、ユーザー入力用のコードと表示用ラベルを持つ。
 * 責務：メニュー選択肢の定義と、コードによる選択肢の取得。
 */
public enum MenuOption {
    // 売上入力処理を選択
    INPUT_SALES("1", "売上入力"),

    // 総合計および平均の表示処理を選択
    SHOW_TOTAL_AND_AVERAGE("2", "総合計・平均表示"),

    // 部門別の集計表示処理を選択
    SHOW_DEPARTMENT_TOTALS("3", "部門別集計表示"),

    // アプリケーションの終了を選択
    EXIT("4", "終了");

    // ユーザーが入力する識別コード（例："1"）
    private final String code;

    // メニュー表示用のラベル（例："売上入力"）
    private final String label;

    /**
     * 列挙定数の初期化。
     * @param code ユーザー入力用のコード
     * @param label 表示用ラベル
     */
    MenuOption(String code, String label) {
        this.code = code;
        this.label = label;
    }

    /**
     * ユーザー入力用コードを取得。
     * @return メニュー選択肢のコード
     */
    public String getCode() {
        return code;
    }

    /**
     * 表示用ラベルを取得。
     * @return メニュー選択肢のラベル
     */
    public String getLabel() {
        return label;
    }

    /**
     * 入力コードから対応する MenuOption を取得。
     * 責務：外部入力（文字列）を列挙型に変換する。
     * @param code ユーザーが入力したコード
     * @return 対応する MenuOption（該当なしの場合は null）
     */
    public static MenuOption fromCode(String code) {
        for (MenuOption option : values()) {
            if (option.code.equals(code)) {
                return option;
            }
        }
        // 不正なコードの場合は null を返す（例外ではなく null にすることで呼び出し側に判断を委ねる）
        return null;
    }
}
