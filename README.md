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
- Cloud Run deploy に `--min-instances` ができたら使う
