package functions

import (
	"fmt"
	"os"
	"os/exec"

	"dev-server/core/utils"
	"dev-server/vars"
)

var (
	pluginsDirectory = vars.WorkingDirectory + "/plugins"
)

// BuildServerJar buils the server jar through the gradle wrapper
func BuildServerJar() {
	fmt.Println("Building the latest version of The Blue Hats Server...")
	utils.HandleCmd(exec.Command("./gradlew", "shadowJar"))

	fmt.Println("Moving to plugins folder...")
	if _, err := os.Stat(pluginsDirectory); os.IsNotExist(err) {
		os.Mkdir(pluginsDirectory, 0755)
	}

	utils.HandleCmd(exec.Command("mv", "-v", "build/libs/*", pluginsDirectory))

	fmt.Println(`
   	Done! The development server was successfully created!

	To run the server, do ./dev-server start
	Otherwise, to delete the server do ./dev-server delete"
	`)
}

// UpdateSpigotJar updates the spigot jar by downloading it from Spigot's website
func UpdateSpigotJar() {
	os.Chdir(vars.WorkingDirectory)
	fmt.Println("Updating the spigot jar...")
	utils.Curl("https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar", "spigot-1.8.8.jar").Run()
}
