package core

import (
	"fmt"
	"os"
	"os/exec"

	"dev-server/core/functions"
	"dev-server/core/utils"
	"dev-server/vars"
)

var (
	defaultTarget     = "clean"
	targets           = map[string]target{"new": new{}, "start": start{}, "delete": delete{}}
	serverTemplateURL = "https://www.dropbox.com/s/ubzaencsai8fmtk/tbhs-dev-server.tar.gz?dl=1"
)

func RunTarget(target string, workingDir string) {
	if t, ok := targets[target]; ok {
		os.Chdir(workingDir)
		t.exec()
	} else {
		fmt.Println("Invalid target.")
	}
}

type target interface {
	exec()
}

// Targets
type new struct{}
type start struct{}
type delete struct{}

func (n new) exec() {
	if _, err := os.Stat(vars.DevServerLocation); !os.IsNotExist(err) {
		fmt.Println("The development server already exists.")
		return
	}

	serverTarball := "tbhs-dev-server.tar.gz"

	os.Mkdir(vars.DevServerLocation, 0755)
	os.Chdir(vars.DevServerLocation)

	fmt.Println("Downloading the server files...")
	utils.HandleCmd(utils.Curl(serverTemplateURL, serverTarball))

	fmt.Println("Unzipping...")
	utils.ExecSafeCmd("tar", "--strip-components=1", "-xf", serverTarball)
	os.Remove(serverTarball)

	os.Chdir("..")

	functions.BuildServerJar()
	functions.UpdateSpigotJar()

	fmt.Println(`
	Done! The development server was successfully created!

	To run the server, do ./dev-server start
	Otherwise, to delete the server do ./dev-server delete
	`)
}

func (s start) exec() {
	functions.BuildServerJar()

	os.Chdir(vars.DevServerLocation)

	fmt.Println("Starting the server...")
	subProcess := exec.Command("java", "-jar", "-Xms2g", "-Xmx4g", "-XX:+UseG1GC", "spigot-1.8.8.jar", "nogui")

	stdin, err := subProcess.StdinPipe()
	if err != nil {
		fmt.Println(err)
	}
	defer stdin.Close()

	subProcess.Stdout = os.Stdout
	subProcess.Stderr = os.Stderr

	if err = subProcess.Start(); err != nil {
		fmt.Println("An error occured: ", err)
	}

	subProcess.Wait()
}

func (d delete) exec() {
	fmt.Println("Removing the development server...")
	os.RemoveAll(vars.DevServerLocation)

	fmt.Println("Successfully removed.")
}
