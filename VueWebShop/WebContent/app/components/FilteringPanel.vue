<template>
<div>
	<h4 class="latest-manifestations mr-5 mt-3">Najskorije manifestacije</h4>
	<hr>
	<ul class="filtering-panel d-flex justify-content-between">
		<li>
			<select class="form-select pl-3 pr-3 pt-2 pb-2 mr-3" v-model="type">
					<option selected>SVE</option>
					<option>Koncert</option>
					<option>Pozoriste</option>
					<option>Festival</option>
			</select>
			<span class="mr-1">Nerasprodato: </span>
			<input type="checkbox" v-model="not_sold_out">
		</li>
		<li>
			<button type="button" class="btn btn-primary" v-on:click="filter">Filtriraj</button>
		</li>
	</ul>
	</div>
</template>

<script>
module.exports = {
	data() {
		return {
			name: "",
			dateFrom:"",
			dateTo:"",
			place:"",
			priceFrom:"",
			priceTo:"",
			selected: "",
			type: "SVE",
			not_sold_out: true
		}
	},
	methods: {
		setType() {
			if(this.type == "Koncert") {
				this.type = "CONCERT";
			} else if(this.type == "Festival") {
				this.type = "FESTIVAL";
			} else if(this.type == "Pozoriste") {
				this.type = "THEATER";
			}
		},
		returnType() {
			if(this.type == "CONCERT") {
				this.type = "Koncert";
			} else if(this.type == "FESTIVAL") {
				this.type = "Festival";
			} else if(this.type == "THEATER") {
				this.type = "Pozoriste";
			} else {
				this.type = "SVE";
			}
		},
		filter() {
			this.setType();
			axios
			.get("rest/manifestations/filter", {
				params: {
					"name" : this.name,
					"dateFrom":this.dateFrom,
					"dateTo":this.dateTo,
					"place":this.place,
					"priceFrom":this.priceFrom,
					"priceTo":this.priceTo,
					"selected":this.selected,
					"type": this.type,
					"not_sold_out": this.not_sold_out
				}
			})
			.then(response => {
				$('.nav').removeClass("error");
				this.returnType();
				this.$root.$emit('filtered-manifestations',response.data);			
			});
		}
	},
	mounted() {
		this.$root.$on('from-search-to-filter',(manifestations,name,dateFrom,dateTo,place,priceFrom,priceTo,selected) => {
			this.manifestations = manifestations;
			this.name = name;
			this.dateFrom = dateFrom;
			this.dateTo = dateTo;
			this.place = place;
			this.PriceFrom = priceFrom;
			this.priceTo = priceTo;
			this.selected = selected;
		});

	}
}
 </script>

 <style>

.latest-manifestations {
	font-family: 'Open Sans Condensed', sans-serif;
	font-family: 'Quicksand', sans-serif;
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