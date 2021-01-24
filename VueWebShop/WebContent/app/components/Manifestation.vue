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
				<div class="logged-user-comment" v-if="user && !comment_success">
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
			user_checked : false,
			mapShowed : false,
			comments : [],
			rating : 0,
			user : null, //treba mi i user,
			comment_success : false
		}
	},
	methods: {
		clickedStar(whichstar) {
			console.log("pozvano u zvezdama:" + this.manifestation.id)
			console.log(whichstar);
			switch(whichstar) {
				case "one-star":
					console.log("jedna");
					this.rating = 1;
					break;
				case "two-stars":
					this.rating = 2;
					break;
				case "three-stars":
					this.rating = 3;
					break;
				case "four-stars":
					this.rating = 4;
					break;
				case "five-stars":
					this.rating = 5;
					break;
				default:
					this.rating = 0;
			}
			this.colorStars(whichstar);
		},
		colorStars(whichstar) {
			//svi su crni na pocetku
			console.log(whichstar);
			$('.one-star').removeClass("checked");
			$('.two-stars').removeClass("checked");
			$('.three-stars').removeClass("checked");
			$('.four-stars').removeClass("checked");
			$('.five-stars').removeClass("checked");
			switch(whichstar) {
				case "five-stars":
					$('.one-star').addClass("checked");
					$('.two-stars').addClass("checked");
					$('.three-stars').addClass("checked");
					$('.four-stars').addClass("checked");
					$('.five-stars').addClass("checked");
					break;
				case "four-stars":
					$('.one-star').addClass("checked");
					$('.two-stars').addClass("checked");
					$('.three-stars').addClass("checked");
					$('.four-stars').addClass("checked");
					break;
				case "three-stars":
					$('.one-star').addClass("checked");
					$('.two-stars').addClass("checked");
					$('.three-stars').addClass("checked");
					break;
				case "two-stars":
					$('.one-star').addClass("checked");
					$('.two-stars').addClass("checked");
					break;
				case "one-star":
					$('.one-star').addClass("checked");
					break;
				default:
					this.rating = 0;
			}
		},
		comment() {
			let description = $('.comment-holder').val();
			if(this.rating == 0) {
				//ne moze da komentarise ako nije ocenio
				$('.logged-user-comment input').addClass("error");
				return;
			}
			
			comment = { user : "",
						manifestation : this.manifestation.id, //undefined
						description : description,
						rating : this.rating,  //undefined
						};	//user-a izvlacimo iz sesije
			console.log(JSON.stringify(comment));
			console.log(comment);
			axios
				.post("rest/manifestationservice/postcomment",JSON.stringify(comment),{
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					$('.logged-user-comment input').removeClass("error");
					this.comment_success = true;
				});
		},
		sold(manifestation) {
			if(manifestation.remainingTickets > 0) {
				sold = false;
			}
			sold = true;
		},
		isCounted(num, comment) {
			return num > comment.rating
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
		console.log(manifestationId);

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
			.get("rest/manifestationservice/getcomments/" + manifestationId)
			.then(response => {
				this.comments = response.data;
				console.log(this.comments.length);
				console.log(this.comments);
			});
		//treba axios za komentar(da li je vec poslao komentar)
		//ako je nasao id i manifestaciju i ako je komentar NONACTIVE onda se ceka
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