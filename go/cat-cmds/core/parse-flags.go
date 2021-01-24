package core

import "flag"

func ParseFlags() (string, string) {
	jsonLoc := flag.String("path", "./plugin.json", "The path of the json file to parse the commands.")
	outLoc := flag.String("out", "./plugin.yml", "The output location.")

	flag.Parse()

	return *jsonLoc, *outLoc
}
