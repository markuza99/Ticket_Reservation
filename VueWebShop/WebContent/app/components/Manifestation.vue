<template>
	<div id="manifestation-page" class="container-fluid" v-if="manifestation">
		<div class="row">
			<div class="col-lg-6 col-md-6 col-sm-12">
				<div class="image-holder">
					<img class="fit-img" v-bind:class="{ gray : sold(manifestation) }" v-bind:src="'images/' + manifestation.image">
				</div>
			</div>
			<div class="col-lg-6 col-md-6 col-sm-12">
				<div class="manifestation-info">
					
					<h1>{{manifestation.name}}</h1>
					<div class="manifestation-description">
						<h3 class="text-info">{{manifestation.type}}</h3>
						<div class="date">
							<span>{{manifestation.date.dayOfMonth}}</span>
							<span>{{manifestation.formattedMonth}}</span>
							<span>{{manifestation.date.year}}</span>
						</div>
					
					
					</div>
				
				
					<div class="ticket-price text-danger text-white" v-if="!sold(manifestation)">{{manifestation.ticketPrice}},00 RSD</div>
					<div class="ticket-price text-danger text-white" v-if="sold(manifestation)">RASPRODATO</div>
					<div class="text-success">{{manifestation.status}}</div>
					<div class="average_rating">
						<span class="fa fa-star" v-bind:class="{ checked : isCountedInAverageRating(1) }"></span>
						<span class="fa fa-star" v-bind:class="{ checked : isCountedInAverageRating(2) }"></span>
						<span class="fa fa-star" v-bind:class="{ checked : isCountedInAverageRating(3) }"></span>
						<span class="fa fa-star" v-bind:class="{ checked : isCountedInAverageRating(4) }"></span>
						<span class="fa fa-star" v-bind:class="{ checked : isCountedInAverageRating(5) }"></span>
					</div>
				</div>
					
					
					<div class="manifestation-location map" id="map" ref="map">
						<div v-if="!mapShowed" v-on:click="displayMap">Pogledaj mapu</div>
					</div>
				</div>
		</div>

		<div class="row">
			<button type="button" v-if="commentParams.user && !manifestation_passed" class="btn btn-primary reservation-button" data-toggle="modal" data-target="#reservationModal">
			Reservisite karte
			</button>

			<button type="button" v-if="commentParams.role == 'ADMIN'" v-on:click="goToEditManifestation(manifestation.id)" class="btn btn-primary">
				Izmeni manifestaciju
			</button>

			<!-- Modal -->
			<div class="modal fade" id="reservationModal" tabindex="-1" role="dialog" aria-labelledby="reservationModalLabel" aria-hidden="true">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="reservationModalLabel">Rezervacija karte</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<label class="form-label">Broj karata</label> 
							<input type="number" min="1" max="5" v-model="number_of_tickets" class="form-control" id="username"/>
						</div>
						<div class="form-group">
							<button type="button" class="btn btn-primary" v-on:click="countPrice">Izracunaj ukupnu cenu</button>
						</div>
						<div class="form-group">
							<label class="form-label">Ukupna cena</label>
							<input class="form-control total-price" type="text" placeholder="Readonly input here…" readonly>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazite</button>
						<button type="button" class="btn btn-primary reserve-button" 
						 data-dismiss="modal" data-toggle="modal" data-target="#areYouSureModal">Izvrsite rezervaciju</button>
					</div>
					</div>
				</div>
			</div>

			<div class="modal fade" id="areYouSureModal" tabindex="-1" role="dialog" aria-labelledby="areYouSureModalLabel" aria-hidden="true">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="areYouSureModalLabel">Potvrda rezervacije</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						Da li ste sigurni?
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazite</button>
						<button type="button" class="btn btn-primary" v-on:click="reserve" data-dismiss="modal">Potvrdite rezervaciju</button>
					</div>
					</div>
				</div>
			</div>

		</div>

		<div class="row">
			<div class="comment-section">
				<h3 v-if="comments.length > 0">Komentari</h3>
				<h3 v-if="comments.length == 0">Nema komentara</h3>
				<div class="comment" v-for="comment in comments" :key="comment.id">
					<ul class="comment-info bg-light d-flex justify-content-between">
						<li class="text-secondary comment-username">{{comment.user}}</li>
						<!-- <li class="comment-date">20 Oktobar 2020</li> -->
						<li>
							<span class="fa fa-star" v-bind:class="{ checked : isCounted(1, comment) }"></span>
							<span class="fa fa-star" v-bind:class="{ checked : isCounted(2, comment) }"></span>
							<span class="fa fa-star" v-bind:class="{ checked : isCounted(3, comment) }"></span>
							<span class="fa fa-star" v-bind:class="{ checked : isCounted(4, comment) }"></span>
							<span class="fa fa-star" v-bind:class="{ checked : isCounted(5, comment) }"></span>
						</li>
					</ul>
					<div class="comment-description">
						<p>{{comment.description}}</p>
					</div>
				</div>
				<div class="logged-user-comment" v-if="correspondsCommentPermision()">
					<div class="input-group mb-3">
						<div class="input-group-prepend">
							<button class="btn btn-outline-secondary" type="button" v-on:click="comment">Komentarisi</button>
						</div>
						<input type="text" class="form-control comment-holder" placeholder="" aria-label="" aria-describedby="basic-addon1">
						<div class="rating">
						<span class="fa fa-star five-stars" v-on:click="clickedStar('five-stars')"></span>
						<span class="fa fa-star four-stars" v-on:click="clickedStar('four-stars')"></span>
						<span class="fa fa-star three-stars" v-on:click="clickedStar('three-stars')"></span>
						<span class="fa fa-star two-stars" v-on:click="clickedStar('two-stars')"></span>
						<span class="fa fa-star one-star" v-on:click="clickedStar('one-star')"></span>
						</div>
					</div>
					
				</div>

				<div v-if="commentParams.commentSuccess">
					<div class="comment-success">
						<input type="text" class="form-control comment-holder " placeholder="Vas komentar je uspesno poslat! Ceka se odobrenje."
				 		aria-label="" aria-describedby="basic-addon1" readonly>
					</div>
					
				</div>
					
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
			comments : [],
			user_rating : 0,
			number_of_tickets : 0,
			ticket_price : 0,
			customer : {},
			manifestation_date : 0,
			manifestation_passed : false,
			commentParams : {}
		}
	},
	methods: {
		goToEditManifestation(id) {
			location.replace("#/manifestation/edit/" + id);
		},
		countPrice() {
			// TO DO : proveri da li je uneo broj < 1
			$('.reserve-button').attr("disabled", false);
			if(!validateNumberRange(1,5,this.number_of_tickets)) {
				$('.total-price').attr("placeholder", "Mozete rezervisati od 0 do 5 karata u jednoj rezervaciji.");
				$('.total-price').addClass("error");
				$('.reserve-button').attr("disabled", true);
				return;
			}
			axios
				.get("rest/customerservice/get-customer")
				.then(response => {
					this.customer = response.data;
					if(this.customer.customerType.discount == 1) {
						this.ticket_price = this.number_of_tickets * this.manifestation.ticketPrice;
					} else {
						this.ticket_price = this.number_of_tickets * (this.manifestation.ticketPrice * (1-this.customer.customerType.discount));
					}
					$('.total-price').attr("placeholder", this.ticket_price + ",00 RSD");
					$('.total-price').removeClass("error");
				});
		},
		reserve() {
			// TO DO : proveri da li ima karata
			let points = this.ticket_price/1000 * 133;
			axios
				.post("rest/customerservice/reserve-ticket", 
				{
					"points" : points,
					"manifestation" : this.manifestation.id,
					"numberOfTickets" : this.number_of_tickets,
					"user" : this.commentParams.user,
					"ticketPrice" : this.ticket_price
				})
				.then(response => {
					alert("Rezervacija uspesno izvrsena!");
				});
		},
		correspondsCommentPermision() {
			
			return (this.commentParams.user && !this.commentParams.commentSuccess 
							  && validateRange(this.manifestation_date, Date.now())
							  && this.commentParams.userAttended && !this.manifestation_passed);
			
		},
		clickedStar(whichstar) {
			this.user_rating = userRating(this.user_rating);
		},
		comment() {
			let description = $('.comment-holder').val();
			if(this.user_rating == 0 || description.trim() == "") {
				$('.logged-user-comment input').addClass("error");
				return;
			}
			
			comment = { user : "",
						manifestation : this.manifestation.id,
						description : description,
						rating : this.user_rating,
						commentStatus : "NONACTIVE" 
						};
			axios
				.post("rest/commentservice/post-comment",JSON.stringify(comment),{
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					$('.logged-user-comment input').removeClass("error");
					this.commentParams.commentSuccess = true;
				});
		},
		sold(manifestation) {
			sold = manifestation.remainingTickets > 0 ? false : true;
		},
		isCounted(num, comment) {
			return !(num > comment.rating);
		},
		isCountedInAverageRating(num) {
			return !(num > this.commentParams.manifestationRating);
		},
		displayMap() {
			if(this.mapShowed)
				return;
			displayMap();
			this.mapShowed = true;
		}
	},
	mounted() {

		let path = window.location.href;
		let pathparams = path.split("//")[1];
		let manifestationId = pathparams.split("/")[4];

		axios
			.get("rest/manifestationservice/getonemanifestation/" + manifestationId)
			.then(response => {
				this.manifestation = response.data;
				makeDate(this.manifestation);
				
				this.manifestation_date = getManifestationDateInMilliseconds(this.manifestation.date.dayOfMonth,
										this.manifestation.date.monthValue -1,this.manifestation.date.year); 
				if(!validateRange(Date.now(), this.manifestation_date)) {
					this.manifestation_passed = true;
				}
			});

		axios
			.get("rest/commentservice/get-comments/" + manifestationId)
			.then(response => {
				this.comments = response.data;
			});

		axios
			.get("rest/commentservice/get-comment-params/" + manifestationId)
			.then(response => {
				this.commentParams = response.data;
				console.log(this.commentParams);
			});

	}

}
</script>

<style scoped>

#manifestation-page {
	margin-top: 2em;
	padding: 2em;
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

.comment-section {
	margin-top:1em;
	width:100%;
}

.comment-section h3 {
	font-family: 'Quicksand', sans-serif;
	padding-left: 1em;
}

.comment {
	border: 1px solid #bebebe;
	margin-top: 1em;
	/* font-family: 'Quicksand', sans-serif; */
}

.comment-info {
	margin:0;
	padding: 0;
	/* background-color: cornflowerblue; */
	padding: 1em;
}

.comment-date {
	color:#747474;
}

.comment-description {
	padding: 1em;
}

.comment-username {
	margin-right: 1em;
	font-family: 'Quicksand', sans-serif;
}

.comment-info li{
	/* display: inline; */
	list-style: none;
}

.checked {
  color: orange;
}

.map {
	height: 200px;
	width: 100%;
}

.logged-user-comment {
	/* border: 1px solid #bebebe; */
	margin: 1em 2em;
	/* font-family: 'Quicksand', sans-serif; */
}

.comment-success {
	padding: 2em;
}

.comment-success input {
	border: 1px solid  rgb(40,167,69);
	box-shadow: 0 0 10px rgb(40,167,69);
	padding: 2em;
	color: rgb(40,167,69);
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

@media screen and (min-width: 1200px) {
	#manifestation-page .image-holder {
		height: 25em;
	}
}
</style>