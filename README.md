# IEC 62325-504 core #

[![Build Status](https://drone.io/bitbucket.org/smree/eemws-core/status.png)](https://drone.io/bitbucket.org/smree/eemws-core/latest)

### What is this repository for? ###

This is a core implementation of IEC 62325-504 technical specification.

* **eemws-core** includes schemas, wsdl, and compiled classes necessary for the eem web services
* **eemws-utils** includes several useful classes to manage xml messages and their digital signatures
* Version **1.1.2**

Please use `./gradlew install` and java 7.x in order to compile.

----

**News**

* **06-07-2016**: Version 1.1.2 [available](https://bitbucket.org/smree/eemws-core/downloads/). Javadoc corrections, xsd and wsdl location fixes within the jar file. Thanks Emanuel for the contribution! 
* **29-06-2016**: Version 1.1.0 available. Libraries published on Maven Central! Please, note the change in the groupId coordinates.
* **08-06-2016**: Version 1.0.1 available. Small fix to gitignore.
* **02-06-2016**: **Version 1.0.0!** Now compiled with gradle (no need to install it, just use the included wrapper)
* **18-04-2016**: New connection kit version (1.0-m12). Magic Folder now can deal with several servers for sending / receiving messages. Added a new program for sending xml documents or binary files using a simple graphical application.
* **04-05-2016**: Windows service wrapper (by Tanuki Software) updated to version 3.5.29
* **01-04-2016**: New connection kit version (1.0-m11) available. Magic Folder now can deal with several input / output folders and can execute scripts / programs as well.
* **28-03-2016**: Check any IEC 62325-504 implementation with [this SoapUI project](https://bitbucket.org/smree/eemws-core/downloads/IEC-62535-504-soapui-project.xml) and [this document guide!](https://bitbucket.org/smree/eemws-core/downloads/IEC%2062325-504%20Test%20Cases.pdf)
* **25-03-2016**: New document explaining [security aspects](https://bitbucket.org/smree/eemws-core/downloads/Understanding%20security%20in%20communications%20with%20IEC%2062325-504.pdf)
* **24-03-2016**: New user's manual version (1.3) [available](https://bitbucket.org/smree/eemws-core/downloads/Connection%20Kit%20User's%20manual%20v.1.3.pdf)
* **20-11-2015**: New connection kit version (1.0-m10) with small fixes. Make it easier to use compressed payloads. Various Magic Folder fixes
* **20-11-2015**: New user's manual version (1.2)
* **21-10-2015**: New connection kit version (1.0-m9) with small fixes and use of SHA-256 for digest and signing messages instead of deprecated SHA-1 available (see Downloads section).
* **14-10-2015**: User's manual review available.
* **29-07-2015**: New connection kit version (1.0-m8) with small fixes.
* **03-06-2015**: IEC 62325-504 is now available!! (https://twitter.com/iec_csc/status/606061621010694146)
* **12-05-2015**: Added an utility pack to use Magic Folder as a Windows Service see Downloads section.
* **18-03-2015**: New connection kit version (1.0-m7) with binary support available (see Downloads section).
* **29-01-2015**: First user's manual version (no draft) available (see Downloads section)
* **29-01-2015**: New connection kit version (1.0-m6) with improvements and fixes available (see Downloads section). The application is now available in Spanish and English.
* **17-11-2014**: The first draft of the user's manual is available

### Who do I talk to? ###

* If you have questions please contact soportesios@ree.es

### How do I compile the project? ###

As stated above, you simply need to use the gradle wrapper included in the root of the repository:

```shell
$ ./gradlew install
```

This will take care of downloading gradle itself and all the project's dependencies.

The `install` task is similar to maven's one. The project artifacts will be installed in your local Maven repository.

The version of the project will be calculated automatically depending on the state of the repository and the working area.

* If there is a tag in your current commit, that tag will be used as version base
* If there isn't a tag in your current commit, the nearest tag (git describe), automatically incremented, will be used as version base
* If there isn't a tag in your current commit or the working dir is dirty, `-SNAPSHOT` will be added to the version
* The auto-increment will use next-minor-version for all branches except `master` and `release/x.y.z`; for these last two, next-patch-version will be used.

For more information, see the [build file](./build.gradle) and the [axion-release-plugin](https://github.com/allegro/axion-release-plugin).

### How do I compile version x.y.z? ###

In order to compile a specific version, you may checkout said version and compile with gradle:

```shell
$ git checkout x.y.z
$ ./gradlew install
```

If in the previous case you get a `-SNAPSHOT` version due to a dirty repository, but you really want to build the version without the snapshot indicator, first check that the modifications in the repository are correct and then you may use a specific property to ignore uncommited changes when generating the version:

```shell
$ ./gradlew install -Prelease.ignoreUncommittedChanges=true
```
