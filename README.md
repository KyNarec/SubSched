<div align="center">
  <a href="https://github.com/KyNarec/SubSched/releases"><img src="https://img.shields.io/github/downloads/KyNarec/SubSched/total?label=Total%20Downloads"></a>
  <a href="https://github.com/KyNarec/SubSched/releases/latest"><img src="https://img.shields.io/github/downloads/KyNarec/SubSched/latest/total?label=Downloads%20of%20latest%20Release"></a>
  <a href="https://github.com/KyNarec/SubSched/releases/latest"><img src="https://img.shields.io/github/v/release/KyNarec/SubSched?label=Release"></a>

</div>

**SubSched** is a A Compose Multiplatform app to fetch substitute schedules from [schule-infoportal.de/infoscreen](https://schule-infoportal.de/infoscreen)

---

## Installation
### Linux
#### Ubuntu/Debian/Mint
```bash
sudo dpkg -i composeApp/build/dist/SubSched_*_amd64_custom.deb
```
#### Arch
```bash
sudo pacman -U composeApp/build/dist/SubSched-*.pkg.tar.zst
```
### Android
Download and install SubSched_v*.apk from [GitHub Releases](https://github.com/KyNarec/SubSched/releases)

### MacOS
Currently there is no official build for MacOS, but you can simply clone the project, run
```bash
./gradlew packageReleaseDistributionForCurrentOS
```
and you will get a .dmg file.

---



## Acknowledgments
- [SubstituteSchedule](https://github.com/KyNarec/SubstituteSchedule)
- Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)...