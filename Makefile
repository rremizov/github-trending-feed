devserver:
	lein run

cljfmt:
	lein cljfmt fix

clean:
	rm -r target

target/uberjar/github-trending-feed.jar:
	lein with-profile prod uberjar

docker_build: target/uberjar/github-trending-feed.jar
	docker build -t github-trending-feed -t registry.gitlab.com/rremizov/github-trending-feed .

docker_push: docker_build
	docker push registry.gitlab.com/rremizov/github-trending-feed

k8s_deploy:
#	kubectl apply -f k8s/github-trending-feed-backend.yml
	kubectl apply -f k8s/github-trending-feed-lb.yml --record
	kubectl apply -f k8s/github-trending-feed-backend-deployment.yml --record
