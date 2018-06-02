MAVEN := mvn

.PHONY: all
all: build
	$(MAKE) run

.PHONY: run
run:
	$(MAVEN) exec:java

.PHONY: build
build: update
	$(MAVEN) compile

.PHONY: update
update: validate
	$(MAVEN) dependency:resolve
	$(MAVEN) versions:use-latest-versions -U

.PHONY: validate
validate:
	$(MAVEN) validate

.PHONY: test
test: update
	$(MAVEN) test

.PHONY: check-updates
check-updates:
	$(MAVEN) versions:display-dependency-updates versions:display-plugin-updates

.PHONY: clean
clean:
	$(MAVEN) clean

.PHONY: jn395913.zip
jn395913.zip:
	+zip $@ `git ls-tree HEAD -r --name-only`
