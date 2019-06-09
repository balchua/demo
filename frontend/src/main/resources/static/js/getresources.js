$( document ).ready(function() {

   $.ajaxSetup({cache: false});

   var url = window.location;


   $(document).on("click", ".castVote", function(){
       var quoteId = $(this).attr('id');
       console.log(quoteId);
       castVote(quoteId);
   });

   $('#modalId').on('hidden.bs.modal', function () {
           $(".modal-body #greetingId").text('');
   });

   $("#nowBtnId").click(function(event){
        event.preventDefault();

        getVotes(getVoteResults);
        // Open Bootstrap Modal
        showTally();

   })

   function getVoteResults(data) {
       $("#votes-table tbody").empty();
       $.each(data, function(i, obj) {
            $("#votes-table tbody").append(
                "<tr>" +
                   "<td>"+ obj.quote +"</td>" +
                   "<td>"+ obj.count +"</td>" +
                "</tr>"
            );
        });
    }

   function showQuotes(data) {
        $.each(data, function(i, obj) {
            $("#quotes-table tbody").append(
                "<tr><td class='castVote' id='" + obj.quoteId +"'> <i class='fa fa-thumbs-up' aria-hidden='true' ></i> </td>" +
                   "<td>"+ obj.quote +"</td>" +
                "</tr>"
            );
        });
    }


    // Open Bootstrap Modal
    function openModel(){
       $("#modalId").modal();
    }

    // Open Bootstrap Modal
    function showTally(){
       $("#voteTally").modal();
    }

    // DO GET
    function getQuotes(url, callback){
        $.ajax({
            type : "GET",
            url : url,
            success: function(data){
               // fill data to Modal Body
                callback(data);
            },
            error : function(e) {
               fillData(null);
            }
        });
    }


    function castVote(quoteId, callback){
        $.ajax({
            type : "POST",
            url : "/api/vote/castVote",
            data: { "quoteId": quoteId }
        }).done(function(data){
           alert(data);
        }).fail(function(data){
           fillData(data.responseText);
           openModel();
        });

    }

    // DO GET
    function getVotes(callback){
        $.ajax({
            type : "GET",
            url : "/api/vote/tally",
            success: function(data){
               // fill data to Modal Body
                callback(data);
            },
            error : function(e) {
               fillData(null);
            }
        });
    }

    function fillData(data){
       if(data!=null){
            $(".modal-body #greetingId").text(data);
       }else{
            $(".modal-body #greetingId").text("Can Not Get Data from Server!");
       }
    }

    getQuotes("/api/quote/list", showQuotes);


})