<template>
	<div id="manifestations" class="row" data-masonry='{"percentPosition": true }'>
		<div class="col-lg-4 col-md-4 col-sm-6" v-bind:key="m.id" v-for="m in manifestations">
				<div class="manifestation">
					
					<div class="image-holder">
						<img class="fit-img" v-bind:src="'images/' + m.image">
					</div>
					
					<div class="description-holder">
						
                        <h3 class="name">{{m.name}}</h3>
						
						<span class="text-info">{{m.location.street}}, </span>
						<span class="text-info">{{m.location.city}}</span><br>
						
					</div>
					<div class="date" style="text-align:right;white-space:nowrap;">
						<div class="bg-warning" style="padding:0 7px;">
						{{m.date.dayOfMonth}}
						{{m.formattedMonth}}
						{{m.date.year}}
						</div>
					</div>	
					
					<div class="text-center w-100 pt-1 pb-1 manifestation-type">{{m.type}}</div>
				</div>
			
		</div>
	</div>
</template>

<script>
module.exports = {
	data() {
		return {
			manifestations: []
		}
	},
	methods: {
		makeDate : (manifestation) => {
			
			switch(manifestation.date.month) {
				
				case "JANUARY":
					console.log("tu");
					manifestation.formattedMonth = "januar";
					break;
				case "FEBRUARY":
					manifestation.formattedMonth = "februar";
					break;
				case "MARCH":
					manifestation.formattedMonth = "mart";
					break;
				case "APRIL":
					manifestation.formattedMonth = "mart";
					break;
				case "MAY":
					manifestation.formattedMonth = "mart";
					break;
				case "JUNE":
					manifestation.formattedMonth = "mart";
					break;
				case "JULY":
					manifestation.formattedMonth = "mart";
					break;
				case "AUGUST":
					manifestation.formattedMonth = "mart";
					break;
				case "SEPTEMBER":
					manifestation.formattedMonth = "mart";
					break;
				case "OCTOBER":
					manifestation.formattedMonth = "mart";
					break;
				case "NOVEMBER":
					manifestation.formattedMonth = "mart";
					break;
				case "DECEMBER":
					manifestation.formattedMonth = "mart";
					break;
				default:
					console.log("nista");
			}
		}
	},
	mounted() {
		axios
			.get("rest/manifestationservice/getall")
			.then(response => {
				
				this.manifestations = response.data;
				console.log(this.manifestations);
				
				this.manifestations.forEach(manifestation => this.makeDate(manifestation));
			});
	}
}
</script>

<style>

#manifestations {
	margin-top:2em;
}

.manifestation {
	border:1px solid darkgray;
	margin-bottom: 20px;
	box-shadow: 0px 0px 12px #767676;
}

.image-holder img {
	max-width: 100%;
	/* box-shadow: 0px 0px 8px #000000; */
}

.image-holder {
	overflow: hidden;
	
}

@media screen and (max-width: 1200px) {
  .image-holder {
	min-height: 10em;
	max-height: 10em;
  }
}

@media screen and (min-width: 1200px) {
	.image-holder {
		height: 15em;
	}
}

@media screen and (max-width: 575px) {
	.image-holder {
		min-height: 30em;
	    max-height: 30em;
	}
}

.manifestation .date {
	text-transform: uppercase;
	font-family: 'Quicksand', sans-serif;
}

.manifestation .name {
	font-size: 20px;
	text-transform: uppercase;
	color:#767676;
}

.manifestation .description-holder {
	padding:20px;
}

.manifestation .manifestation-type {
    background-color: #bbbb;
}

</style>