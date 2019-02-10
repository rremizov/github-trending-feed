build:
	lein with-profile prod uberjar

deploy: build
	scp -r supervisor.conf $(HOST):/etc/supervisor/conf.d/github-trending-feed.conf
	scp -r target/uberjar/github-trending-feed.jar $(HOST):/var/www/github-trending-feed.jar
	ssh $(HOST) touch /etc/supervisor/conf.d/github-trending-feed.env
	ssh $(HOST) sudo supervisorctl reread
	ssh $(HOST) sudo supervisorctl restart github-trending-feed
