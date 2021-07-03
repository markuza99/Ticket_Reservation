<template>
	<div class="maps">
		<div id="map" class="map" ></div>
		<div class="container-fluid">
			<div class="row">
				<div class="col-6 pt-5 pl-5 pr-5">
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
							<input type="text" class="form-control" placeholder="Izaberite lokaciju na mapi" v-model="locationString">
						</div>
						<div class="col-md-6">
							<label>Poster manifestacije</label>
							<input type="file" ref="file" id="file" class="form-control-file" v-on:change="loadFile">
						</div>
						<div class="col-md-6 text-right">
							<button type="button" class="btn btn-green mt-3" v-on:click="createManifestation()">Potvrda</button>
						</div>
					</form>
					<p id="error"></p>
				</div>
				<div class="col-6">
					<div class="image-holder mt-3">
						<h3 v-if="imagePreview" class="text-green image-preview-h">Pregled slike</h3>
						<img id="output" v-bind:src="new_manifestation.image64base">
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
module.exports = {
	data() {
		return {
			currentPosition: {lat: 45.252600, lon: 19.830002, adresa: "Cirpanova 51, Novi Sad"},
			new_manifestation : {
				id: "",
				name: "",
				startTime:"",
				endTime:"",
				numberOfSeats: 0,
				type: "CONCERT",
				ticketPrice : 0,
				locationDTO: {
					longitude: 0,
					latitude: 0,
					street: "",
					number: "0",
					postNumber: 0,
					city: "",
					state: ""
				},
				image64base:""
			},
			locationString: "",
			url : null,
			imagePreview: false,
			updating: false
		}
	},
	methods: {
		createManifestation() {
			$('#error').html("");

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
			
			let path = 'rest/manifestations/'
			if(this.updating) {
				path += 'update'
			} else {
				path += 'add'
			}

			axios
				.post(path, JSON.stringify(this.new_manifestation), {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					if(response.data != "") {
						this.$router.push('/manifestations')
						new Toast({
							message: 'Manifestacija uspesno dodata!',
							type: 'success'
						});
					} else {
						$('#error').html("Datum za manifestaciju se poklapa sa datumom neke druge manifestacije na istoj lokaciji.");
					}
				})
    },
		loadFile(event) {
			this.imagePreview = true
			var reader = new FileReader();
			let vueComponent = this;

			reader.onload = function(){
				var output = document.getElementById('output');
				output.src = reader.result;
				
				vueComponent.new_manifestation.image64base = reader.result;
			}
			reader.readAsDataURL(event.target.files[0]);
		},
		reverseGeolocation: function(coords){
		let self = this;
			fetch('http://nominatim.openstreetmap.org/reverse?format=json&lon=' + coords[0] + '&lat=' + coords[1])
				.then(function(response) {
							return response.json();
					}).then(function(json) {
							console.log(json);

								self.new_manifestation.locationDTO.longitude = coords[1]
								self.new_manifestation.locationDTO.latitude = coords[0]
								self.new_manifestation.locationDTO.street = json.address.road
								if(json.address.house_number == undefined) {
									self.new_manifestation.locationDTO.number = "0"
								} else {
									self.new_manifestation.locationDTO.number = json.address.house_number
								}
								
								self.new_manifestation.locationDTO.postNumber = json.address.postcode
								self.new_manifestation.locationDTO.state = json.address.country
								if(json.address.city != undefined) {
									self.new_manifestation.locationDTO.city = json.address.city
								} else if(json.address.town != undefined) {
									self.new_manifestation.locationDTO.city = json.address.town
								} else if(json.address.village != undefined) {
									self.new_manifestation.locationDTO.city = json.address.village
								}
								
								self.locationString = self.new_manifestation.locationDTO.street + " " + self.new_manifestation.locationDTO.number + ", " + self.new_manifestation.locationDTO.city;

							
							
					});
		},
		showMap : function(){
			let self = this;
			
			var vectorSource = new ol.source.Vector({});
		    var vectorLayer = new ol.layer.Vector({source: vectorSource});
			
			var map = new ol.Map({
		        target: 'map',
		        layers: [
		          new ol.layer.Tile({
		            source: new ol.source.OSM()
		          }),vectorLayer
		        ],
		        view: new ol.View({
		          center: ol.proj.fromLonLat([19.8335, 45.2671]),
		          zoom: 11
		        })
		      });
		      
			var marker;
			  
			setMarker = function(position) {
				marker = new ol.Feature(new ol.geom.Point(ol.proj.fromLonLat(position)));
				vectorSource.addFeature(marker);
			}
			
			map.on("click", function(event) {
				let position = ol.proj.toLonLat(event.coordinate);
				self.currentPosition.lat = parseFloat(position.toString().split(",")[1]).toFixed(6);
				self.currentPosition.lon = parseFloat(position.toString().split(",")[0]).toFixed(6);
				vectorSource.clear();
				setMarker(position);
				self.reverseGeolocation(position);
			});
			
		} 
  },
	mounted () {
		axios
			.get("rest/users/me")
			.then(response => {
				if(response.data == "") this.$router.push('/unauthorized')
			});
		let temp = this;
		this.showMap();

		const manifestationId = this.$route.params.id
		if(manifestationId) {
			//proveri jel user null
			axios
      .get('rest/manifestations/' + manifestationId)
      .then(response => {
				if(response.data == "") {
					this.$router.push('/unauthorized')
				} else {
					this.new_manifestation = response.data
					this.updating = true
					this.imagePreview = true
					this.locationString = this.new_manifestation.locationDTO.street + " " + this.new_manifestation.locationDTO.number + ", " + this.new_manifestation.locationDTO.city
				}
      })
		}
	}
}
</script>

<style scoped>

.image-holder {
	height: 28em;
	border: 1px solid pink;
	overflow: hidden;
}

.map {
	
height: 400px;
width: 100%;
}

.image-preview-h {
	position: absolute;
	top: 2em;
	left:0;
	right:0;
	text-align: center;
	font-weight: normal;
}
</style>