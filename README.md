# SmartCounter
仕事で使える高機能カウンター

# 概要
日常で何かと数を数えることが多いですが、仕事の場面でも数えることはしばしばあります。\
そこで基本的なカウンターの機能に加え、カウントをした時間のログが残せる機能、そして集計するときに役立つカウントした内容や個数、ログを取り出して外部へ共有できる機能をつけたカウンターを制作しました。

# スクリーンショット
## トップページ
![Screenshot_20201026_053655_com matsumoto smartconuter](https://user-images.githubusercontent.com/34024835/97118652-88815f80-174e-11eb-9fa1-eb56ef2442ff.jpg)
## 記録管理画面
![Screenshot_20201026_053718_com matsumoto smartconuter](https://user-images.githubusercontent.com/34024835/97118655-8b7c5000-174e-11eb-8943-4eb0bae892ad.jpg)
## データの共有
![Screenshot_20201026_053810_com google android gm](https://user-images.githubusercontent.com/34024835/97118656-8ddeaa00-174e-11eb-88f2-c2576345877d.jpg)
## 設定画面
![Screenshot_20201026_053736_com matsumoto smartconuter](https://user-images.githubusercontent.com/34024835/97118661-9505b800-174e-11eb-977a-0f3fc8396d0a.jpg)


# 使用技術
* Java
* android SDK
* sqlchiper(SQLite3) 4.2.0
* passay-android 1.2.0


# 機能一覧
* カウンター登録（数え上げのみ）
* カウンターの記録名変更
* アクティブになっている記録をバックグラウンド（アプリ内ではネガティブと呼ぶ）へ移行できる機能
* 各カウンターのタイムスタンプの一覧表示
* 各アクティブになっている記録のカウント数をCSV形式で外部アプリへ共有（例、G-Mailの本文へそのまま書き込み）
* 全体のタイムスタンプ（ネガティブ化したもの含む）の一覧表示
* 全体のタイムスタンプをCSV形式で外部アプリへ共有（例、G-Mailの本文へそのまま書き込み）
