package utils

import (
	"fmt"
	"os/exec"
)

// Curl is an alias for curl with the silent and output flags
func Curl(url string, outFile string) *exec.Cmd {
	return exec.Command("curl", "-s", "-L", url, "-o", outFile)
}

// ExecSafeCmd builds a comamnd from the specified options and catches errors and displays a generic error message
func ExecSafeCmd(name string, arg ...string) {
	err := exec.Command(name, arg...).Run()

	if err != nil {
		fmt.Println("An unhandled error has occured.")
		fmt.Printf("%s\n", err)
	}
}

// ExecSafeCmdWithOutput builds a comamnd from the specified options and catches errors and displays a generic error message
func ExecSafeCmdWithOutput(name string, arg ...string) {
	o, err := exec.Command(name, arg...).Output()

	if err != nil {
		fmt.Println("An unhandled error has occured.")
		fmt.Printf("%s\n", err)
	}

	fmt.Printf("Output: %s", o)
}

// HandleCmd catches errors and displays a generic error message
func HandleCmd(cmd *exec.Cmd) {
	err := cmd.Run()

	if err != nil {
		fmt.Println("An unhandled error has occured.")
		// fmt.Println(err.Error())
		return
	}

	// fmt.Println(output)
}
