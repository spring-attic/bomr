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

  artifacts    Lists the artifacts in a group with a matching version
  upgrade      Upgrades the versions of the plugins and dependencies managed by a bom
  verify       Verifies the dependencies managed by a bom
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

### artifacts

The `artifacts` command lists the artifacts in a group with a particular version. It is
intended to be used when adding new dependency management to a bom. The command takes two
required arguments and two options:

```
Usage: bomr artifacts <group> <version> [<options>]

Option                       Description
------                       -----------
--repository <URI>           Repository to query
--version-property <String>  Version property to use in generated dependency management
```

For example, to list the artifacts in the `org.quartz-scheduler` group with a version of
`2.3.0` and use the `quartz.version` property in the resulting dependency management:

```
$ bomr.jar artifacts org.quartz-scheduler 2.3.0 --version-property quartz.version
```

This command will produce the following output:

```xml
<dependency>
	<groupId>org.quartz-scheduler</groupId>
	<artifactId>quartz</artifactId>
	<version>${quartz.version}</version>
</dependency>
<dependency>
	<groupId>org.quartz-scheduler</groupId>
	<artifactId>quartz-jobs</artifactId>
	<version>${quartz.version}</version>
</dependency>
```

`--repository` can be used to list artifacts that are not yet available in Maven Central,
for example for a milestone or snapshot.

### artifacts-delta

The `artifacts-delta` command lists the change in the artifacts in a group across two
versions. It is intended to be used when upgrading to a new version of a dependency. The
command takes three required arguments and two options:

```
Usage: bomr artifacts <group> <old-version> <new-version> [<options>]

Option                       Description
------                       -----------
--repository <URI>           Repository to query
--version-property <String>  Version property to use in generated dependency management
```

For example, to list the change in artifacts in the `org.springframework.restdocs` group
across versions `1.2.0` and `2.0.0.RELEASE` and use the `spring-restdocs.version` property
in any new dependency management:

```
$ bomr.jar artifacts org.springframework.restdocs 1.2.0.RELEASE 2.0.0.RELEASE \
    --version-property spring-restdocs.version
```

This command will produce the following output:

```
Removed:

None

Added:

<dependency>
  <groupId>org.springframework.restdocs</groupId>
  <artifactId>spring-restdocs-webtestclient</artifactId>
  <version>${spring-restdocs.version}</version>
</dependency>
```

`--repository` can be used to list the delta when artifacts are not yet available in Maven
Central, for example for a milestone or snapshot.

### upgrade

The `upgrade` command is used to upgrade the dependency and plugin management in a Maven
bom. It should be run from within the cloned Git repository that contains the bom you wish
to upgrade. The command takes three required arguments and two options:

```
Usage: bomr upgrade <pom> <org> <repository> [<options>]

Option                Description
------                -----------
--label <String>      Label to apply to upgrade issues
--milestone <String>  Milestone to which upgrade issues are assigned
```

For example, to upgrade Spring Boot's bom:

```
$ bomr.jar upgrade spring-boot-project/spring-boot-dependencies/pom.xml spring-projects spring-boot \
    --label="priority: normal" \
    --label="type: dependency-upgrade" \
    --milestone=2.0.4.RELEASE
```

For each manged plugin or dependency in the bom with one or more newer versions, you will
be prompted to select the version to use. Pressing enter without entering a number will
leave the managed version unchanged. Once the upgrades have been selected, a GitHub issue
will be opened and a change commited for each. Having checked that the upgraded versions
work and haven't introduced any deprecation warnings, the changes can be pushed.

### verify

The `verify` command is used to verify the dependency management in a Maven bom. The
dependency management is verified by attempting to resolve every dependency that is
managed by the bom. The command takes one required argument and two options:

```
Usage: bomr verify <pom> [<options>]

Option              Description
------              -----------
--ignore <String>   groupId:artifactId of a managed dependency to ignore
--repository <URI>  Additional repository to use for dependency resolution
```

For example, to verify Spring Boot's bom:

```
$ bomr.jar verify spring-boot-project/spring-boot-dependencies/pom.xml \
    --ignore=io.netty:netty-example \
    --repository=https://repo.spring.io/libs-release
```

In the example above, the `libs-release` repository is required as `spring-data-gemfire`,
which is managed by the bom, depends on GemFire artifacts that are not available in Maven
Central.
