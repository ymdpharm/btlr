## btlr 
![](https://github.com/ymdpharm/btlr/workflows/CI/badge.svg)

Play 2.8 study, with Scala 2.13.3.

- required
    - ${APPLICATION_SECRET}
    - ${GOOGLE_APPLICATION_CREDENTIALS} + credential
- deploy
    - `sbt assembly` -> `docker build` -> push to `gcr.io/hoge`  -> Cloud Run pochi
- request
    - Slack App -> API server on Cloud Run -> Firestore 

### memo
- Cloud Run が 0 までスケールインすると Slack App が timeout してきつい
    - cron (https://cloud.google.com/scheduler) で疑似トラフィック作って回避
    - minimum scale 指定できるようになるっぽいので信じて待つ
- Test
    - kaku.
