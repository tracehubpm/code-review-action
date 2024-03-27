[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/trarcehubpm/code-review-action)](http://www.rultor.com/p/tracehubpm/code-review-action)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn](https://github.com/tracehubpm/code-review-action/actions/workflows/mvn.yml/badge.svg)](https://github.com/tracehubpm/code-review-action/actions/workflows/mvn.yml)
[![codecov](https://codecov.io/gh/tracehubpm/code-review-action/graph/badge.svg?token=JlkMLSJgRo)](https://codecov.io/gh/tracehubpm/code-review-action)
//docker repo

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

### Configurations

Code review quality checker can be configured the way you want.
These are the parameters you can use/override:

* `openai_token`: Open AI API key, you can obtain it [here](https://platform.openai.com/api-keys).
* `github_token`: GitHub token in order to post comments in the pull request.
* `openai_model`: Open AI ChatGPT model, the default one is `gpt-4`.
* `deepinfra_token`: Deep Infra API key, you can obtain it [here](https://deepinfra.com/dash/api_keys).
* `deepinfra_model`: Deep Infra API model, the default one is `Phind/Phind-CodeLlama-34B-v2`,
  check out [all available models](https://deepinfra.com/models/text-generation).

### How to contribute

Fork repository, make changes, send us a [pull request](https://www.yegor256.com/2014/04/15/github-guidelines.html).
We will review your changes and apply them to the `master` branch shortly,
provided they don't violate our quality standards. To avoid frustration,
before sending us your pull request please run full build:

```bash
$ gradle build
```
