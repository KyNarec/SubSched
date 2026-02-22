<div align="center">
  <a href="https://github.com/KyNarec/SubSched/releases"><img src="https://img.shields.io/github/downloads/KyNarec/SubSched/total?label=Total%20Downloads"></a>
  <a href="https://github.com/KyNarec/SubSched/releases/latest"><img src="https://img.shields.io/github/downloads/KyNarec/SubSched/latest/total?label=Downloads%20of%20latest%20Release"></a>
  <a href="https://github.com/KyNarec/SubSched/releases/latest"><img src="https://img.shields.io/github/v/release/KyNarec/SubSched?label=Release"></a>

</div>

**SubSched** is a Compose Multiplatform app to fetch substitute schedules from [schule-infoportal.de/infoscreen](https://schule-infoportal.de/infoscreen)

---
## Previews

<div align="center">
    <img src="./assets/preview_02.png" width="1080"/> <br>
    <img src="./assets/preview_03.png" width="1080"/> <br>
    <img src="./assets/preview_04.png" width="1080"/> <br>
</div>

<div align="center">
    <img src="./assets/preview_01.jpg" width="32%"/> 
    <img src="./assets/preview_05.jpg" width="32%"/>
    <img src="./assets/preview_06.jpg" width="32%"/>
</div>

--- 
## Installation
### Linux
#### Ubuntu/Debian/Mint
```bash
sudo dpkg -i SubSched_*_amd64_custom.deb
```
#### Arch
```bash
sudo pacman -U SubSched-*.pkg.tar.zst
```
### Android
Download and install SubSched_v*.apk from [latest GitHub release](https://github.com/KyNarec/SubSched/releases/latest)

### MacOS
Currently there is no official build for MacOS, but you can simply clone the project, run
```bash
./gradlew packageReleaseDistributionForCurrentOS
```
and you will get a .dmg file

### Windows
Download and install SubSched-*.msi from [latest GitHub releases](https://github.com/KyNarec/SubSched/releases/latest)

---



## Acknowledgments
- [SubstituteSchedule](https://github.com/KyNarec/SubstituteSchedule) A Compose Multiplatform App replacing the need for the "DSBmobile" app.
- [KSafe](https://github.com/ioannisa/KSafe) Enterprise-Grade Encrypted key-value storage for Kotlin Multiplatform and Native Android with Hardware-Backed Security.
- Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)...
