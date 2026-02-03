# Day09 - Enum Order System

## 作成物
コンソール注文管理アプリ（enum練習用）

## 学習内容
- enum（DrinkType）の基本的な使い方
- values() による選択肢の列挙
- valueOf() による String → enum 変換
- Order / Service / Main の責務分離
- Listによる注文データ管理
- IDの自動採番ロジック
- 不変寄りクラス設計（Order）

## 気づき
- enumは「定数」ではなく「型」として使うと安全性が上がる
- String管理よりもenum管理の方がバグを防ぎやすい
- Serviceに処理を集約すると流れが把握しやすい
- valueOfは失敗する可能性があるため、本来は例外対策が必要

## 将来へのつながり
- switch文を使ったDrinkType別処理の追加
- ドリンクごとの売上・個数集計機能
- 入力バリデーションの実装
- ファイル・DBへの注文履歴保存
- GUI化・Webアプリ化への発展
