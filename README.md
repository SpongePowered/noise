# Noise
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/KyoriPowered/adventure/build/master) [![MIT License](https://img.shields.io/badge/license-MIT-blue)](LICENSE.txt) [![Maven Central](https://img.shields.io/maven-central/v/org.spongepowered/noise?label=stable)](https://search.maven.org/search?q=g:org.spongepowered%20AND%20a:noise) ![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/org.spongepowered/noise?label=dev&server=https%3A%2F%2Foss.sonatype.org)

Noise generation library for Java, based on the [libnoise](http://libnoise.sourceforge.net/) C++ library by Jason Bevins. It is used to generate coherent noise, a type of smoothly-changing noise. It can also generate Perlin noise, ridged multifractal noise, and other types of coherent noise.

* [Source]
* [Issues]
* [Community Discord]

## Prerequisites
* Java 8

## Building from Source
In order to build math you simply need to run the `./gradlew build` command. You can find the compiled JAR file in `./build/libs` labeled similarly to 'noise-x.x.x-SNAPSHOT.jar'.

## Contributing
Are you a talented programmer looking to contribute some code? We'd love the help!
* Open a pull request with your changes, following our [guidelines](CONTRIBUTING.md).

## Usage

Noise publishes releases on Maven Central and Sponge's own repository. 
Snapshots are published on Sonatype OSSRH and Sponge's repository.

If you're using [Gradle] to manage project dependencies, simply include the following in your `build.gradle` file:
```gradle
repositories {
  // releases
  mavenCentral()
  // snapshots
  maven {
    url "https://repo.spongepowered.org/repository/maven-public/"
  }
}

dependencies {
  implementation "org.spongepowered:noise:2.0.0-SNAPSHOT"
}
```

If you're using [Maven] to manage project dependencies, simply include the following in your `pom.xml` file:
```xml
<dependency>
  <groupId>org.spongepowered</groupId>
  <artifactId>noise</artifactId>
  <version>2.0.0-SNAPSHOT</version>
</dependency>
```

## Credits
* Jason Bevins and contributors of the original [libnoise](http://libnoise.sourceforge.net/) C++ library.
* [Spout](https://spout.org/) and contributors - *where we all began, and for much of the re-licensed code.*
* All the people behind [Java](http://www.oracle.com/technetwork/java/index.html), [Maven], and [Gradle].

[Gradle]: https://gradle.org
[Maven]: https://maven.apache.org/
[Source]: https://github.com/SpongePowered/noise
[Issues]: https://github.com/SpongePowered/noise/issues
[License]: https://opensource.org/licenses/MIT
[Community Discord]: https://discord.gg/sponge
