<template>
	<div class="search-panel">
	
		<nav class="navbar navbar-expand-lg navbar-light bg-primary">
		
		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse search-navbar" id="navbarSupportedContent">
			<ul class="nav navbar-nav navbar-center mr-auto">
			<li class="nav-item">
				<input v-model="name" type="text" class="form-control" placeholder="Naziv manifestacije..." id="nazivMan"/>
			</li>
			<li class="nav-item">
				<input v-model="dateFrom" class="form-control" type="date" id="datumOdMan"/>
			</li>
			<li class="nav-item">
				<input v-model="dateTo" class="form-control" type="date" id="datumDoMan"/>
			</li>
			
			<li class="nav-item">
				<input v-model="priceFrom" class="form-control" type="text" placeholder="Cena od" id="cenaOdMan"/>
			</li>
			<li class="nav-item">
				<input v-model="priceTo" class="form-control" type="text" placeholder="Cena do" id="cenaDoMan"/>
			</li>
			<li>
				<input v-model="place" class="form-control mr-sm-2" type="search" placeholder="Mesto" aria-label="Search" id="mestoMan"/>
			</li>
			<li>
				<button class="my-2 my-sm-0 search-button" type="submit" v-on:click="search"><i class="fa fa-search"></i></button>
			</li>
			</ul>
			
				
				
		</div>
		</nav>
	</div>
</template>

<script>
var x = require("./SearchPanel.vue");
var y = require("./FilteringPanel.vue");
module.exports = {
	props: ['manifestations'],
	data() {
		return {	
			name: "",
			dateFrom:"",
			dateTo:"",
			place:"",
			priceFrom:"",
			priceTo:""		
		}
	},
	methods: {
	//za front provera:
	//za datum:
	//da li je start before end 
	// za cenu:
	//da li je vrednost negativna,
	// da li je max veci od nula
	//da li je broj
	
		search: function() {
			axios
			.get("rest/manifestationservice/search", {
				params: {
					"name" : this.name,
					"dateFrom":this.dateFrom,
					"dateTo":this.dateTo,
					"place":this.place,
					"priceFrom":this.priceFrom,
					"priceTo":this.priceTo
				}
			})
			.then(response => {
				this.$root.$emit('searched-manifestations',response.data);			
			});

		}
	}
}
</script>
<style scoped>

.search-panel {
	background-color: #007bff;
	padding: 0px;
	margin:0;
	/* height: 20px; */
	/* font-size: 0; */
	text-align: center;
	padding-bottom: 20px;
	/* margin-top:19px; */
}

.navbar-nav.navbar-center {
    margin-left: auto;
	margin-right: auto;
	left: 0;
	right: 0;
}


.search-panel li input {
	background-color: #fff;
	border-radius: 0;
	border:none;
	height:calc(2.5em + .75rem + 2px);
	padding:.375rem .75rem;
	box-shadow: 0px 0px 8px #474747;
}

.search-panel li {
	/* padding: 10px 0 10px 0; */
}

.search-panel li input:focus {
	outline:0;
}

@media only screen and (max-width: 992px) {
	.search-panel .search-button {
		width: 100%;
		text-align: right;
	}

	#navbarSupportedContent {
		margin-top: 20px;
	}
}

.search-panel .search-button {
	padding: 15px 23px;
	border:none;
	box-shadow: 0px 0px 8px #000000;
}

.search-panel .search-button:hover {
	background-color: #dddddd;
	color:rgb(0, 123, 255);
	transition:color .15s ease-in-out, background-color .15s ease-in-out;
}
.search-panel i {
	border:none;
}

.search-panel .navbar .navbar-toggler{
	background-color: #ffffff;
}

</style>