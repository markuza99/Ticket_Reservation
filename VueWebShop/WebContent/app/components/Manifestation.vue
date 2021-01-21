<template>
	<div id="manifestation-page" class="container-fluid" v-if="displayOneManifestation">
		<div class="row">
			<div class="col-lg-6 col-md-6 col-sm-12">
				<div class="image-holder">
					<img class="fit-img" v-bind:src="'images/' + manifestation.image">
				</div>
			</div>
			<div class="col-lg-6 col-md-6 col-sm-12">
				<div class="row">
					<div class="col-lg-6 col-md-6">
						<h1>{{manifestation.name}}</h1>
						<div class="manifestation-description">
						<h3 class="text-info">{{manifestation.type}}</h3>
							<div class="date">
								<span>{{manifestation.date.dayOfMonth}}</span>
								<span>{{manifestation.formattedMonth}}</span>
								<span>{{manifestation.date.year}}</span>
							</div>
						
						
						</div>
					</div>
					<div class="col-lg-6 col-md-6">
						<div class="ticket-price bg-danger text-white">{{manifestation.ticketPrice}},00 RSD</div>
					</div>
				</div>
					

					
					<div class="manifestation-location">
						<!-- position relative, bottom 1 -->
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
			manifestation: {}
		}
	},
	mounted() {
		//pretplata na goToManifestation
		console.log("udje");
		this.$root.$on('display-manifestation', (manifestation) => {
			this.manifestation = manifestation;
			console.log(this.manifestation);
			this.displayOneManifestation = true;
		});
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
	padding: 20px;
	position: absolute;
	right: 5em;
}

#manifestation-page .date {
	text-transform: uppercase;
	font-family: 'Quicksand', sans-serif;
}
@media screen and (min-width: 1200px) {
	#manifestation-page .image-holder {
		height: 25em;
	}
}
</style>