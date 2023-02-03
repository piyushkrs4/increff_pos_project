function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/sales-report";
}

function getOperatorBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/operator/brands";
}

var brandCategoryMap = new Map();

//BUTTON ACTIONS
function filteredSalesReport(event){
	//Set the values to update
	$('#sales-report-modal').modal('toggle');
	var $form = $("#salesReport-form");
	var json = toJson($form);
	var url = getSalesReportUrl();
	var obj = JSON.parse(json)

    var startDate = obj.startDate;
    var endDate = obj.endDate;
    var date = new Date(startDate);
    var utc = new Date(date.getTime() + date.getTimezoneOffset() * 60000);
    obj.startDate = utc.toISOString().slice(0, -1);

    date = new Date(endDate);
    date.setDate(date.getDate() + 1)
    utc = new Date(date.getTime() + date.getTimezoneOffset() * 60000);
    obj.endDate = utc.toISOString().slice(0, -1);


	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(obj),
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		displaySalesReportList(response);
	   },
	   error: handleAjaxError
	});

	return false;
}


function getSalesReportList(){
	var url = getSalesReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displaySalesReportList(data);

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

//UI DISPLAY METHODS

function displaySalesReportList(data){
	var $tbody = $('#salesReport-table').find('tbody');
	$tbody.empty();
	var sno = 1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + sno + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>'  + e.revenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
        sno++;
	}
	pagenation();
}

function pagenation(){
    $('#salesReport-table').DataTable();
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
    $dropDownBrand.empty();
    var brandOption = '<option value="All">All</option>'
    $dropDownBrand.append(brandOption);
    for(let [key, value] of brandCategoryMap){
        brandOption = '<option value="'+ key + '">' + key + '</option>'
        $dropDownBrand.append(brandOption);
    }
}

function getCategory(value){
    var $dropDownCategory = $('#inputBrandCategory')
        $dropDownCategory.empty();
        var categoryOption = '<option value="All">All</option>';
        $dropDownCategory.append(categoryOption);
        var category = brandCategoryMap.get(value)
        for(var i in category){
            var e = category[i];
            categoryOption = '<option value="'+ e + '">' + e + '</option>'
            $dropDownCategory.append(categoryOption);
        }

}

function displayCategory(data){
    var $dropDownCategory = $('#inputBrandCategory')
    $dropDownCategory.empty();
    var categoryOption = '<option value="All">All</option>';
    $dropDownCategory.append(categoryOption);
    for(var i in data){
        var e = data[i];
        categoryOption = '<option value="'+ e + '">' + e + '</option>'
        $dropDownCategory.append(categoryOption);
    }
}

function addModal(){
    document.getElementById("salesReport-form").reset()
    $('#sales-report-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
    $('#add').click(addModal);
	$('#get-filteredSalesReport').click(filteredSalesReport);
	$('#all-data').click(getSalesReportList);
}

$(document).ready(init);
$(document).ready(getBrandCategoryList);

