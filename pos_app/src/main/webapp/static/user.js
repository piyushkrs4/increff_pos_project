function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/users";
}

var currentUserEmail = ''
var supervisorEmail = ''

//BUTTON ACTIONS
function addUser(event){
	//Set the values to update
	var $form = $("#user-form");
	if(!validateForm($form))
	    return;
	var json = toJson($form);
	var url = getUserUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	        successMessage("User added successfully")
	   		getUserList();
	   		$('#add-user-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateUser(event){
	//Get the ID
	var id = $("#user-edit-form input[name=id]").val();
	var url = getUserUrl() + "/" + id;
	var $form = $("#user-edit-form");
    if(!validateForm($form)){
        return;
    }
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        successMessage("User updated successfully.")
	   		getUserList();
	   		$('#edit-user-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});

	return false;
}

function getUserList(){
	var url = getUserUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);   
	   },
	   error: handleAjaxError
	});
}




function deleteUser(id){
	var url = getUserUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	        successMessage("User deleted successfully")
	   		getUserList();
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayUserList(data){
    $('#user-table').DataTable().destroy();
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	var sno = 1;
	for(var i in data){
		var e = data[i];
		var buttonHtml;
		if(e.email == currentUserEmail) {
		    buttonHtml = ' <button class = "btn btn-warning mr-2" onclick="displayEditUser(' + e.id + ')"><i class="fa-solid fa-pen"></i></button>'
            buttonHtml += '<button class = "btn btn-danger" onclick="deleteUser(' + e.id + ')" disabled><i class="fa-solid fa-trash"></i></button>'
		}
		else if(e.email == supervisorEmail){
		    buttonHtml = ' <button class = "btn btn-warning mr-2" onclick="displayEditUser(' + e.id + ')" disabled><i class="fa-solid fa-pen"></i></button>'
            buttonHtml += '<button class = "btn btn-danger" onclick="deleteUser(' + e.id + ')" disabled><i class="fa-solid fa-trash"></i></button>'
		}
		else{
		    buttonHtml = ' <button class = "btn btn-warning mr-2" onclick="displayEditUser(' + e.id + ')"><i class="fa-solid fa-pen"></i></button>'
            buttonHtml += '<button class = "btn btn-danger" onclick="deleteUser(' + e.id + ')"><i class="fa-solid fa-trash"></i></button>'
		}
		var row = '<tr>'
		+ '<td>' + sno++ + '</td>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' + e.role + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	pagenation();
}

function pagenation(){
    $('#user-table').DataTable();
    $('.dataTables_length').addClass("bs-select");
}

function displayEditUser(id){
    var url = getUserUrl() + "/" + id;

    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
            displayUser(data)
       },
       error: handleAjaxError
    });
}

function displayUser(data){
	$("#user-edit-form input[name=email]").val(data.email);
	$("#user-edit-form select[name=role]").val(data.role);
	$("#user-edit-form input[name=id]").val(data.id);
	$("#user-edit-form input[name=password]").val(data.password);
	if(data.email == currentUserEmail)
	    document.getElementById('edit-modal-role').disabled=true;
	else
	    document.getElementById('edit-modal-role').disabled=false;
	$('#edit-user-modal').modal('toggle');
}

function addModal(){
    document.getElementById("user-form").reset()
    $('#add-user-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
    $('#add').click(addModal);
	$('#add-user').click(addUser);
	$('#refresh-data').click(getUserList);
	$('#update-user').click(updateUser);
	currentUserEmail = $("meta[name=userEmail]").attr("content")
    supervisorEmail = $("meta[name=supervisorEmail]").attr("content")
}

$(document).ready(init);
$(document).ready(getUserList);

