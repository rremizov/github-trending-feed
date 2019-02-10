build:
	lein with-profile prod uberjar

docker_build: build
	docker build -t github-trending-feed -t registry.gitlab.com/rremizov/github-trending-feed .

docker_push: docker_build
	docker push registry.gitlab.com/rremizov/github-trending-feed

docker_run: docker_build
	docker run -p 3000:3000 --name github-trending-feed github-trending-feed:latest

deploy: build
	scp -r supervisor.conf $(HOST):/etc/supervisor/conf.d/github-trending-feed.conf
	scp -r target/uberjar/github-trending-feed.jar $(HOST):/var/www/github-trending-feed.jar
	ssh $(HOST) touch /etc/supervisor/conf.d/github-trending-feed.env
	ssh $(HOST) sudo supervisorctl reread
	ssh $(HOST) sudo supervisorctl restart github-trending-feed
