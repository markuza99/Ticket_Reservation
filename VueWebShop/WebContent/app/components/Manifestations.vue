<template>
	<div class="container">
		<button type="button" class="btn btn-primary" v-if="role == 'SELLER'" data-toggle="modal" data-target="#createManifestationModal">Dodaj manifestaciju</button>

		<div class="modal fade" id="createManifestationModal" tabindex="-1" role="dialog" aria-labelledby="createManifestationModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createManifestationModalLabel">Dodavanje manifestacije</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
						<div class="form-row">
							<div class="form-group col-md-6">
								<label>Id manifestacije</label>
								<input type="text" id="inputId" class="form-control" v-model="new_manifestation.id"/>
							</div>
							<div class="form-group col-md-6">
							<label for="type">Tip manifestacije</label>
							<select v-model="new_manifestation.type" class="form-control" id="type">
							
							<option v-for="type in types" :key="type.id" >{{ type }}</option>
							</select>
						</div>
						</div>
						<div class="form-row">
							<div class="form-group col-md-6">
								<label for="inputName">Naziv manifestacije</label>
								<input type="text" class="form-control" id="inputName" v-model="new_manifestation.name" >
							
							</div>
							<div class="form-group col-md-6">
								<label for="maintainanceDate">Datum odrzavanja</label>
								<input type="datetime-local" class="form-control" id="maintainanceDate" v-model="new_manifestation.date"/>
								<!-- <small id="data-error" v-if="date_error" class="form-text">Postoji vec manifestacija na istoj lokaciji u isto vreme</small> -->
							</div>
						</div>
						<div class="form-row">
							<div class="form-group col-md-6">
							<label for="inputNumOfSeats">Broj mesta</label>
							<input type="number" class="form-control" id="inputNumOfSeats" v-model="new_manifestation.numberOfSeats">
							<small id="number-of-seats-error" v-if="number_of_seats_error" class="form-text data-error">Nevalidan unos preostalih mesta!</small>
							</div>
							<div class="form-group col-md-6">
								<label for="inputPrice">Cena karte</label>
								<input type="number" class="form-control" id="inputPrice" v-model="new_manifestation.ticketPrice">
								<small id="data-error" v-if="price_error" class="form-text">Nevalidan unos!</small>
						
							</div>
						</div>
						
						<div class="form-row">
							<div class="form-group col-md-6">
								<label for="inputState">Lokacija</label>
								<select id="inputState" v-model="new_manifestation.location" class="form-control">
									<option v-for="location in locations" :key="location.id" >{{ location }}</option>
								</select>
							</div>
							<div class="form-group col-md-6">
								<!-- <label for="file">Poster manifestacije</label>
								<p>NAPOMENA! Sami stavite sliku u folder</p>
								<input type="file" id="file" ref="file" class="form-control-file"> -->
							</div>
						</div>
					</form>
					<p id="error"></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazivanje</button>
                    <button type="button" class="btn btn-primary" v-on:click="createManifestation">Potvrda</button>
                </div>
                </div>
            </div>
        </div>

		<div id="manifestations" class="row">
			<div class="col-lg-4 col-md-4 col-sm-6" v-for="m in manifestations" :key="m.id">
					<div class="manifestation" v-bind:id="m.id" v-on:click="goToManifestation(m.id)">
						<div class="image-holder">
							<img class="fit-img" v-bind:src="'images/' + m.image">
						</div>
						<div class="description-holder">
							<h3 class="name">{{m.name}}</h3>
							<span class="text-info">{{m.location.street}}, </span>
							<span class="text-info">{{m.location.city}}</span><br>
						</div>
						<div class="price" style="text-align:right;" >
							<span class="p-2 bg-danger" style="color:white; position:relative;bottom:6px">{{m.ticketPrice}},00 RSD</span>
						</div>
						<div class="date" style="text-align:right;white-space:nowrap;">
							<div class="bg-warning" style="padding:0 7px;">
							{{m.date.dayOfMonth}}
							{{m.formattedMonth}}
							{{m.date.year}}
							</div>
						</div>	
						
						<div class="text-center w-100 pt-1 pb-1 manifestation-type">{{m.formattedType}}</div>
					</div>
				
			</div>
		</div>
	</div>
</template>

<script>
module.exports = {
	data() {
		return {
			role:null,
			manifestations: [],
			new_manifestation : {
				id: "",
				name: "",
				date:"",
				numberOfSeats: 0,
				remainingNumberOfSeats : 0,
				type: "Koncert",
				status: "NONACTIVE",
				ticketPrice : 0,
				location: "",
				image: "bla"
			},
			types : ["Koncert","Festival","Pozoriste"],
			locations : [],
			number_of_seats_error: false,
			price_error: false
		}
	},
	methods: {
		setManifestationType() {
			switch(this.new_manifestation.type) {
				case "Koncert":
					this.new_manifestation.type = "CONCERT";
					break;
				case "Festival":
					this.new_manifestation.type = "FESTIVAL";
					break;
				case "Pozoriste":
					this.new_manifestation.type = "THEATER";
				default:
					break;
			}
		},
		makeDate : (manifestation) => {
			makeDate(manifestation);
		},
		goToManifestation(id) {
			location.replace("#/manifestation/" + id);
		},
		removeClass() {
			$('#inputNumOfSeats').removeClass("error");
			$('#inputPrice').removeClass("error");
			$('#inputId').removeClass("error");
			$('#error').css("color","red");
			$('#error').html("");
		},
		addClass(id, className) {
			$(id).addClass(className);
		},
		setErrorsToFalse() {
			this.number_of_seats_error = false;
			
		},
		createManifestation() {
			this.new_manifestation.remainingNumberOfSeats = parseInt(this.new_manifestation.numberOfSeats);
			this.new_manifestation.numberOfSeats = parseInt(this.new_manifestation.numberOfSeats);
			this.new_manifestation.ticketPrice = parseInt(this.new_manifestation.ticketPrice);
			this.setErrorsToFalse();
			this.removeClass();

			if(areInputFieldsEmpty(this.new_manifestation)) {
				$('#error').html("Sva polja su obavezna!");
				return;
			}

			if(forbiddenSignInFields(this.new_manifestation)) {
                alert("Ne mozete koristiti ; znak");
                return;
            }
			
			if(!validateNumberRange(10, 100000, parseInt(this.new_manifestation.numberOfSeats))) {
				this.number_of_seats_error = true;
				this.addClass("#inputNumOfSeats", "error");
                return;
            }

            if(!validateNumberRange(100, 100000, parseInt(this.new_manifestation.ticketPrice))) {
				this.price_error = true;
				this.addClass("#inputPrice", "error");
                return;
			}

			let date =  new Date();
			if(!validateRange(date.toISOString(), this.new_manifestation.date)) {
				alert("datum ne valja");
				return;
			}
			
			this.setManifestationType();
			dateparams = this.new_manifestation.date.split("T");
            dateparams[1] = dateparams[1] + ":00";
            this.new_manifestation.date = dateparams.join(" ");
			console.log(this.new_manifestation);
			
			
			axios
				.post("rest/sellerservice/add-manifestation", JSON.stringify(this.new_manifestation), {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					if(response.data != "") {
						this.manifestations = response.data;
						$('#error').html("Manifestacija uspesno dodata!");
						$('#error').css("color","green");
						this.new_manifestation.date = "";
						this.new_manifestation.type = "Koncert";
						this.format();
					} else {
						$('#error').html("Manifestacija pod tim ID-jem vec postoji!");
						this.addClass("#inputId", "error");
					}
				})

		},
		formatType(manifestation) {
			formatType(manifestation);
		},
		format() {
			this.manifestations.forEach(manifestation => this.formatType(manifestation));
			this.manifestations.forEach(manifestation => this.makeDate(manifestation));
		}
	},
	mounted() {
		this.$root.$on('searched-manifestations',(manifestations) => {
			this.manifestations = manifestations;
			this.format();
		});
		this.$root.$on('filtered-manifestations',(manifestations) => {
			this.manifestations = manifestations;
			this.format();
		});
		
		
		axios
			.get("rest/manifestations/active")
			.then(response => {
				this.manifestations = response.data;
				this.format();
			});

		axios
			.get("rest/locations/")
			.then(response => {
				this.locations = response.data;
				this.new_manifestation.location = this.locations[0];
			})

		axios
			.get("rest/users/role")
			.then(response => {
				this.role = response.data;
			});
	}
}
</script>

<style>

#error {
	color: red;
}

#manifestations {
	margin-top:2em;
}

.manifestation {
	border:1px solid darkgray;
	margin-bottom: 20px;
	box-shadow: 0px 0px 12px #767676;
}

.manifestation .image-holder {
	
	overflow:hidden;
	position:relative;
	cursor: pointer;
}

.manifestation .image-holder img {
	max-width: 100%;
	-webkit-transition:all .4s linear;
    transition:all .4s linear;
}

.manifestation .image-holder:hover img {
	-ms-transform:scale(1.2);
	-webkit-transform:scale(1.2);
	transform:scale(1.2);
	transition: all 0.2s;
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

@media screen and (max-width: 1200px) {
  .manifestation .image-holder {
	min-height: 10em;
	max-height: 10em;
  }
}

@media screen and (min-width: 1200px) {
	.manifestation .image-holder {
		height: 15em;
	}
}

@media screen and (max-width: 575px) {
	.manifestation .image-holder {
		min-height: 30em;
	    max-height: 30em;
	}
}



</style>