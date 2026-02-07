# Day12 - Generic Management （ジェネリック管理アプリ）

## 作成物
コンソール型 Task / User 管理アプリ（ジェネリック設計）

Task と User を共通構造で管理し、登録・一覧表示ができる。  
Repository / Service / Controller をジェネリック化し、再利用可能な設計を体験するため作成。

---

## 機能一覧
- Task の登録
- User の登録
- Task / User の一覧表示
- メニュー操作による切り替え

※ 入力チェック・高度なエラー処理は設計理解を優先し、簡易実装。

---

## 使用技術・構成
- Java（コンソールアプリ）
- HashMap によるデータ管理
- MVC風構成（Controller / Service / Repository）
- ジェネリック設計（Repository<T>, Service<T>）
- Identifiable による型制約
- Factory パターンによる生成分離
- DI（コンストラクタ注入）構成

---

## 学習内容
- ジェネリック型制約（T extends Identifiable）
- interface と実装クラスの分離
- Repository / Service / Controller の責務分離
- 型安全な共通処理設計
- DI（依存性注入）の基本構造
- Factory によるオブジェクト生成管理

---

## 工夫した点
- Repository / Service をジェネリック化し、Task / User 共通で利用可能にした
- Identifiable による ID 管理の統一
- DataFactory による生成責務の分離
- Controller では Service<Task> / Service<User> を分離管理
- Main を組み立て専用（Composition Root）に限定

---

## 気づき・反省
- ジェネリック化すると Service はnewできなくなることを理解できた
- 型制約を付けないと設計が破綻することを体感した
- Controller をジェネリックにするかどうかで設計方針が大きく変わる
- 責務分離を意識するとコードは増えるが、見通しは良くなる
- 無理なキャストは設計崩壊する

---

## 今後の課題・拡張案
- 削除・検索機能の追加
- Result / ErrorCode によるエラー管理導入
- ファイル保存対応
- DB連携（JDBC / Spring Data）
- GUI化（JavaFX）
- Webアプリ化（Spring Boot）

---

## 学習ログ
- ジェネリック / interface / DI / MVC設計の実践練習として作成
