package main

import (
	"fmt"
	"strconv"
	"time"

	"github.com/sandeep-mishra/demo/bot-go/util"
	"github.com/sandeep-mishra/demo/bot-go/
)

var cfg util.Config

func init() {
	cfg = util.Cfg
}

func main() {
	fmt.Println("Go version of Bot starting...")
	var intCastVoteFixedRate int64
	intCastVoteFixedRate, err := strconv.ParseInt(cfg.Application.CastVoteFixedRate, 10, 64)

	if err != nil {
		util.ProcessError(err)
	}

	tickerCastVote := time.NewTicker(time.Duration(intCastVoteFixedRate) * time.Millisecond)
	go func() {
		for t := range tickerCastVote.C {
			_ = t // we don't print the ticker time, so assign this `t` variable to underscore `_` to avoid error
			votes.CastGhostVote()
		}
	}()

	var intListQuotesFixedRate int64
	intListQuotesFixedRate, err1 := strconv.ParseInt(cfg.Application.ListQuotesFixedRate, 10, 64)
	if err1 != nil {
		util.ProcessError(err1)
	}

	tickerListQuotes := time.NewTicker(time.Duration(intListQuotesFixedRate) * time.Millisecond)
	go func() {
		tickerListQuotes := time.NewTicker(time.Duration(intListQuotesFixedRate) * time.Millisecond)
		for t := range tickerListQuotes.C {
			_ = t // we don't print the ticker time, so assign this `t` variable to underscore `_` to avoid error
			votes.ListQuotes()
		}
	}()

	// wait for 10 seconds
	time.Sleep(20 * time.Second)
	tickerCastVote.Stop()
	tickerListQuotes.Stop()
}
