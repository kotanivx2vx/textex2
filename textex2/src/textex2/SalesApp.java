package textex2;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * 売上管理アプリケーションの UI ロジック。
 * ユーザーとの対話を通じて SalesService にデータを渡し、集計結果を表示する。
 * 責務分離により、集計ロジックは SalesService に委譲。
 */
public class SalesApp {
    // 標準入力用 Scanner（static に保持）
    private static final Scanner scanner = new Scanner(System.in);

    // 売上集計サービス（DI ではなく直接生成）
    private final SalesService service = new SalesService();
    
 // SalesApp クラス内に定数を定義
    private static final String INPUT_SALES = "1";
    private static final String SHOW_TOTAL_AND_AVERAGE = "2";
    private static final String SHOW_DEPARTMENT_TOTALS = "3";
    private static final String EXIT = "4";   

    /**
     * アプリケーションのメインループ。
     * メニュー表示 → 入力受付 → 処理分岐を繰り返す。
     */
    public void runMenu() {
        while (true) {
            showMenu();
            String input = scanner.nextLine();

            // ユーザー入力に応じて処理を分岐
            switch (input) {
                case INPUT_SALES -> inputSales();                           // 売上入力
                case SHOW_TOTAL_AND_AVERAGE -> showTotalAndAverage();     // 総合計・平均表示
                case SHOW_DEPARTMENT_TOTALS -> showDepartmentTotals();    // 部門別集計表示
                case EXIT -> {
                    System.out.println("終了します。");
                    scanner.close(); // リソース解放
                    return;
                }
                default -> System.out.println("不正な入力です。もう一度入力してください。");
            }
        }
    }

    /**
     * メニューを表示する。
     * ユーザーに選択肢を提示。
     */
    private void showMenu() {
        System.out.println("\n--- メニュー ---");
        System.out.println("1. 売上入力");
        System.out.println("2. 総合計・平均表示");
        System.out.println("3. 部門別集計表示");
        System.out.println("4. 終了");
        System.out.print("選択してください: ");
    }

    /**
     * 売上データの入力処理。
     * 日付・金額・部署名を受け取り、バリデーション後に SalesService に登録。
     */
    private void inputSales() {
        try {
            System.out.print("日付 (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("売上額 (数値): ");
            String amountStr = scanner.nextLine();

            // 売上額の形式チェック（数値のみ）
            if (!amountStr.matches("\\d+")) {
                System.out.println("売上額は数値で入力してください。");
                return;
            }

            // 桁数制限（10桁まで）
            if (amountStr.length() > 10) {
                System.out.println("売上額は最大10桁までです。");
                return;
            }

            long amount = Long.parseLong(amountStr);

            System.out.print("部署名: ");
            String dept = scanner.nextLine();

            // 部署名の文字数制限（10文字まで）
            if (dept.length() > 10) {
                System.out.println("部署名は最大10文字までです。");
                return;
            }

            // 売上データを登録
            service.addRecord(new SalesRecord(date, amount, dept));
            System.out.println("売上を登録しました。");

        } catch (DateTimeParseException e) {
            System.out.println("日付形式が正しくありません（例: 2025-08-06）。");
        }
    }

    /**
     * 総合計・平均売上を表示する。
     * データが存在しない場合はメッセージのみ表示。
     */
    private void showTotalAndAverage() {
        if (!service.hasData()) {
            System.out.println("売上データがありません。");
            return;
        }
        System.out.println("--- 総合計・平均レポート ---");
        System.out.println("合計: " + service.getTotalSales());
        System.out.printf("平均: %.2f\n", service.getAverageSales());
    }

    /**
     * 部門別売上合計を表示する。
     * データが存在しない場合はメッセージのみ表示。
     */
    private void showDepartmentTotals() {
        if (!service.hasData()) {
            System.out.println("売上データがありません。");
            return;
        }
        System.out.println("--- 部門別小計レポート ---");
        service.getDepartmentTotals().forEach((dept, total) ->
            System.out.println(dept + ": " + total)
        );
    }
}
