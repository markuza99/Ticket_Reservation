<template>
	<div id="manifestation-page"  v-if="manifestation">
		<div  class="container-fluid">
		<div class="row">
			<div class="col-lg-6 col-md-6 col-sm-12">
				<div class="map-holder map" id="map" ref="map">
					<div v-if="!mapShowed" v-on:click="displayMap">Pogledaj mapu</div>
				</div>
			</div>
			<div class="col-lg-6 col-md-6 col-sm-12">
				<div class="manifestation">
					<div class="image-holder">
						<img class="fit-img" v-bind:class="{ gray : sold(manifestation) }" v-bind:src="'images/' + manifestation.image">
					</div>
					<div class="manifestation-information">
						<div class="mb-2 mt-4 font-weight-bold">{{manifestation.type}}</div>
						<h1>{{manifestation.name}}</h1>
						<div class="startTime">
							<span>{{manifestation.startTime.dayOfMonth}}</span>
							<span>{{manifestation.formattedMonth}}</span>
							<span>{{manifestation.startTime.year}}</span>
						</div>
						-
						<div class="endTime">
							<span>{{manifestation.endTime.dayOfMonth}}</span>
							<span>{{manifestation.formattedMonth}}</span>
							<span>{{manifestation.endTime.year}}</span>
						</div>

						<div class="ticket-price text-danger text-white" v-if="!sold(manifestation)">{{manifestation.ticketPrice}},00 RSD</div>
						<div class="ticket-price text-danger text-white" v-if="sold(manifestation)">RASPRODATO</div>
						<!-- <div class="text-success">{{manifestation.status}}</div> -->
						<div class="average_rating" v-if="manifestation_passed">
							<span class="fa fa-star" v-bind:class="{ checked : isCountedInAverageRating(1) }"></span>
							<span class="fa fa-star" v-bind:class="{ checked : isCountedInAverageRating(2) }"></span>
							<span class="fa fa-star" v-bind:class="{ checked : isCountedInAverageRating(3) }"></span>
							<span class="fa fa-star" v-bind:class="{ checked : isCountedInAverageRating(4) }"></span>
							<span class="fa fa-star" v-bind:class="{ checked : isCountedInAverageRating(5) }"></span>
						</div>
					</div>
				</div>
				</div>
		</div>

		<div class="row">
			<button type="button" v-if="commentParams.user && !manifestation_passed" class="btn btn-primary reservation-button" data-toggle="modal" data-target="#reservationModal">
			Reservisite karte
			</button>

			<button type="button" v-if="commentParams.role == 'ADMIN'" v-on:click="goToEditManifestation(manifestation.id)" class="btn btn-primary mt-3">
				Izmeni manifestaciju
			</button>

			<!-- Modal -->
			<reservation-modal></reservation-modal>

			

		<!-- </div> -->

		<!-- <div class="row"> -->
			<comment-section></comment-section>
			
		</div>
		</div>
	</div>
</template>

<script> 

module.exports = {
	data() {
		return {
			manifestation: null,
			mapShowed : false,
			// comments : [],
			// user_rating : 0,
			// number_of_tickets : 0,
			// ticket_price : 0,
			// ticket_type: "REGULAR",
			// customer : {},
			// manifestation_date : 0,
			manifestation_passed : false,
			// commentParams : {},
			// remaining_number_error: false,
			// new_price: 0
		}
	},
	components: {
		'reservation-modal':httpVueLoader('./modals/ReservationModal.vue'),
		'comment-section':httpVueLoader('./CommentSection.vue')
	},
	methods: {
		goToEditManifestation(id) {
			location.replace("#/manifestation/edit/" + id);
		},
		countPrice() {
			// this.remaining_number_error = false;
			// $('.reserve-button').attr("disabled", false);
			// if(!validateNumberRange(1,5,this.number_of_tickets)) {
			// 	$('.total-price').attr("placeholder", "Mozete rezervisati od 0 do 5 karata u jednoj rezervaciji.");
			// 	$('.total-price').addClass("error");
			// 	$('.reserve-button').attr("disabled", true);
			// 	return;
			// }

			// if(this.number_of_tickets > this.manifestation.remainingNumberOfSeats) {
			// 	this.remaining_number_error = true;
			// 	return;
			// }

			// if(this.ticket_type == "FAN_PIT") {
			// 	this.new_price = this.manifestation.ticketPrice * 2;
			// } else if(this.ticket_type == "VIP"){
			// 	this.new_price = this.manifestation.ticketPrice * 4;
			// } else {
			// 	this.new_price = this.manifestation.ticketPrice;
			// }

			// axios
			// 	.get("rest/customerservice/get-customer")
			// 	.then(response => {
			// 		this.customer = response.data;
			// 		if(this.customer.customerType.discount == 1) {
			// 			this.ticket_price = this.number_of_tickets * this.new_price;
			// 		} else {
			// 			this.ticket_price = this.number_of_tickets * (this.new_price * (1-this.customer.customerType.discount));
			// 		}
			// 		$('.total-price').attr("placeholder", this.ticket_price + ",00 RSD");
			// 		$('.total-price').removeClass("error");
			// 	});
		},
		reserve() {
			
			// let points = this.ticket_price/1000 * 133;
			// axios
			// 	.post("rest/customerservice/reserve-ticket", 
			// 	{
			// 		"points" : points,
			// 		"manifestation" : this.manifestation.id,
			// 		"numberOfTickets" : this.number_of_tickets,
			// 		"user" : this.commentParams.user,
			// 		"ticketPrice" : this.ticket_price
			// 	})
			// 	.then(response => {
			// 		alert("Rezervacija uspesno izvrsena!");
			// 	});
		},
	// 	correspondsCommentPermision() {
	// 		console.log(this.commentParams);
	// 		return (this.commentParams.user && !this.commentParams.commentSuccess 
	// 						  && validateRange(this.manifestation_date, Date.now())
	// 						  && this.commentParams.userAttended && this.manifestation_passed);
			
	// 	},
	// 	clickedStar(whichstar) {
	// 		this.user_rating = userRating(this.user_rating);
	// 	},
	// 	comment() {
	// 		let description = $('.comment-holder').val();
	// 		if(this.user_rating == 0 || description.trim() == "") {
	// 			$('.logged-user-comment input').addClass("error");
	// 			return;
	// 		}
			
	// 		comment = { user : "",
	// 					manifestation : this.manifestation.id,
	// 					description : description,
	// 					rating : this.user_rating,
	// 					commentStatus : "NONACTIVE",
	// 					approval: "NOT_CHECKED" 
	// 					};
	// 		axios
	// 			.post("rest/commentservice/post-comment",JSON.stringify(comment),{
	// 				headers: {'content-type':'application/json'}
	// 			})
	// 			.then(response => {
	// 				$('.logged-user-comment input').removeClass("error");
	// 				this.commentParams.commentSuccess = true;
	// 			});
	// 	},
	// 	sold(manifestation) {
	// 		return manifestation.remainingNumberOfSeats > 0 ? false : true;
	// 	},
	// 	isCounted(num, comment) {
	// 		return !(num > comment.rating);
	// 	},
	// 	isCountedInAverageRating(num) {
	// 		return !(num > this.commentParams.manifestationRating);
	// 	},
	// 	displayMap() {
	// 		if(this.mapShowed)
	// 			return;
	// 		displayMap();
	// 		this.mapShowed = true;
	// 	}
	// },
	mounted() {

		let path = window.location.href;
		let pathparams = path.split("//")[1];
		let manifestationId = pathparams.split("/")[4];

		//ovo zameniti sa this.route

		axios
			.get("rest/manifestations/" + manifestationId)
			.then(response => {
				this.manifestation = response.data;
				makeDate(this.manifestation);
				formatType(this.manifestation);
				
				// this.manifestation_date = getManifestationDateInMilliseconds(this.manifestation.date.dayOfMonth,
				// 						this.manifestation.date.monthValue -1,this.manifestation.date.year); 
				let date = new Date();
				if(!validateRange(date.toISOString(), this.manifestation.endTime)) {
					this.manifestation_passed = true;
				}
			});

		axios
			.get("rest/comments/manifestation/" + manifestationId)
			.then(response => {
				this.comments = response.data;
			});

		axios
			.get("rest/comments/commenting-conditions/manifestation/" + manifestationId)
			.then(response => {
				this.comment_conditions = response.data;
				console.log(this.comment_conditions);
			});

	}

}
</script>

<style scoped>

#manifestation-page .container-fluid {
	padding-left: 0;
	padding-right: 0;
}

#manifestation-page .row {
	margin-right: 0;
	margin-left: 0;
}

#manifestation-page .col-md-6, 
#manifestation-page .col-sm-12, 
#manifestation-page .col-lg-6 {
	padding-left: 0;
	padding-right: 0;
}

#manifestation-page .image-holder {
	
	overflow:hidden;
	position:relative;
	cursor: pointer;
}

#manifestation-page .image-holder img {
	max-width: 100%;
	-webkit-transition:all .4s linear;
    transition:all .4s linear;
}

#manifestation-page h1 {
	color: #767676;
	text-transform: uppercase;
}
#manifestation-page .ticket-price {
	font-size: 20px;
	/* margin-bottom: 10px; */
	/* padding: 20px; */
	position: relative;
}

#manifestation-page .date {
	text-transform: uppercase;
	font-family: 'Quicksand', sans-serif;
}

.gray {
  -webkit-filter: grayscale(100%); /* Safari 6.0 - 9.0 */
  filter: grayscale(100%);
}

.rating {
	/* position: absolute; */
	/* right:5em; */
	direction: rtl;
  	unicode-bidi: bidi-override;
}

.rating > span:hover:before,
.rating > span:hover ~ span:before {
   color:orange;
}


.error {
    outline: none !important;
    border:1px solid red;
    box-shadow: 0 0 10px #f40b0b;
}

.reservation-button {
	padding: 1em;
	margin: 1em;
}

#remaining_number_error {
	color: red;
}

@media screen and (min-width: 1200px) {
	#manifestation-page .image-holder {
		height: 25em;
	}
}
</style>