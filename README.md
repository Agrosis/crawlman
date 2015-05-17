crawlman
=============================================

A web crawler that counts up links.

Setup
---------------------------------------------
In the crawlman folder, just create a directory titled `conf` and put `crawlman.json` in it:

```
{
  "crawler.initial": "INITIAL_PAGE_TO_CRAWL",
  "crawler.threads": 8,
  "crawler.maxQueueSize": 300,
  "db.driver": "com.mysql.jdbc.Driver",
  "db.url": "jdbc:mysql://localhost/crawlman",
  "db.user": "MYSQL_DB_USER",
  "db.password": "MYSQL_DB_PASSWORD"
}
```
