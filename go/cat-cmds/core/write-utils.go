package core

import (
	"log"
	"os"
)

// StartScope writes a left curly brace
func StartScope(f *os.File) {
	if _, err := f.WriteString("{"); err != nil {
		log.Println(err)
	}
}

// EndScope writes a right curly brace
func EndScope(f *os.File) {
	if _, err := f.WriteString("}"); err != nil {
		log.Println(err)
	}
}

// Write writes a string to the file proceeding with a comma
func Write(f *os.File, s string) {
	if _, err := f.WriteString(s + ", "); err != nil {
		log.Println(err)
	}
}

// Write writes a string without a trailing comma
func WriteNoComma(f *os.File, line string) {
	if _, err := f.WriteString(line); err != nil {
		log.Println(err)
	}
}
