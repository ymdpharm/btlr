## btlr 
![](https://github.com/ymdpharm/btlr/workflows/CI/badge.svg)

Play 2.8 study, with Scala 2.13.3.

- request
    - Slack App -> API server on Cloud Run -> Firestore
- GitHub Sectets
    - ${APPLICATION_SECRET} (for Play conf)
    - ${GCP_PROJECT_ID} 
    - ${GCP_SA}
    - ${GCP_SA_KEY}

### memo
- Cloud Run deploy に `--min-instances` ができたら使う
