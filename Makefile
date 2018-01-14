deploy:
	lein with-profile prod uberjar
	scp -r supervisor.conf $(HOST):/etc/supervisor/conf.d/github-trending-feed.conf
	scp -r target/uberjar/github-trending-feed.jar $(HOST):/var/www/github-trending-feed.jar
	ssh $(HOST) supervisorctl reread
	ssh $(HOST) supervisorctl restart github-trending-feed
