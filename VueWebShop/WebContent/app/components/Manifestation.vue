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

				<div v-if="comment_success">
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
			manifestation_rating : 0,
			user : null, //treba mi i user,
			comment_success : false,
			user_attended : false
		}
	},
	methods: {
		correspondsCommentPermision() {
			console.log(this.user + " user")
			console.log(!this.comment_success + " comment");
			console.log("hey");
			return (this.user && !this.comment_success 
							  && validateDateRange(this.manifestation.date, Date.now()));
							 // 
			//TO DO -- dodati i && user_attended
		},
		clickedStar(whichstar) {
			$('.one-star').removeClass("checked");
			$('.two-stars').removeClass("checked");
			$('.three-stars').removeClass("checked");
			$('.four-stars').removeClass("checked");
			$('.five-stars').removeClass("checked");

			switch(whichstar) {
				case "one-star":
					console.log("jedna");
					this.user_rating = 1;

					$('.one-star').addClass("checked");
					break;
				case "two-stars":
					this.user_rating = 2;

					$('.one-star').addClass("checked");
					$('.two-stars').addClass("checked");
					break;
				case "three-stars":
					this.user_rating = 3;

					$('.one-star').addClass("checked");
					$('.two-stars').addClass("checked");
					$('.three-stars').addClass("checked");
					break;
				case "four-stars":
					this.user_rating = 4;

					$('.one-star').addClass("checked");
					$('.two-stars').addClass("checked");
					$('.three-stars').addClass("checked");
					$('.four-stars').addClass("checked");
					break;
				case "five-stars":
					this.user_rating = 5;

					$('.one-star').addClass("checked");
					$('.two-stars').addClass("checked");
					$('.three-stars').addClass("checked");
					$('.four-stars').addClass("checked");
					$('.five-stars').addClass("checked");
					break;
				default:
					this.user_rating = 0;
			}
		},
		comment() {
			let description = $('.comment-holder').val();
			if(this.user_rating == 0) {
				//ne moze da komentarise ako nije ocenio
				$('.logged-user-comment input').addClass("error");
				return;
			}
			
			comment = { user : "",
						manifestation : this.manifestation.id,
						description : description,
						rating : this.user_rating, 
						};	//user-a izvlacimo iz sesije
			axios
				.post("rest/commentservice/postcomment",JSON.stringify(comment),{
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					$('.logged-user-comment input').removeClass("error");
					this.comment_success = true;
				});
		},
		sold(manifestation) {
			sold = manifestation.remainingTickets > 0 ? false : true;
		},
		isCounted(num, comment) {
			return !(num > comment.rating);
		},
		isCountedInAverageRating(num) {
			return !(num > this.manifestation_rating);
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
				if(response.data == "") {
					makeErrorPage();
				}
				this.manifestation = response.data;
				makeDate(this.manifestation);
				console.log(this.manifestation);
			});

		axios
			.get("rest/userservice/testlogin")
			.then(response => {
				this.user = response.data;
			});
		
		axios
			.get("rest/commentservice/getcomments/" + manifestationId)
			.then(response => {
				this.comments = response.data;
			});

		axios
			.get("rest/commentservice/usercommented/" + manifestationId)
			.then(response => {
				if(response.data) {
					this.comment_success = true;
				}
			});

		axios
			.get("rest/commentservice/manifestationrating/" + manifestationId)
			.then(response => {
				console.log(response.data);
				this.manifestation_rating = response.data;
			})

		// TO DO
		// axios
		// 	.get("rest/commentservice/userattended/" + manifestationId)
		// 	.then(response => {
		// 		if(response.data) {
		// 			this.user_attended = true;
		// 		}
		// 	});
	}

}
</script>

<style scoped>

#manifestation-page {
	margin-top: 2em;
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
	margin: 1em 2em;
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

@media screen and (min-width: 1200px) {
	#manifestation-page .image-holder {
		height: 25em;
	}
}
</style>