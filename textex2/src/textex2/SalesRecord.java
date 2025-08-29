package textex2;

import java.time.LocalDate;

/**
 * 売上レコードを表すドメインモデル。
 * 1件の売上情報（日時・金額・部門）を保持する不変オブジェクト。
 */
public class SalesRecord {
    // 売上日：売上が発生した日付
    private LocalDate date;

    // 売上金額：単位は long（小数不要な場合を想定）
    private long amount;

    // 部門名：売上が属する部署を表す文字列
    private String department;

    /**
     * コンストラクタ：すべてのフィールドを初期化。
     * @param date 売上日
     * @param amount 売上金額
     * @param department 部門名
     */
    public SalesRecord(LocalDate date, long amount, String department) {
        this.date = date;
        this.amount = amount;
        this.department = department;
    }

    /**
     * 売上金額を取得。
     * @return amount フィールドの値
     */
    public long getAmount() {
        return amount;
    }

    /**
     * 部門名を取得。
     * @return department フィールドの値
     */
    public String getDepartment() {
        return department;
    }
}