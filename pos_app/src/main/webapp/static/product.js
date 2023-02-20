function getSupervisorProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/products";
}

function getOperatorProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/operator/products";
}

function getOperatorBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/operator/brands";
}

var brandCategoryMap = new Map();

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
    if(!validateForm($form)){
        return;
    }
	var json = toJson($form);
	var url = getSupervisorProductUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	        successMessage("Product added successfully")
	   		getProductList();
	   		$('#add-product-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateProduct(event){
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getSupervisorProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
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
	        successMessage("Product updated successfully")
	        $('#edit-product-modal').modal('toggle');
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
    displayBrand()
	var url = getOperatorProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);

	   },
	   error: handleAjaxError
	});
}

function getBrandCategoryList(){
    var url = getOperatorBrandUrl();
    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
            storeBrandCategoryPair(data);
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
	var file = $('#productFile')[0].files[0];
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
    if(title.length!=5 || title[0]!='brand' || title[1]!='category' || title[2]!='barcode' || title[3]!='name' || title[4]!='mrp'){
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
            successMessage("Products uploaded successfully!")
        }
        getProductList();
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getSupervisorProductUrl();

	var rowObj = Object.keys(row);
    if(rowObj.length != 5){
        errorFlag = 1;
        row.error = "Column length must be 5!";
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
	   		row.error=response.responseJSON.message
	   		errorFlag = 1;
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayProductList(data){
    $('#product-table').DataTable().destroy();
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	var sno = 0;

	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class = "btn btn-warning" onclick="displayEditProduct(' + ++sno + ')"><i class="fa-solid fa-pen"></i></button>'
		var rowId = 'product-row-' + sno
		var row = '<tr id = "' + rowId + '">'
		+ '<td>' + sno + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.name + '</td>'
		+ '<td>' + e.mrp.toFixed(2) + '</td>'
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

function pagenation(){
    $('#product-table').DataTable();
    $('.dataTables_length').addClass("bs-select");
}

function storeBrandCategoryPair(data){
    for(var i in data){
        var e = data[i];
        if(brandCategoryMap.has(e.brand)){
            var category = brandCategoryMap.get(e.brand)
            category.push(e.category);
            brandCategoryMap.set(e.brand, category)
        }
        else
            brandCategoryMap.set(e.brand, [e.category])
    }
    displayBrand();
}

function displayBrand(){
    var $dropDownBrand = $('#inputBrand');
    var $editDropDown = $('#editBrand');
    $dropDownBrand.empty();
    $editDropDown.empty();
    $dropDownBrand.append('<option value="choose">Choose...</option>')
    for (let [key, value] of brandCategoryMap){
        var brandOption = '<option value="'+ key + '">' + key + '</option>'
        $dropDownBrand.append(brandOption);
        $editDropDown.append(brandOption);
    }
}

function getCategory(value){
    var $dropDownCategory = $('#inputBrandCategory')
    $dropDownCategory.empty();
    $dropDownCategory.append('<option value="choose">Choose...</option>')
    var category = brandCategoryMap.get(value)
    for(var i in category){
        var e = category[i]
        var categoryOption = '<option value="'+ e + '">' + e + '</option>'
        $dropDownCategory.append(categoryOption);
    }
}

function displayEditProduct(index){
    var row = document.getElementById('product-row-' + index)
    var data = {
        "brand": row.cells[1].innerHTML,
        "category": row.cells[2].innerHTML,
        "barcode": row.cells[3].innerHTML,
        "name": row.cells[4].innerHTML,
        "mrp": row.cells[5].innerHTML,
        "id": row.cells[6].innerHTML
    }
    displayProduct(data)
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	var ok = String(fileName).split(/(\\|\/)/g).pop();
    if(ok.split('.')[1]!="tsv"){
        errorMessage("Please select a tsv file!");
        return;
    }
    $('#productFileName').html(ok);
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
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}

function addModal(){
    document.getElementById("product-form").reset()
    document.getElementById("inputBrandCategory").options.length = 0;
    $('#inputBrandCategory').append('<option value="choose">Choose...</option>')
    $('#add-product-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
    $('#add').click(addModal);
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandCategoryList);

