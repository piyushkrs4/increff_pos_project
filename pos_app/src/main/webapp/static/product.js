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
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getSupervisorProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
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


function processData(){
	var file = $('#productFile')[0].files[0];
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
	    successMessage("Products uploaded successfully")
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getSupervisorProductUrl();

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
		+ '<td>' + e.mrp + '</td>'
		+ '<td style="display: none;">' + e.id + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
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
    for (let [key, value] of brandCategoryMap){
        var brandOption = '<option value="'+ key + '">' + key + '</option>'
        $dropDownBrand.append(brandOption);
        $editDropDown.append(brandOption);
    }
}

function getCategory(value){
    var $dropDownCategory = $('#inputBrandCategory')
    $dropDownCategory.empty();
    var category = brandCategoryMap.get(value)
    for(var i in category){
        var e = category[i]
        var categoryOption = '<option value="'+ e + '">' + e + '</option>'
        $dropDownCategory.append(categoryOption);
    }
}

function displayEditProduct(index){
//    var table = document.getElementById("product-table");
//    var firstRow = table.rows[index];
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
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
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
    console.log("clear and working")
    document.getElementById("product-form").reset()
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
    if($("meta[name=role]").attr("content") == "operator"){
        document.getElementById('admin-view').style.display = "none";
    }
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandCategoryList);

