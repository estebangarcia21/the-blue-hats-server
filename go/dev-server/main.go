package main

import (
	"dev-server/core"
	"os"
)

func main() {
	target := os.Args[1]

	core.RunTarget(target)
}
