function getSignupUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/session/signup";
}

function getLoginUiUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/site/login";
}

//BUTTON ACTIONS
function addUser(event){
	//Set the values to update
	var $form = $("#signup-form");
	var json = toJson($form);
	var url = getSignupUrl();
	console.log(url);
	console.log(getLoginUiUrl());
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	        successMessage("Account created successfully")
//	   		alert("account created");
	   		window.location.href = getLoginUiUrl();
	   },
	   error: handleAjaxError
	});
	console.log("sdt")
	return false;
}


function emptyForm(){
    $("#brand-form input[name=email]").val("");
    $("#brand-form input[name=password]").val("");
}


//INITIALIZATION CODE
function init(){
	$('#signup').click(addUser);
}

$(document).ready(init);

