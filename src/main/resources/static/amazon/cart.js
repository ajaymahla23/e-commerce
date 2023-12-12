//first payment

const paymentStart = () => {
	let amount = document.getElementById("payment_checkout").innerHTML;
	console.log("Total Amount : " + amount);

//we will use to ajax to send request to server to create oreder -jquery
	$.ajax(
		{
			url: "/create_order",
			data: JSON.stringify({ amount: amount, info: "order_request" }),
			contentType: "application/json",
			type: "POST",
			dataType: "json",

			success: function(response) {
				//invoked when success
				console.log(response);
				if (response.status == "created") {

					//open payment form
					let options = {
						key: 'rzp_test_qQKb332vF7D5vF',
						amount: response.amount,
						currency: "USD",
					//	currency: "INR",
						name: "Amazon",
						description: "Welcome ",
						iImage:"amazon/amazon_logo.png",
					//	image: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnxlmi9Luew5uQsblXcVspa_fD1S2-GvW9iw&usqp=CAU",
						order_id: response.id,
						handler: function(response) {
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(response.razorpay_signature);
							console.log("payment successfull !!");

							/*alert("congrates !! Payment successfull !!");*/


							updatePaymentOnServer(
								response.razorpay_payment_id,
								response.razorpay_order_id,
								"paid"
							);
						},
						prefill: {
							name: "",
							email: "",
							contact: "",
						},

						notes: {
							address: "online shopping payment",
						},
						theme: {
							color: " #e74c3c"
						},
					};

					let rzp = new Razorpay(options);

					rzp.on("payment.failed", function(response) {
						console.log(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);
						swal("Failed!!", "Oops payment failed !!", "error");
					});
					rzp.open();
				}
			},
			error: function(error) {
				//invoked when error 
				console.log(error);
				alert("something went wrong !!");
			}
		}
	);

}



//check status -paid or not paid in database

function updatePaymentOnServer(payment_id, order_id, status) {
	$.ajax(
		{
			url: "/update_order",
			data: JSON.stringify({
				payment_id: payment_id,
				order_id: order_id,
				status: status,
			}),

			contentType: "application/json",
			type: "POST",
			dataType: "json",
			success: function(response) {
				console.log(response);
				swal("Good job!", " congrates !! Payment successfull !!", "success");
			},

			error: function(error) {
				console.log(error);
				swal("Failed!!", "Your payment is successful, but we did not get on server, we will contact you as soos as possible !!", "error");
			}

		});
}

//====================================================Remove cart item=====================================

/*function removeCartItem() {

	let cartList = document.getElementById("remove_cart").value;
		var cartList = $(this).attr( "id" );
	console.log("remove item id :" + cartList);

	$.ajax({
		url: "/cart_remove/{id}",
		data: JSON.stringify({ cartList: cartList }),
		contentType: "application/json",
		type: "delete",
		dataType: "json",
		success: function(response) {
			//response => document.location.reload(true)
			console.log(response);
		},
		error: function(error) {
			console.log(error);

		}
	})
}*/


/*function updateProductQty() {
	let quantity = document.getElementById("quantity").value;
	let price = document.getElementById("productPrice").value;
	let total = quantity * price;
	document.getElementById("totalAmount").innerHTML = total;
	
		var cartList = $(this).attr( "id" );
	console.log("product qty :" + quantity);


	$.ajax({
		url: "/update_qty",
		data: JSON.stringify({ quantity: quantity,total: total }),
		contentType: "application/json",
		type: "post",
		dataType: "json",
		success: function(response) {
			//response => document.location.reload(true)
			console.log(response);
		},
		error: function(error) {
			console.log(error);

		}
	})
}*/



