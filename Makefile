.PHONY: test

repl:
	lein repl :start { :host 0.0.0.0 :port 4000 }

test:
	DB_NAME=rss_test lein test
