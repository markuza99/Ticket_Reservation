<template>
	<div id="manifestation-page" class="container-fluid" v-if="displayOneManifestation">
		<div class="row">
			<div class="col-lg-6 col-md-6 col-sm-12">
				<div class="image-holder">
					<img class="fit-img" v-bind:src="'images/' + manifestation.image">
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
				
				
					<div class="ticket-price text-danger text-white">{{manifestation.ticketPrice}},00 RSD</div>
					<div class="text-success">{{manifestation.status}}</div>
				<!-- </div> -->
					<div class="rating">
						Oceni:
						<!-- <span class="fa fa-star checked" v-for="star in manifestation.rating" :key="star">

						</span> -->
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star"></span>
						<span class="fa fa-star"></span>
					</div>
					
					<div class="manifestation-location map" id="map" ref="map">
						<!-- position relative, bottom 1 -->
						<div v-if="!mapShowed" v-on:click="klik">Pogledaj mapu</div>
					</div>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="comment-section">
				<h3 class="text-center">Komentari</h3>
				<!-- <hr> -->
				<div class="comment">
					<ul class="comment-info bg-light">
						<li class="text-warning comment-username">Petar Markovic</li>
						<li class="comment-date">20 Oktobar 2020</li>
						<li>
							<div class="rating">
								<!-- <span class="fa fa-star checked" v-for="star in manifestation.rating" :key="star">

								</span> -->
								<span class="fa fa-star checked"></span>
								<span class="fa fa-star checked"></span>
								<span class="fa fa-star checked"></span>
								<span class="fa fa-star"></span>
								<span class="fa fa-star"></span>
							</div>
						</li>
					</ul>
					<div class="comment-description">
						<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod 
							tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, 
							quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
							Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat 
							nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia 
							deserunt mollit anim id est laborum.
						</p>
					</div>
				</div>

				<div class="comment">
					<ul class="comment-info">
						<li>Petar Markovic</li>
						<li>20 Oktobar 2020</li>
					</ul>
					<div class="comment-description">
						<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod 
							tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, 
							quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
							Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat 
							nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia 
							deserunt mollit anim id est laborum.
						</p>
					</div>
				</div>

				<div class="comment">
					<ul class="comment-info">
						<li>Petar Markovic</li>
						<li>20 Oktobar 2020</li>
					</ul>
					<div class="comment-description">
						<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod 
							tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, 
							quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
							Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat 
							nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia 
							deserunt mollit anim id est laborum.
						</p>
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
			displayOneManifestation : false,
			manifestation: {},
			user_checked : false,
			mapShowed : false
		}
	},
	methods: {
		klik : () => {
			console.log("konacno");
			displayMap();
			mapShowed = true;
		}
	},
	mounted() {
		//pretplata na goToManifestation
		console.log("udje");
		this.$root.$on('display-manifestation', (manifestation) => {
			this.manifestation = manifestation;
			console.log(this.manifestation);
			this.displayOneManifestation = true;
			//setTimeout(displayMap(),3000);

			// var map = new ol.Map({
			// 	target: this.$refs['map'],
			// 	layers: [
			// 	new ol.layer.Tile({
			// 		source: new ol.source.OSM()
			// 	})
			// 	],
			// 	view: new ol.View({
			// 		center: ol.proj.fromLonLat([37.41, 8.82]),
			// 		zoom: 4
			// 	})
			// });
		});
		//displayMap();
		// axios
			// .get('rest/manifestationservice/manifestation/' + )
			// .then();
	}

}
</script>

<style scoped>
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

.manifestation-info .rating {
	position: absolute;
	right:5em;
}

.comment-section {
	margin-top:1em;
}

.comment-section h3 {
	font-family: 'Quicksand', sans-serif;
}

.comment-section .rating {

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
	display: inline;
	list-style: none;
}

.checked {
  color: orange;
}

.map {
	height: 200px;
	width: 100%;
}

@media screen and (min-width: 1200px) {
	#manifestation-page .image-holder {
		height: 25em;
	}
}
</style>