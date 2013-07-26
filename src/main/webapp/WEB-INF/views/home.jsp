<html>
<style type="text/css">
body {
    font-family: Arial, Helvetica;
    }
#outer {
    background-color:#aaa;
    position:absolute;
    top:0px;
    left:0px;
    height:100%;
    width:100%; 
}

#inner {
    position:relative;
    margin-left:auto;
    margin-right:auto;
    margin-top:10px;
    width:800px;
    height:700px;
    background-color:#eee;
    border:1px solid #666;
    box-shadow: .1em .1em .2em #666;
    padding-left:50px;
    padding-right:50px;
}
#inner h2 {

    text-align:center;
}
#nameEntry, #addressEntry, #phoneEntry, #emailEntry, #lastPresentEntry, #dobEntry {
    font-weight:bold;
    position:relative;
    left:50px;
    margin-top:30px;
}
#nameField, #addressField, #phoneField, #emailField, #lastPresentField, #dobField {
    position:relative;
    left:100px;
    top:-20px;
    width:300px;
}
#listEntry{
    position:absolute;
    right:50px;
    top:90px;
    width:300px;
}
#addressList {
    background-color:white;
    width:300px;
    height:250px;
}
#saveEntry{
    position:absolute;
    bottom:50px;
    left:350px;
}
#saveButton,#deleteButton{
    width:100px;
}
#deleteEntry{
    position:absolute;
    bottom:50px;
    right:50px;
}

#friendListBox{

}

#friendList{
	
}

#suggestedPresentBox {
  position: absolute;
  bottom: 120px;
  right: 50px;
}


</style>
<head>
    <title>Granny's Addressbook</title>

<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"> </script>

</head>
<body>
<div id="outer">
<div id="inner">
<h2>
    Granny's Addressbook
</h2>

<div id="content">
    <form id="addressForm" name="addressForm">
        <input id="selectedIndexField" name="selectedIndex" type="hidden" value=""/>
        <input id="oldNameField" name="oldName" type="hidden" value=""/>
        <div id="nameEntry">
            <div id="nameLabel" class="label">Name</div><input id="nameField" name="name" type="text" value=""/>
        </div>
        <div id="addressEntry">
            <div id="addressLabel" class="label">Address</div><input id="addressField" name="address" type="text" value=""/>
        </div>  
        <div id="phoneEntry">
            <div id="phoneLabel" class="label">Phone</div><input id="phoneField" name="phone" type="text" value=""/>
        </div>   
        
        <div id="emailEntry">
            <div id="emailLabel" class="label">Email</div><input id="emailField" name="email" type="text" value=""/>
        </div>
        
		<div id="dobEntry">
            <div id="dobLabel" class="label">DOB</div><input id="dobField" name="dob" type="text" value=""/>
        </div>
        
        
        <div id="lastPresentEntry">
            <div id="lastPresentLabel" class="label">Last Present</div><input id="lastPresentField" name="lastPresent" type="text" value=""/>
        </div>
        
        <!-- a box to select friends -->
        <p>
        	<strong>Friends:</strong>
        </p>
        <div id="friendListBox">
        	<select size="4" id="friendsList" name="friendsList">                  
             </select>
        </div>
        <p />

        <!--  a drop down of "candidate" friends (every user in the system who isn't me and who isn't already my friend -->
        <div id="candidateFriendBox">
        	<select size="1" id="candidateFriendsList" name="candidateFriendsList">     
        	</select>
                
            <!--  and a button to make an AJAX call to add the new friend association -->
            <input id="addFriendButton" value="Add to Friends" type="button" onClick="app.addFriend()" />
            
        </div>
        
        <!-- a box to list suggestions -->

        <div id="suggestedPresentBox">
        	<p>
        		<strong>Suggested Presents:</strong>
        	</p>
        	
        	<select size="4" id="suggestedPresentList" name="suggestedPresentList">                  
            </select>
        </div>
        <p />


        
        
        <div id="saveEntry">
            <input id="saveButton" value="Save" type="button" onClick="app.saveAddress()"/>
        </div>  
        <div id="listEntry"> 
            <div id="list">
                <select size="10" id="addressList" name="addressList">                  
                </select>
            </div>
        </div>
        <div id="deleteEntry">
            <input id="deleteButton" value="Delete" onClick="app.deleteAddress()" type="button"/>
        </div>  
    </form>

</div><!-- end content-->
</div><!-- end inner -->
</div><!-- end outer -->
<script type="text/javascript">
//create an object to store our functions and variables

var app = {
     //populate the list box
    getAllAddresses: function () {
        $.getJSON("addresses", function(data){
            // data = data.data;
            
            var entries = [];
            entries.push("<option value='' />");
            $.each(data, function(){
            	entries.push("<option name='"+ this.name + "' value='"+ this.uuid+"'>"+ this.name +"</option>");
            });
            
            $("#addressList").empty();
            $(entries.join("")).appendTo("#addressList");
        });
        
    },
     //save addresses
    saveAddress: function(){
        var address = app.makeAddress();
        if(address.uuid === "" || address.uuid.length <1) { app.createAddress(address); return;}
        $.ajax({
            url:"update",
            type:"put",
            contentType:"application/json",
            processData: false,
            data: JSON.stringify(address),
            success: function() { 
            	
            	app.populateFields(app.emptyUser);
            	app.getAllAddresses(); }
        });
        
    },
    createAddress: function(address) {
//          delete address._id;
          $.ajax({
                  url:"create",
                  type:"post",
                  contentType:"application/json",
                  processData: false,
                  data: JSON.stringify(address),
                  success: function() { 
                	  app.populateFields(app.emptyUser);
                	  app.getAllAddresses(); },
                	  
                  error: function( one, two, three ) { alert( "createAddress error: " + three); }
           });
    },
    makeAddress: function(){
        var addressObject = {
        	uuid: $("#selectedIndexField").attr("value"),
        	name: $("#nameField").attr("value"),
            address: $("#addressField").attr("value"),
            phone: $("#phoneField").attr("value"),
            email: $("#emailField").attr("value"),
            dob: $("#dobField").attr("value"),
            last_present: $("#lastPresentField").attr("value")
        };
        return addressObject;
     }, 
    emptyUser: {
    		uuid:"",
            name: "",
            address: "",
            phone: "",
            email: "",
            dob: "",
            last_present: ""
    },
    getAddressById: function (id){
        if(id){
             $.ajax({
                 url:"get/"+id,
                 type:"get",
                 success: function(data) { app.populateFields(data); },
                 error: function( one, two,three ) { alert( "getAddressById error: " + three ); }
             });
        }
        else{
            app.populateFields(app.emptyUser);
            
        }
    },
    
    populateCandidateFriends: function( cFriends )
    {
		// alert( "about to populate candidate friends" );
    	var entries = [];
    	entries.push("<option value='' />");
		$.each( cFriends, function() {
			
                entries.push("<option name='"+ this.name + "' value='"+ this.uuid+"'>"+ this.name +"</option>");
		} );         
        
		$("#candidateFriendsList").empty();
        $(entries.join("")).appendTo("#candidateFriendsList");
    		
    	
    },

    populateFriendsList: function( friends )
    {
		// alert( "about to populate friends" );
    	var entries = [];
    	// entries.push("<option value='' />");
		$.each( friends, function() {
			
                entries.push("<option name='"+ this.name + "' value='"+ this.uuid+"'>"+ this.name +"</option>");
		} );         
        
		$("#friendsList").empty();
        $(entries.join("")).appendTo("#friendsList");
    		
    	
    },
    
    populateSuggestionsList: function( suggestions )
    {
    	// alert( "here" );
    	var entries = [];
    	// entries.push("<option value='' />");
		$.each( suggestions, function() {
		    // alert( "suggestion: " + this );	
			entries.push("<option name='"+ this + "' value='"+ this+"'>"+ this +"</option>");
		} );         
        
		$("#suggestedPresentList").empty();
        $(entries.join("")).appendTo("#suggestedPresentList");
    },
    
    
    
    addFriend: function()
    {
    	var selectedOption = $("#candidateFriendsList option:selected");
    	var personId = $("#selectedIndexField").val();
    	var newFriendId = selectedOption.val();
    	
    	// alert( "for user: " + personId + ", new friend is: " + newFriendId );		
    	alert( "personId: " + personId + ", newFriendId: " + newFriendId );
    	
    	$.ajax( { 
    		
    		url: "addFriends/" + personId + "/" + newFriendId,
    		type: "post",
    		success: function(data) { app.populateFields(data); },
    		error: function(jqXHR, textStatus, errorThrown) { alert( "addFriend: error: " + errorThrown );}
    	});
    
    },
    
    populateFields: function(addressJSON) {
    	
    	// alert( "here" );
        $("#nameField").attr("value", addressJSON.name);
        $("#addressField").attr("value", addressJSON.address);
        $("#phoneField").attr("value", addressJSON.phone);
        $("#emailField").attr("value", addressJSON.email);
        $("#selectedIndexField").val(addressJSON.uuid || "");
        $("#lastPresentField").val( addressJSON.last_present);
        $("#dobField").val( addressJSON.dob);

        // populate candidate friends list
        $.ajax({
                 url:"findCandidateFriends/"+addressJSON.uuid,
                 type:"get",
                 success: function(data) { app.populateCandidateFriends(data); }
             });
        
        // if there are friends, show them in the #friendList
        $.ajax( {
        	url: "findFriends/" + addressJSON.uuid,
        	type: "get",
        	success: function(data) { app.populateFriendsList(data); app.populateSuggestions(addressJSON.uuid); }
            // error: function( one, two, three ) { alert( three ); }
        });

    },
    
    populateSuggestions: function(id) {
    	
    	// empty out any leftover values
    	$("#suggestedPresentList").empty();
    	
        // populate suggested gifts box if criteria are met
        var dob = $("#dobField").val();
        var lastPresent = $("#lastPresentField").val();
        var numFriends = $("#friendsList option").length;
        
        if( !dob || !lastPresent || ( numFriends < 1 ) )
        {
        	// alert( "NOT getting suggestions!");
        	return;
        }
        else
        {
        	// alert( "Asking for suggestions!");
        	$.ajax( {
        		
            	url: "findSuggestions/" + id,
            	type: "get",
            	success: function(data) { app.populateSuggestionsList(data); },
            	error: function(one, two, three) { alert( "populateSuggestions error: " + three);}
        	});
        }
    	
    	
    },
    
    deleteAddress: function(){
        var address = app.makeAddress();
     // var currentName = $("#nameField").attr("value");
        $.ajax({
            url:"delete",
            type:"delete",
            contentType:"application/json",
            data: JSON.stringify(address),
            success: function() {
            	
            	$("#candidateFriendsList").empty();
            	$("#friendsList").empty(); 
            	$("#suggestedPresentList").empty();
            	app.getAllAddresses();
                app.populateFields(app.emptyUser);
            },
            error: function( one, two, three ) { alert( "deleteAddress error: " + three ); }
        });
    }


};

//after the page loads
$(function () {
    app.getAllAddresses();
    $("#addressList").on("click","option", function(){ app.getAddressById(this.value); });
});
</script>
</body>
</html>

