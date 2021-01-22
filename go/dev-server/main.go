package main

import (
	"dev-server/core"
	"fmt"
	"os"
)

func main() {
	args := os.Args

	if len(args) != 2 {
		fmt.Println(`
     THE BLUE HATS DEVELOPMENT SERVER by Stevemmmmm

     Command       Description
     ----------    ------------------------------------------------------------
     new           Creates a new directory with the development server.

     start         Starts the development server with the latest plugin build.

     delete        Deletes the development server.

     update        Updates the Spigot jar to the latest version. Run this if
                   Spigot warns that the jar is out of date.
     ---------     ------------------------------------------------------------

     SPIGOT VERSION: 1.8.8
     ACCEPTABLE LOGIN VERSIONS: 1.8.0 - 1.8.9

     Commands are case sensitive.
		`)

		return
	}

	target := os.Args[1]

	core.RunTarget(target)
}
