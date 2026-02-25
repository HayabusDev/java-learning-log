# Day18 - Inventory Management System

## 作成物
コンソール型 在庫管理アプリ

商品登録・入荷・出荷・削除・一覧表示が可能な学習用システム。
ValueObject・Resultパターン・Validator分離・SafeActionによる例外吸収を取り入れ、業務設計を意識して構築。

---

## 機能一覧
- 商品登録（初期在庫・再発注点・在庫不足閾値設定）
- 入荷処理（最大在庫制限あり）
- 出荷処理（在庫不足チェックあり）
- 登録商品一覧表示（番号選択方式）
- 商品削除（確認付き）
- アプリ終了処理

※ DB未使用・メモリ管理による簡易実装

---

## 在庫仕様
- 在庫数は 0 以上
- 最大在庫数は Quantity.MAX_QUANTITY
- 再発注条件：quantity <= reorderPoint
- 在庫不足警告：quantity <= lowStockThreshold
- ステータス優先順位
- OUT_OF_STOCK → NEED_REORDER → LOW_STOCK → OK

---

## 使用技術・構成
- Java（コンソールアプリ）
- MVC風構成（Controller / Service / Repository / Domain）
- ValueObject設計（Quantity / ReorderPoint / LowStockThreshold / ItemId）
- Resultパターンによる例外非依存設計
- Validatorによる入力健全性チェック
- SafeActionによる例外ラッピング
- Optionalによる空データ制御
- 不変オブジェクト設計（ValueObject群）

---

## 学習内容
- ValueObjectによる不変条件管理
- 業務ロジックと入力検証の分離
- Service層による業務判断集約
- Resultパターンによるエラーハンドリング統一
- 業務エラーとシステムエラーの分離設計
- Controllerの責務限定設計
- 在庫状態管理ロジックの優先順位設計

---

## 工夫点
- Quantityを中心に数量制約を集約
- 在庫状態判定を Item 内に閉じ込めた設計
- ControllerでValueObjectを生成し、Serviceは業務判断のみに限定
- Validatorで null / 0以下 を事前に排除
- 例外を直接UIに漏らさない構造
- 番号選択方式でUXを向上しつつ、Serviceは常にItemIdを扱う設計を維持

---

## 気づき・反省
- ValueObjectは「値を包む」よりも「不変条件を守る」役割が強い
- ValidatorとDomainの責務境界を明確にすることが重要
- Resultパターンは例外よりも制御が明示的になる
- Serviceは薄くても“業務境界”として意味を持つ
- SafeActionは学習用途としてはやや過剰だが、業務設計の練習として有効

---

## 今後の課題・拡張案
- 在庫履歴の保存（入出庫ログ）
- 並び替え・検索機能追加
- ページング機能
- CSVファイル保存
- ログ出力機構の整備
- 単体テスト追加（JUnit）
- DB連携（JDBC / Spring）
- REST API化
- GUI化（JavaFX）
- Webアプリ化（Spring Boot）

---

## 学習ログ
- ValueObject設計の実践
- Validator分離設計の実装
- Service層の業務境界設計
