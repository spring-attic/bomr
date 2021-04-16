# Bomr

CLI tool for maintaining a Maven bom.

## Building

Bomr requires Java 8 and is built with Gradle:

```
./gradlew clean build
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

Bomr also loads configuration from `.bomr/bomr.properties` or `.bomr/bomr.yaml` relative
to the current working directory. This file is intended to be checked into source
control. As such, it should not be used to configure credentials (such as those for
GitHub) or settings that are specific to your machine (such as Maven's home directory).

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
bom. It should be run from the root of the cloned Git repository that contains the bom
you wish to upgrade. `upgrade` uses the following configuration properties:

| Property                              | Description                                    |
| ------------------------------------- | -----------------------------------------------|
| `bomr.bom`                            | Bom to upgrade                                 |
| `bomr.upgrade.github.organization`    | Organization of repository where upgrade issues should be opened |
| `bomr.upgrade.github.repository`      | Repository where upgrade issues should be opened |
| `bomr.upgrade.github.labels`          | Labels to apply to opened issues               |
| `bomr.upgrade.policy`                 | Policy used to identify eligible versions      |
| `bomr.upgrade.prohibited.[].project`  | Project identifier, based on its version property in the bom |
| `bomr.upgrade.prohibited.[].versions` | List of prohibited versions                    |

The command takes three options:

```
Usage: bomr upgrade [<options>]

Option                Description
------                -----------
--milestone <String>  Milestone to which upgrade issues are assigned
--no-commits          Suppress the creation of commits during the upgrade
--no-issues           Suppress the creation of issues during the upgrade
```

For example, to upgrade a bom and assign issues to the `2.0.5` milestone:

```
$ bomr.jar upgrade --milestone=2.0.5
```

For each managed plugin or dependency in the bom with one or more newer versions that
match a configurable upgrade policy, you will be prompted to select the version to use.
The upgrade policy should be configured in `./bomr/bomr.properties` or `./bomr/bomr.yaml`
using the property `bomr.upgrade.policy`. The following values are supported:

| Value                | Description                                               |
| -------------------- | --------------------------------------------------------- |
| `any`                | Any newer version                                         |
| `same-major-version` | Newer versions with the same major as the current version |
| `same-minor-version` | Newer versions with the same minor as the current version |

When being prompted to select a version, pressing enter without entering a number will
leave the managed version unchanged. Once the upgrades have been selected, a GitHub issue
will be opened and a change committed for each. Having checked that the upgraded versions
work and haven't introduced any deprecation warnings, the changes can be pushed.

#### Prohibited Versions

Versions of a project can be prohibited if they are known to be bad. Prohibited versions
will not be offered as possible updates, even if they match the configured upgrade
policy. A prohibited version can be configured as shown in the following example:

```yaml
bomr:
  bom: spring-boot-dependencies/pom.xml
  upgrade:
    prohibited:
      - project: commons-collections
        versions:
          - '[20030101,)' # Old versions that use yyyymmdd format
```

The `project` property identifies the project with prohibited versions. The identifier
is based on the project's `<name>.version` property in the bom. In this example, the
version property is `commons-collections.version` so the identifier is
`commons-collections`.

The `versions` property provides a list of one or more version ranges that are
prohibited. Maven's version range syntax is used. In this example, all versions with a
major component equal to or greater than `20030101` are prohibited.

### verify

The `verify` command is used to verify the dependency management in a Maven bom. It
should be run from the root of the cloned Git repository that contains the bom you wish
to upgrade. The dependency management is verified by attempting to resolve every
dependency that is managed by the bom. `verify` uses the following configuration
properties:

| Property                           | Description                                     |
| ---------------------------------- | ------------------------------------------------|
| `bomr.bom`                         | Bom to verify                                   |
| `bomr.maven.home`                  | Maven home directory                            |
| `bomr.verify.ignored-dependencies` | Dependencies (artifactId:groupId) to ignore     |
| `bomr.verify.repositories`         | Additional repositories to use for resolution   |

The command takes no options and can be used to verify a bom as shown in the following
example:

```
$ bomr.jar verify
```
