package util

import (
	"fmt"
	"os"

	"github.com/kelseyhightower/envconfig"
	"gopkg.in/yaml.v2"
)

var Cfg Config

type Config struct {
	Application struct {
		CastVoteFixedRate   string `yaml:"castVoteFixedRate"`
		ListQuotesFixedRate string `yaml:"listQuotesFixedRate"`
		TallyVotesFixedRate string `yaml:"tallyVotesFixedRate"`
	} `yaml:"application"`

	Frontend struct {
		Port string `yaml:"port" envconfig:"FRONTEND_SERVER_PORT"`
		Host string `yaml:"host" envconfig:"FRONTEND_SERVER_HOST"`
	} `yaml:"frontend-server"`

	Zipkin struct {
		Port         string `yaml:"port" envconfig:"ZIPKIN_SERVER_PORT"`
		Host         string `yaml:"host" envconfig:"ZIPKIN_SERVER_HOST"`
		samplingRate string `yaml:"samplingRate"`
	} `yaml:"zipkin-server"`
}

func readFile(cfg *Config) {
	f, err := os.Open("config/application.yaml")
	if err != nil {
		ProcessError(err)
	}

	decoder := yaml.NewDecoder(f)
	err = decoder.Decode(cfg)
	if err != nil {
		ProcessError(err)
	}
}

func readEnv(cfg *Config) {
	err := envconfig.Process("", cfg)
	if err != nil {
		ProcessError(err)
	}
}

func init() {
	readFile(&Cfg)
	readEnv(&Cfg)
	fmt.Printf("From init %+v", Cfg)

}

func ProcessError(err error) {
	fmt.Println(err)
	os.Exit(2)
}
