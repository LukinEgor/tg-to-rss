# Clojure project

## TODO
### Improve dev env
- [X] run simple server
- [ ] create api
  - [X] connect db
  - [X] create migrations
    - [X] create channels table 
    - [X] create posts table 
  - [-] create index api
- [ ] cache deps


(require '[rss.db.config :as config])
(require '[ragtime.repl :as repl])
(repl/migrate config/migrations-config)
(repl/rollback config/migrations-config)
