<template>
	<div class="maps">
		<div id="map" class="map" ></div>
        <label for="lon">Lon:</label>
        <input type="text" name="lon" id="lon" disabled>
        <br>
        <label for="lat">Lat:</label>
        <input type="text" name="lat" id="lat" disabled>
        <br>
        <label for="street">Street:</label>
        <input type="text" name="street" id="street" disabled>
        <br>
        <label for="number">Number:</label>
        <input type="text" name="number" id="number" disabled>
        <br>
        <label for="post">Post number:</label>
        <input type="text" name="post" id="post" disabled>
        <br>
        <label for="city">City: </label>
        <input type="text" name="city" id="city" disabled>
        <br>
        <label for="state">State:</label>
        <input type="text" name="state" id="state" disabled>
	</div>
</template>

<script>
  module.exports = {
    data() {
      return {
            currentPosition: {lat: 45.252600, lon: 19.830002, adresa: "Cirpanova 51, Novi Sad"}
      }
	},
    methods: {
      	reverseGeolocation: function(coords){
			let self = this;
		   	fetch('http://nominatim.openstreetmap.org/reverse?format=json&lon=' + coords[0] + '&lat=' + coords[1])
			     .then(function(response) {
			            return response.json();
			        }).then(function(json) {
			            console.log(json);
			            if (json.address.house_number == undefined){
			            	if (json.address.building == undefined){
			            		self.currentPosition.adresa = json.address.road + ", " + json.address.city;
                                
                            }else{
			            		self.currentPosition.adresa = json.address.road + ", " + json.address.building + ", " + json.address.city;
			            	}
			            }else{
			            	self.currentPosition.adresa = json.address.road + " " + json.address.house_number + ", " + json.address.city;
                            $("#lat").val(coords[1]);
                            $("#lon").val(coords[0]);
                            $("#street").val(json.address.road);
                            $("#number").val(json.address.house_number);
                            $("#post").val(json.address.postcode);
                            $("#state").val(json.address.state);
                            $("#city").val(json.address.city);

                        }
			            
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
			
			map.on("click", function(event){
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
		let temp = this;
        this.showMap();
	}
  }
</script>

<style scoped>

    .map {
      
    height: 400px;
    width: 100%;
    }
</style>