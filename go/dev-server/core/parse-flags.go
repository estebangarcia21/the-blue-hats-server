package core

import (
	"flag"
	"os"
)

// ParseFlags reads the flags that the user specifies
// Currently the only flag that exists is --working-directory=PATH
func ParseFlags() []string {
	subflags := os.Args[2:]

	targets := flag.NewFlagSet("targets", flag.ExitOnError)
	workingDirectory := targets.String("working-directory", ".", "The working directory of the development server.")

	targets.Parse(subflags)

	return []string{*workingDirectory}
}
