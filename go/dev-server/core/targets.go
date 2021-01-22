package core

import (
	"bufio"
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

// RunTarget executes the specified target
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

	cmd := exec.Command("java", "-jar", "-Xms2g", "-Xmx4g", "-XX:+UseG1GC", "spigot-1.8.8.jar", "nogui")
	c := make(chan struct{})

	go run(cmd, c)

	c <- struct{}{}
	cmd.Start()

	<-c

	if err := cmd.Wait(); err != nil {
		fmt.Println(err)
	}
}

func (d delete) exec() {
	fmt.Println("Removing the development server...")
	os.RemoveAll(vars.DevServerLocation)

	fmt.Println("Successfully removed.")
}

func run(cmd *exec.Cmd, c chan struct{}) {
	defer func() { c <- struct{}{} }()

	stdout, err := cmd.StdoutPipe()
	if err != nil {
		panic(err)
	}

	<-c

	scanner := bufio.NewScanner(stdout)
	for scanner.Scan() {
		m := scanner.Text()
		fmt.Println(m)
	}
}
