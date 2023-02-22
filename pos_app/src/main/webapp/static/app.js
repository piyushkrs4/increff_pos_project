//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function replace(message){
    return message.replace(/\n/g, "<br />");
}

function handleAjaxError(response){
    var message = JSON.parse(response.responseText);
    toastr.error(replace(message.message), "Error: ", {
        "closeButton": true,
        "timeOut": "0",
        "extendedTimeOut": "0",
        "preventDuplicates": true,
        "newestOnTop": true,
    });
}

function successMessage(message){
    toastr.success(message, "Success: ", {
        "progressBar": true,
        "preventDuplicates": true,
        "newestOnTop": true,
    });
}

function warningMessage(message){
    toastr.warning(message, "Warning: ", {
        "progressBar": true,
        "preventDuplicates": true,
        "newestOnTop": true,
    });
}

function errorMessage(message){
    toastr.error(message, "Error: ", {
        "closeButton": true,
        "timeOut": "0",
        "extendedTimeOut": "0",
        "preventDuplicates": true,
        "newestOnTop": true,
    });
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
        fileUrl = navigator.msSaveBlob(blob, 'errors.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'errors.tsv');
    tempLink.click(); 
}

function supervisorView()
{
    if($("meta[name=role]").attr("content") == "supervisor"){
        var elements = document.getElementsByClassName('supervisor-view');
        for (var i = 0; i < elements.length; i ++) {
            elements[i].style.display = '';
        }
    }
}

function supervisorViewFlex() {
    if($("meta[name=role]").attr("content") == "supervisor"){
        var elements = document.getElementsByClassName('supervisor-view-flex');
        for (var i = 0; i < elements.length; i ++) {
            elements[i].style.display = 'flex';
        }
    }
}

function roundUpTo2DecimalPlaces(value){
    return Math.round((parseFloat(value) + Number.EPSILON) * 100) / 100
}

function validateForm(form){
    return form[0].reportValidity();
}

$(document).ready(supervisorView);
$(document).ready(supervisorViewFlex);
