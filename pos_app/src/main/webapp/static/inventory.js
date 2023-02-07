function getSupervisorInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/inventories";
}

function getOperatorInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/operator/inventories";
}

//BUTTON ACTIONS
function addInventory(event){
	//Set the values to update
	var $form = $("#inventory-form");
	if(!validateForm($form))
	    return;
	var json = toJson($form);
	var url = getSupervisorInventoryUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	        successMessage("Inventory added successfully!")
	   		getInventoryList();
	   		$('#add-inventory-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();	
	var url = getSupervisorInventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	        successMessage("Inventory updated successfully!")
	   		getInventoryList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getInventoryList(){
    emptyForm()
	var url = getOperatorInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);  
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#inventoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	    successMessage("Brands and categories uploaded successfully!")
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getSupervisorInventoryUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayInventoryList(data){
    $('#inventory-table').DataTable().destroy();
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	var sno = 0;

	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class = "btn btn-warning" onclick="displayEditInventory(' + ++sno + ')"><i class="fa-solid fa-pen"></i></button>'
		var rowId = 'inventory-row-' + sno
        var row = '<tr id = "' + rowId + '">'
		+ '<td>' + sno + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.productName + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td style="display: none;">' + e.id + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	pagenation();
}

function pagenation(){
    $('#inventory-table').DataTable();
    $('.dataTables_length').addClass("bs-select");
}

function displayEditInventory(index){
    var row = document.getElementById('inventory-row-' + index)
    var data = {
        "barcode": row.cells[1].innerHTML,
        "productName": row.cells[2].innerHTML,
        "quantity": row.cells[3].innerHTML,
        "id": row.cells[4].innerHTML
    }
    displayInventory(data)
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=productName]").val(data.productName);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$("#inventory-edit-form input[name=id]").val(data.id);
	$('#edit-inventory-modal').modal('toggle');
}

function emptyForm(){
    $("#inventory-form input[name=barcode]").val("");
    $("#inventory-form input[name=quantity]").val("");
}

function addModal(){
    document.getElementById("inventory-form").reset()
    $('#add-inventory-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
    $('#add').click(addModal);
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName)
    if($("meta[name=role]").attr("content") == "operator"){
        document.getElementById('admin-view').style.display = "none";
    }
}

$(document).ready(init);
$(document).ready(getInventoryList);

