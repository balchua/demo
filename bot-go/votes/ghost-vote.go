package votes

import (
	"fmt"
	"io/ioutil"
	"log"
	"math/rand"
	"net/http"
	"net/url"
	"strconv"

	"github.com/sandeep-mishra/demo/bot-go/util"
)

//CastGhostVote ...
func CastGhostVote() {
	fmt.Println("casting vote")
	var urlStr string = voteBaseURL + "/castVote"
	quoteId := strconv.Itoa(rand.Intn(16))
	strconv.Itoa(97)
	formData := url.Values{
		"quoteId": {quoteId},
	}
	makePostRequest(urlStr, formData)
}

//ListQuotes ...
func ListQuotes() {
	fmt.Println("listing quotes")
	var urlStr string = quoteBaseURL + "/list"
	makeGetRequest(urlStr)

}

//TallyVotes ...
func TallyVotes() {
	fmt.Println("tally votes'")
	var urlStr string = voteBaseURL + "/tallyVote"

	makeGetRequest(urlStr)

}

func makeGetRequest(url string) {
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

func makePostRequest(url string, formData url.Values) {

	resp, err := http.PostForm(url, formData)
	if err != nil {
		log.Fatalln(err)
	}

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Fatalln(err)
	}
	log.Println(string(body))

}

var voteBaseURL string
var quoteBaseURL string

func init() {
	voteBaseURL = "http://" + util.Cfg.Frontend.Host + ":" + util.Cfg.Frontend.Port + "/api/vote"
	quoteBaseURL = "http://" + util.Cfg.Frontend.Host + ":" + util.Cfg.Frontend.Port + "/api/quote"
}
