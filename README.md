# Introduction

This repository contains Whistle2 based implementations to provide additional Whistle2 features, provide custom distributions, and other useful implementation content. 

This is very early work, not documented yet, and will arbitrary change based on my specific needs. However, I am interested in providing stable releases for anything that might be useful to others at some point.

# What's available

- A plugin to provide additional Java based functions. See `/plugins/seutil-plugin-build/seutil-plugin`
  - To check if a Whistle function exists. This is useful before trying to call such a function especially when the function name and arguments are dynamically generated.
- Custom Whistle2 distributions that contains the upstream code (with plugins) along with features from this repository.
  - cd to the `/distributions/all-dist-build` directory and then run `./gradlew clean build installDist`. If all goes well, you should see the unpacked distribution under `/distributions/all-dist-build/build/install/se-whistle2-cli` and archived distributions under `/distributions/all-dist-build/build/distributions`.


# Available artifacts

See [here](https://oss.sonatype.org/content/repositories/snapshots/com/essaid/whistle/) for currently published artifacts.