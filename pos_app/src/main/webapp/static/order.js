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
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	var sno = 0


	for(var i in data){
		var e = data[i];
		var orderStatus = e.status;
		var buttonHtml;

		if(!orderStatus){
		    buttonHtml = '<button class = "btn btn-warning mr-2" onclick="viewOrder(' + e.id + ')"><i class="fa-solid fa-pen"></i></button>'
		    buttonHtml += '<button class = "btn btn-success" onclick="createInvoice(' + e.id + ')"><i class="fa-sharp fa-solid fa-check"></i></button>'
		}
		else{
		    buttonHtml = '<button class = "btn btn-secondary mr-2" onclick="getInvoice(' + e.id + ')"><i class="fa-solid fa-download"></i></button>'
            buttonHtml += '<button class = "btn btn-secondary" onclick="viewPlacedOrder(' + e.id + ')"><i class="fa-solid fa-eye"></i></button>'
		}
		var row = '<tr>'
		+ '<td>' + ++sno + '</td>'
		+ '<td>' + e.orderCode + '</td>'
		+ '<td>' + e.datetime + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	pagenation();
}

function pagenation(){
    $('#order-table').DataTable();
    $('.dataTables_length').addClass("bs-select");
}


function createInvoice(id){
	var url = getOrderUrl() + "/" + id + "/generate-invoice";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	        successMessage("Invoice created successfully!")
            getOrderList();
	   },
	   error: handleAjaxError
	});

}

function getInvoice(id){
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
    if(!validateForm($form)){
        return;
    }
    var json = toJson($form)
    var e = JSON.parse(json)
    e.barcode = e.barcode.trim()
    e.sellingPrice = roundUpTo2DecimalPlaces(e.sellingPrice)
    var quantity = parseInt(e.quantity)
    if(cartItemsMap.has(e.barcode)){
        if(cartItemsMap.get(e.barcode)[1] != e.sellingPrice){
            errorMessage("Selling price cannot be different for same product!")
            return
        }
        if(quantity + parseInt(cartItemsMap.get(e.barcode)[0]) > 200){
            warningMessage("Cannot add mor than 200 items!")
            return
        }
        quantity += parseInt(cartItemsMap.get(e.barcode)[0])
    }

    cartItemsMap.set(e.barcode, [quantity, e.sellingPrice])
    openModal()
    displayCartItems()
}

function addOrderItem(event){
    var $form = $("#order-item-form")
    if(!validateForm($form)){
        return;
    }
    var json = toJson($form)
    var e = JSON.parse(json)
    e.barcode = e.barcode.trim()
    e.sellingPrice = roundUpTo2DecimalPlaces(e.sellingPrice)
    var quantity = parseInt(e.quantity)
    if(cartItemsMap.has(e.barcode)){
        if(cartItemsMap.get(e.barcode)[1] != e.sellingPrice){
            errorMessage("Selling price cannot be different for same product!")
            return
        }
        if(quantity + parseInt(cartItemsMap.get(e.barcode)[0]) > 200){
            warningMessage("Cannot add mor than 200 items!")
            return
        }
        quantity += parseInt(cartItemsMap.get(e.barcode)[0])
    }
    cartItemsMap.set(e.barcode, [quantity, e.sellingPrice])
    openOrderModal()
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
	var $tbody = $('#cartItem-table').find('tbody');
    $tbody.empty();
	var itr = 1;
	var total = 0;
	for (let [key, value] of cartItemsMap){
	    var barcode = key.toString()
	    var buttonHtml = '<button class = "btn btn-danger" onclick="deleteCartItem(\'' + barcode + '\')"><i class="fa-solid fa-trash"></i></button>'
	    var totalPrice = roundUpTo2DecimalPlaces(value[0] * value[1]);
        var editButtonHtml = '<button id="button'+itr+'"class="btn btn-dark" onclick="editItem(' + itr + ')"><i class="fa-solid fa-pen-to-square"></i></button>';

        var row = '<tr>'
        +'<td><form id="row'+ itr +'">'+ itr +'</form>'
        + '<td><label for="barcode" form="row'+itr+'" name="barcode" id="barcode'+itr+'">'+key+'</label></td>'
        + '<td><input type="number" class="form-control" step = "1" form="row'+itr+'"name="quantity" id="quantity'+itr+'" value="'+value[0]+'" MIN="1" MAX="200" required disabled></td>'
        + '<td><input type="number" class="form-control" step=".0001" form="row'+itr+'"name="sellingPrice" id="price'+itr+'" value="'+value[1].toFixed(2)+'" MIN="0" MAX="1000000" required disabled></td>'
        + '<td > <label for="totalPrice" form="row'+itr+'" name="totalPrice" id="totalPrice'+itr+'">' + totalPrice.toFixed(2) + '</td>'
        + '<td>' + editButtonHtml +"   "+ buttonHtml + '</td>'
        + '</tr>';

        itr++;

        $tbody.append(row);
        total += totalPrice;
	}
	if(itr < 2)
	    document.getElementById('place-order').disabled=true;
	else
	    document.getElementById('place-order').disabled=false;
	if(total > 0)
	    $tbody.append('<tr style="font-weight: bold; background-color: LemonChiffon"><td colspan="3"></td><td>'+ 'Total:' +'</td><td>'+roundUpTo2DecimalPlaces(total).toFixed(2)+'</td><td></td></tr>')
}

function editItem(id){
    document.getElementById('price'+id).disabled=false;
    document.getElementById('quantity'+id).disabled=false;
    document.getElementById('button'+id).innerHTML = "<i class='fa-sharp fa-solid fa-check'></i>";
    document.getElementById('button'+id).onclick = function(){edits(id);}
}

function editOrderItem(id){
    document.getElementById('price_update'+id).disabled=false;
    document.getElementById('quantity_update'+id).disabled=false;
    document.getElementById('button_update'+id).innerHTML = "<i class='fa-sharp fa-solid fa-check'></i>";
    document.getElementById('button_update'+id).onclick = function(){orderEdits(id);}
}

function edits(id){
    var $form = $("#row"+id);
    if(!validateForm($form))
        return;
    var json = toJson($form)
    var e = JSON.parse(json)
    var barcode = document.getElementById('barcode'+id).innerHTML.trim()

    e.sellingPrice = roundUpTo2DecimalPlaces(e.sellingPrice)
    var quantity = parseInt(e.quantity)
    cartItemsMap.set(barcode, [quantity, e.sellingPrice])

    document.getElementById('price'+id).disabled=true;
    document.getElementById('quantity'+id).disabled=true;
    document.getElementById('button'+id).innerHTML = '<i class="fa-solid fa-pen-to-square"></i>';
    document.getElementById('button'+id).onclick = function(){editItem(id);}
    displayCartItems();
}

function orderEdits(id){
    var $form = $("#row_update"+id);
    if(!validateForm($form))
        return;
    var json = toJson($form)
    var e = JSON.parse(json)
    var barcode = document.getElementById('barcode_update'+id).innerHTML.trim()

    e.sellingPrice = roundUpTo2DecimalPlaces(e.sellingPrice)
    var quantity = parseInt(e.quantity)
    cartItemsMap.set(barcode, [quantity, e.sellingPrice])

    document.getElementById('price_update'+id).disabled=true;
    document.getElementById('quantity_update'+id).disabled=true;
    document.getElementById('button_update'+id).innerHTML = '<i class="fa-solid fa-pen-to-square"></i>';
    document.getElementById('button_update'+id).onclick = function(){editOrderItem(id);}
    displayOrderItems();

}


function displayOrderItems(){
	var $tbody = $('#orderItem-table').find('tbody');
    $tbody.empty();
	var itr = 1;
    var total = 0;
	for (let [key, value] of cartItemsMap){
	    var barcode = key.toString()
	    var buttonHtml = '<button class = "btn btn-danger" onclick="deleteOrderItem(\'' + barcode + '\')"><i class="fa-solid fa-trash"></i></button>'
	    var totalPrice = roundUpTo2DecimalPlaces(value[0] * value[1]);

	    var editButtonHtml = '<button id="button_update'+itr+'"class="btn btn-dark" onclick="editOrderItem(' + itr + ')"><i class="fa-solid fa-pen-to-square"></i></button>';

        var row = '<tr>'
        +'<td><form id="row_update'+ itr +'">'+ itr +'</form>'
        + '<td><label for="barcode" class="form-control" form="row_update'+itr+'" name="barcode" id="barcode_update'+itr+'">'+key+'</label></td>'
        + '<td><input type="number" class="form-control" step = "1" form="row_update'+itr+'"name="quantity" id="quantity_update'+itr+'" value="'+value[0]+'" MIN="1" MAX="200" required disabled></td>'
        + '<td><input type="number" class="form-control" step=".0001" form="row_update'+itr+'"name="sellingPrice" id="price_update'+itr+'" value="'+value[1].toFixed(2)+'" MIN="0" MAX="1000000" required disabled></td>'
        + '<td > <label for="totalPrice" class="form-control" form="row_update'+itr+'" name="totalPrice" id="totalPrice_update'+itr+'">' + totalPrice.toFixed(2) + '</td>'
        + '<td>' + editButtonHtml +"   "+ buttonHtml + '</td>'
        + '</tr>';

        itr++;
        $tbody.append(row);
        total += totalPrice;
	}
	if(itr < 2)
	    document.getElementById('update-order').disabled=true;
	else
	    document.getElementById('update-order').disabled=false;
	if(total > 0)
	    $tbody.append('<tr style="font-weight: bold; background-color: LemonChiffon"><td colspan="3"></td><td>'+ 'Total:' +'</td><td>'+roundUpTo2DecimalPlaces(total).toFixed(2)+'</td><td></td></tr>')
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
        + '<td>' + e.unitPrice.toFixed(2) + '</td>'
        + '<td>' + e.total.toFixed(2) + '</td>'
        + '</tr>';
        $tbody.append(row);
	}
    $tbody.append('<tr style="font-weight: bold; background-color: LemonChiffon"><td colspan="4"></td><td>'+ 'Total:' +'</td><td>'+data.total.toFixed(2)+'</td></tr>')
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
    document.getElementById('place-order').disabled=true;
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
	if(cartItemsMap.length == 0){
	    errorMessage("Add at least one item to order!")
	    return;
	}
	var totalPrice = 0;
	for (let [key, value] of cartItemsMap){
	    var json = {
	        "barcode": key,
	        "quantity": value[0],
	        "sellingPrice": value[1]
	    }
	    totalPrice += value[0] * value[1]
	    jsonList.push(json)
	}
	if(totalPrice > 2147483647){
	    errorMessage("Cannot order with total price more than 2000000000!")
        return;
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
	   },
	   error: handleAjaxError
	});
	return false;
}

function updateOrder(){
	var url = getOrderUrl() + "/" + order_id ;
    if(cartItemsMap.length == 0){
        errorMessage("Add at least one item to order!")
        return;
    }
	var jsonList = [];
	var totalPrice = 0;
	for (let [key, value] of cartItemsMap){
	    var json = {
	        "barcode": key,
	        "quantity": value[0],
	        "sellingPrice": value[1]
	    }
	    totalPrice += value[0] * value[1];
	    jsonList.push(json)
	}
	if(totalPrice > 2147483647){
        errorMessage("Cannot order with total price more than 2000000000!")
        return;
    }
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: JSON.stringify(jsonList),
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        successMessage("Order updated successfully")
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

}


function openOrderModal(){
    var $tbody = $('#orderItem-table').find('tbody');
    $tbody.empty();
    $("#order-item-form input[name=barcode]").val("");
    $("#order-item-form input[name=quantity]").val("");
    $("#order-item-form input[name=sellingPrice]").val("");
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

