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
    // 標準入力用 Scanner（UI 入力責務）
    private static final Scanner scanner = new Scanner(System.in);

    // 売上集計サービス（集計責務を委譲）
    private final SalesService service = new SalesService();
    
    // 入力制約定数（意味の明確化）
    private static final int MAX_AMOUNT_DIGITS = 10;   // 売上額の最大桁数
    private static final int MAX_DEPARTMENT_LENGTH = 10; // 部署名の最大文字数

    /**
     * アプリケーションのメインループ。
     * メニュー表示 → 入力受付 → 処理分岐を繰り返す。
     * MenuOption により選択肢の意味を明確化。
     */
    public void runMenu() {
        while (true) {
            showMenu(); // メニュー表示（UI責務）
            String input = scanner.nextLine();
            MenuOption option = MenuOption.fromCode(input); // 入力値を意味ある選択肢へ変換

            if (option == null) {
                System.out.println("不正な入力です。もう一度入力してください。");
                continue;
            }

            // 選択肢に応じた処理分岐（UI → 業務ロジックへの橋渡し）
            switch (option) {
                case INPUT_SALES -> inputSales();                     // 売上入力処理
                case SHOW_TOTAL_AND_AVERAGE -> showTotalAndAverage(); // 総合計・平均表示
                case SHOW_DEPARTMENT_TOTALS -> showDepartmentTotals(); // 部門別集計表示
                case EXIT -> {
                    System.out.println("終了します。");
                    scanner.close(); // リソース解放（責任ある終了処理）
                    return;
                }
            }
        }
    }

    /**
     * メニューを表示する。
     * MenuOption のラベルを使用して表示内容を一元管理。
     */
    private void showMenu() {
        System.out.println("\n--- メニュー ---");
        for (MenuOption option : MenuOption.values()) {
            System.out.println(option.getCode() + ". " + option.getLabel()); // 選択肢の意味を明示
        }
        System.out.print("選択してください: ");
    }

    /**
     * 売上データの入力処理。
     * 日付・金額・部署名を受け取り、バリデーション後に SalesService に登録。
     * 入力値の妥当性を逐次検証し、意味のあるデータのみを登録。
     */
    private void inputSales() {
        try {
            System.out.print("日付 (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine()); // 日付形式チェック

            System.out.print("売上額 (数値): ");
            String amountStr = scanner.nextLine();

            // 売上額の形式チェック（数値のみ）
            if (!amountStr.matches("\\d+")) {
                System.out.println("売上額は数値で入力してください。");
                return;
            }

            // 桁数制限（定数で管理）
            if (amountStr.length() > MAX_AMOUNT_DIGITS) {
                System.out.println("売上額は最大" + MAX_AMOUNT_DIGITS + "桁までです。");
                return;
            }

            long amount = Long.parseLong(amountStr); // 数値変換

            System.out.print("部署名: ");
            String dept = scanner.nextLine();

            // 部署名の文字数制限（定数で管理）
            if (dept.length() > MAX_DEPARTMENT_LENGTH) {
                System.out.println("部署名は最大" + MAX_DEPARTMENT_LENGTH + "文字までです。");
                return;
            }

            // 売上データを登録（業務ロジックへ委譲）
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

