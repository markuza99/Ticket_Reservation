<template>
	<div>
		<div class="mb-2 mt-5 font-weight-bold">Sortiraj manifestacije</div>
		<div class="list-group" id="list-tab" role="tablist">
			<a class="list-group-item user-status list-group-item-action" data-toggle="list" id="name" v-on:click="selectByWhatToSort('name')">
				Naziv
			</a>
			<a class="list-group-item user-status list-group-item-action active" data-toggle="list" id="date" v-on:click="selectByWhatToSort('date')">
				Datum
			</a>
			<a class="list-group-item user-status list-group-item-action" data-toggle="list" id="price" v-on:click="selectByWhatToSort('price')">
				Cena
			</a>
			<a class="list-group-item user-status list-group-item-action" data-toggle="list" id="location" v-on:click="selectByWhatToSort('location')">
				Lokacija
			</a>
			<select class="custom-select mt-2" v-model="order_by" @change="selectOrderBy()">
				<option value="Asc" selected>Rastuce</option>
				<option value="Desc">Opadajuce</option>
			</select>
		</div>
		<div class="text-right mt-2">
		<button type="submit" class="btn btn-outline-primary" v-on:click="sort">Sortiraj</button>
		</div>
		
		<div class="form-group">
			<div class="mb-2 mt-4 font-weight-bold">Tip manifestacije</div>
			<select class="form-control" v-model="manifestation_type">
				<option value="all">Sve</option>
				<option value="CONCERT">Koncert</option>
				<option value="THEATER">Pozoriste</option>
				<option value="FESTIVAL">Festival</option>
			</select>
		</div>
		<div class="list-group" id="list-tab2" role="tablist">
			<a class="list-group-item user-status list-group-item-action active" data-toggle="list" v-on:click="selectTicketConditions('all')">
				Sve
			</a>
			<a class="list-group-item user-status list-group-item-action" data-toggle="list" v-on:click="selectTicketConditions('soldOut')">
				Rasprodato
			</a>
			<a class="list-group-item user-status list-group-item-action" data-toggle="list" v-on:click="selectTicketConditions('notSoldOut')">
				Nerasprodato
			</a>
		</div>
		<div class="text-right mt-2">
			<button type="submit" class="btn btn-outline-primary mb-5" v-on:click="filter">Filtriraj</button>
		</div>
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
			manifestation_type: "all",
			not_sold_out: false,
			sort_condition:"date",
			sort_by:"",
			order_by:"Asc",
			ticket_condition:"all"
		}
	},
	methods: {
		selectByWhatToSort(type) {
			this.sort_condition = type;
			this.sort_by = this.sort_condition + this.order_by;
        },
		selectOrderBy() {
			this.sort_by = this.sort_condition + this.order_by;
		},
		selectTicketConditions(condition) {
			this.ticket_condition = condition;
			console.log("tu");
		},
		sort() {

			dateFrom = this.dateFrom;
			dateTo = this.dateTo;
			if(this.dateFrom != "") {
				dateFrom = this.dateFrom.trim() + " 00:00:00";
			}
			if(this.dateTo != "") {
				dateTo = this.dateTo.trim() + " 00:00:00";
			}

			axios
				.get("rest/manifestations/sort", {
					params: {
						"name" : this.name,
						"dateFrom":dateFrom,
						"dateTo":dateTo,
						"priceFrom":this.priceFrom,
						"priceTo":this.priceTo,
						"place":this.place,
						"sortBy":this.sort_by
					}
				})
				.then(response => {
					this.$root.$emit('sorted-filtered-manifestations', response.data);
				});
		},
		filter() {
			dateFrom = this.dateFrom;
			dateTo = this.dateTo;
			if(this.dateFrom != "") {
				dateFrom = this.dateFrom.trim() + " 00:00:00";
			}
			if(this.dateTo != "") {
				dateTo = this.dateTo.trim() + " 00:00:00";
			}

			axios
				.get("rest/manifestations/filter", {
				 	params: {
			 			"name" : this.name,
						"dateFrom":dateFrom,
						"dateTo":dateTo,
						"priceFrom":this.priceFrom,
						"priceTo":this.priceTo,
						"place":this.place,
						"sortBy":this.sort_by,
						"manifestationType":this.manifestation_type,
						"ticketCondition":this.ticket_condition
			 		}
				})
				.then(response => {
				 	this.$root.$emit('sorted-filtered-manifestations', response.data);			
				});
		}
	},
	mounted() {
		this.$root.$on('from-search-to-sort-and-filter',(name, dateFrom, dateTo, priceFrom, priceTo, place) => {
			this.name = name;
			this.dateFrom = dateFrom;
			this.dateTo = dateTo;
			this.priceFrom = priceFrom;
			this.priceTo = priceTo;
			this.place = place;
		});
		this.sort_by = this.sort_condition + this.order_by;
	}
}
</script>

<style>


.list-group-item.active {
	background-color: #bdbdbd;
	border-color: #bdbdbd;
}

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