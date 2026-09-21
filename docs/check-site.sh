#!/usr/bin/env bash
# Assertions this project's documentation build must satisfy.
#
# Run by the shared site workflow in jlt-commons/ci-builds against the freshly
# built _site, with BASE_PATH exported. This project has no docs/guide/ and no
# asset dirs, so the checks below are scoped to what actually exists: the
# generic homepage (README.md, via the docs/templates/generic-home.html
# override) and the always-generated 404 page.
#
# Run it locally the same way:
#   bb site:build && BASE_PATH=/awesome-jolt bash docs/check-site.sh

set -euo pipefail
out=_site

test -f "$out/index.html"     || { echo "no homepage generated"; exit 1; }
test -f "$out/404.html"       || { echo "no 404 page generated"; exit 1; }
test -f "$out/css/screen.css" || { echo "static assets missing"; exit 1; }

# The homepage is the rendered README.md. If that ever stops being true
# silently, the site loses its front page without failing anything else.
grep -q 'Awesome Jolt' "$out/index.html" \
  || { echo "homepage rendered but has no content"; exit 1; }

# The tag cloud is the point of docs/templates/generic-home.html overriding
# the engine's own template. If the override stops applying (a filename
# typo, a templates-dir misconfiguration), the site silently falls back to
# the engine's plain generic homepage and this is the only thing that
# would catch it.
grep -q 'tag-cloud' "$out/index.html" \
  || { echo "tag cloud missing: generic-home.html override did not apply"; exit 1; }

! grep -rq '{{site-base}}' "$out"/index.html "$out"/404.html \
  || { echo "unrendered template variable"; exit 1; }

# The failure mode this site's base path exists to prevent. Served at
# jlt-commons.github.io/awesome-jolt/, a root-absolute URL loads the
# ORGANIZATION site's asset instead of this project's. The page still
# renders, wearing the wrong clothes, so nothing but a check catches it.
if grep -ohE '(href|src)="/[^"]*"' "$out"/index.html "$out"/404.html \
     | grep -vE "=\"$BASE_PATH/"; then
  echo "the URLs above escape $BASE_PATH and would resolve against the org site"
  exit 1
fi

# Everything above proves the tag cloud is PRESENT. Its numbers are a separate
# question, and a hand-kept one: each category is sized by how many entries sit
# under it in the README. A stale count renders perfectly and simply misinforms,
# so nothing here would catch it. This re-tallies the README and fails on drift.
#
# jolt first, because that is what the shared workflow installs; bb locally.
# Neither present is an error rather than a skip, since a check that quietly
# stops running is worse than one that was never added.
if command -v jolt >/dev/null 2>&1; then
  jolt run scripts/check_counts.clj
elif command -v bb >/dev/null 2>&1; then
  bb scripts/check_counts.clj
else
  echo "cannot tally the tag cloud: neither jolt nor bb is on PATH"; exit 1
fi

echo "build looks correct: homepage, tag cloud and 404 page all present, every URL under $BASE_PATH"
