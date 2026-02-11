# Day14 - Login Validator
## 作成物
コンソール型 ユーザー登録・ログイン管理アプリ

ユーザーIDとパスワードによる登録・認証機能を備えた学習用システム 
Validator / Service / Repository / Controller に分離し、設計と例外管理を意識して構築

---

## 機能一覧
- ユーザー登録（パスワード確認付き）
- ログイン認証
- 登録ユーザー一覧表示
- 入力チェック（Validator対応）
- メニュー操作
- アプリ終了処理

※ DB未使用・メモリ管理による簡易実装

---

## 使用技術・構成
- Java（コンソールアプリ）
- HashMap によるユーザー管理
- MVC風構成（Controller / Service / Repository）
- Validator + ValidationRule による入力検証
- Result / ErrorCode によるエラー管理
- SafeAction による安全実行設計
- DI（コンストラクタ注入）
- InputUtil による入力補助

---

## 学習内容
- enum を用いた ValidationRule / ErrorCode 設計
- ジェネリクスの実践利用
- Service層での業務ロジック整理
- Controllerでの表示制御設計
- 入力検証と認証処理の分離

---

## 工夫した点
- ゲートチェック＋通常チェック方式のValidator設計
- 複数エラーをまとめて返却できる構造
- 認証エラーを AUTH 系コードで統一
- Controllerでエラー表示を共通化
- Repositoryの内部構造を直接公開しない設計

---

## 気づき・反省
- 入力検証と業務処理を分離による設計安定
- Result設計により例外依存を減らせる
- エラー分類を意識すると保守性が上がる
- 責務を分けないとクラスが肥大化する

---

## 今後の課題・拡張案
- ログイン試行回数制限
- 最終ログイン日時の保存
- パスワードハッシュ化対応
- ファイル保存対応
- DB連携（JDBC / Spring）
- セッション管理
- GUI化（JavaFX）
- Webアプリ化（Spring Boot）

---

## 学習ログ
- MVC設計 / Validator / Result / 認証処理の実践演習として作成
