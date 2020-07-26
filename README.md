# Noise [![Build Status](http://img.shields.io/travis/SpongePowered/noise/develop.svg?style=flat)](https://travis-ci.org/SpongePowered/noise) [![License](http://img.shields.io/badge/license-MIT-lightgrey.svg?style=flat)][License] [![Coverage Status](https://coveralls.io/repos/github/SpongePowered/noise/badge.svg)](https://coveralls.io/github/SpongePowered/noise)

Noise generation library for Java, based on the [libnoise](http://libnoise.sourceforge.net/) C++ library by Jason Bevins. It is used to generate coherent noise, a type of smoothly-changing noise. It can also generate Perlin noise, ridged multifractal noise, and other types of coherent noise.

* [Source]
* [Issues]
* [Community Discord]
* [Development IRC]: [#spongedev on irc.esper.net]

## Prerequisites
* Java 8

## Building from Source
In order to build math you simply need to run the `./gradlew build` command. You can find the compiled JAR file in `./build/libs` labeled similarly to 'noise-x.x.x-SNAPSHOT.jar'.

## Contributing
Are you a talented programmer looking to contribute some code? We'd love the help!
* Open a pull request with your changes, following our [guidelines](CONTRIBUTING.md).

## Usage
If you're using [Gradle] to manage project dependencies, simply include the following in your `build.gradle` file:
```gradle
repositories {
  maven {
    url 'https://repo.spongepowered.org/maven/'
  }
}

dependencies {
  implementation 'org.spongepowered:noise:2.0.0-SNAPSHOT'
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
[Community Discord]: https://discord.gg/PtaGRAs
[Development IRC]: https://webchat.esper.net/?channels=spongedev
