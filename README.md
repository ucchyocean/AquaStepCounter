# AquaStepCounter
汎用ステップ数カウンター

![スクリーンショット](https://github.com/ucchyocean/AquaStepCounter/blob/master/release/image/screenshot.png?raw=true)

## ダウンロード

Windows 64bitJava版 - [ダウンロード](https://github.com/ucchyocean/AquaStepCounter/blob/master/release/AquaStepCounter_win64.zip?raw=true)
Windows 32bitJava版 - [ダウンロード](https://github.com/ucchyocean/AquaStepCounter/blob/master/release/AquaStepCounter_win32.zip?raw=true)

## 使い方

事前に、Java Runtime（1.8 以降） をダウンロードしてインストールしてください。
ダウンロードしたzipファイルを、ローカルの任意フォルダへ展開して、出てきた `AquaStepCounter_xxx.exe` を実行してください。
「変更後(New)」や「変更前(Old)」にフォルダをドラッグアンドドロップして、「カウント実行」を押してください。
NewとOldの各ファイルを比較して、ステップ数の増減を計測します。

## 使い方（コマンドライン実行）

`asc_commandline.bat [-?] [-ui] [-new (folder path)] [-old (folder path)] [-r (report file path)] [-dc|-cc] [-dw|-cw]`

* `-?` ヘルプを表示します。
* `-ui` 計測結果を、UIを出して表示します。
* `-new (folder path)` 変更後(New)のフォルダを指定します。
* `-old (folder path)` 変更前(Old)のフォルダを指定します。
* `-r (report file path)` 計測結果を、指定されたファイル名へ、CSV形式で出力します。-ui が指定されている場合は動作しません。
* `-dc` コメントを計測の対象外にします。
* `-cc` コメントを計測の対象にします。
* `-dw` 空白や空行を計測の対象外にします。
* `-cw` 空白や空行を計測の対象にします。

## ライセンス

LGPLv3を適用します。ソースコードを流用する場合は、流用先にもLGPLv3を適用してください。

## コントリビューション

イシュー登録やプルリクエストは、いつでも歓迎です！
ガイドラインをご参照ください。

## 問い合わせ先

[ツイッター](https://twitter.com/ucchy99)へご連絡ください。

