<template>
<div>
	<h4 class="latest-manifestations mr-5 mt-3">Najskorije manifestacije</h4>
	<hr>
	<ul class="filtering-panel d-flex justify-content-between">
				<li>
					
					<select class="form-select pl-3 pr-3 pt-2 pb-2 mr-3" v-model="izborTipa">
							<option>SVE</option>
							<option>CONCERT</option>
							<option>THEATER</option>
							<option>FESTIVAL</option>
					</select>
					<span class="mr-1">Nerasprodato: </span>
					<input type="checkbox" id="" name="" value="Boat" v-model="nijeRasprodato">
					
				</li>
				
				<li>
					<button type="button" class="btn btn-primary" v-on:click="filtriraj">Filtriraj</button>
				</li>
				
				

	</ul>
	</div>
</template>

 <script>
module.exports = {
	data() {
		return {
			manifestations: [],
			name: "",
			dateFrom:"",
			dateTo:"",
			place:"",
			priceFrom:"",
			priceTo:"",
			selected: "",
			izborTipa: "",
			nijeRasprodato: true
		}
	},
	methods: {
		filtriraj : function() {
			axios
			.get("rest/manifestationservice/filter", {
				params: {
					"name" : this.name,
					"dateFrom":this.dateFrom,
					"dateTo":this.dateTo,
					"place":this.place,
					"priceFrom":this.priceFrom,
					"priceTo":this.priceTo,
					"selected":this.selected,
					"izborTipa": this.izborTipa,
					"nijeRasprodato": this.nijeRasprodato
				}
			})
			.then(response => {
				$('.nav').removeClass("error");
				this.$root.$emit('filtered-manifestations',response.data);
				// this.$root.$emit('from-search-to-filter', response.data);			
			});
		}
	},
	mounted() {
		//pretplata na metode
		// search panel prosledjuje manifestacije u Manifestations
		this.$root.$on('from-search-to-filter',(manifestations,name,dateFrom,dateTo,place,priceFrom,priceTo,selected) => {
			// console.log("pretplata na search panel");
			this.manifestations = manifestations;
			this.name = name;
			this.dateFrom = dateFrom;
			this.dateTo = dateTo;
			this.place = place;
			this.PriceFrom = priceFrom;
			this.priceTo = priceTo;
			this.selected = selected;
			// this.manifestations.forEach(manifestation => this.makeDate(manifestation));
		});
	}
}
 </script>

 <style>

.latest-manifestations {
	font-family: 'Open Sans Condensed', sans-serif;
	font-family: 'Quicksand', sans-serif;
	/* margin-top: 20px; */
	font-size: 25px;
}

.filtering-panel {
	margin-top: 20px;
	list-style: none;
}

.filtering-panel select {
	border: 1px solid rgb(0, 123, 255);
	border-radius: 5px;
}

 </style>