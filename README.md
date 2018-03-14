# Bomr

CLI tool for maintaining a Maven bom.

## Building

Bomr requires Java 8 and is built with Maven:

```
./mvnw clean package
```

## Running

Bomr is a fully executable Spring Boot fat jar that can be executed directly

```
$ ./target/bomr.jar
Usage: bomr <command> [<args>]

Commands:

  upgrade    Upgrades the versions of the dependencies managed by a bom
  verify     Verifies the dependencies managed by a bom
```

## Configuration

Bomr loads configuration from a `.bomr.properties` file in your home directory.
It is used to configure the credentials required to authenticate with GitHub:

```
bomr.github.username=<<username>>
bomr.github.password=<<password>>
```

And the location of Maven's home directory:

```
bomr.maven.home=/usr/local/Cellar/maven/3.5.3/libexec
```

## Commands

### upgrade

The `upgrade` command is used to upgrade to dependency management in a Maven
bom. It should be run from within the cloned Git repository that contains the
bom you wish to upgrade. The command takes three required arguments and two
options:

```
Usage: bomr upgrade <pom> <org> <repository> [<options>]

Option                Description
------                -----------
--label <String>      Label to apply to dependency upgrade issues
--milestone <String>  Milestone to which dependency upgrade issues are assigned
```

For example, to upgrade Spring Boot's bom:

```
$ bomr.jar upgrade spring-boot-project/spring-boot-dependencies/pom.xml spring-projects spring-boot --label="priority: normal" --label="type: dependency-upgrade" --milestone=2.0.0.M5
```

For each dependency in the bom with one or more newer versions, you will be
prompted to select the version to use. Pressing enter without entering a
number will leave the dependency unchanged. Once the upgrades have been
selected, a GitHub issue will be opened and a change commited for each. Having
checked that the upgraded dependencies work and haven't introduced any
deprecation warnings, the changes can be pushed.

### verify

The `verify` command is used to verify the dependency management in a Maven bom. The
dependency management is verified by attempting to resolve every dependency that is
managed by the bom. The command takes one required argument and one option:

```
Usage: bomr verify <pom> [<options>]

Option             Description
------             -----------
--ignore <String>  groupId:artifactId of a managed dependency to ignore
```

For example, the verify Spring Boot's bom:

```
$ ~/dev/spring-io/bomr/target/bomr.jar verify spring-boot-project/spring-boot-dependencies/pom.xml \
    --ignore org.eclipse.jetty:jetty-distribution \
    --ignore org.eclipse.jetty:jetty-home \
    --ignore io.netty:netty-example \
    --ignore org.eclipse.jetty.cdi:cdi-full-servlet
```