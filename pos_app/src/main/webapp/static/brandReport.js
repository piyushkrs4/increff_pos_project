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
	console.log($tbody)
	pagination();
}

//async function downloadCSV() {
//    var url = getBrandReportUrl() + "/download"
//    const response = await fetch(url);
//    const data = await response.text();
//
//    const link = document.createElement('a');
//    link.setAttribute('href', 'data:text/csv;charset=utf-8,' + encodeURIComponent(data));
//    link.setAttribute('download', 'brand-report.csv');
//    link.style.display = 'none';
//
//    document.body.appendChild(link);
//    link.click();
//    document.body.removeChild(link);
//}

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

