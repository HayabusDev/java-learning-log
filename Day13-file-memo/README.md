# Day13 - File Memo App（ファイル保存メモアプリ）

## 作成物
コンソール型 複数行メモ管理アプリ（ファイル保存対応）

入力したメモをテキストファイルに保存し、再起動後も読み込める
Storage / Service / Controller/ ファイルI/O

---

## 機能一覧
- 複数行メモの入力・保存（上書き保存）
- 保存データの読み込み・表示
- メニュー操作
- アプリ終了処理

※ 高度な例外設計は学習優先のため簡易実装

---

## 使用技術・構成
- Java（コンソールアプリ）
- FileWriter / BufferedWriter による書き込み処理
- FileReader / BufferedReader による読み込み処理
- MVC風構成（Controller / Service / Storage）
- DI（コンストラクタ注入）構成
- InputUtil による入力補助

---

## 学習内容
- ファイル保存／読み込みの基本構造
- BufferedReader / BufferedWriter の使い方
- 複数行データの蓄積処理
- String の不変性（+= のコスト）
- Controller / Service / Storage の責務分離
- DI（依存性注入）の基本構成
- 複数行入力の設計方法

---

## 工夫点
- Controller 側で複数行入力をまとめて管理
- 改行を保持したまま保存・復元できる設計
- Storage に I/O 処理を集約
- Service を薄い窓口として設計
- Main を組み立て専用クラスに限定

---

## 気づき・反省
- 複数行対応には「蓄積」と「区切り管理」が重要だと理解
- while / += の使い方を通して設計ミスを修正
- I/O処理は二重呼び出しすると非効率
- 責務分離を意識すると構造が安定する
- String の扱い方によってパフォーマンスが変わる

---

## 今後の課題・拡張案
- 追記保存（日記形式）への対応
- StringBuilder による高速化
- 検索機能の追加
- Result / ErrorCode によるエラー管理導入
- JSON形式での保存対応
- DB連携（JDBC / Spring）
- GUI化（JavaFX）
- Webアプリ化（Spring Boot）

---

## 学習ログ
- ファイルI/O / MVC設計 / 複数行処理の実践練習として作成
