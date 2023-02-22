function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/brand-report";
}


function getBrandList(){
	var url = getBrandReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayBrandList(data){
    $('#brandReport-table').DataTable().destroy();
	var $tbody = $('#brandReport-table').find('tbody');
	$tbody.empty();
	$tbody.empty();
	var sno = 0;

	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + ++sno + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	pagination();
}

var $fileName = 'Brand Report'

function pagination(){
    $('#brandReport-table').DataTable({
        dom: 'Bfrtip',
        buttons : [
            {
                extend: 'csv',
                title : 'Brand Report',
                filename: $fileName,
                text: '<i class="fa-solid fa-download"></i>',
            }
        ]
    });
    $('.dataTables_length').addClass("bs-select");
}

$(document).ready(getBrandList);

