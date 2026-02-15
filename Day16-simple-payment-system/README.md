# Day16 - Simple Payment System（支払いシステム）

## 作成物
コンソール型 簡易決済アプリ（クレジットカード・現金・QR決済）

支払い方法を選択し、手数料計算と決済結果（メッセージ／レシート情報）を表示する学習用システム
継承と abstract class を用いて **Template Method パターン**（処理順序の固定＋差分の分離）を中心に設計

---

## 機能一覧
- 支払い方法選択（1.クレジットカード / 2.現金 / 3.QR決済）
- 支払金額入力（範囲入力）
- 手数料計算
- 決済実行メッセージ表示
- レシート情報表示
- アプリ終了処理

※ DB未使用・メモリ保持なしの単発実行

---

## 決済仕様
- クレジットカード：手数料 3%（端数切り上げ）
- 現金：手数料なし
- QR決済：固定30円 + 1%（端数切り上げ）

---

## 使用技術・構成
- Java（コンソールアプリ）
- 継承 + abstract class
- Template Method パターン（Payment#pay を final で固定）
- Factory パターン（PaymentFactory による生成責務分離）
- Service 層による境界分離（processPayment）
- DI（コンストラクタ注入）
- InputUtil による入力補助
- 不変オブジェクト（Receipt）

---

## 学習内容
- abstract class による処理順序の固定（Template Method）
- ポリモーフィズムによる分岐排除（Payment として扱う）
- 差分の実装を子クラスへ委譲（methodName / calcFee / executeCore）
- Factory による生成責務の分離
- UI（Controller）とドメイン（Payment群）の責務分離
- 値オブジェクト（Receipt）設計の基礎

---

## 工夫点
- pay() を final にして「計算→実行→Receipt生成」の順序を固定
- 手数料ロジックを「固定 / 比例 / 複合」で分け、差分が見えるように設計
- Controller が具体クラス（CreditCardPayment 等）を直接生成しない構造
- Receipt に結果をまとめ、表示側（Controller）は Receipt を表示するだけに整理

---

## 気づき・反省
- 継承は共通化というより「振る舞いを制約して事故を防ぐ」用途が強い
- abstract を使うと if/switch より構造が安定する
- Factory を挟むと UI とドメインの依存が減って拡張しやすい
- Service は薄くても境界として意味を持つ（後でログ/保存等を足せる）

---

## 今後の課題・拡張案
- 支払い履歴の保存（List管理）
- 支払い失敗パターン追加（承認失敗など）
- Result / ErrorCode によるエラー管理
- 割引・クーポン機能
- ログ出力
- ファイル保存対応
- DB連携（JDBC / Spring）
- GUI化（JavaFX）
- Webアプリ化（Spring Boot）

---

## 学習ログ
- 継承 / abstract / Template Method の実践演習として作成
