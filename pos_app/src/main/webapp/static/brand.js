function getSupervisorBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/brands";
}

function getOperatorBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/operator/brands";
}

//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	if(!validateForm($form)){
	    return;
	}
	var json = toJson($form);
	var url = getSupervisorBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	        successMessage("Brand and category added successfully!")
	   		getBrandList();
	   		$('#add-brand-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});
	return false;
}

function updateBrand(event){
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();	
	var url = getSupervisorBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
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
	        successMessage("Brand and category updated successfully.")
	   		getBrandList();
	   		$('#edit-brand-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandList(){
    emptyForm()
	var url = getOperatorBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
	   },
	   error: handleAjaxError
	});
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;
var errorFlag = 0;


function processData(){
	var file = $('#brandFile')[0].files[0];
	document.getElementById("process-data").disabled = true;
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	if(fileData.length === 0){
	    errorMessage("File is empty!")
	    return;
	}
	var row = fileData[0];
	var title = Object.keys(row);
    if(title.length!=2 || title[0]!='brand' || title[1]!='category'){
        errorMessage("Incorrect tsv format please check the sample file!");
        return;
    }
	uploadRows();
}


function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	    if(errorFlag){
	        warningMessage("There was error in uploading some data!")
	        document.getElementById("download-errors").disabled = false;
	    }
	    else{
	        successMessage("Brands and categories uploaded successfully!")
	    }
	    getBrandList();
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getSupervisorBrandUrl();

    var rowObj = Object.keys(row);
	if(rowObj.length != 2){
        errorFlag = 1;
        row.error = "Column length must be 2!";
        errorData.push(row);
        uploadRows();
        return;
    }

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
            errorFlag = 1;
	   		row.error=response.responseJSON.message
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBrandList(data){
    $('#brand-table').DataTable().destroy();
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	var sno = 0;

	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class = "btn btn-warning" onclick="displayEditBrand(' + ++sno + ')"><i class="fa-solid fa-pen"></i></button>'
		var rowId = 'brand-row-' + sno
		var row = '<tr id = "' + rowId + '">'
		+ '<td>' + sno + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td style="display: none;">' + e.id + '</td>'
        + '<td class = "supervisor-view">' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	if($("meta[name=role]").attr("content") == "operator"){
        var elements = document.getElementsByClassName('supervisor-view');

        for (var i = 0; i < elements.length; i ++) {
            elements[i].style.display = 'none';
        }
    }
	pagenation();
}

function displayEditBrand(index){
    var row = document.getElementById('brand-row-' + index)
    var data = {
        "brand": row.cells[1].innerHTML,
        "category": row.cells[2].innerHTML,
        "id": row.cells[3].innerHTML
    }
    displayBrand(data)
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	errorFlag = 0;
	//Update counts
	updateUploadDialog();
 	document.getElementById("process-data").disabled = true;
 	document.getElementById("download-errors").disabled = true;
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	var ok = String(fileName).split(/(\\|\/)/g).pop();
    if(ok.split('.')[1]!="tsv"){
        errorMessage("Please select a tsv file!");
        return;
    }
	$('#brandFileName').html(ok);
	$('#rowCount').html("0");
	$('#processCount').html("0");
	$('#errorCount').html("0");
	processCount = 0;
	rowCount = 0;
	errorCount = 0;
	errorFlag = 0;
	errorData = [];
	document.getElementById("download-errors").disabled = true;
 	document.getElementById("process-data").disabled = false;
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);	
	$('#edit-brand-modal').modal('toggle');
}

function emptyForm(){
    $("#brand-form input[name=brand]").val("");
    $("#brand-form input[name=category]").val("");
}

function pagenation(){
    $('#brand-table').DataTable();
    $('.dataTables_length').addClass("bs-select");
}

function addModal(){
    document.getElementById("brand-form").reset()
    $('#add-brand-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
    $('#add').click(addModal);
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getBrandList);

