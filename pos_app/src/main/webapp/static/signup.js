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
	if(!validateForm($form))
	    return;
	var json = toJson($form);
	var url = getSignupUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	        successMessage("Account created successfully")
	   		window.location.href = getLoginUiUrl();
	   },
	   error: handleAjaxError
	});
	return false;
}




//INITIALIZATION CODE
function init(){
	$('#signup').click(addUser);
}

$(document).ready(init);

