## Using `grpcurl`

To cast a vote

`grpcurl -plaintext -d '{"quoteId":"0"}' localhost:50052 org.bal.vote.proto.internal.VoteManagement/CastVote`


To get all votes

`grpcurl -plaintext  localhost:50052 org.bal.vote.proto.internal.VoteManagement/GetAllVotes`