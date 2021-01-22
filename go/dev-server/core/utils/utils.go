package utils

import (
	"fmt"
	"os/exec"
)

// Curl is an alias for curl with the silent and output flags
func Curl(url string, outFile string) *exec.Cmd {
	return exec.Command("curl", "-s", "-L", url, "-o", outFile)
}

// HandleCmd catches errors and displays a generic error message
func HandleCmd(cmd *exec.Cmd) {
	err := cmd.Run()

	if err != nil {
		fmt.Println("An unhandled error has occured.")
		fmt.Printf("%s\n", err)
	}
}
