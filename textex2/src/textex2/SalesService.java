package textex2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SalesRecord の集計処理を担うサービスクラス。
 * 単一責任原則に基づき、データの追加・集計・分類を担当。
 */
public class SalesService {
    // 売上レコードの保持リスト。外部から追加される SalesRecord を蓄積。
    private List<SalesRecord> records = new ArrayList<>();

    /**
     * 売上レコードを追加する。
     * @param record 追加対象の SalesRecord インスタンス
     */
    public void addRecord(SalesRecord record) {
        records.add(record);
    }

    /**
     * 売上データが1件以上存在するかを判定する。
     * 主に表示制御や集計処理の前提条件として使用。
     * @return データが存在する場合 true、空なら false
     */
    public boolean hasData() {
        return !records.isEmpty();
    }

    /**
     * 売上額の合計を算出する。
     * Stream API による宣言的な集計処理。
     * @return 全 SalesRecord の amount の合計値
     */
    public long getTotalSales() {
        return records.stream()
                      .mapToLong(SalesRecord::getAmount)
                      .sum();
    }

    /**
     * 売上額の平均を算出する。
     * データが空の場合は 0 を返すことで null 回避。
     * @return 平均売上額（double 型）
     */
    public double getAverageSales() {
        return records.stream()
                      .mapToLong(SalesRecord::getAmount)
                      .average()
                      .orElse(0);
    }

    /**
     * 部門ごとの売上合計を算出する。
     * groupingBy により department ごとにグループ化し、amount を合計。
     * @return 部門名をキー、売上合計を値とする Map
     */
    public Map<String, Long> getDepartmentTotals() {
        return records.stream()
                      .collect(Collectors.groupingBy(
                          SalesRecord::getDepartment,
                          Collectors.summingLong(SalesRecord::getAmount)
                      ));
    }
}

