package util

import (
	"fmt"
	"os"

	"github.com/spf13/viper"
)

//Cfg Structure to hold application configuration
var Cfg Config

//Config template for application configuration
type Config struct {
	Application struct {
		CastVoteFixedRate   string `mapstructure:"castVoteFixedRate"`
		ListQuotesFixedRate string `mapstructure:"listQuotesFixedRate"`
		TallyVotesFixedRate string `mapstructure:"tallyVotesFixedRate"`
	} `mapstructure:"application"`

	Frontend struct {
		Port string `mapstructure:"port" envconfig:"FRONTEND_SERVER_PORT"`
		Host string `mapstructure:"host" envconfig:"FRONTEND_SERVER_HOST"`
	} `mapstructure:"frontend-server"`

	Zipkin struct {
		Port         string `mapstructure:"port" envconfig:"ZIPKIN_SERVER_PORT"`
		Host         string `mapstructure:"host" envconfig:"ZIPKIN_SERVER_HOST"`
		samplingRate string `mapstructure:"samplingRate"`
	} `mapstructure:"zipkin-server"`
}

func init() {
	viper.SetConfigName("application") // name of config file (without extension)
	viper.SetConfigType("yaml")
	viper.AddConfigPath("$KO_DATA_PATH/")
	viper.AddConfigPath(".")
	err := viper.ReadInConfig() // Find and read the config file
	if err != nil {             // Handle errors reading the config file
		panic(fmt.Errorf("fatal error config file: %s", err))
	}

	viper.Unmarshal(&Cfg)

	fmt.Printf("From init %+v  ", Cfg)

}

//ProcessError function to process and error
func ProcessError(err error) {
	fmt.Println(err)
	os.Exit(2)
}
