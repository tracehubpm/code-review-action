[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/trarcehubpm/code-review-action)](http://www.rultor.com/p/tracehubpm/code-review-action)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn](https://github.com/tracehubpm/code-review-action/actions/workflows/mvn.yml/badge.svg)](https://github.com/tracehubpm/code-review-action/actions/workflows/mvn.yml)
[![codecov](https://codecov.io/gh/tracehubpm/code-review-action/graph/badge.svg?token=JlkMLSJgRo)](https://codecov.io/gh/tracehubpm/code-review-action)
[![docker](https://img.shields.io/docker/v/tracehub/code-review-action/latest)](https://hub.docker.com/repository/docker/tracehub/code-review-action/general)

[![Hits-of-Code](https://hitsofcode.com/github/tracehubpm/code-review-action)](https://hitsofcode.com/view/github/tracehubpm/code-review-action)
[![PDD status](http://www.0pdd.com/svg?name=tracehubpm/code-review-action)](http://www.0pdd.com/p?name=tracehubpm/code-review-action)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/tracehubpm/code-review-action/blob/master/LICENSE.txt)

Code Review Quality Checker.

**Motivation**.
[Code review](https://en.wikipedia.org/wiki/Code_review) is an important practice for every software team that cares about
the quality of its software product. On [GitHub](https://github.com), code reviews are usually done
within [pull requests](https://github.com/features/code-review), where one programmer (reviewer) makes comments asking
another programmer (author) to improve the code just submitted in a branch.
However, [very often](https://www.yegor256.com/2015/02/09/serious-code-reviewer.html),
the quality of code review may be rather low: [reviewers just say "LGTM"](https://www.l3r8y.ru/2023/08/06/dont-be-shy-cry#dont-upset-people)
and the pull request gets merged. This GitHub action, with the help of LLMs,
analyzes how thorough the code review was and posts a number of suggestions
for the reviewer so that they can improve in the future.
Besides that, this action suggests "review score," like
"excellent review" or "poor review."

### How to use

Use it like this:
```yml
name: code-review
on:
 pull_request_review:
   types: submitted
permissions:
  pull-requests: write
  contents: read
jobs:
  check:
    if: ${{ github.event.review.state == 'approved' }}
    runs-on: ubuntu-22.04
    steps:
      - uses: actions/checkout@v4
      - uses: docker://tracehub/code-review-action:latest
        with:
          openai_token: ${{ secrets.OPENAI_TOKEN }}
          github_token: ${{ secrets.GITHUB_TOKEN }}
```

### Skip Pull Requests by the amount of lines

In order to skip "too small" pull requests, you can configure `min_lines`
parameter:

```yml
name: code-review
on:
 pull_request_review:
   types: submitted
permissions:
  pull-requests: write
  contents: read
jobs:
  check:
    if: ${{ github.event.review.state == 'approved' }}
    runs-on: ubuntu-22.04
    steps:
      - uses: actions/checkout@v4
      - uses: docker://tracehub/code-review-action:latest
        with:
          openai_token: ${{ secrets.OPENAI_TOKEN }}
          github_token: ${{ secrets.GITHUB_TOKEN }}
          min_lines: 15
```

### Configurations

Code review quality checker can be configured the way you want.
These are the parameters you can use/override:

* `openai_token`: Open AI API key, you can obtain it [here](https://platform.openai.com/api-keys).
* `github_token`: GitHub token in order to post comments in the pull request.
* `openai_model`: Open AI ChatGPT model, the default one is `gpt-4`.
* `deepinfra_token`: Deep Infra API key, you can obtain it [here](https://deepinfra.com/dash/api_keys).
* `deepinfra_model`: Deep Infra API model, check out [all available models](https://deepinfra.com/models/text-generation).
* `min_lines`: Minimal amount of lines in the pull request to get analyzed
by this action, pull requests with fewer lines than provided `min_size`
won't be processed.

### Analysis Method

To analyze code review quality, performed by other programmer, we employ
[LLM](https://en.wikipedia.org/wiki/Large_language_model).
First we parse GitHub pull request to this format:

```json
[
  {
    "filename": "eo-parser/src/test/resources/org/eolang/parser/packs/add-locators.yaml",
    "additions": 5,
    "deletions": 6,
    "changes": 11,
    "patch": "@@ -12,11 +12,10 @@ tests:\n   - //o[not(@base) and @name='e' and @loc='Φ.org.abc.tt.α2.e']\n   - //o[@base='.hello' and @loc='Φ.org.abc.tt.α2.φ']\n   - //o[@base='e' and @loc='Φ.org.abc.tt.α2.φ.ρ']\n-  - //o[@name='q' and @base='.<' and @loc='Φ.org.abc.q']\n-  - //o[@base='.p' and not(@name) and @loc='Φ.org.abc.q.ρ']\n-  - //o[@base='.^' and not(@name) and @loc='Φ.org.abc.q.ρ.ρ']\n-  - //o[@base='.&' and not(@name) and @loc='Φ.org.abc.q.ρ.ρ.ρ']\n-  - //o[@base='$' and not(@name) and @loc='Φ.org.abc.q.ρ.ρ.ρ.ρ']\n+  - //o[@name='q' and @base='.p' and @loc='Φ.org.abc.q']\n+  - //o[@base='.^' and not(@name) and @loc='Φ.org.abc.q.ρ']\n+  - //o[@base='.&' and not(@name) and @loc='Φ.org.abc.q.ρ.ρ']\n+  - //o[@base='$' and not(@name) and @loc='Φ.org.abc.q.ρ.ρ.ρ']\n eo: |\n   +alias org.abc.foo.b\n   +alias x\n@@ -38,4 +37,4 @@ eo: |\n     [e]\n       e.hello > @\n   \n-  $.&.^.p.< > q\n+  $.&.^.p > q"
  },
  ...
]
```

Then we parse the all the reviews made by the reviewer in this pull request:

```json
[
  {
    "submitted": "@maxonfjvipon, take a look, please",
    "comments": [
      "h1alexbel: Let's refactor it, since..."
    ]
  },
  ...
]
```

After all this prepared we instruct LLM to analyze how thorough the code review was.
In the end of analysis LLM suggests a review score like "excellent review",
"fair review", and "poor review".

The next step is to generate suggestions for the reviewer, on how to improve
the code review process in future from his side. To do so, we again ask LLM to conduct
in this area.

### How to contribute

Fork repository, make changes, send us a [pull request](https://www.yegor256.com/2014/04/15/github-guidelines.html).
We will review your changes and apply them to the `master` branch shortly,
provided they don't violate our quality standards. To avoid frustration,
before sending us your pull request please run full maven build:

```bash
$ mvn clean install -Pjacoco
```

If you want to run simulation integration tests (annotated with `@Tag("simulation")`):

```shell
$ mvn clean install -Psimulation -DINPUT_GITHUB_TOKEN=... -DINPUT_DEEPINFRA_TOKEN=... -DINPUT_DEEPINFRA_MODEL=...
```

For `INPUT_GITHUB_TOKEN` provide your GitHub [token](https://github.com/settings/tokens) with write permissions
to the next repositories:

* [tracehubpm/test](https://github.com/tracehubpm/test)

For `INPUT_DEEPINFRA_TOKEN` provide your token from Deep Infra,
you can obtain it [here](https://deepinfra.com/dash/api_keys).
For `INPUT_DEEPINFRA_MODEL` pick one of the [available models](https://deepinfra.com/models/text-generation).

You will need Maven 3.8+ and Java 17+.
