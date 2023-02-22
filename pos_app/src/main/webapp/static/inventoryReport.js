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
	pagination();
}


var $fileName = 'Inventory Report'

function pagination(){
    $('#inventoryReport-table').DataTable({
        dom: 'Bfrtip',
        buttons : [
            {
                extend: 'csv',
                title : 'Inventory Report',
                filename: $fileName,
                text: '<i class="fa-solid fa-download"></i>',
            }

        ]
    });
    $('.dataTables_length').addClass("bs-select");
}

$(document).ready(getInventoryList);

