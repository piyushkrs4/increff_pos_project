function getDailyReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/daily-report";
}

//BUTTON ACTIONS
function filteredDailyReport(event){
	//Set the values to update
	var $form = $("#dailyReport-form");
	if(!validateForm($form))
	    return;
	var json = toJson($form);
	var url = getDailyReportUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		displayDailyReportList(response);
	   		$('#daily-report-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});

	return false;
}


function getDailyReportList(){
	var url = getDailyReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayDailyReportList(data);  
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayDailyReportList(data){
    $('#dailyReport-table').DataTable().destroy();
	var $tbody = $('#dailyReport-table').find('tbody');
	$tbody.empty();
	var sno = 1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + sno + '</td>'
		+ '<td>' + e.date + '</td>'
		+ '<td>'  + e.totalOrders + '</td>'
		+ '<td>'  + e.totalItems + '</td>'
		+ '<td>'  + e.totalRevenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
        sno++;
	}
	pagination();
}

//async function downloadCSV() {
//    var url = getDailyReportUrl() + "/download"
//    const response = await fetch(url);
//    const data = await response.text();
//
//    const link = document.createElement('a');
//    link.setAttribute('href', 'data:text/csv;charset=utf-8,' + encodeURIComponent(data));
//    link.setAttribute('download', 'daily-report.csv');
//    link.style.display = 'none';
//
//    document.body.appendChild(link);
//    link.click();
//    document.body.removeChild(link);
//}

function setMAxDateLimit(){
    document.getElementById("inputEndDate").min = document.getElementById("inputStartDate").value
}

function setMinDateLimit(){
    document.getElementById("inputStartDate").max = document.getElementById("inputEndDate").value
}

function addModal(){
    document.getElementById("dailyReport-form").reset()
    $('#daily-report-modal').modal('toggle');
}

var $fileName = 'Daily Report'

function pagination(){
    $('#dailyReport-table').DataTable({
        dom: 'Bfrtip',
        buttons : [
            {
                extend: 'csv',
                title : 'Daily Report',
                filename: $fileName,
                text: '<i class="fa-solid fa-download"></i>',
            }
        ]
    });
    $('.dataTables_length').addClass("bs-select");
}

//INITIALIZATION CODE
function init(){
    $('#add').click(addModal);
	$('#get-filteredDailyReport').click(filteredDailyReport);
	$('#refresh-data').click(getDailyReportList);
	$('#inputStartDate').change(setMAxDateLimit);
	$('#inputEndDate').change(setMinDateLimit);
}

$(document).ready(getDailyReportList);
$(document).ready(init);


