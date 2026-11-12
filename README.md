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
- [Applications and Libraries](#applications-and-libraries)
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

### Networking and Web

- [http-client](https://github.com/jolt-lang/http-client) - An HTTP client over POSIX sockets, OpenSSL for TLS, and zlib. Runs `clj-http-lite` unmodified.
- [ring-chez-adapter](https://github.com/jolt-lang/ring-chez-adapter) - A Ring adapter for serving HTTP, binding BSD sockets directly through the FFI, plus middleware whose Ring originals need a JVM library: multipart file uploads, gzip, and static files.
- [multipart](https://github.com/jolt-lang/multipart) - An RFC 7578 `multipart/form-data` parser for Clojure and Jolt, ported from [defnull/multipart](https://github.com/defnull/multipart), with an incremental push parser for streaming a body chunk by chunk and a `parse-form-data` call for parsing it all at once.
- [router](https://github.com/jolt-lang/router) - A routing trie that mirrors `reitit.Trie`, so `reitit-core` runs on Jolt unmodified.

### Serialization and Parsing

- [transit-jolt](https://github.com/jolt-lang/transit-jolt) - Transit (JSON) read and write.
- [jolt-fressian](https://github.com/jolt-lang/jolt-fressian) - [Fressian](https://github.com/Datomic/fressian) binary serialization, the format Datomic and `clojure.data.fressian` use, wire-compatible both ways and gated against a real JVM Fressian reader and writer.
- [xml](https://github.com/jolt-lang/xml) - `clojure.xml/parse` over libxml2, plus a `clojure.data.xml` emit API.
- [yaml](https://github.com/jolt-lang/yaml) - YAML load and dump over the system libyaml, with `jolt.yaml` and `clj-yaml.core` compatible APIs.

### Cryptography

- [crypto](https://github.com/jolt-lang/crypto) - OpenSSL bindings through the FFI for hashing, HMAC, and ciphers. Backs `ring-defaults`' session and CSRF crypto.

### Logging Metrics and Tracing

- [logging](https://github.com/jolt-lang/logging) - A logging API with a native backend that drives `clojure.tools.logging`.
- [otel](https://github.com/jolt-lang/otel) - An OpenTelemetry SDK: tracing (`with-span`, W3C Trace Context propagation) and metrics (counters, histograms), exported over OTLP. Reads the standard `OTEL_*` environment variables, and every API has a no-op fallback.

### Date and Time

- [time](https://github.com/jolt-lang/time) - The formatting and zone layer of `java.time` (`DateTimeFormatter`, `ZoneOffset`/`ZoneId`, `ZonedDateTime`/`OffsetDateTime`, localized formatting, `java.util.Locale`), plus [tick](https://github.com/juxt/tick)'s idiomatic API on top. The base value types (`Instant`, `LocalDate`, `Duration`, `Period`, ...) ship in core with no dependency; this library adds the rest.

### Math and Primitives

- [primitive-math](https://github.com/jolt-lang/primitive-math) - A compatibility port of [primitive-math](https://github.com/clj-commons/primitive-math), so code written against it (`clj-uuid`'s bit operations, for one) runs unchanged. Jolt has no primitive types, so this is about compatibility rather than speed.

### Process and Shell

- [babashka/process](https://github.com/babashka/process) - Built into Jolt, so there's nothing to add to `deps.edn`. Jolt vendors the upstream library unchanged and loads it as `babashka.process` for shelling out and managing sub-processes. The old jolt-lang/process fork is gone now that Jolt reads the library's `:clj` branch.

### The Glimmer GUI Toolkit

Glimmer is Jolt's reactive GUI toolkit. One portable core plus a backend per platform.

- [glimmer](https://github.com/jolt-lang/glimmer) - The portable core: Reagent-style reactive atoms, components that return hiccup, and a reconciler that patches the live widget tree in place. Widgets come from a backend, so the same components render as GTK widgets or as text in a terminal.
- [glimmer-gtk](https://github.com/jolt-lang/glimmer-gtk) - The GTK4 backend: widget constructors, prop setters and `:on-*` signals bound through the FFI, and the `g_application_run` app loop, including the main-thread marshalling that keeps nREPL-driven UI development safe.
- [glimmer-tui](https://github.com/jolt-lang/glimmer-tui) - The terminal backend, over ncursesw: a widget set, box layout, painting, and an input loop with keyboard focus and mouse support. Needs nothing installed beyond what macOS and Linux already ship.
- [glimmer-appkit](https://github.com/jolt-lang/glimmer-appkit) - The AppKit backend: the same hiccup renders as real macOS `NSWindow`/`NSStackView`/`NSButton` widgets, driven through the Objective-C runtime by a plain C FFI.
- [glimmer-uikit](https://github.com/jolt-lang/glimmer-uikit) - The UIKit backend for iPhone: hiccup renders as real `UIStackView`/`UILabel`/`UIButton` views, through the Objective-C runtime with no Objective-C source to build.
- [glimmer-android](https://github.com/jolt-lang/glimmer-android) - The Android backend: hiccup renders into real Android Views such as `LinearLayout`, `TextView` and `Button`, through a small C half that makes the JNI calls. The screens compile into an Android shared library and run on the UI thread.
- [glimmer-qt](https://github.com/jolt-lang/glimmer-qt) - A Qt Quick backend, extracted from [snitt](https://github.com/isaksky/snitt). Its native bridge builds on first use from a git dep, given Qt 6 and a C++17 compiler.
- [glimmer-gl](https://github.com/jolt-lang/glimmer-gl) - OpenGL primitives for glimmer: composable 3D geometry and shaders as data, plus `:gl-area` and `:scale` widgets registered into glimmer-gtk.
- [glimmer-datastar](https://github.com/jolt-lang/glimmer-datastar) - A web backend over [Datastar](https://data-star.dev): implements the server side of the Datastar v1.0 wire protocol, JSON signals on actions and `datastar-patch-*` SSE events, with no Ring middleware stack needed.

## Community Projects

Projects maintained at [jlt-commons](https://jlt-commons.github.io), a community-led home for adopted Jolt libraries and for new work the core team doesn't want to own directly. Six of these arrived by transfer from their original maintainer, with stars, issues, and history intact. Four moved over from jolt-lang, and their old URLs redirect. Twenty-seven were started here, twenty of them by Jolt's author. Each keeps its own release cadence, and thirteen publish their own docs at `jlt-commons.github.io/<repo>/`.

### Graphics and Games

- [raylib-jlt](https://github.com/jlt-commons/raylib-jlt) - [raylib](https://www.raylib.com) bindings in native Clojure, calling the system `libraylib` over its C ABI through `jolt.ffi`, with a keyword-argument drawing API on top. [Docs](https://jlt-commons.github.io/raylib-jlt/).
- [raylib-jolt-demo](https://github.com/jlt-commons/raylib-jolt-demo) - 187 raylib examples built on raylib-jlt, each one its own small runnable project. [Docs and gallery](https://jlt-commons.github.io/raylib-jolt-demo/).
- [raygui-jlt](https://github.com/jlt-commons/raygui-jlt) - 24 examples of raygui, raylib's immediate-mode GUI library, bound the same way. [Docs and gallery](https://jlt-commons.github.io/raygui-jlt/).
- [raylib-ios](https://github.com/jlt-commons/raylib-ios) - raylib and SDL2 running on a physical iPhone as portable bytecode with no JIT, since iOS forbids generating code at runtime. The platform: host loop, bindings, and the build, deploy, and live tools. [Docs](https://jlt-commons.github.io/raylib-ios/).
- [raylib-ios-demo](https://github.com/jlt-commons/raylib-ios-demo) - What runs on raylib-ios: 137 scenes, each a sub-project that builds an iPhone app of its own, plus a gallery app holding them all. [Docs and gallery](https://jlt-commons.github.io/raylib-ios-demo/).
- [raylib-android](https://github.com/jlt-commons/raylib-android) - raylib on an Android phone as native arm64 code, with no JVM, Kotlin, or Java anywhere in the app. Seventeen scenes under an owner loop of about thirty lines. [Docs](https://jlt-commons.github.io/raylib-android/).
- [graviton](https://github.com/jlt-commons/graviton) - A physics game where you place gravitational attractors to steer a ship toward prizes and away from death zones. A Jolt and raylib port of a 2018 ClojureScript game, with its field math checked by [writ](https://github.com/jlt-commons/writ).
- [ents](https://github.com/jlt-commons/ents) - Bindings for [flecs](https://github.com/SanderMertens/flecs), the C entity component system, with an API modeled on [vybe](https://github.com/pfeodrippe/vybe). Components read and write as plain maps, and systems and observers are written as binding vectors over components. When a callback throws, the exception is rethrown from the `progress` or `set-c!` call that drove it, so the world stays usable.
- [slipstream](https://github.com/jlt-commons/slipstream) - GPU vector rendering over [Slughorn](https://github.com/AlphaPixel/slughorn): a declarative scene compiler, a reference GLFW renderer, and an incremental glimmer backend with gradients, masks, compositing, SDF/MSDF and shader effects.
- [upbge-jolt](https://github.com/jlt-commons/upbge-jolt) - A Jolt nREPL inside the UPBGE/Blender game engine in place of its Python API, so CIDER, Calva or Cursive can evaluate against the running editor or game.

### User Interfaces

- [glitter-core](https://github.com/jlt-commons/glitter-core) - The natives-free two-thirds of glitter: the [Replicant](https://github.com/cjohansen/replicant)-style reconciler and `IRender`/`IMemory` protocols, with no GTK4/AppKit/anything-else toolkit dependency at all. What glitter, glitter-uikit, and uikit-demo all build on. [Docs](https://jlt-commons.github.io/glitter-core/).
- [nexus-jolt](https://github.com/jlt-commons/nexus-jolt) - A Jolt port of [nexus](https://github.com/cjohansen/nexus): data-driven action/effect/placeholder dispatch. Not GUI-specific, but its only consumers today are glitter and friends, so it's listed here. [Docs](https://jlt-commons.github.io/nexus-jolt/).
- [glitter](https://github.com/jlt-commons/glitter) - The GTK4 renderer built on glitter-core: one state atom, a pure `state -> hiccup` view, and event handlers as data. [Docs](https://jlt-commons.github.io/glitter/).
- [glitter-gl](https://github.com/jlt-commons/glitter-gl) - OpenGL geometry, matrices, and shaders for glitter, plus a `:gl-area` widget to draw them in. [Docs](https://jlt-commons.github.io/glitter-gl/).
- [glitter-uikit](https://github.com/jlt-commons/glitter-uikit) - The same renderer model driving native macOS `NSView` widgets through AppKit instead of GTK4. [Docs](https://jlt-commons.github.io/glitter-uikit/).
- [uikit-demo](https://github.com/jlt-commons/uikit-demo) - A demo of glitter-uikit: a hub of live example windows (counter, currency converter, live FX, particle toy) built as a real macOS app bundle. [Docs](https://jlt-commons.github.io/uikit-demo/).
- [ftxui-jolt](https://github.com/jlt-commons/ftxui-jolt) - A reagent-style API over [FTXUI](https://github.com/ArthurSonzogni/FTXUI), the C++ terminal UI library: components as functions returning hiccup, rendered through FTXUI's own event loop, focus handling, and mouse support.
- [glimmer-bui](https://github.com/jlt-commons/glimmer-bui) - A glimmer backend over [BUI](https://github.com/ppmpreetham/BUI), Blender's UI as a standalone C library. Panels, number sliders and color swatches, with damage-driven rendering that costs nothing while the app sits idle.

### Concurrency

- [ebb](https://github.com/jlt-commons/ebb) - A port of [missionary](https://github.com/leonoel/missionary): composable tasks and flows with real cancellation and glitch-free dataflow, running on Chez fibers. [Docs](https://jlt-commons.github.io/ebb/).
- [ensemble](https://github.com/jlt-commons/ensemble) - Erlang processes and the OTP behaviours on Jolt's native fibers: links, exit signals, monitors, selective receive, `gen_server`, `gen_statem`, `gen_event`, supervisors, and applications. Distribution is not part of it yet.
- [tapestry](https://github.com/jlt-commons/tapestry) - Structured concurrency primitives for Clojure on Jolt, backed by `core.async` fiber handles rather than manifold. Marked pre-1.0.

### Storage

- [duratom](https://github.com/jlt-commons/duratom) - A durable, dereffable atom that persists every mutation to a pluggable backend. It implements glimmer's `IReactiveCell` protocol, so it drops into a glimmer app unchanged.

### Parsing and Translation

- [instaparse](https://github.com/jlt-commons/instaparse) - A port of [Instaparse](https://github.com/Engelberg/instaparse): EBNF and ABNF grammars turned into parsers, with left-recursive and ambiguous grammar support, hiccup and enlive output, and detailed error reporting.
- [clj-to-ys](https://github.com/jlt-commons/clj-to-ys) - Translates Clojure source into idiomatic [YS (YAMLScript)](https://yamlscript.org), as a CLI or a library, built on the instaparse port.

### Logging

- [mulog](https://github.com/jlt-commons/mulog) - A port of [BrunoBonacci/mulog](https://github.com/BrunoBonacci/mulog): structured event logging with global and lexical context, tracing with flake trace IDs, duration, and error capture, buffered dispatch to publishers.

### Cloud Services

- [aws-api-jolt](https://github.com/jlt-commons/aws-api-jolt) - Runs cognitect's [aws-api](https://github.com/cognitect-labs/aws-api) unmodified from Maven, supplying the HTTP client it expects over [http-client](https://github.com/jolt-lang/http-client) plus the classpath resource that makes it discoverable. All four AWS wire protocols are exercised against live AWS.

### Java Interop

- [jolt-openldk](https://github.com/jlt-commons/jolt-openldk) - Java libraries from Jolt with no JVM, by loading SBCL with [OpenLDK](https://github.com/atgreen/openldk) into the Jolt process and calling across a small C shim. Alpha.

### Machine Learning

- [lev](https://github.com/jlt-commons/lev) - Pure-Clojure inference for the [Laya](https://huggingface.co/convaiinnovations/laya) decision models: ModernBERT and mmBERT encoders plus a decision head, scorer and act head, f32 end to end with no JVM and no Python. Reproduces the Python package's answers across all three checkpoints and routes per request the way its `Router` does.

### Statistics and Geospatial

- [augur](https://github.com/jlt-commons/augur) - Statistics for forecasting and for learning from outcomes, with no dependencies: online Bayesian posteriors with forgetting, calibration, logistic hazards, change-point detection and forecast scoring. The statistics core of [bayesian-news-model](https://github.com/yogthos/bayesian-news-model), taken out as its own library.
- [dodeca](https://github.com/jlt-commons/dodeca) - A port of [A5](https://github.com/felixpalmer/a5), the equal-area pentagonal discrete global grid. It covers indexing, the cell hierarchy, cell measures and grid disks so far.

### Specs and Verification

- [writ](https://github.com/jlt-commons/writ) - Checks plain Clojure against a spec of what the code is for: signatures for the public functions plus laws run through test.check, with static rules taken from [Bend](https://github.com/HigherOrderCO/Bend). Built as a gate for code an LLM writes.
- [jolt-metrics](https://github.com/jlt-commons/jolt-metrics) - Code metrics for Clojure, Jolt and Scheme, from cyclomatic and cognitive complexity to Halstead, maintainability and coupling, led by a ranked refactoring queue. Reports come out as Markdown, EDN, JSON or one self-contained HTML page, and every metric is checked against a writ spec.

### Agents and Editor Tooling

- [jolt-lsp](https://github.com/jlt-commons/jolt-lsp) - A language server and agent toolset for Jolt projects. One pure engine works over an in-memory index, and MCP, a JSON CLI and LSP are thin transports over it, with a coding agent as the first consumer and the editor as the second.
- [jolt-nrepl-mcp](https://github.com/jlt-commons/jolt-nrepl-mcp) - An MCP server, written in Jolt, that gives a coding agent a persistent nREPL session to evaluate, reload, run tests, repair syntax and make structural edits in.
- [jolt-skills](https://github.com/jlt-commons/jolt-skills) - Agent skills for building on the jolt-lang and jlt-commons stack, covering projects and FFI through web, data, GUI, games and concurrency.
- [nrepl-kimi-code-plugin](https://github.com/jlt-commons/nrepl-kimi-code-plugin) - A Kimi Code plugin, written in Jolt, that evaluates in a persistent Jolt nREPL session.
- [overseer](https://github.com/jlt-commons/overseer) - A local-first agent loop. Sensors watch the screen, camera or clipboard, [lev](https://github.com/jlt-commons/lev) answers typed questions about what they saw, and a Jolt function acts on the answers.

## Applications and Libraries

Independently maintained software built with Jolt, hosted and maintained outside jlt-commons and jolt-lang.

### Runtimes and Interop

- [champaca](https://github.com/b12n-oss/champaca) - A Java runtime inside Jolt, rebuilding Anthony Green's [OpenLDK](https://github.com/atgreen/openldk) on Jolt. JVM bytecode is translated to Chez Scheme and compiled to native code, and the classes are the JDK's own, so Jolt calls Java libraries in-process. Experimental.
- [clinj](https://github.com/johnmn3/clinj) - Runs JVM Clojure libraries unchanged on non-JVM runtimes such as Jolt, jank, let-go and ClojureScript, by shimming the JDK API surface those libraries call rather than emulating a JVM.
- [cohost.cljc](https://github.com/johnmn3/cohost.cljc) - A `clj`-style command, `cohost`, that runs Clojure 1.12's own `clojure.core` source unchanged on the runtime a project picks, Jolt among them, with `deps.edn`, Maven and git deps, a REPL and tools.build working as usual.
- [jolt-edge](https://gitlab.com/nandithebull/jolt-edge) - A Clojure HTTP server that runs as a WebAssembly module on [Wasmer Edge](https://wasmer.io): Jolt compiled to `wasm32-wasix`, with the source compiled on cold start and a second variant compiled ahead of time for comparison. No JVM, no JavaScript, no build step.
- [jolt-native](https://gitlab.com/nandithebull/jolt-native) - Native capabilities for Jolt, one shared object per capability: a retained-tree UI ABI over egui (also paintable to a terminal) and freeq's AV media plane over MoQ. What frq links against.
- [lambda-mvp-jlt](https://github.com/b12n-oss/lambda-mvp-jlt) - An AWS Lambda custom runtime for Jolt, implementing the `provided.al2023` contract in about 60 lines of Clojure over Jolt's built-in HTTP client, with a benchmarking tool for cold vs. warm boot time.
- [lambda-mvp-jnk](https://github.com/b12n-oss/lambda-mvp-jnk) - The same custom-runtime contract implemented in [jank](https://jank-lang.org) (native Clojure via C++/Clang/LLVM) and deployed as a Lambda container image rather than a zip, since jank's toolchain needs a newer glibc than AL2023 ships.
- [lambda-mvp-cljs](https://github.com/b12n-oss/lambda-mvp-cljs) - The same family running on AWS Lambda's managed `nodejs24.x` runtime instead of a custom one, so the whole deployable is a `shadow-cljs`-compiled handler and a zip.
- [lambda-mvp-rst](https://github.com/b12n-oss/lambda-mvp-rst) - A fork of `lambda-mvp-jlt` that embeds a Rust capability into the runtime via [jolt-diplomat](https://github.com/jolt-lang/jolt-diplomat): the handler builds its JSON response with real `serde_json` instead of hand-assembled strings, since Jolt has no JSON library of its own.
- [lambda-mvp-bb](https://github.com/b12n-oss/lambda-mvp-bb) - The same family in [Babashka](https://babashka.org) via [blambda](https://github.com/jmglov/blambda). Nothing is compiled ahead of time here: the handler is `require`d and interpreted at cold start by Babashka's own SCI interpreter.

### Libraries

- [jolt-net](https://github.com/casselc/jolt-net) - A portable, dependency-free socket layer for Jolt: endpoints, name resolution, owned sockets and structured native errors. It was written because Jolt had three partial socket stacks, each duplicating the same raw FFI over `sockaddr` structs.
- [yyjson-jolt](https://github.com/fricze/yyjson-jolt) - JSON over the [yyjson](https://github.com/ibireme/yyjson) C library, with a `clojure.data.json`-shaped API.
- [jolt-sdl3](https://github.com/jbondeson/jolt-sdl3) - [SDL3](https://libsdl.org) bindings, all through `jolt.ffi`, so there's no JVM, JNI or C shim to build.
- [joltc-jlt](https://github.com/noonian/joltc-jlt) - The [Jolt Physics](https://github.com/jrouwe/JoltPhysics) engine for the Jolt runtime, an unrelated project that happens to share the name, bound through [joltc](https://github.com/amerkoleci/joltc), the engine's C API.
- [jolt-hegel](https://github.com/chucklehead-dev/jolt-hegel) - Property-based and stateful testing for Jolt, Babashka and JVM Clojure, backed by the [Hegel](https://hegel.dev/) generation and shrinking engine.
- [jolt-otel-clickhouse](https://github.com/chucklehead-dev/jolt-otel-clickhouse) - Exports [otel](https://github.com/jolt-lang/otel) telemetry straight into an embedded, in-process chDB database, using ClickStack's column names so later ClickStack queries line up.
- [jolt-otel-viewer](https://github.com/chucklehead-dev/jolt-otel-viewer) - A small, host-agnostic HTML renderer for OpenTelemetry traces and logs. The host hands it bounded trace summaries and trees, and it owns no database, server or exporter.
- [oscope](https://github.com/chucklehead-dev/oscope) - An embeddable, local-first telemetry explorer for Jolt programs. One EDN contract drives a server-rendered web UI, a glitter/GTK UI and a glimmer UI over the chDB tables jolt-otel-clickhouse writes.
- [eve](https://github.com/SeniorCareMarket/eve) - Persistent data structures in shared memory, the same values byte for byte across Jolt, the JVM, ClojureScript, babashka, ClojureCLR and more, so several runtimes can transact in parallel into one store.
- [lasertag](https://github.com/paintparty/lasertag) - Categorizes values across Clojure dialects, with Jolt supported from v0.8.5 alongside Clojure, ClojureScript and Babashka.
- [clojev](https://github.com/antlobach/clojev) - An unofficial Clojure SDK for TypeSafe's System One API: typed Noul, Choice and Score questions, validated answers, and retries with exponential backoff that respect `Retry-After`. The core is portable `.cljc` over a swappable transport, so the same code runs on the JVM over `java.net.http` or on Jolt over [http-client](https://github.com/jolt-lang/http-client).
- [farolero](https://github.com/antlobach/farolero) - A Jolt port of Farolero, implementing Common Lisp-style conditions, handlers, restarts, and interactive recovery while preserving JVM, ClojureScript, and Babashka support.
- [bayesian-news-model](https://github.com/yogthos/bayesian-news-model) - Models world events as a hierarchy of connected, self-exciting processes and forecasts how they're likely to evolve, learning online from events an LLM or an event dataset has coded from the news. The whole model is plain data.
- [distilla](https://github.com/yogthos/distilla) - A Jolt port of [Memorilla](https://github.com/snap-stanford/memorilla), which compresses a whole document collection into a fixed number of question-dependent memory tokens for a frozen LLM. It trains the memory module as well as running it.

### Agents and LLM Tools

- [samizdat](https://github.com/samizdat-code/samizdat) - A self-hosting agentic harness for Clojure development. The harness is a live image, the agent's output space is the harness's own code, and the agent loop itself is data.
- [kmet-agent](https://github.com/kmetia/kmet-agent) - A minimal coding agent in the spirit of [pi](https://pi.dev), with a terminal UI, streaming LLM conversations and a local tool loop. The same source runs on Babashka, Jolt and the JVM.
- [brainyard-jolt](https://github.com/grumatic/brainyard-jolt) - byj, a coding agent whose loop is data: [brainyard](https://github.com/grumatic/brainyard)'s agent runtime ported to Jolt along the lines samizdat set out.
- [march](https://github.com/not-unread/march) - DIRGE, an agentic coding loop and intent-grounding engine, translated from Rust to Jolt, covering its agent loop, compression engine and memory system.
- [veriframe](https://github.com/yogthos/veriframe) - An OpenAI-compatible server that wraps a model in a claim-first verification loop, checking each step of its reasoning with Z3 and Prolog before letting a combinatorial lemma through.

### Applications

- [newsroom](https://github.com/yogthos/newsroom) - Reads the day's news from RSS feeds and web searches and has an LLM write a briefing on how politics, economics, and science and tech push on each other. Released as builds for macOS, Linux and Windows.
- [almaz](https://github.com/yogthos/almaz) - AI denoise and sharpen for photos, camera RAW included, with a Blender-style UI through glimmer-bui. NAFNet models run through ONNX Runtime on Core ML, CUDA/TensorRT or the CPU.
- [splat-painter](https://github.com/yogthos/splat-painter) - Paints an oil-painting version of an image out of 2D Gaussian splats, starting from large soft strokes and refining with finer, more translucent layers traced along the edges. Built with glimmer.
- [talk](https://gitlab.com/nandithebull/talk) - An XMPP server speaking RFC 6120 (core) and RFC 6121 (IM and presence): SASL, SCRAM-SHA-256, resource binding, rosters, presence, offline delivery, and a Prosody-inspired module architecture.
- [frq](https://gitlab.com/nandithebull/frq) - A [freeq](https://github.com/codegod100/freeq) IRC client, its screens built as [glimmer](https://github.com/jolt-lang/glimmer) components over Vidya/egui, ported from a Rust client of the same shape. Runs unchanged on desktop, in a terminal, and on Android.
- [go-to-sleep](https://github.com/didibus/go-to-sleep) - A macOS menu-bar app that enforces a weekly bedtime schedule by locking the session during sleep blocks, and locking it again after each unlock. A root LaunchDaemon owns the schedule, so quitting the app doesn't stop enforcement, and once a block is within eight hours the schedule can only grow. Built and packaged as an Apple silicon `.pkg` with Jolt.
- [snitt](https://github.com/isaksky/snitt/tree/jolt) - A screenshot and screen-recording app for macOS and Windows that lives in the menu bar or tray, with region capture, annotation, and video trimming. The `jolt` branch ports it from C++ to Jolt, and its UI runs on a [glimmer](https://github.com/jolt-lang/glimmer) backend over Qt Quick, since extracted as [glimmer-qt](https://github.com/jolt-lang/glimmer-qt). The default branch is still the C++ app.
- [sound-jutsu](https://github.com/olivergg/sound-jutsu) - A soundboard for macOS with global hotkeys, in the spirit of Soundux, playing audio through a small C shim over [miniaudio](https://miniaud.io/).
- [tinifu](https://github.com/devcarbon-com/tinifu) - Push-to-talk speech-to-text for Linux that types into whatever window has focus on any Wayland compositor, and also transcribes WAV files from the command line.
- [metapaste](https://github.com/rvolz/metapaste) - A small paste server for thoughts, snippets and files, each stored with an explicit MIME type and a TTL, plus API-key access, Prometheus metrics and health endpoints. `jolt build` turns it into a single executable.
- [zent](https://github.com/olivergg/zent) - An orchestrator for local dev stacks such as Docker Compose services, processes and Quarkus apps, driven by a catalog of plain EDN data, with a live dashboard.
- [agentic-guitar-partner](https://github.com/gzmask/agentic-guitar-partner) - Guitar-input, pitch-detection and looping experiments around Jolt, Carp and Csound. The current focus is a looper, tested on hardware, that records and replays the real stereo signal from a CoreAudio input.

### Graphics and Games

- [raylib-pacman](https://github.com/b12n-oss/raylib-pacman) - The same Pac-Man written four times against [raylib](https://www.raylib.com), once each for babashka, Clojure on the JVM, [jank](https://jank-lang.org) and Jolt, whose copy binds [raylib-jlt](https://github.com/jlt-commons/raylib-jlt) over `jolt.ffi`. The game logic barely changes between the four, so what they really compare is the drawing layer and each runtime's own route into C. [Docs and demos](https://b12n-oss.github.io/raylib-pacman/).
- [lisp-games](https://github.com/b12n-oss/lisp-games) - Two small graphical programs, borkdude's helitorus and [raylib](https://www.raylib.com)'s own mesh-instancing example, each ported to babashka, Clojure on the JVM, [jank](https://jank-lang.org) and Jolt, whose ports bind [raylib-jlt](https://github.com/jlt-commons/raylib-jlt). helitorus rebuilds and projects its whole surface every frame and reports compute and draw milliseconds separately, so what separates the four runtimes is measured rather than guessed. [Docs and demos](https://b12n-oss.github.io/lisp-games/).
- [voxel-siege](https://github.com/yogthos/voxel-siege) - An artillery sandbox where you fire a cannon at a voxel castle, rendered with raylib over the C ABI.
- [vector-feedback](https://github.com/jasalt/vector-feedback) - An interactive video-feedback instrument built on [slipstream](https://github.com/jlt-commons/slipstream). Previous frames are reprojected through a movable pinhole camera, so panning, rolling and dollying it produces spirals and tunnels.
- [jolt-wasm-raylib-experiment](https://github.com/jasalt/jolt-wasm-raylib-experiment) - An experiment in running a Jolt app in the browser through Chez Scheme's Emscripten portable-bytecode target, rendering with raylib.

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
- [standard-clojure-style-jolt](https://github.com/crimeminister/standard-clojure-style-jolt) - A port of [Standard Clojure Style](https://github.com/oakmac/standard-clojure-style-js) to Jolt: a `standard-clj` CLI (`check`/`fix`/`list`, stdin support, `.standard-clj.edn` config) plus a library API (`format`/`parse`/`parse-ns`), compiling to a native binary with no JVM.

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
- [raylib-jnk](https://github.com/b12n-oss/raylib-jnk) - A guide to calling C and C++ from [jank](https://jank-lang.org), written while porting raylib's examples. The 213 ported examples live in [raylib-jank-demo](https://github.com/b12n-oss/raylib-jank-demo), one small project each, so they sit side by side with [raylib-jolt-demo](https://github.com/jlt-commons/raylib-jolt-demo). [Guide](https://b12n-oss.github.io/raylib-jnk/) and [gallery](https://b12n-oss.github.io/raylib-jank-demo/).
- [raygui-jnk](https://github.com/b12n-oss/raygui-jnk) - The 24 [raygui-jlt](https://github.com/jlt-commons/raygui-jlt) examples ported to jank, with the same names and descriptions so the two read as one library through two Clojure implementations. [Docs and gallery](https://b12n-oss.github.io/raygui-jnk/).
- [raylib-clj](https://github.com/b12n-oss/raylib-clj) - raylib bindings for Clojure on the JVM, calling the C library through [coffi](https://github.com/IGJoshua/coffi) over JDK 22's foreign-function API. Its 113 examples moved to [raylib-clj-demo](https://github.com/b12n-oss/raylib-clj-demo), laid out the same way as the Jolt and jank demos. [Docs](https://b12n-oss.github.io/raylib-clj/) and [gallery](https://b12n-oss.github.io/raylib-clj-demo/).

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
