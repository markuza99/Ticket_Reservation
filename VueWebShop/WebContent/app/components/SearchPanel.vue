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
						<input v-model="dateFrom" class="form-control" type="datetime-local" id="datumOdMan"/>
					</li>
					<li class="nav-item">
						<input v-model="dateTo" class="form-control" type="datetime-local" id="datumDoMan"/>
					</li>
					<li class="nav-item">
						<input v-model="priceFrom" class="form-control" type="number" placeholder="Cena od" id="cenaOdMan"/>
					</li>
					<li class="nav-item">
						<input v-model="priceTo" class="form-control" type="number" pattern="[1-9][0-9]*" placeholder="Cena do" id="cenaDoMan"/>
					</li>
					<li class="nav-item">
						<input v-model="place" class="form-control mr-sm-2" type="search" placeholder="Mesto" aria-label="Search" id="mestoMan"/>
					</li>
					<li class = "nav-item">
						<button class="my-2 my-sm-0 search-button" type="submit" v-on:click="search"><i class="fa fa-search"></i></button>
					</li>
				</ul>
			</div>
		</nav>
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
		}
	},
	methods: {
		
		search: function() {
			if(!(validatePrice(this.priceFrom, this.priceTo) && validateRange(this.dateFrom, this.dateTo))) {
				$('.nav').addClass("error");
				return;
			}
			
			axios
			.get("rest/manifestations/search", {
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
				$('.nav').removeClass("error");
				this.$root.$emit('searched-manifestations', response.data);
				this.$root.$emit('from-search-to-sort-and-filter', this.name, this.dateFrom, this.dateTo, this.priceFrom, this.priceTo, this.place);			
			});

		}
	}
}
</script>
<style scoped>


.error {
    outline: none !important;
    border:1px solid red;
    box-shadow: 0 0 10px #f40b0b;
}

.search-panel {
	background-color: #007bff;
	padding: 0px;
	margin:0;
	text-align: center;
	padding-bottom: 20px;
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