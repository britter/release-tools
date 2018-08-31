# release-tools

This project contains Apache Commons release verification tools.

## Usages

Copy the `gradle-template.properties` files to `gradle.properties` and modify it according to the component you want do
a release verification for.

To list all available tasks execute:

```
./gradlew tasks
```

All tasks needed for release verification are grouped under 'Release Verification tasks'.
The `verifyRelease` tasks executes the whole release verification:

```
./gradlew verifyRelease
```

To clear the whole output use the `clean` task.

## Features

- [x] Download artifacts from dist area
- [ ] Download artifacts from Maven staging repo
- [ ] Verify gpg signatures
- [ ] Verify SHA check sums
- [ ] Build release from source archive
- [ ] Checkout release tag
- [ ] Compare contents of source archive with contents of release tag
- [ ] Compare RELEASE-NOTES from dist are with RELEASE-NOTES from release tag
- [ ] Check that year in NOTICE file is the current year

## Contribution policy

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License

This code is open source software licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html).
