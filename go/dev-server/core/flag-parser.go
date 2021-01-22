package core

import (
	"flag"
)

// ParseFlags parses the flags for the dev-server
func ParseFlags() bool {
	force := flag.Bool("force", false, "Forces a rebuild of the server.")

	return *force
}
