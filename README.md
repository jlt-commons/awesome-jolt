# Awesome Jolt

[![Awesome](https://awesome.re/badge.svg)](https://awesome.re)

> A curated list of libraries, tools, and resources for [Jolt](https://github.com/jolt-lang/jolt), a Clojure implementation that runs on Chez Scheme.

Jolt reads Clojure source, analyzes it to a host-neutral IR, and emits Scheme
that runs on [Chez](https://cisco.github.io/ChezScheme/) by default, or on
Gambit compiled to JavaScript for the browser. The compiler is self-hosted,
written in Clojure, and produces a single native binary with no JVM or Chez
install required, so it starts fast and runs light. Read the
[rationale](https://jolt-lang.net/docs/rationale.html) for why Chez Scheme,
or jump straight to [Getting Started](https://jolt-lang.net/docs/getting-started.html).

## Contents

- [Language and Compiler](#language-and-compiler)
- [Getting Started](#getting-started)
- [Official Libraries](#official-libraries)
- [Community Projects](#community-projects)
- [Applications](#applications)
- [JVM and Clojure Libraries That Run on Jolt](#jvm-and-clojure-libraries-that-run-on-jolt)
- [Tooling, Testing and Examples](#tooling-testing-and-examples)
- [Documentation and Guides](#documentation-and-guides)
- [Ecosystem Infrastructure](#ecosystem-infrastructure)
- [Related Projects](#related-projects)
- [Community and Support](#community-and-support)
- [Contributing](#contributing)
- [License](#license)

## Language and Compiler

- [jolt](https://github.com/jolt-lang/jolt) - The core project: a self-hosted Clojure implementation that compiles to Chez Scheme by default, or to Gambit compiled to JavaScript for the browser. Ships a Clojure-compatible standard library.

## Getting Started

- [Getting Started guide](https://jolt-lang.net/docs/getting-started.html) - Installation for Linux and macOS: prebuilt binaries, Homebrew, or building from source.
- [homebrew-jolt](https://github.com/jolt-lang/homebrew-jolt) - The Homebrew tap (`brew install jolt-lang/jolt/jolt`), a formula that installs prebuilt binaries or builds from source.
- [deps.edn Internals](https://jolt-lang.net/docs/tools-deps.html) - How Jolt resolves dependencies straight from git repositories with no Maven involved, cached at `~/.jolt/gitlibs`.

## Official Libraries

First-party libraries maintained in the [jolt-lang](https://github.com/jolt-lang) organization. Many bind native code through Jolt's FFI to cover ground the JVM would get from `java.*`.

### Database and Storage

- [db](https://github.com/jolt-lang/db) - `jdbc.core` for Jolt: PostgreSQL and SQLite access binding `libpq` and `libsqlite3` through the FFI, running real `clojure.jdbc` and a `next.jdbc`-shaped surface with no JVM.
- [doltera](https://github.com/jolt-lang/doltera) - A pure-Jolt MySQL-protocol client for [Dolt](https://github.com/dolthub/dolt), speaking directly to `dolt sql-server` over TCP: handshake, `mysql_native_password` auth, packet framing, and prepared statements.
- [duratom](https://github.com/jolt-lang/duratom) - A durable, dereffable atom that persists every mutation to a pluggable backend. It implements glimmer's `IReactiveCell` protocol, so it drops into a glimmer app unchanged.

### Networking and Web

- [http-client](https://github.com/jolt-lang/http-client) - An HTTP client over POSIX sockets, OpenSSL for TLS, and zlib. Runs `clj-http-lite` unmodified.
- [ring-chez-adapter](https://github.com/jolt-lang/ring-chez-adapter) - A Ring adapter for serving HTTP, binding BSD sockets directly through the FFI, plus middleware whose Ring originals need a JVM library: multipart file uploads, gzip, and static files.
- [router](https://github.com/jolt-lang/router) - A routing trie that mirrors `reitit.Trie`, so `reitit-core` runs on Jolt unmodified.

### Serialization and Parsing

- [transit-jolt](https://github.com/jolt-lang/transit-jolt) - Transit (JSON) read and write.
- [jolt-fressian](https://github.com/jolt-lang/jolt-fressian) - [Fressian](https://github.com/Datomic/fressian) binary serialization, the format Datomic and `clojure.data.fressian` use, wire-compatible both ways and gated against a real JVM Fressian reader and writer.
- [xml](https://github.com/jolt-lang/xml) - `clojure.xml/parse` over libxml2, plus a `clojure.data.xml` emit API.
- [yaml](https://github.com/jolt-lang/yaml) - YAML load and dump over the system libyaml, with `jolt.yaml` and `clj-yaml.core` compatible APIs.
- [instaparse](https://github.com/jolt-lang/instaparse) - A port of [Instaparse](https://github.com/Engelberg/instaparse): EBNF and ABNF grammars turned into parsers, with left-recursive and ambiguous grammar support, hiccup and enlive output, and detailed error reporting.

### Cryptography

- [crypto](https://github.com/jolt-lang/crypto) - OpenSSL bindings through the FFI for hashing, HMAC, and ciphers. Backs `ring-defaults`' session and CSRF crypto.

### Logging Metrics and Tracing

- [logging](https://github.com/jolt-lang/logging) - A logging API with a native backend that drives `clojure.tools.logging`.
- [mulog](https://github.com/jolt-lang/mulog) - A port of [BrunoBonacci/mulog](https://github.com/BrunoBonacci/mulog): structured event logging with global and lexical context, tracing with flake trace IDs, duration, and error capture, buffered dispatch to publishers.
- [otel](https://github.com/jolt-lang/otel) - An OpenTelemetry SDK: tracing (`with-span`, W3C Trace Context propagation) and metrics (counters, histograms), exported over OTLP. Reads the standard `OTEL_*` environment variables, and every API has a no-op fallback.

### Structured Concurrency

- [tapestry](https://github.com/jolt-lang/tapestry) - Structured concurrency primitives for Clojure on Jolt, backed by `core.async` fiber handles rather than manifold. Marked pre-1.0.

### Date and Time

- [time](https://github.com/jolt-lang/time) - The formatting and zone layer of `java.time` (`DateTimeFormatter`, `ZoneOffset`/`ZoneId`, `ZonedDateTime`/`OffsetDateTime`, localized formatting, `java.util.Locale`), plus [tick](https://github.com/juxt/tick)'s idiomatic API on top. The base value types (`Instant`, `LocalDate`, `Duration`, `Period`, ...) ship in core with no dependency; this library adds the rest.

### Math and Primitives

- [primitive-math](https://github.com/jolt-lang/primitive-math) - A compatibility port of [primitive-math](https://github.com/clj-commons/primitive-math), so code written against it (`clj-uuid`'s bit operations, for one) runs unchanged. Jolt has no primitive types, so this is about compatibility rather than speed.

### Process and Shell

- [process](https://github.com/jolt-lang/process) - The [babashka/process](https://github.com/babashka/process) library for shelling out and managing sub-processes.

### The Glimmer GUI Toolkit

Glimmer is Jolt's reactive GUI toolkit. One portable core plus a backend per platform.

- [glimmer](https://github.com/jolt-lang/glimmer) - The portable core: Reagent-style reactive atoms, components that return hiccup, and a reconciler that patches the live widget tree in place. Widgets come from a backend, so the same components render as GTK widgets or as text in a terminal.
- [glimmer-gtk](https://github.com/jolt-lang/glimmer-gtk) - The GTK4 backend: widget constructors, prop setters and `:on-*` signals bound through the FFI, and the `g_application_run` app loop, including the main-thread marshalling that keeps nREPL-driven UI development safe.
- [glimmer-tui](https://github.com/jolt-lang/glimmer-tui) - The terminal backend, over ncursesw: a widget set, box layout, painting, and an input loop with keyboard focus and mouse support. Needs nothing installed beyond what macOS and Linux already ship.
- [glimmer-uikit](https://github.com/jolt-lang/glimmer-uikit) - The AppKit backend: the same hiccup renders as real macOS `NSWindow`/`NSStackView`/`NSButton` widgets, driven through the Objective-C runtime by a plain C FFI.
- [glimmer-gl](https://github.com/jolt-lang/glimmer-gl) - OpenGL primitives for glimmer: composable 3D geometry and shaders as data, plus `:gl-area` and `:scale` widgets registered into glimmer-gtk.
- [glimmer-datastar](https://github.com/jolt-lang/glimmer-datastar) - A web backend over [Datastar](https://data-star.dev): implements the server side of the Datastar v1.0 wire protocol, JSON signals on actions and `datastar-patch-*` SSE events, with no Ring middleware stack needed.

## Community Projects

Projects maintained at [jlt-commons](https://jlt-commons.github.io), a community-led home for adopted Jolt libraries and for new work the core team doesn't want to own directly. Six of these arrived by transfer from their original maintainer, with stars, issues, and history intact; two were started here directly by Jolt's author. Each keeps its own release cadence and publishes its own docs at `jlt-commons.github.io/<repo>/`.

### Graphics and Games

- [raylib-jlt](https://github.com/jlt-commons/raylib-jlt) - 119 [raylib](https://www.raylib.com) examples in native Clojure, calling the system `libraylib` over its C ABI through `jolt.ffi`. [Docs and gallery](https://jlt-commons.github.io/raylib-jlt/).
- [raygui-jlt](https://github.com/jlt-commons/raygui-jlt) - 24 examples of raygui, raylib's immediate-mode GUI library, bound the same way. [Docs and gallery](https://jlt-commons.github.io/raygui-jlt/).
- [raylib-ios](https://github.com/jlt-commons/raylib-ios) - raylib and SDL2 running on a physical iPhone as portable bytecode with no JIT, since iOS forbids generating code at runtime. Seventeen scenes at 60 fps. Parts derive from a demo repository whose own license request is still pending; see the repo's `NOTICE`. [Docs and gallery](https://jlt-commons.github.io/raylib-ios/).
- [raylib-android](https://github.com/jlt-commons/raylib-android) - raylib on an Android phone as native arm64 code, with no JVM, Kotlin, or Java anywhere in the app. Seventeen scenes under an owner loop of about thirty lines.

### GUI Rendering

- [glitter](https://github.com/jlt-commons/glitter) - A [Replicant](https://github.com/cjohansen/replicant)-style GTK4 renderer: one state atom, a pure `state -> hiccup` view, and event handlers as data. [Docs](https://jlt-commons.github.io/glitter/).
- [glitter-gl](https://github.com/jlt-commons/glitter-gl) - OpenGL geometry, matrices, and shaders for glitter, plus a `:gl-area` widget to draw them in. [Docs](https://jlt-commons.github.io/glitter-gl/).
- [glitter-uikit](https://github.com/jlt-commons/glitter-uikit) - The same renderer model driving native macOS `NSView` widgets through AppKit instead of GTK4. [Docs](https://jlt-commons.github.io/glitter-uikit/).

### Concurrency

- [ebb](https://github.com/jlt-commons/ebb) - A port of [missionary](https://github.com/leonoel/missionary): composable tasks and flows with real cancellation and glitch-free dataflow, running on Chez fibers.

## Applications

Independently maintained software built with Jolt, hosted and maintained outside jlt-commons and jolt-lang.

- [lambda-mvp-jlt](https://github.com/b12n-oss/lambda-mvp-jlt) - An AWS Lambda custom runtime for Jolt, implementing the `provided.al2023` contract in about 60 lines of Clojure over Jolt's built-in HTTP client, with a benchmarking tool for cold vs. warm boot time.
- [lambda-mvp-jnk](https://github.com/b12n-oss/lambda-mvp-jnk) - The same custom-runtime contract implemented in [jank](https://jank-lang.org) (native Clojure via C++/Clang/LLVM) and deployed as a Lambda container image rather than a zip, since jank's toolchain needs a newer glibc than AL2023 ships.
- [lambda-mvp-cljs](https://github.com/b12n-oss/lambda-mvp-cljs) - The same family running on AWS Lambda's managed `nodejs24.x` runtime instead of a custom one, so the whole deployable is a `shadow-cljs`-compiled handler and a zip.
- [lambda-mvp-rst](https://github.com/b12n-oss/lambda-mvp-rst) - A fork of `lambda-mvp-jlt` that embeds a Rust capability into the runtime via [jolt-diplomat](https://github.com/jolt-lang/jolt-diplomat): the handler builds its JSON response with real `serde_json` instead of hand-assembled strings, since Jolt has no JSON library of its own.
- [lambda-mvp-bb](https://github.com/b12n-oss/lambda-mvp-bb) - The same family in [Babashka](https://babashka.org) via [blambda](https://github.com/jmglov/blambda). Nothing is compiled ahead of time here: the handler is `require`d and interpreted at cold start by Babashka's own SCI interpreter.
- [talk](https://gitlab.com/nandithebull/talk) - An XMPP server speaking RFC 6120 (core) and RFC 6121 (IM and presence): SASL, SCRAM-SHA-256, resource binding, rosters, presence, offline delivery, and a Prosody-inspired module architecture.
- [frq](https://gitlab.com/nandithebull/frq) - A [freeq](https://github.com/codegod100/freeq) IRC client, its screens built as [glimmer](https://github.com/jolt-lang/glimmer) components over Vidya/egui, ported from a Rust client of the same shape. Runs unchanged on desktop, in a terminal, and on Android.
- [jolt-native](https://gitlab.com/nandithebull/jolt-native) - Native capabilities for Jolt, one shared object per capability: a retained-tree UI ABI over egui (also paintable to a terminal) and freeq's AV media plane over MoQ. What frq links against.
- [jolt-edge](https://gitlab.com/nandithebull/jolt-edge) - A Clojure HTTP server that runs as a WebAssembly module on [Wasmer Edge](https://wasmer.io): Jolt compiled to `wasm32-wasix`, with the source compiled on cold start and a second variant compiled ahead of time for comparison. No JVM, no JavaScript, no build step.

## JVM and Clojure Libraries That Run on Jolt

Ordinary Clojure/JVM libraries confirmed to load and pass their conformance checks on Jolt unchanged, a few leaning on host shims Jolt provides. Loading is per-function, so a namespace can load with most functions working and a few not. See the [full compatibility list](https://jolt-lang.net/docs/libraries.html) for the details behind each one.

### Web and Routing

- [ring-core](https://github.com/ring-clojure/ring) - Via `:deps/root "ring-core"`, on the [ring-app example](https://github.com/jolt-lang/examples/tree/main/ring-app).
- [ring-codec](https://github.com/ring-clojure/ring-codec) - URL and form encoding.
- [ring-defaults](https://github.com/ring-clojure/ring-defaults) - The standard middleware stack (params, static resources with content type, session, security headers), with session and CSRF crypto via [crypto](https://github.com/jolt-lang/crypto).
- [reitit-core](https://github.com/metosin/reitit) - Data-driven routing. Its `reitit.Trie` Java class is mirrored by [router](https://github.com/jolt-lang/router).
- [integrant](https://github.com/weavejester/integrant) - Data-driven system configuration (`#ig/ref`), with its `dependency` and `meta-merge` dependencies.

### Data and Schemas

- [malli](https://github.com/metosin/malli) - Data schema validation, on the [malli-app example](https://github.com/jolt-lang/examples/tree/main/malli-app).
- [honeysql](https://github.com/seancorfield/honeysql) - A SQL formatter and helpers.
- [clojure.data.json](https://github.com/clojure/data.json) - JSON reading and writing.
- [clojure.spec.alpha](https://github.com/clojure/spec.alpha) - Data specs.
- [core.match](https://github.com/clojure/core.match) - Pattern matching.
- [core.cache](https://github.com/clojure/core.cache) - Caching (basic, FIFO, LRU, LU, TTL, soft, and wrapped), over `data.priority-map`.
- [core.memoize](https://github.com/clojure/core.memoize) - Function memoization over `core.cache`.
- [core.async](https://github.com/clojure/core.async) - CSP channels and `go` blocks (`<!`/`>!`/`alts!`, `pipeline`, `mult`/`mix`/`pub`/`sub`), on real OS threads or on [fibers](https://jolt-lang.net/docs/fibers.html).
- [core.logic](https://github.com/clojure/core.logic) - Relational logic programming (unification, `run`/`fresh`/`conde`, finite domains).
- [math.combinatorics](https://github.com/clojure/math.combinatorics) - Permutations, combinations, subsets, selections, cartesian products, partitions.
- [core.contracts](https://github.com/clojure/core.contracts) - Programming by contract (`contract`/`with-constraints`/`provide`), over `core.unify`.
- [data.zip](https://github.com/clojure/data.zip) - Zipper navigation, including `clojure.data.zip.xml`, via [xml](https://github.com/jolt-lang/xml).
- [data.csv](https://github.com/clojure/data.csv) - Reading and writing CSV.
- [data.codec](https://github.com/clojure/data.codec) - Base64 encode and decode over byte arrays.
- [data.priority-map](https://github.com/clojure/data.priority-map) - Priority maps (keyfn or a custom comparator), with `subseq`/`rsubseq`.
- [tools.macro](https://github.com/clojure/tools.macro) - Local macros (`macrolet`/`symbol-macrolet`), `mexpand`/`mexpand-all`.
- [algo.monads](https://github.com/clojure/algo.monads) - Monad macros and monads (maybe, seq, state, writer, reader, and more), over `tools.macro`.
- [test.check](https://github.com/clojure/test.check) - Property-based testing: generators, `quick-check`, shrinking.
- [tools.cli](https://github.com/clojure/tools.cli) - Command-line argument parsing.
- [tools.reader](https://github.com/clojure/tools.reader) - A Clojure reader written in Clojure, covering edn and the full reader, with indexing and pushback reader types.
- [rewrite-clj](https://github.com/clj-commons/rewrite-clj) - Parses and rewrites Clojure source while preserving whitespace and comments (nodes and a zipper), over `tools.reader`.
- [edamame](https://github.com/borkdude/edamame) - A configurable EDN/Clojure parser with source locations (`parse-string`, `parse-next`, syntax quote, reader conditionals, auto-resolve).
- [yamlstar](https://github.com/yaml/yamlstar) - YAML load and dump: a pure-Clojure parser with a JSON-safe integer policy.
- [medley](https://github.com/weavejester/medley) - Collection utilities.
- [ordered](https://github.com/clj-commons/ordered) - Insertion-ordered map and set (`ordered-map`/`ordered-set`), with transients and `#ordered/map`/`#ordered/set` reader tags.
- [config](https://github.com/yogthos/config) - Environment configuration.
- [aero](https://github.com/juxt/aero) - EDN configuration with tag literals (`#ref`/`#env`/`#or`/`#profile`/`#long`, and more).
- [mount](https://github.com/tolitius/mount) - Application state lifecycle (`defstate`, start/stop/swap, restart on recompile).

### Databases

- [clojure.jdbc](https://github.com/yogthos/clojure.jdbc) - Via [db](https://github.com/jolt-lang/db)'s `jdbc.core`, over built-in SQLite access.
- [migratus](https://github.com/yogthos/migratus) - Database migrations over [db](https://github.com/jolt-lang/db).

### Templating Markup and Text

- [Selmer](https://github.com/yogthos/Selmer) - Django-style templates.
- [hiccup](https://github.com/weavejester/hiccup) - HTML from Clojure data, on the [hiccup-app example](https://github.com/jolt-lang/examples/tree/main/hiccup-app).
- [markdown-clj](https://github.com/yogthos/markdown-clj) - Markdown to HTML, on the [markdown-app example](https://github.com/jolt-lang/examples/tree/main/markdown-app).
- [cuerdas](https://github.com/funcool/cuerdas) - String manipulation.
- [camel-snake-kebab](https://github.com/clj-commons/camel-snake-kebab) - Word-case conversions.
- [clj-rss](https://github.com/yogthos/clj-rss) - RSS feed generation, over [xml](https://github.com/jolt-lang/xml)'s `clojure.data.xml` emit.
- [text-diff](https://github.com/borkdude/text-diff) - Line-level diffing in unified `diff -u` format, with ANSI colorization.

### Date and Time

- [tick](https://github.com/juxt/tick) - Runs on Jolt through [time](https://github.com/jolt-lang/time), which adds the formatting and zone layer over core's base `java.time` types.

### Logging

- [tools.logging](https://github.com/clojure/tools.logging) - Runs verbatim over a native `clojure.tools.logging.impl` stderr backend.

## Tooling, Testing and Examples

- [nrepl](https://github.com/jolt-lang/nrepl) - nREPL middleware and client for editor connections, growing Jolt's built-in nREPL server into the op set editors like CIDER expect: sessions, completion, eldoc, macroexpand, tests, stack traces.
- [clojure-test-suite](https://github.com/jolt-lang/clojure-test-suite) - A `clojure.core` compliance test suite, originally built to characterize JVM Clojure's behavior for the [jank](https://github.com/jank-lang/jank) native-code dialect, used here to validate Jolt's own conformance.
- [examples](https://github.com/jolt-lang/examples) - Runnable sample projects (an HTTP client and server, a Ring app, hiccup, Markdown, malli, a ray tracer, FFI) that double as a tour of the ecosystem. Each has a `deps.edn`; run one with `jolt run -m app.core` or `jolt -M:alias`.

## Documentation and Guides

- [Getting Started](https://jolt-lang.net/docs/getting-started.html) - Requirements and installation for Linux and macOS.
- [Rationale](https://jolt-lang.net/docs/rationale.html) - Why Jolt targets Chez Scheme instead of the JVM.
- [Differences from Clojure](https://jolt-lang.net/docs/differences.html) - Where Jolt's semantics diverge from JVM Clojure.
- [Host Interop](https://jolt-lang.net/docs/host-interop.html) - Supplying a missing `clojure.core` function or shimming a Java class Jolt doesn't cover yet.
- [Native Interop, the FFI](https://jolt-lang.net/docs/native-interop.html) - Calling C libraries directly from Jolt.
- [Cljc Interop](https://jolt-lang.net/docs/cljc-interop.html) - Writing `.cljc` code that runs on both the JVM and Jolt.
- [REPL-Driven Development](https://jolt-lang.net/docs/repl-driven-development.html) - Working against a live Jolt process.
- [Writing Libraries](https://jolt-lang.net/docs/writing-libraries.html) - Conventions for publishing a Jolt library.
- [Testing](https://jolt-lang.net/docs/testing.html) - How Jolt's own test suite and conformance checks run.
- [Building and Running](https://jolt-lang.net/docs/building-and-deps.html) - Building `jolt` from source and running projects.
- [deps.edn Internals](https://jolt-lang.net/docs/tools-deps.html) - How dependency resolution works under the hood.
- [Scheme Backends](https://jolt-lang.net/docs/scheme-backends.html) - Chez by default, Gambit compiled to JavaScript for the browser.
- [Fibers](https://jolt-lang.net/docs/fibers.html) - Jolt's lightweight concurrency primitive, underneath `core.async`.
- [Extension Points](https://jolt-lang.net/docs/extension-points.html) - Where and how to hook into the compiler.
- [Numeric Performance](https://jolt-lang.net/docs/numeric-performance.html) - The numeric tower and where its costs show up.
- [Module Map](https://jolt-lang.net/docs/MODULES.html) - A contributor-oriented tour of the `jolt` codebase.
- [RFCs](https://jolt-lang.net/docs/rfc/README.html) - Design notes behind Jolt's language decisions: transients, type hints, structural type inference, the numeric tower, and more.
- [Language Specification](https://jolt-lang.net/docs/spec/README.html) - The formal spec, including a coverage report against `clojure.core`.

## Ecosystem Infrastructure

Shared tooling behind the jlt-commons organization, so no individual project maintainer has to build it alone.

- [docs-engine](https://github.com/jlt-commons/docs-engine) - A small Babashka static-site generator, its only external dependency `markdown-clj`, that turns a project's `docs/` markdown and a `site.edn` config into a GitHub Pages-ready site.
- [ci-builds](https://github.com/jlt-commons/ci-builds) - A fan-out CI tool that dispatches every project's own workflow against one pinned Jolt version and renders a single pass/fail table across the org, so a Jolt release regression is caught fleet-wide rather than repo by repo.
- [setup-jolt](https://github.com/jlt-commons/setup-jolt) - A GitHub Action that installs jolt on a runner from its prebuilt releases and verifies the published sha256, covering Linux x86_64 and macOS x86_64/arm64.
- [meta](https://github.com/jlt-commons/meta) - Governance and the project-proposal queue: how to propose a project, volunteer to maintain one, or ask for something the org isn't doing yet.
- [.github](https://github.com/jlt-commons/.github) - Org-wide default community health files (profile README, code of conduct, contributing guide, security policy, issue and PR templates), applied to any org repo that doesn't define its own.

## Related Projects

Other projects worth knowing about while exploring the Jolt ecosystem.

- [jank](https://github.com/jank-lang/jank) - A native-code Clojure dialect on LLVM. Its `clojure.core` compliance suite is what [clojure-test-suite](https://github.com/jolt-lang/clojure-test-suite) started from.
- [clj-commons](https://github.com/clj-commons) - The community-maintained home for Clojure/JVM libraries. jlt-commons is modeled openly on it.
- [Clojure](https://clojure.org) - The language Jolt implements, still running on the JVM.

## Community and Support

- [#jolt on the Clojurians Slack](https://clojurians.net) - Development discussion, questions, and design decisions, shared with the wider Jolt community rather than split by organization.
- [jolt-lang/jolt issues](https://github.com/jolt-lang/jolt/issues) - Bug reports and feature requests for the language and compiler.
- [jlt-commons/meta issues](https://github.com/jlt-commons/meta/issues) - Propose a project, volunteer to maintain one, or ask for something new.
- [jolt-lang on GitHub](https://github.com/jolt-lang) - The core language organization.
- [jlt-commons on GitHub](https://github.com/jlt-commons) - The community organization for adopted and new libraries.

## Contributing

Contributions welcome. Read the [contribution guidelines](https://github.com/jlt-commons/awesome-jolt/blob/main/CONTRIBUTING.md) first, then open a pull request.

## License

[![CC0](https://licensebuttons.net/p/zero/1.0/88x31.png)](https://creativecommons.org/publicdomain/zero/1.0/)

To the extent possible under law, the contributors to this list have waived
all copyright and related or neighboring rights to it, under the
[CC0 1.0 Universal](https://github.com/jlt-commons/awesome-jolt/blob/main/LICENSE) public domain dedication. This list only points
to and describes other people's work; each linked project keeps its own
license, so check a repository before depending on it.
