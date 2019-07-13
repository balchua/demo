$( document ).ready(function() {

   $.ajaxSetup({cache: false});

   var url = window.location;

   $(document).on("click",'.castVote', function(){
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
       var index = 0;
       const cardClassMap = new Map();
       cardClassMap.set(0, 'card-header-success');
       cardClassMap.set(1, 'card-header-warning');
       $.each(data, function(i, obj) {
           console.log(cardClassMap.get(index%2));
            $("#quotes-container").append(
            "<div class='col-md-3'>" +
                 "<div class='card card-nav-tabs'>" +
                    "<div class='card-header " + cardClassMap.get(index%2) + "'>" +
                        "Quote" +
                     "</div>" +
                     "<div class='card-body'>" +
                       "<blockquote class='blockquote mb-0'>" +
                           "<p>" + obj.quote + "</p>" +
                           "<footer class='blockquote-footer'>" + obj.name + "</footer>" +
                       "</blockquote>" +
                       "<button class='btn castVote' id='"  + obj.quoteId + "'>" +
                         "<i style='cursor:pointer' class='material-icons'>thumb_up</i>" +
                       "</button>" +
                     "</div>" +
                  "</div>" +
            "</div>"
            );
           index++;
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
            data: { "quoteId": quoteId },
            dataType: 'text',
            success: function(data){
                fillData(data);
                openModel();
             },
             error : function(e) {
                fillData('BOOM!');
                openModel();
             }
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