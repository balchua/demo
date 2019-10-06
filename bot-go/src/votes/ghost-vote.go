package votes

import (
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"util"
)

func CastGhostVote() {
	fmt.Println("casting vote")
	var url string = vote_base_url + "/castVote"
	makeRequest(url)

}

func ListQuotes() {
	fmt.Println("listing quotes")
	var url string = quote_base_url + "/list"
	makeRequest(url)

}

func TallyVotes() {
	fmt.Println("tally votes'")
	var url string = vote_base_url + "/tallyVote"
	makeRequest(url)
}

func makeRequest(url string) {
	resp, err := http.Get(url)
	if err != nil {
		log.Fatalln(err)
	}

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Fatalln(err)
	}
	log.Println(string(body))
}

var vote_base_url string
var quote_base_url string

func init() {
	vote_base_url = "http://" + util.Cfg.Frontend.Host + ":" + util.Cfg.Frontend.Port + "/api/vote"
	quote_base_url = "http://" + util.Cfg.Frontend.Host + ":" + util.Cfg.Frontend.Port + "/api/quote"
}
