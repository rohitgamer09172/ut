Gradle build for decompiled Grim plugin

What I added
- `build.gradle` — Java 17 project using the Shadow plugin. Adds `paper-api` as `compileOnly`.
- `settings.gradle` — sets the project name.

How to build

1. Install Java 17 and Gradle, or add a Gradle wrapper.
2. From the workspace root run:

```bash
gradle shadowJar
```

The resulting plugin will be at `build/libs/minevia-2.3.73.jar` (or similar).

Notes
- Adjust the `paper-api` version in `build.gradle` to match your target server.
- If you want a full fat-jar including third-party libs, run `gradle shadowJar` — otherwise `gradle jar` will produce a jar with compiled classes only.
- If build fails due to missing dependencies, add appropriate `compileOnly`/`implementation` entries or provide local jars in a `libs/` folder and update `dependencies` to `compileOnly files('libs/...')`.
