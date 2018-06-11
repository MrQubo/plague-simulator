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
	$(RM) jn395913.zip

.PHONY: jn395913.zip
jn395913.zip:
	+rm -f $@                                    && \
	 zip -9 $@ `git ls-tree HEAD -r --name-only` && \
	 mv $@ ..                                    && \
	 cd ..                                       && \
	 mkdir tmp_3214~                             && \
	 mv $@ tmp_3214~                             && \
	 cd tmp_3214~                                && \
	 mkdir $(basename $@)                        && \
	 mv $@ $(basename $@)                        && \
	 cd $(basename $@)                           && \
	 unzip $@                                    && \
	 rm $@                                       && \
	 cd ..                                       && \
	 zip -r9 $@ $(basename $@)                   && \
	 mv $@ $(CURDIR)                             && \
	 cd ..                                       && \
	 rm -rf tmp_3214~
