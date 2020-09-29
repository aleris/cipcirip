# Cip Cirip

Android App with the catalog of birds from Romania

<img src="https://github.com/aleris/cipcirip/blob/master/assets/screenshots/1.png" alt="App Screenshot" width="200"/>

The app is published in Google Play Store at:

https://play.google.com/store/apps/details?id=ro.cipcirip

# Projects

datagather - scraps information from diverse sources to ansemble the database and assets
androidapp - the android app

# Data Sources

- Pictures - extracted from Romanian Bird Atlas 2015 (https://monitorizareapasarilor.cndd.ro/atlasul_pasarilor.html).
It uses a simple algorithm to detect the picture in the PDF and avoids the texts.
- Sounds - www.xeno-canto.org (files automatically scrapped based on index and some rules)
- Texts - scrapped from Wikipedia and manually updated from other sources.

The catalog main index is also based on Romanian Bird Atlas 2015.

# Android App

The app is an androidx app written in Kotlin, the data is stored in a local database and uses Room as ORM. The catalog search is implemented using sqlite FTS.
