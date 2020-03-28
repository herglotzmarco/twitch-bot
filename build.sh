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
cd twitch-bot-core
mvn clean package
cd ..
echo 'Done -> Compiling java backend'