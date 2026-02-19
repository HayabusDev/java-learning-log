# Day17 - Ticket Sales Simulator

## 作成物
コンソール型 チケット販売管理アプリ（イベント作成・販売開始/終了・購入・キャンセル）

イベントの販売状態（DRAFT / ON_SALE / SOLD_OUT / CLOSED）と残席数を管理し、
購入時は在庫減算＋注文作成、キャンセル時は注文取消＋在庫復元を行う学習用システム
Result / ErrorCode / Validator / Service / Repository 分離と、整合性維持（補償トランザクション）を中心に設計

---

## 機能一覧
- イベント作成（DRAFT固定生成）
- イベント一覧表示
- 販売開始（DRAFT → ON_SALE）
- 販売終了（ON_SALE / SOLD_OUT → CLOSED）
- チケット購入（ON_SALEのみ可能）
- 注文キャンセル（在庫復元）
- イベント削除（CLOSEDのみ）
- UUID入力による注文指定キャンセル
- アプリ終了処理

※ DB未使用・InMemory管理

---

## チケット仕様
- 最大座席数：60000
- 購入可能条件
- saleStatus = ON_SALE
- quantity > 0
- remainSeats >= quantity
- 残席0で SOLD_OUT へ遷移
- キャンセル成功時は残席復元
- SOLD_OUT 状態でキャンセルが発生した場合 ON_SALE に戻る

---

## 使用技術・構成
- Java（コンソールアプリ）
- MVC風構成（Controller / Service / Repository）
- InMemory Repository（HashMap）
- Result + ErrorCode によるエラー制御
- Validator による入力検証
- 状態遷移制御（SaleStatus#canTransitTo）
- 補償トランザクション（注文保存失敗時の在庫戻し）
- InputUtil による入力補助（範囲 / YesNo / UUID）

---

## 学習内容
- ドメインに状態遷移ルールを閉じ込める設計
- Service層での業務フロー制御（存在確認 → 条件確認 → 実行）
- Result型による失敗理由の明示
- ValidatorによるUI入力と業務ロジックの分離
- Repository責務の明確化
- 論理トランザクションと補償処理の実装

---

## 工夫点
- saleStatus を直接変更させず startSale / closeSale に限定
- Event と Order の両側でキャンセル可否を判定して二重防御
- 在庫減算と注文作成を一体操作として扱い整合性を維持
- UIは番号選択方式にして不正入力を最小化
- ErrorCode をメッセージ変換せず Controller で表示

---

## 気づき・反省
- 「保存失敗」を考慮しない設計は簡単に在庫不整合を起こす
- UI制御よりドメイン制約の方が安全性が高い
- 状態遷移は汎用changeStatusより意図付き操作の方が読みやすい
- Validatorを分離するとServiceが業務ロジックに集中できる

---

## 今後の課題・拡張案
- 注文履歴表示
- イベント検索・ソート
- 購入上限・ユーザー概念の導入
- 販売期限（LocalDateTime）
- ファイル保存
- DB連携（JDBC / Spring）
- GUI化（JavaFX）
- Webアプリ化（Spring Boot）

## 学習ログ
- 状態遷移・在庫管理・キャンセル整合性を題材にした業務ロジック設計の演習として作成
