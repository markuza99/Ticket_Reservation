<template>
  <div v-if="manifestation" id="manifestation">
    <div class="container">
      <div class="row">
        <div class="col-lg-9 col-md-9">
          <div class="image-holder manifestation-image-holder">
            <img
              class="fit-img"
              v-bind:class="{ gray: manifestation_sold }"
              v-bind:src="manifestation.image"
            />
          </div>
          <div>
            <div class="row">
              <div class="col mt-3">
                <div class="d-flex">
                  <button v-if="!manifestation.manifestationPassed && role =='CUSTOMER' && manifestation.status == 'Aktivna'" class="btn btn-green" data-toggle="modal" data-target="#reservationModal">
                    Rezervacija karata
                  </button>
                  <button v-on:click="showMap()" id="show-map-button" class="btn btn-pink">Prikazi mapu</button>
                </div>
              </div>
              <div class="col text-right">
                <h3 class="text-uppercase p-3">{{ manifestation.name }}</h3>
              </div>
            </div>
            <comment-section
              v-bind:commenting_conditions="comment_conditions" v-bind:manifestation_passed="manifestation.manifestationPassed"
            ></comment-section>
          </div>

          <!-- RESERVATION MODAL -->

          <div class="modal fade" id="reservationModal" tabindex="-1" role="dialog" aria-labelledby="reservationModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="reservationModalLabel">Rezervacija karte</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Broj karata</label> 
                        <input type="number" min="1" max="5" v-model="number_of_tickets" class="form-control"/>
                    </div>
                    <div class="form-group">
                        <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" v-model="ticket_type" name="inlineRadioOptions" id="inlineRadio1" value="REGULAR">
                        <label class="form-check-label" for="inlineRadio1">Regular</label>
                        </div>
                        <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" v-model="ticket_type" name="inlineRadioOptions" id="inlineRadio2" value="FAN_PIT">
                        <label class="form-check-label" for="inlineRadio2">Fan pit</label>
                        </div>
                        <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" v-model="ticket_type" name="inlineRadioOptions" id="inlineRadio2" value="VIP">
                        <label class="form-check-label" for="inlineRadio2">Vip</label>
                        </div>
                        <small id="remaining_number_error" class="form-text">Cena Fan Pit karte je dva puta veca od regularne cene, a cena Vip karte je cetiri puta veca od regularne cene.</small>
                    </div>
                    <div class="form-group">
                        <button type="button" class="btn btn-green" v-on:click="countPrice()">Izracunaj ukupnu cenu</button>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Ukupna cena</label>
                        <input class="form-control total-price" type="text" placeholder="Ukupna cena" readonly>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazite</button>
                    <button type="button" class="btn btn-green reserve-button" 
                        data-dismiss="modal" data-toggle="modal" data-target="#areYouSureModal">Izvrsite rezervaciju</button>
                </div>
                </div>
            </div>
          </div>
          
          <!-- ARE YOU SURE MODAL -->
          <div class="modal fade" id="areYouSureModal" tabindex="-1" role="dialog" aria-labelledby="areYouSureModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="areYouSureModalLabel">Potvrda rezervacije</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    Da li ste sigurni?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazite</button>
                    <button type="button" class="btn btn-green" data-dismiss="modal" v-on:click="reserve()">Potvrdite rezervaciju</button>
                </div>
                </div>
            </div>
          </div>

        </div>
        <div class="col-lg-3 col-md-3">
          <div class="manifestation-details">
            <ul class="shadow">
              <li class="font-weight-bold text-muted">
                Detalji o manifestaciji
              </li>
              <li class="d-flex">
                <i class="fa fa-calendar pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Pocetak</div>
                  <small class="text-uppercase fw-light text-muted">{{
                    manifestation.startTime
                  }}</small>
                </div>
              </li>
              <li class="d-flex">
                <i class="fa fa-calendar pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Kraj</div>
                  <small class="text-uppercase fw-light text-muted">{{
                    manifestation.endTime
                  }}</small>
                </div>
              </li>
              <li class="d-flex">
                <i class="fa fa-location-arrow pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Lokacija</div>
                  <small class="text-uppercase fw-light text-muted" v-if="manifestation">
                    {{manifestation.location}}
                  </small>
                </div>
              </li>
              <li class="d-flex">
                <i
                  class="fa fa-check pt-3 pb-3 pr-3 pink"
                  v-if="manifestation.status == 'Aktivna'"
                ></i>
                <i
                  class="fa fa-times pt-3 pb-3 pr-3 pink"
                  v-if="manifestation.status == 'Neaktivna'"
                ></i>
                <div>
                  <div>Status</div>
                  <small class="text-uppercase fw-light text-muted">{{
                    manifestation.status
                  }}</small>
                </div>
              </li>
              <li class="d-flex">
                <i
                  class="fa fa-music pt-3 pb-3 pr-3 pink"
                  v-if="manifestation.type == 'Koncert'"
                ></i>
                <i
                  class="material-icons pt-3 pb-3 pr-3 pink"
                  v-if="manifestation.type == 'Pozoriste'"
                  >movie_filter</i
                >
                <i
                  class="material-icons pt-3 pb-3 pr-3 pink"
                  v-if="manifestation.type == 'Festival'"
                  >wb_sunny</i
                >
                <div>
                  <div>Tip</div>
                  <small class="text-uppercase fw-light text-muted">{{
                    manifestation.type
                  }}</small>
                </div>
              </li>
              <li class="d-flex">
                <i class="fa fa-ticket pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Preostali broj karata</div>
                  <small class="text-uppercase fw-light text-muted">{{
                    manifestation.remainingNumberOfSeats
                  }}</small>
                </div>
              </li>
              <li class="d-flex">
                <i class="fa fa-money pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Cena</div>
                  <small
                    class="text-uppercase fw-light text-muted"
                    v-if="!manifestation_sold"
                    >{{ manifestation.ticketPrice }},00 RSD</small
                  >
                  <small
                    class="text-uppercase fw-light text-muted"
                    v-if="manifestation_sold"
                    >rasprodato</small
                  >
                </div>
              </li>
              <li class="d-flex">
                <i class="fa fa-ticket pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Ocena</div>
                  <div class="average_rating" v-if="manifestation.manifestationPassed">
                    <span
                      class="fa fa-star"
                      v-bind:class="{ yellow: isCountedInAverageRating(1) }"
                    ></span>
                    <span
                      class="fa fa-star"
                      v-bind:class="{ yellow: isCountedInAverageRating(2) }"
                    ></span>
                    <span
                      class="fa fa-star"
                      v-bind:class="{ yellow: isCountedInAverageRating(3) }"
                    ></span>
                    <span
                      class="fa fa-star"
                      v-bind:class="{ yellow: isCountedInAverageRating(4) }"
                    ></span>
                    <span
                      class="fa fa-star"
                      v-bind:class="{ yellow: isCountedInAverageRating(5) }"
                    ></span>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div id="map" class="map" >
    </div>
  </div>
</template>

<script>
module.exports = {
  data() {
    return {
      currentPosition: {lat: 45.252600, lon: 19.830002, adresa: "Cirpanova 51, Novi Sad"},
      manifestation: null,
      manifestation_passed: false,
      manifestation_sold: false,
      comment_conditions: {},
      location: null,
      role: null,
      ticket_type : "REGULAR",
      number_of_tickets: 0,
      ticket_price: 0
    };
  },
  components: {
    "comment-section": httpVueLoader("./CommentSection.vue")
  },
  created() {
    axios
      .get("rest/users/role")
      .then(response => {
          this.role = response.data;
      })
    const manifestationId = this.$route.params.id;
    axios.get("rest/manifestations/view/" + manifestationId).then((response) => {
      this.manifestation = response.data;
      this.manifestation.startTime = this.manifestation.startTime.split("T").join(" ")
      this.manifestation.endTime = this.manifestation.endTime.split("T").join(" ")
    });

    axios
      .get(
        "rest/comments/commenting-conditions/manifestation/" + manifestationId
      )
      .then((response) => {
        this.comment_conditions = response.data;
        this.comment_conditions.manifestation_passed = this.manifestation_passed;
      });
  },
  methods: {
    isCountedInAverageRating(num) {
      return !(num > this.comment_conditions.manifestationRating);
    },
    countPrice() {
      console.log(this.ticket_type, this.number_of_tickets)
      if(!isNumberInRange(1,5,this.number_of_tickets)) {
				$('.total-price').attr("placeholder", "Mozete rezervisati od 0 do 5 karata u jednoj rezervaciji.");
				$('.reserve-button').attr("disabled", true);
				return;
			}
            
      axios
				.get("rest/tickets/total-price", {
					params: {
            "numberOfTickets":this.number_of_tickets,
            "ticketType":this.ticket_type,
            "manifestationId":this.manifestation.id
          }
				})
				.then(response => {
					this.ticket_price = response.data
          $('.total-price').attr("placeholder", this.ticket_price);
				});
            
    },
    reserve() {
      let points = this.ticket_price/1000 * 133;
      const reservationDTO = {
          "points" : points,
          "manifestationId" : this.manifestation.id,
          "numberOfTickets" : this.number_of_tickets,
          "ticketType" : this.ticket_type
      }
      console.log(reservationDTO)
			axios
				.post("rest/tickets/reserve-ticket", JSON.stringify(reservationDTO), {
					headers: {'content-type':'application/json'}
				})
				.then(() => {
					new Toast({
            message: 'Rezervacija uspesno izvrsena!',
            type: 'success'
          });
				});
    },
    showMap (){
			let self = this;
      document.getElementById('show-map-button').disabled = true
			
			var vectorSource = new ol.source.Vector({});
		  var vectorLayer = new ol.layer.Vector({source: vectorSource});
			const lon = this.manifestation.locationDTO.longitude
      const lat = this.manifestation.locationDTO.latitude
      var map = new ol.Map({
        target: 'map',
        layers: [
          new ol.layer.Tile({
            source: new ol.source.OSM()
          }),vectorLayer
        ],
        view: new ol.View({
          center: ol.proj.fromLonLat([lat, lon]),
          zoom: 16
        })
      });
      var myStyle = new ol.style.Style({
        image: new ol.style.Circle({
          radius: 8,
          fill: new ol.style.Fill({color: 'red'}),
          stroke: new ol.style.Stroke({
            color: [255,0,0], width: 2
          })
        })
      })
			var layer = new ol.layer.Vector({
      source: new ol.source.Vector({
          features: [
              new ol.Feature({
                  geometry: new ol.geom.Point(ol.proj.fromLonLat([lat, lon]))
              })
          ]
      }),
      style: myStyle

      });
      map.addLayer(layer);
    } 
  }
};
</script>

<style scoped>

.map {
  height: 400px;
  width: 100%;
}

.manifestation-image-holder {
  height: 20em;
  overflow: hidden;
  border-radius: 10px;
  border:1px solid #FFAAA7;
}

.manifestation-details ul {
  list-style-type: none;
  /* border-color: #0d6efd; */
  /* border: 1px solid #0d6efd; */
  padding: 0;
  border-radius: 10px;
}

.yellow {
  color: #FFD3B4;
}

.yellow-bg {
  background-color: #FFD3B4;
}

.blue {
  color: rgb(3, 54, 255);
}

.pink {
  color: #FFAAA7;
}

.manifestation-details li {
  padding: 15px 30px;
  border-top: 1px solid #d7d7d7;
}

.manifestation-details li:first-child {
  border-top: none;
}

#manifestation {
  margin-top: 5em;
}

#manifestation h3 {
  font-weight: 200;
}

#manifestation .average_rating {
  border-top-left-radius: 0.25rem;
  border-bottom-left-radius: 0.25rem;
  padding: 5px 10px 0 10px;
}

#manifestation .manifestation-button {
  border-radius: 0.25rem;

  display: inline-block;
  font-weight: 400;
  line-height: 1.5;
  text-align: center;
  text-decoration: none;
  vertical-align: middle;
  cursor: pointer;
  -webkit-user-select: none;
  -moz-user-select: none;
  user-select: none;
  border: 1px solid transparent;
  border-left: 1px solid rgb(255, 222, 3);
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
}


</style>