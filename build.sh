#!/bin/bash

# build angular
echo 'Compiling angular sources...'
cd twitch-bot-angular
ng build --prod
cd ..
echo 'Done -> Compiling angular sources'

# copy angular results
echo "Copying angular dist..."
rm -rf twitch-bot-core/src/main/resources/web-content/angular
mkdir twitch-bot-core/src/main/resources/web-content/angular
cp -r twitch-bot-angular/dist/twitch-bot-angular/* twitch-bot-core/src/main/resources/web-content/angular/
echo "Done -> Copying angular dist"

# build backend
echo 'Compiling java backend...'
mvn package -f 'twitch-bot-core/pom.xml'
echo 'Done -> Compiling java backend'

# fetching artefact
cp twitch-bot-core/target/Twitch-Bot.jar Twitch-Bot.jar

# cleaning up
echo 'Cleaning up...'
rm -rf twitch-bot-angular/dist/
mvn clean -f 'twitch-bot-core/pom.xml'
echo 'Done -> Cleaning up'