<template>
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
</template>

<script>
module.exports = {
    data() {
		return {
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
    mounted() {
        axios
			.get("rest/locations/")
			.then(response => {
				this.locations = response.data; //promeniti
				this.new_manifestation.location = this.locations[0];
			})
    },
    methods: {
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
                $('#error').html("Ne mozete koristiti ; znak.");
                return;
            }
			
			if(!validateNumberRange(1, 100000, parseInt(this.new_manifestation.numberOfSeats))) {
				this.number_of_seats_error = true;
				this.addClass("#inputNumOfSeats", "error");
                return;
            }

            if(!validateNumberRange(1, 100000, parseInt(this.new_manifestation.ticketPrice))) {
				this.price_error = true;
				this.addClass("#inputPrice", "error");
                return;
			}

			let date =  new Date();
			if(!validateRange(date.toISOString(), this.new_manifestation.date)) {
				$('#error').html("Odabran je pogresan datum.");
				return;
			}
			
			this.setManifestationType();
            this.formatDate();

			axios
				.post("rest/sellers/add-manifestation", JSON.stringify(this.new_manifestation), {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					if(response.data != "") {
                        this.$root.$emit('create-manifestation',response.data);
						$('#error').html("Manifestacija uspesno dodata!");
						$('#error').css("color","green");
						this.new_manifestation.date = "";
						this.new_manifestation.type = "Koncert";
						
					} else {
						$('#error').html("Manifestacija pod tim ID-jem vec postoji!");
						this.addClass("#inputId", "error");
					}
				})

		},
        formatDate() {
            dateparams = this.new_manifestation.date.split("T");
            dateparams[1] = dateparams[1] + ":00";
            this.new_manifestation.date = dateparams.join(" ");
        },
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
        setErrorsToFalse() {
			this.number_of_seats_error = false;
			
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
		}
    }
}
</script>