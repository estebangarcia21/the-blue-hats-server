package core

type Data struct {
	Settings Settings
	Commands []Command
}

type Settings struct {
	Name string
	Main string
	Version string
}

type Command struct {
	Name string
	Description string
}
