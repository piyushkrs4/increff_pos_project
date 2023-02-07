function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/operator/orders";
}

//BUTTON ACTIONS

function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);  
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS
function displayOrderList(data){
    $('#order-table').DataTable().destroy();
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	var sno = 0


	for(var i in data){
		var e = data[i];
		var orderStatus = e.status;
		var buttonHtml;
        let date = new Date(e.datetime);
        let localDate = date.toLocaleString('en-IN');

		if(!orderStatus){
		    buttonHtml = '<button class = "btn btn-warning mr-2" onclick="viewOrder(' + e.id + ')"><i class="fa-solid fa-pen"></i></button>'
		    buttonHtml += '<button class = "btn btn-success" onclick="createInvoice(' + e.id + ')"><i class="fa-sharp fa-solid fa-check"></i></button>'
		}
		else{
		    buttonHtml = '<button class = "btn btn-light mr-2" onclick="getInvoice(' + e.id + ')"><i class="fa-solid fa-download"></i></i></button>'
            buttonHtml += '<button class = "btn btn-light" onclick="viewPlacedOrder(' + e.id + ')"><i class="fa-solid fa-eye"></i></button>'
		}
		var row = '<tr>'
		+ '<td>' + ++sno + '</td>'
		+ '<td>' + e.orderCode + '</td>'
		+ '<td>' + localDate + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	pagenation();
}

function pagenation(){
    $('#order-table').DataTable();
    $('#cartItem-table').DataTable();
    $('#orderItem-table').DataTable();
    $('.dataTables_length').addClass("bs-select");
}


function createInvoice(id){
	var url = getOrderUrl() + "/" + id + "/generate-invoice";
    var date = new Date();
    var localDate = date.toLocaleString();
//    var json = {
//        "currentDateTime": dateStr,
//    }
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: localDate,
	   headers: {
          'Content-Type': 'application/json'
       },
	   success: function(data) {
	        successMessage("Invoice created successfully!")
            getOrderList();
	   },
	   error: handleAjaxError
	});

}

function getInvoice(id){
//    window.location.href = url
    var url = getOrderUrl() + "/" + id + "/download-invoice"
    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
            downloadInvoice(data, id);
       },
       error: handleAjaxError
    });
}


function downloadInvoice(base64EncodedPdf, id) {
    const link = document.createElement("a");
    link.href = "data:application/pdf;base64," + base64EncodedPdf;
    link.download = "invoice_" + id + ".pdf";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

//cart
var cartItemsMap = new Map();
var order_id;

function addCartItem(event){
    var $form = $("#cart-item-form")
    var json = toJson($form)
    var e = JSON.parse(json)
    e.barcode = e.barcode.trim()
    if(e.barcode.length == 0){
        errorMessage("Please enter barcode!")
        return
    }
    if(e.quantity <= 0){
        errorMessage("Quantity cannot be less than one!")
        return
    }
    if(e.sellingPrice < 0){
        errorMessage("Selling price cannot be less than zero")
        return
    }
    var quantity = parseInt(e.quantity)
    if(cartItemsMap.has(e.barcode))
        quantity += parseInt(cartItemsMap.get(e.barcode)[0])
    cartItemsMap.set(e.barcode, [quantity, e.sellingPrice])
    openModal()
    displayCartItems()
}

function addOrderItem(event){
    var $form = $("#order-item-form")
    var json = toJson($form)
    var e = JSON.parse(json)
    e.barcode = e.barcode.trim()
    if(e.barcode.length == 0){
        errorMessage("Please enter barcode!")
        return
    }
    if(e.quantity <= 0){
        errorMessage("Quantity cannot be less than one!")
        return
    }
    if(e.sellingPrice < 0){
        errorMessage("Selling price cannot be less than zero")
        return
    }
    var quantity = parseInt(e.quantity)
    if(cartItemsMap.has(e.barcode))
        quantity += parseInt(cartItemsMap.get(e.barcode)[0])
    cartItemsMap.set(e.barcode, [quantity, e.sellingPrice])

    displayOrderItems()
}



function viewOrder(id){
    displayOrder();
    order_id = id;
    var url = getOrderUrl() + "/" + id + "/order-items";
    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
            getOrderItemList(data);
       },
       error: handleAjaxError
    });
}

function viewPlacedOrder(id){
    displayPlacedOrder();
    order_id = id;
    var url = getOrderUrl() + "/" + id + "/view-invoice";
    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
            displayPlacedOrderItems(data);
       },
       error: handleAjaxError
    });
}



function getOrderItemList(data){
    cartItemsMap.clear()
	for(var i in data){
	    var e = data[i];
	    cartItemsMap.set(e.barcode, [e.quantity, e.sellingPrice])
	}
    displayOrderItems();
}




function displayCartItems(){
    $('#cartItem-table').DataTable().destroy();
	var $tbody = $('#cartItem-table').find('tbody');
    $tbody.empty();
	var sno = 1;
	var total = 0;
	for (let [key, value] of cartItemsMap){
	    var barcode = key.toString()
	    var buttonHtml = '<button class = "btn btn-danger" onclick="deleteCartItem(\'' + barcode + '\')">delete</button>'
	    var totalPrice = value[0] * value[1];
        var row = '<tr>'
        + '<td>' + sno++ + '</td>'
        + '<td>' + key + '</td>'
        + '<td>' + value[0] + '</td>'
        + '<td>'  + value[1] + '</td>'
        + '<td>' + totalPrice + '</td>'
        + '<td>' + buttonHtml + '</td>'
        + '</tr>';
        $tbody.append(row);
        total += totalPrice;
	}
	$('#cartItemTotal').text(total);

	pagenation();
}


function displayOrderItems(){
    $('#orderItem-table').DataTable().destroy();
    console.log("hi")
	var $tbody = $('#orderItem-table').find('tbody');
    $tbody.empty();
	var sno = 1;
//	for (let [key, value] of cartItemsMap){
//	    var barcode = key.toString()
//	    var buttonHtml = '<button class = "btn btn-danger" onclick="deleteOrderItem(\'' + barcode + '\')">delete</button>'
//        var row = '<tr>'
//        + '<td>' + sno++ + '</td>'
//        + '<td>' + key + '</td>'
//        + '<td>' + value[0] + '</td>'
//        + '<td>'  + value[1] + '</td>'
//        + '<td>' + buttonHtml + '</td>'
//        + '</tr>';
//        $tbody.append(row);
//	}
    var total = 0;
	for (let [key, value] of cartItemsMap){
	    var barcode = key.toString()
	    var buttonHtml = '<button class = "btn btn-danger" onclick="deleteOrderItem(\'' + barcode + '\')">delete</button>'
	    var totalPrice = value[0] * value[1];
        var row = '<tr>'
        + '<td>' + sno++ + '</td>'
        + '<td>' + key + '</td>'
        + '<td>' + value[0] + '</td>'
        + '<td>'  + value[1] + '</td>'
        + '<td>' + totalPrice + '</td>'
        + '<td>' + buttonHtml + '</td>'
        + '</tr>';
        $tbody.append(row);
        total += totalPrice;
	}
	$('#orderItemTotal').text(total);
	pagenation();
}

function displayPlacedOrderItems(data){
	var $tbody = $('#placedOrder-table').find('tbody');
    $tbody.empty();
    var $invoiceNumber = $('#inputInvoiceNumber')
    $invoiceNumber.empty()
    $invoiceNumber.append(data.invoiceNumber);
    var $invoiceDate = $('#inputInvoiceDate')
    $invoiceDate.empty()
    $invoiceDate.append(data.invoiceDate);
    var $invoiceTime = $('#inputInvoiceTime')
    $invoiceTime.empty()
    $invoiceTime.append(data.invoiceTime);
    var $total = $('#inputTotal')
    $total.empty()
    $total.append(data.total);
    var $orderCode = $('#inputOrderCode')
    $orderCode.empty()
    $orderCode.append(data.orderCode);
	for(var i in data.lineItems){
    	var e = data.lineItems[i];
	    var row = '<tr>'
        + '<td>' + e.sno + '</td>'
        + '<td>' + e.productName + '</td>'
        + '<td>' + e.barcode + '</td>'
        + '<td>'  + e.quantity + '</td>'
        + '<td>' + e.unitPrice + '</td>'
        + '<td>' + e.total + '</td>'
        + '</tr>';
        $tbody.append(row);
	}
	var row = '<tr>'
	+ '<td></td>'
	+ '<td></td>'
	+ '<td></td>'
	+ '<td></td>'
	+ '<td> Total: </td>'
    + '<td>' + data.total + '</td>'
    + '</tr>';
    $tbody.append(row);
}

function deleteCartItem(barcode){
    cartItemsMap.delete(barcode)
    displayCartItems()
}

function deleteOrderItem(barcode){
    cartItemsMap.delete(barcode)
    displayOrderItems()
}

function displayCart(){
    cartItemsMap.clear()
    var $tbody = $('#cartItem-table').find('tbody');
    $tbody.empty();
    openModal();
    $('#create-order-modal').modal('toggle');
}

function displayOrder(){
    cartItemsMap.clear()
    var $tbody = $('#orderItem-table').find('tbody');
    $tbody.empty();
    openOrderModal()
    $('#view-order-modal').modal('toggle');
}

function displayPlacedOrder(){
    var $tbody = $('#placedOrder-table').find('tbody');
    $tbody.empty();
    $('#view-placedOrder-modal').modal('toggle');
}


function placeOrder(){
	var url = getOrderUrl();
	var jsonList = [];
	for (let [key, value] of cartItemsMap){
	    var json = {
	        "barcode": key,
	        "quantity": value[0],
	        "sellingPrice": value[1]
	    }
	    jsonList.push(json)
	}
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(jsonList),
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        successMessage("Order created successfully.")
	        $('#create-order-modal').modal('toggle');
	        cartItemsMap.clear()
	   		getOrderList();
	   		displayCartItems();
	   },
	   error: handleAjaxError
	});
	return false;
}

function updateOrder(){
	var url = getOrderUrl() + "/" + order_id ;
	var jsonList = [];
	for (let [key, value] of cartItemsMap){
	    var json = {
	        "barcode": key,
	        "quantity": value[0],
	        "sellingPrice": value[1]
	    }
	    jsonList.push(json)
	}
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: JSON.stringify(jsonList),
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        alert("Order updated successfully")
	        $('#view-order-modal').modal('toggle');
	   		getOrderList();
	   },
	   error: handleAjaxError
	});
	return false;
}

function openModal(){
    var $tbody = $('#cartItem-table').find('tbody');
    $tbody.empty();
    $("#cart-item-form input[name=barcode]").val("");
    $("#cart-item-form input[name=quantity]").val("");
    $("#cart-item-form input[name=sellingPrice]").val("");
    $('#cartItemTotal').text(0);
    $('#cartItem-table').DataTable().destroy();

}


function openOrderModal(){
    var $tbody = $('#orderItem-table').find('tbody');
    $tbody.empty();
    $("#order-item-form input[name=barcode]").val("");
    $("#order-item-form input[name=quantity]").val("");
    $("#order-item-form input[name=sellingPrice]").val("");
    $('#orderItemTotal').text(0);
    $('#orderItem-table').DataTable().destroy();
}



//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getOrderList);
	$('#open-cart').click(displayCart);
	$('#add-cartItem').click(addCartItem);
	$('#add-orderItem').click(addOrderItem);
	$('#place-order').click(placeOrder);
	$('#update-order').click(updateOrder);

}

$(document).ready(init);
$(document).ready(getOrderList);

