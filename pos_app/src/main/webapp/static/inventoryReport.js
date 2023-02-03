function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/inventory-report";
}

function getInventoryList(){
	var url = getInventoryReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: handleAjaxError
	});
}

async function downloadCSV() {
    var url = getInventoryReportUrl() + "/download"
    const response = await fetch(url);
    const data = await response.text();

    const link = document.createElement('a');
    link.setAttribute('href', 'data:text/csv;charset=utf-8,' + encodeURIComponent(data));
    link.setAttribute('download', 'your-file-name.csv');
    link.style.display = 'none';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}


//UI DISPLAY METHODS

function displayInventoryList(data){
    $('#inventoryReport-table').DataTable().destroy();
	var $tbody = $('#inventoryReport-table').find('tbody');
	$tbody.empty();
	var sno = 1
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + sno++ + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	pagenation();
}

//function downloadCSV() {
//    var url = getInventoryReportUrl() + "/download"
//    $.ajax({
//        type: "GET",
//        url: url,
//        success: function (data) {
//            console.log("CSV download success");
//        },
//        error: function (error) {
//            console.log("CSV download failed: ", error);
//        },
//        dataType: "text/csv"
//    });
//}



function pagenation(){
    $('#inventoryReport-table').DataTable();
    $('.dataTables_length').addClass("bs-select");
}

$(document).ready(getInventoryList);

