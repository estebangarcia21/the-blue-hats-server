package main

import (
	"cat-cmds/core"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"os"
)

func main() {
	jsonLoc, outFile := core.ParseFlags()

	data := parseJson(jsonLoc)

	_ = os.Remove(outFile)

	f, err := os.OpenFile(outFile, os.O_APPEND | os.O_CREATE | os.O_WRONLY, 0644)
	if err != nil {
		fmt.Println(err)
	}
	defer f.Close()

	settings := data.Settings

	core.StartScope(f)

	core.Write(f, "name: " + settings.Name)
	core.Write(f, "main: " + settings.Name)
	core.Write(f, "version: " + settings.Version)
	core.WriteNoComma(f, "commands: ")

	core.StartScope(f)

	for i := 0; i < len(data.Commands); i++ {
		d := data.Commands[i]

		core.WriteNoComma(f, d.Name + ": ")
		core.StartScope(f)
		core.Write(f, "description: " + d.Description)
		core.WriteNoComma(f, "usage: " + d.Name)
		core.EndScope(f)
		core.Write(f, "")
	}

	core.EndScope(f)
	core.EndScope(f)
}

func parseJson(path string) core.Data {
	f, err := ioutil.ReadFile(path)
	if err != nil {
		log.Println(err)
	}

	data := core.Data{}

	_ = json.Unmarshal(f, &data)

	return data
}
