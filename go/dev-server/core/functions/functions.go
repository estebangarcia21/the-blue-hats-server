package functions

import (
	"fmt"
	"os"

	"dev-server/core/utils"
	"dev-server/vars"
)

var (
	pluginsDirectory = vars.DevServerLocation + "/plugins"
)

// BuildServerJar buils the server jar through the gradle wrapper
func BuildServerJar() {
	fmt.Println("Building the latest version of The Blue Hats Server...")
	utils.ExecSafeCmd("./gradlew", "shadowJar")

	fmt.Println("Moving to the plugins folder...")
	os.RemoveAll(pluginsDirectory)

	utils.ExecSafeCmd("mv", "build/libs", vars.DevServerLocation)
	utils.ExecSafeCmd("mv", dsl("/libs"), dsl("/plugins"))
}

// UpdateSpigotJar updates the spigot jar by downloading it from Spigot's website
func UpdateSpigotJar() {
	os.Chdir(vars.DevServerLocation)
	fmt.Println("Updating the spigot jar...")
	utils.Curl("https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar", "spigot-1.8.8.jar").Run()
}

func dsl(dir string) string {
	return vars.DevServerLocation + dir
}
