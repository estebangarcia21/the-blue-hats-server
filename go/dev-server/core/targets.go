package core

import (
	"bufio"
	"fmt"
	"io"
	"log"
	"os"
	"os/exec"
	"strings"

	"dev-server/core/functions"
	"dev-server/core/utils"
	"dev-server/vars"
)

var (
	defaultTarget     = "clean"
	targets           = map[string]target{"new": newTarget{}, "start": startTarget{}, "delete": deleteTarget{}, "update": updateTarget{}}
	serverTemplateURL = "https://www.dropbox.com/s/ubzaencsai8fmtk/tbhs-dev-server.tar.gz?dl=1"
)

// RunTarget runs the specified target. If an invalid target is called, the default target is called.
func RunTarget(target string, workingDir string) {
	if t, ok := targets[target]; ok {
		_ = os.Chdir(workingDir)
		t.exec()
	} else {
		fmt.Printf("Invalid target called, executing the default target: %s\n.", defaultTarget)
		targets[defaultTarget].exec()
	}
}

type target interface {
	exec()
}

type newTarget struct{}
type startTarget struct{}
type deleteTarget struct{}
type updateTarget struct{}

func (n newTarget) exec() {
	if _, err := os.Stat(vars.DevServerLocation); !os.IsNotExist(err) {
		fmt.Println("The development server already exists.")
		return
	}

	serverTarball := "tbhs-dev-server.tar.gz"

	_ = os.Mkdir(vars.DevServerLocation, 0755)
	_ = os.Chdir(vars.DevServerLocation)

	fmt.Println("Downloading the server files...")
	utils.HandleCmd(utils.Curl(serverTemplateURL, serverTarball))

	fmt.Println("Unzipping...")
	utils.ExecSafeCmd("tar", "--strip-components=1", "-xf", serverTarball)
	_ = os.Remove(serverTarball)

	_ = os.Chdir("..")

	functions.BuildServerJar()
	functions.UpdateSpigotJar()

	fmt.Println(`
    Done! The development server was successfully created!

    To run the server, do ./dev-server startTarget
    Otherwise, to deleteTarget the server do ./dev-server deleteTarget
	`)
}

func (s startTarget) exec() {
	functions.BuildServerJar()

	_ = os.Chdir(vars.DevServerLocation)

	fmt.Println("Starting the server...")
	cmd := exec.Command("java", "-jar", "-Xms2g", "-Xmx4g", "-XX:+UseG1GC", "spigot-1.8.8.jar", "nogui")

	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	stdin, _ := cmd.StdinPipe()

	_ = cmd.Start()

	readStdin(&stdin)

	_ = cmd.Wait()
}

func (d deleteTarget) exec() {
	fmt.Println("Removing the development server...")
	_ = os.RemoveAll(vars.DevServerLocation)

	fmt.Println("Successfully removed.")
}

func (u updateTarget) exec() {
	functions.UpdateSpigotJar()
}

func readStdin(buffer *io.WriteCloser) {
	input := make(chan string)

	go func(in chan string) {
		reader := bufio.NewReader(os.Stdin)

		for {
			s, err := reader.ReadString('\n')
			if err != nil {
				close(in)
				log.Println("An error occurred with the input string.", err)
			}

			in <- s
		}
	}(input)
exit:
	for {
		select {
		case in := <-input:
			in = strings.TrimSpace(in)
			_, _ = (*buffer).Write([]byte(in + "\n"))

			if in == "stop" {
				break exit
			}
		}
	}
}
