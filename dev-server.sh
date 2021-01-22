#!/bin/bash

WORKING_DIRECTORY="__dev-server"
SERVER_TEMPLATE_URL="https://www.dropbox.com/s/ubzaencsai8fmtk/tbhs-dev-server.tar.gz?dl=1"
SPIGOT_VERSION="1.8.8"
SPIGOT_JAR="spigot-$SPIGOT_VERSION.jar"

check_if_server_exists() {
  if [[ ! -d $WORKING_DIRECTORY ]]; then
    echo "No development server found! Try creating it with ./dev-server create"
    exit
  fi
}

build_and_move_plugin() {
  PLUGINS_DIRECTORY="$WORKING_DIRECTORY/plugins"

  echo "Building the latest version of The Blue Hats Server..."
  ./gradlew shadowJar

  echo "Moving to the plugins folder..."

  if [[ ! -d $PLUGINS_DIRECTORY ]]; then
    mkdir $PLUGINS_DIRECTORY
  fi

  PLUGIN_NAME="the-blue-hats-server-1.0-SNAPSHOT-all.jar"

  mv -f "build/libs/$PLUGIN_NAME" "$PLUGINS_DIRECTORY/$PLUGIN_NAME"
}

update_spigot_jar() {
  cd $WORKING_DIRECTORY || exit

  rm -f $SPIGOT_JAR

  echo "Downloading the latest Spigot $SPIGOT_VERSION jar..."
  curl --silent -L https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar --output $SPIGOT_JAR
}

if [[ "$1" == create ]]; then
  SERVER_TEMPLATE_TARBALL="tbhs-dev-server.tar.gz"

  if [[ -d $WORKING_DIRECTORY ]]; then
    echo "The development server already exists. To delete the server, run ./dev-server delete"
    exit
  fi

  mkdir $WORKING_DIRECTORY

  echo "Downloading the server files..."

  cd $WORKING_DIRECTORY || exit
  curl --silent -L "$SERVER_TEMPLATE_URL" --output $SERVER_TEMPLATE_TARBALL

  echo "Unzipping..."
  tar --strip-components=1 -xf $SERVER_TEMPLATE_TARBALL
  rm -f $SERVER_TEMPLATE_TARBALL

  cd ..

  build_and_move_plugin

  update_spigot_jar

  echo "
       Done! The development server was successfully created!

       To run the server, do ./dev-server start
       Otherwise, to delete the server do ./dev-server delete"
  exit
fi

if [[ "$1" == start ]]; then
  MAX_RAM="Xmx${2:-4}g"

  check_if_server_exists

  build_and_move_plugin

  echo "Starting the server"

  cd $WORKING_DIRECTORY || exit
  java -jar -Xms2g -"$MAX_RAM" -XX:+UseG1GC $SPIGOT_JAR nogui
  exit
fi

if [[ "$1" == delete ]]; then
  echo "Deleting the development server..."
  rm -rf $WORKING_DIRECTORY

  echo "Done! Successfully deleted the development server!"
  exit
fi

if [[ "$1" == rebuild ]]; then
  build_and_move_plugin
  echo "Finished rebuilding the jar!"

  exit
fi

if [[ "$1" == update ]]; then
  update_spigot_jar

  echo "Finished updating the Spigot jar!"
  exit
fi

echo "
     THE BLUE HATS DEVELOPMENT SERVER by Stevemmmmm

     Command       Description
     ----------    ------------------------------------------------------------
     create        Creates the development server in the $WORKING_DIRECTORY
                   directory.

     start         Starts the development server with the latest plugin build.

     rebuild       Rebuilds the plugin and moves it to the plugins directory.
                   To reload the server, run /reload in-game or execute reload
                   in the terminal to apply the changes.

     delete        Deletes the development server.

     update        Updates the Spigot jar to the latest version. Run this if
                   Spigot warns that the jar is out of date.

     ---------     ------------------------------------------------------------

     SPIGOT VERSION: $SPIGOT_VERSION
     ACCEPTABLE LOGIN VERSIONS: 1.8.x - $SPIGOT_VERSION

     Commands are case sensitive.
    "
