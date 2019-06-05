$( document ).ready(function() {

   $.ajaxSetup({cache: false});

   var url = window.location;

   $('#modalId').on('hidden.bs.modal', function () {
           $(".modal-body #greetingId").text('');
   });

   $("#nowBtnId").click(function(event){
        event.preventDefault();

        // Open Bootstrap Modal
        openModel();
        // Get data from Server
        ajaxGet("/rightNow");
   })


    $("#randomBtnId").click(function(event){
           event.preventDefault();

           // Open Bootstrap Modal
           openModel();
           // Get data from Server
           ajaxGet("/randomNames");
      })


       $("#fileBtnId").click(function(event){
              event.preventDefault();

              // Open Bootstrap Modal
              openModel();
              // Get data from Server
              ajaxGet("/whatsInTheFile");
         })

    // Open Bootstrap Modal
    function openModel(){
       $("#modalId").modal();
    }

    // DO GET
    function ajaxGet(url){
        $.ajax({
            type : "GET",
            url : url,
            success: function(data){
               // fill data to Modal Body
                fillData(data);
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
})