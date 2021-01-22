package core

import (
	"fmt"
	"os"
	"os/exec"

	"dev-server/core/utils"
	"dev-server/vars"
)

var (
	defaultTarget     = "clean"
	targets           = map[string]target{"new": new{}, "start": start{}, "delete": delete{}}
	serverTemplateURL = "https://www.dropbox.com/s/ubzaencsai8fmtk/tbhs-dev-server.tar.gz?dl=1"
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

func (n new) exec() {
	if _, err := os.Stat(vars.WorkingDirectory); os.IsExist(err) {
		fmt.Println("The development server already exists.")
		return
	}

	serverTarball := "tbhs-dev-server.tar.gz"

	os.Mkdir(vars.WorkingDirectory, 0755)
	os.Chdir(vars.WorkingDirectory)

	fmt.Println("Downloading the server files...")
	utils.HandleCmd(utils.Curl(serverTemplateURL, serverTarball))

	fmt.Println("Unzipping...")
	utils.HandleCmd(exec.Command("tar", "--strip-components=1", "-xf", serverTarball))
	os.Remove(serverTarball)

	fmt.Println("Building the server jar...")
	utils.HandleCmd(exec.Command("../gradlew", "shadowJar"))
}

func (s start) exec() {
}

func (d delete) exec() {
	fmt.Println("Removing the development server...")
	os.RemoveAll(vars.WorkingDirectory)

	fmt.Println("Successfully removed.")
}
