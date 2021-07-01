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
					<div class="col-md-6">
						<label>Broj mesta</label>
						<input type="number" class="form-control" v-model="new_manifestation.numberOfSeats">
					</div>
					<div class="col-md-6">
						<label>Cena karte</label>
						<input type="number" class="form-control" v-model="new_manifestation.ticketPrice">
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
				<button type="button" class="btn btn-secondary" data-dismiss="modal" id="cancel-create-manifestation">Otkazivanje</button>
				<button type="button" class="btn btn-green" v-on:click="createManifestation()">Potvrda</button>
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
				ticketPrice : 0,
				location: "",
				image64base:""
			},
			
			url : null,
			locations : []
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
		$('#error').html("");
		this.new_manifestation.location = this.$refs.locationId.value;

		manifestation_to_check = {
			id: this.new_manifestation.id,
			name: this.new_manifestation.name
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
			$('#error').html("Pogresan unos broja mesta.");
			return;
		}

		if(!isNumberInRange(1, 100000, parseInt(this.new_manifestation.ticketPrice))) {
			$('#error').html("Pogresan unos cene karte.");
			return;
		}

		if(!validateRange(this.new_manifestation.startTime, this.new_manifestation.endTime)) {
			$('#error').html("Pogresan datum.");
			return;
		}

		let date =  new Date();
		if(!validateRange(date.toISOString(), this.new_manifestation.startTime)) {
			$('#error').html("Odabran je pogresan datum.");
			return;
		}
		
		this.new_manifestation.id.trim()
		this.new_manifestation.name.trim()
		
		axios
			.post("rest/manifestations/add", JSON.stringify(this.new_manifestation), {
				headers: {'content-type':'application/json'}
			})
			.then(response => {
				if(response.data != "") {
					document.getElementById('cancel-create-manifestation').click();
					this.cleanModal();
				} else {
					$('#error').html("Pogresan unos.");
				}
			})
    },
		loadFile(event) {

			var reader = new FileReader();
			let vueComponent = this;

			reader.onload = function(){
				var output = document.getElementById('output');
				output.src = reader.result;
				
				vueComponent.new_manifestation.image64base = reader.result;
			}
			reader.readAsDataURL(event.target.files[0]);
		},
		cleanModal() {
			this.new_manifestation = {
				id: "",
				name: "",
				startTime:"",
				endTime:"",
				numberOfSeats: 0,
				type: "CONCERT",
				ticketPrice : 0,
				location: "",
				image64base:""
			}

			output.src = "";
			this.$refs.locationId.value = "";
			this.$refs.file.value = "";
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