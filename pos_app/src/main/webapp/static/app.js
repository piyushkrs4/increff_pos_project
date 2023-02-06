
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function handleAjaxError(response){
    var message = JSON.parse(response.responseText);
    document.getElementById('toast').classList.remove('bg-warning','bg-danger','bg-success');
    document.getElementById('toast').classList.add('bg-danger');
    document.getElementById('toast-message').innerHTML=message.message;
    $(".toast").toast('show');
}

function errorMessage(message){
    document.getElementById('toast').classList.remove('bg-warning','bg-danger','bg-success');
    document.getElementById('toast').classList.add('bg-danger');
    document.getElementById('toast-message').innerHTML=message;
    $(".toast").toast('show');
}

function successMessage(message){
    document.getElementById('toast').classList.remove('bg-warning','bg-danger','bg-success');
    document.getElementById('toast').classList.add('bg-success');
    document.getElementById('toast-message').innerHTML=message;
    $(".toast").toast('show');
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}

function standardView()
{
    if($("meta[name=role]").attr("content") == "operator"){
        var elements = document.getElementsByClassName('admin-view');

        for (var i = 0; i < elements.length; i ++) {
            elements[i].style.display = 'none';
        }
    }

}

$(document).ready(standardView);
