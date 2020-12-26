WORKING_DIRECTORY="__dev-server"
SERVER_TEMPLATE_URL="https://github.com/Im-Stevemmmmm/the-blue-hats-server-env"
SPIGOT_VERSION="1.16.3"
SPIGOT_JAR="spigot-$SPIGOT_VERSION.jar"

check_if_server_exists () {
  if [[ ! -d $WORKING_DIRECTORY ]]; then
    echo "No development server found! Try bootstrapping it with ./dev-server.sh bootstrap"
    exit
  fi
}

build_and_move_plugin () {
  PLUGINS_DIRECTORY="$WORKING_DIRECTORY/plugins"

  echo "Building the latest version of The Blue Hats Server..."
  ./gradlew clean shadowJar

  echo "Moving to the plugins folder..."

  if [[ ! -d $PLUGINS_DIRECTORY ]]; then
    mkdir $PLUGINS_DIRECTORY
  fi

  mv ./build/libs/* $PLUGINS_DIRECTORY

  ./gradlew clean
}

if [[ "$1" == create ]]; then
  if [[ -d $WORKING_DIRECTORY ]]; then
    echo "The development server already exists. To delete the server, run ./dev-server.sh clean"
    exit
  fi

  mkdir $WORKING_DIRECTORY

  echo "Copying the server files..."
  git clone $SERVER_TEMPLATE_URL $WORKING_DIRECTORY
  echo "Success cloned the server files!"

  build_and_move_plugin

  echo "
  Done! The development server was successfully bootstrapped!

  To run the server, do ./dev-server.sh start
  Otherwise, to delete the server do ./dev-server.sh clean"
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

if [[ "$1" == clean ]]; then
  echo "Removing the development server..."
  rm -rf $WORKING_DIRECTORY

  echo "Done! Successfully removed the development server!"
  exit
fi

if [[ "$1" == build ]]; then
  build_and_move_plugin
  exit
fi

if [[ "$1" == update ]]; then
  SPIGOT_BUILD_TOOLS_JAR="SpigotBuildTools.jar"
  UPDATE_PROCESS_DIRECTORY="__update-spigot"

  check_if_server_exists

  cd $WORKING_DIRECTORY || exit

  rm -rf $UPDATE_PROCESS_DIRECTORY
  mkdir $UPDATE_PROCESS_DIRECTORY

  cd $UPDATE_PROCESS_DIRECTORY || exit

  echo "Downloading the latest Spigot build tools..."
  curl --silent https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar --output $SPIGOT_BUILD_TOOLS_JAR

  echo "Building the Spigot jar..."

  export MAVEN_OPTS="-Xmx2G"
  java -Xmx2G -jar $SPIGOT_BUILD_TOOLS_JAR --rev $SPIGOT_VERSION

  cd .. || exit

  rm -f $SPIGOT_JAR

  mv "$UPDATE_PROCESS_DIRECTORY/$SPIGOT_JAR" .

  rm -rf $UPDATE_PROCESS_DIRECTORY

  echo "Done updating!"
  exit
fi

echo "
     THE BLUE HATS DEVELOPMENT SERVER by Stevemmmmm

     Command       Description
     ----------    ------------------------------------------------------------
     create        Creates the development server in the $WORKING_DIRECTORY
                   directory.

     start         Starts the development server with the latest plugin build.

     build         Builds the plugin and moves it to the plugins directory. To
                   reload the server, run /reload in-game or execute reload in
                   the terminal to apply the changes.

     clean         Deletes the development server.

     update        Updates the Spigot jar to the latest version. Run this if
                   you get an outdated jar message on start.

     --------------------------------------------------------------------------

     SPIGOT VERSION: $SPIGOT_VERSION
     LOGIN VERSIONS: 1.8.x - $SPIGOT_VERSION

     Commands are case sensitive.
    "
