package core

import (
	"fmt"
	"os"
	"os/exec"
)

var (
	workingDirectory  = "__dev-server"
	defaultTarget     = "clean"
	targets           = map[string]target{"new": new{}, "start": start{}, "delete": delete{}}
	serverTemplateURL = "https://www.dropbox.com/s/ubzaencsai8fmtk/tbhs-dev-server.tar.gz?dl=1"
	spigotJarURL      = "https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar"
)

// RunTarget executes the specified target
func RunTarget(t string) {
	if t, ok := targets[t]; ok {
		t.exec()
	} else {
		fmt.Println("Invalid target.")
	}
}

type target interface {
	exec()
}

// Build Targets
type new struct{}
type start struct{}
type delete struct{}

func (b new) exec() {
	if _, err := os.Stat(workingDirectory); os.IsExist(err) {
		fmt.Println("The development server already exists.")
		return
	}

	serverTarball := "tbhs-dev-server.tar.gz"

	os.Mkdir(workingDirectory, 0755)
	os.Chdir(workingDirectory)

	fmt.Println("Downloading the server files...")
	handleCmd(curl(serverTemplateURL, serverTarball))

	fmt.Println("Unzipping...")
	handleCmd(exec.Command("tar", "--strip-components=1", "-xf", serverTarball))
	os.Remove(serverTarball)

	fmt.Println("Building the server jar...")
	handleCmd(exec.Command("./gradlew", "shadowJar"))
}

func (s start) exec() {
}

func (d delete) exec() {

}

func buildServerJar() {

}

func updateSpigotJar() {
	fmt.Println("Updating the spigot jar...")
	curl(spigotJarURL, "spigot-1.8.8.jar").Run()
}

func curl(url string, outFile string) *exec.Cmd {
	return exec.Command("curl", "-s", "-L", url, "--output", outFile)
}

func handleCmd(cmd *exec.Cmd) {
	err := cmd.Run()

	if err != nil {
		fmt.Println("An unknown error has occured.")
		fmt.Printf("%s\n", err)
	}
}
