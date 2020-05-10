# AquaStepCounter
汎用ステップ数カウンター

![スクリーンショット](https://github.com/ucchyocean/AquaStepCounter/blob/master/release/img/screenshot.png?raw=true)

## ダウンロード

* Windows 64bitJava版 - [ダウンロード](https://github.com/ucchyocean/AquaStepCounter/blob/master/release/AquaStepCounter_win64.zip?raw=true)
* Windows 32bitJava版 - [ダウンロード](https://github.com/ucchyocean/AquaStepCounter/blob/master/release/AquaStepCounter_win32.zip?raw=true)

## 使い方

事前に、Java Runtime（1.8 以降） をダウンロードしてインストールしてください。<br/>
ダウンロードしたzipファイルを、ローカルの任意フォルダへ展開して、出てきた `AquaStepCounter_xxx.exe` を実行してください。<br/>
「変更後(New)」や「変更前(Old)」にフォルダをドラッグアンドドロップして、「カウント実行」を押してください。<br/>
NewとOldの各ファイルを比較して、ステップ数の増減を計測します。

## 使い方（コマンドライン実行）

`AquaStepCounterCLI.bat [-?] [-ui] [-new (folder path)] [-old (folder path)] [-conf (config folder path)] [-r (output file path)] [-dc|-cc] [-dw|-cw]`

* `-?` ヘルプを表示します。
* `-ui` 計測結果を、UIを出して表示します。
* `-new (folder path)` 変更後(New)のフォルダを指定します。
* `-old (folder path)` 変更前(Old)のフォルダを指定します。
* `-conf (config folder path)` 追加のコメント解析設定ファイルが格納されているファイルを指定します。
* `-r (output file path)` 計測結果を、指定されたファイル名へ、CSV形式で出力します。-ui が指定されている場合は動作しません。
* `-dc` コメントを計測の対象外にします。
* `-cc` コメントを計測の対象にします。
* `-dw` 空白や空行を計測の対象外にします。
* `-cw` 空白や空行を計測の対象にします。

## ライセンス

LGPLv3を適用します。ソースコードを流用する場合は、流用先にもLGPLv3を適用してください。

## コントリビューション

イシュー登録やプルリクエストは、いつでも歓迎です！<br/>
[ガイドライン](https://github.com/ucchyocean/AquaStepCounter/blob/master/CONTRIBUTING.md)をご参照ください。

## 問い合わせ先

[ツイッター](https://twitter.com/ucchy99)へご連絡ください。

