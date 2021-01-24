package main

import "flag"

func main() {
	parseFlags()
}

func parseFlags() {
	jsonLoc := flag.String("path", ".", "The path of the json file to parse the commands.")
}
