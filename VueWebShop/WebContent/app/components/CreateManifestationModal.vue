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
				<form class="row g-3">
					<div class="col-md-6">
						<label>Id manifestacije</label>
						<input type="text" class="form-control" v-model="new_manifestation.id">
					</div>
					<div class="col-md-6">
						<label>Tip manifestacije</label>
						<select class="form-control" v-model="new_manifestation.type">
							<option value="CONCERT">Koncert</option>
							<option value="THEATER">Pozoriste</option>
							<option value="FESTIVAL">Festival</option>
						</select>
					</div>
					<div class="col-12">
						<label>Naziv manifestacije</label>
						<input type="text" class="form-control" v-model="new_manifestation.name" >
					</div>
					<div class="col-md-6">
						<label>Datum pocetka</label>
						<input type="datetime-local" class="form-control" v-model="new_manifestation.startTime"/> 
					</div>
					<div class="col-md-6">
						<label>Datum kraja</label>
						<input type="datetime-local" class="form-control" v-model="new_manifestation.endTime">
					</div>
					<small id="data-error" v-if="date_error" class="form-text">Postoji vec manifestacija na istoj lokaciji u isto vreme</small>
					<div class="col-md-6">
						<label>Broj mesta</label>
						<input type="number" class="form-control" v-model="new_manifestation.numberOfSeats">
						<small id="number-of-seats-error" v-if="number_of_seats_error" class="form-text data-error">Nevalidan unos preostalih mesta!</small>
					</div>
					<div class="col-md-6">
						<label>Cena karte</label>
						<input type="number" class="form-control" v-model="new_manifestation.ticketPrice">
						<small id="data-error" v-if="price_error" class="form-text">Nevalidan unos!</small>
					</div>
					<div class="col-md-12">
						<label>Lokacija</label>
						<input list="locations" class="form-control" ref="locationId">
						<datalist id="locations">
							<option v-for="location in locations" :key="location.id" :value="location.id" v-on:click="setLocationId(location.id)">
								{{location.street}} {{location.number}}, {{location.city}}
							</option>
						</datalist>
					</div>
					<div class="col-md-6">
						<label>Poster manifestacije</label>
						<input type="file" ref="file" id="file" class="form-control-file" v-on:change="loadFile">
					</div>
				</form>
				<p id="error"></p>
				<div class="image-holder">
					<img id="output">
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazivanje</button>
				<button type="button" class="btn btn-primary" v-on:click="createManifestation()">Potvrda</button>
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
				startTime:"",
				endTime:"",
				numberOfSeats: 0,
				type: "CONCERT",
				status: "NONACTIVE",
				ticketPrice : 0,
				location: "",
				imageName: "",
				image64base:""
			},
			
			img_url : null,
			url : null,
			locations : [],
			date_error: false,
			number_of_seats_error: false,
			price_error: false
		}
	},
    mounted() {
        axios
			.get("rest/locations/")
			.then(response => {
				this.locations = response.data;
			});
    },
    methods: {
        createManifestation() {

			console.log("?");
			this.new_manifestation.location = this.$refs.locationId.value;
			
			// this.new_manifestation.numberOfSeats = parseInt(this.new_manifestation.numberOfSeats);
			// this.new_manifestation.ticketPrice = parseInt(this.new_manifestation.ticketPrice);
			// this.setErrorsToFalse();
			// this.removeClass();
			manifestation_to_check = {
				id: this.new_manifestation.id,
				name: this.new_manifestation.name,
				imageName : this.new_manifestation.imageName
			}

			if(areInputFieldsEmpty(this.new_manifestation)) {
				$('#error').html("Sva polja su obavezna!");
				return;
			}

			if(forbiddenSignInFields(manifestation_to_check)) {
                $('#error').html("Ne mozete koristiti ; znak.");
                return;
            }
			
			if(!isNumberInRange(1, 100000, parseInt(this.new_manifestation.numberOfSeats))) {
				this.number_of_seats_error = true;
				this.addClass("#inputNumOfSeats", "error");
                return;
            }

            if(!isNumberInRange(1, 100000, parseInt(this.new_manifestation.ticketPrice))) {
				this.price_error = true;
				this.addClass("#inputPrice", "error");
                return;
			}

			//date range
			if(!validateRange(this.new_manifestation.startTime, this.new_manifestation.endTime)) {
				$('#error').html("Pogresan datum.");
			 	return;
			}
			let date =  new Date();
			if(!validateRange(date.toISOString(), this.new_manifestation.startTime)) {
				$('#error').html("Odabran je pogresan datum.");
			 	return;
			}
			
            this.formatDate();
			console.log("sve kul");

			// console.log(this.new_manifestation);
			// axios
			// 	.post("rest/manifestations/add", JSON.stringify(this.new_manifestation), {
			// 		headers: {'content-type':'application/json'}
			// 	})
			// 	.then(response => {
					// if(response.data != "") {
                    //     this.$root.$emit('create-manifestation',response.data);
					// 	$('#error').html("Manifestacija uspesno dodata!");
					// 	$('#error').css("color","green");
					// 	this.new_manifestation.date = "";
					// 	this.new_manifestation.type = "Koncert";
						
					// } else {
					// 	$('#error').html("Manifestacija pod tim ID-jem vec postoji!");
					// 	this.addClass("#inputId", "error");
					// }
				// })
        },
		loadFile(event) {
			var filename = document.getElementById('file').value;
			parts = filename.split("\\");
			this.new_manifestation.imageName = parts[parts.length-1];

			var reader = new FileReader();
			let vueComponent = this;

			reader.onload = function(){
				var output = document.getElementById('output');
				output.src = reader.result;
				
				vueComponent.new_manifestation.image64base = reader.result;
			}
			reader.readAsDataURL(event.target.files[0]);
		},

        formatDate() {
            dateparams = this.new_manifestation.date.split("T");
            dateparams[1] = dateparams[1] + ":00";
            this.new_manifestation.date = dateparams.join(" ");
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

<style scoped>

.image-holder {
  overflow: hidden;
  position: relative;
  cursor: pointer;
}

.image-holder img {
  max-width: 100%;
}
</style>