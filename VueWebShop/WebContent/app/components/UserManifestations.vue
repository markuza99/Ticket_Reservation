<template>
  <div>
    <div class="pl-5 pr-5 pt-4 pb-5" style="background:#e6f7f2">
      <form @submit.prevent="searchManifestations()">
        <div class="form-row">
          <div class="form-group col-md-6">
            <label>Naziv</label>
            <input type="text" v-model="name" class="form-control">
          </div>
          <div class="form-group col-md-6">
            <label>Mesto</label>
            <input type="text" v-model="place" class="form-control">
          </div>
        </div>
        <div class="form-row">
          <div class="form-group col-md-3">
            <label>Datum od</label>
            <input type="date" v-model="dateFrom" class="form-control">
          </div>
          <div class="form-group col-md-3">
            <label>Datum do</label>
            <input type="date" v-model="dateTo" class="form-control">
          </div>
          <div class="form-group col-md-3">
            <label>Cena od</label>
            <input type="int" v-model="priceFrom" class="form-control">
          </div>
          <div class="form-group col-md-3">
            <label>Cena do</label>
            <input type="int" v-model="priceTo" class="form-control">
          </div>
        </div>
        <div class="form-row">
          <div class="form-group col-md-2">
            <label>Sortiraj po</label>
            <select class="form-control" v-model="sortBy">
              <option value="price">Cena</option>
              <option value="name">Naziv manifestacije</option>
              <option value="date">Datum manifestacije</option>
              <option value="location">Lokacija manifestacije</option>
            </select>
          </div>
          <div class="form-group col-md-2 pt-4">
            <input type="radio" id="one" value="Asc" v-model="sortOrder" class="mt-3">
            <label for="one">Rastuce</label>
            <input type="radio" id="two" value="Desc" v-model="sortOrder">
            <label for="two">Opadajuce</label>
          </div>
          <div class="form-group col-md-4">
            <label>Tip manifestacije</label>
            <select class="form-control" v-model="type">
              <option value="all">Sve</option>
              <option value="CONCERT">Koncert</option>
              <option value="THEATER">Pozoriste</option>
              <option value="FESTIVAL">Festival</option>
            </select>
          </div>
          <div class="form-group col-md-3">
            <label>Filtriraj po</label>
            <select class="form-control" v-model="ticketCondition">
              <option value="all">Sve</option>
              <option value="soldOut">Rasprodato</option>
              <option value="notSoldOut">Nerasprodato</option>
            </select>
          </div>
          <div class="col-md-1 pt-4 text-right">
            <button type="submit" class="btn btn-green mt-2">Potvrdi</button>
          </div>
        </div>
      </form>
    </div>
    <div class="container">
      <div class="col-lg-12 col-md-12">
        <div id="manifestations" class="row mt-4" v-if="role != 'ADMIN'">
          <div class="col-lg-6 col-xl-4 col-md-6 col-sm-6" v-for="m in manifestations" :key="m.id">
            <div class="card mb-4 box-shadow manifestation">
              <div class="image-holder" v-on:click="goToManifestation(m.id)">
                <h5 v-if="manifestationStatus(m) == 'rejected'" class="rejected-h">Odbijena</h5>
                <h5 v-if="manifestationStatus(m) == 'waiting'" class="waiting-h">Ceka se odobrenje</h5>
                <img class="card-img-top" v-bind:src="m.image" alt="Card image cap" v-bind:class="{'grey-img' : manifestationStatus(m) == 'rejected', 'yellow-img' : manifestationStatus(m) == 'U cekanju'}">
              </div>
              <div class="card-body">
                <div class="d-flex justify-content-between">
                  <h5 class="card-title" v-on:click="goToManifestation(m.id)">{{ m.name }}</h5>
                  <button class="btn btn-pink"
                  v-on:click="goToEditManifestation(m.id)"
                  v-if="!m.manifestationPassed && role == 'SELLER' && manifestationStatus(m) != 'rejected'">izmeni</button>
                </div>
                <div class="modal fade" id="updateManifestationModal" tabindex="-1" aria-labelledby="updateManifestationModalLabel" aria-hidden="true">
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title" id="updateManifestationModalLabel">Izmena manifestacije</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                      </div>
                      <div class="modal-body">
                        <form class="row g-3">
                          <div class="col-md-6">
                            <label>Id manifestacije</label>
                            <input type="text" class="form-control" v-model="manifestation.id" readonly>
                          </div>
                          <div class="col-md-6">
                            <label>Tip manifestacije</label>
                            <select class="form-control" v-model="manifestation.type">
                              <option value="Koncert">Koncert</option>
                              <option value="Pozoriste">Pozoriste</option>
                              <option value="Festival">Festival</option>
                            </select>
                          </div>
                          <div class="col-12">
                            <label>Naziv manifestacije</label>
                            <input type="text" class="form-control" v-model="manifestation.name" >
                          </div>
                          <div class="col-md-6">
                            <label>Datum pocetka</label>
                            <input type="datetime-local" class="form-control" v-model="manifestation.startTime"/> 
                          </div>
                          <div class="col-md-6">
                            <label>Datum kraja</label>
                            <input type="datetime-local" class="form-control" v-model="manifestation.endTime">
                          </div>
                          <div class="col-md-6">
                            <label>Broj mesta</label>
                            <input type="number" class="form-control" v-model="manifestation.numberOfSeats">
                          </div>
                          <div class="col-md-6">
                            <label>Cena karte</label>
                            <input type="number" class="form-control" v-model="manifestation.ticketPrice">
                          </div>
                          <div class="col-md-12">
                            <label>Lokacija</label>
                            <input list="locations" class="form-control" v-model="manifestation.locationId">
                            <datalist id="locations">
                              <option v-for="location in locations" :key="location.id" :value="location.id">
                                {{location.street}} {{location.number}}, {{location.city}}
                              </option>
                            </datalist>
                          </div>
                          <div class="col-md-6">
                            <label>Poster manifestacije</label>
                            <input type="file" ref="file" id="file" class="form-control-file" v-on:change="loadFile">
                          </div>
                        </form>
                        <div class="image-holder-file">
                          <img id="output" v-bind:src="manifestation.image">
                        </div>
                      </div>
                      <div class="modal-footer">
                        <button type="button" id="closeEditManifestationButton" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" v-on:click="updateManifestation()">Save changes</button>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="d-flex justify-content-between align-items-center">
                  <div class="btn-group">
                    <button type="button" class="btn btn-sm btn-outline-secondary">{{ m.type }}</button>
                    <button type="button" class="btn btn-sm btn-green">{{ m.price }},00 RSD</button>
                  </div>
                  <small class="text-muted">
                    {{ m.date }}
                  </small>
                </div>
              </div>
              <div class="card-footer text-muted">
                {{ m.location}}
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <table class="table table-hover table-borderless mt-5">
          <thead>
            <tr>
              <th scope="col">Id</th>
              <th scope="col">Naziv</th>
              <th scope="col">Cena</th>
              <th scope="col">Datum</th>
              <th scope="col">Tip</th>
              <th scope="col">Mesto</th>
              <th scope="col">Status</th>
              <th scope="col">Prodavac</th>
              <th scope="col">#</th>
              <th scope="col">#</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="m in manifestations" :key="m.id">
              <th scope="row">{{m.id}}</th>
              <td v-on:click="goToManifestation(m.id)" style="cursor: pointer">{{m.name}}</td>
              <td>{{m.price}}</td>
              <td>{{m.date}}</td>
              <td>{{m.type}}</td>
              <td>{{m.location}}</td>
              <td>
                <p v-bind:class="{'text-green': m.status == 'Aktivna', 'text-pink': m.status == 'Neaktivna'}">{{m.status}}</p>
              </td>
              <td>{{m.seller}}</td>
              <td v-on:click="setManifestationId(m.id)">
                <div  v-if="m.status == 'Neaktivna' && m.checked == false" class="d-flex">
                  <button v-on:click="setModalType('approve')" class="btn btn-green" data-toggle="modal" data-target="#manifestationModal">odobri</button>
                  <button v-on:click="setModalType('decline')" class="btn btn-pink ml-1" data-toggle="modal" data-target="#manifestationModal">odbij</button>
                </div>
                <div v-else>
                  <div v-if="m.deleted == true">
                    <p class="text-orange">Obrisana</p>
                  </div>
                  
                </div>
              </td>
              <td v-on:click="setManifestationId(m.id)">
                <button v-if="m.deleted == false && m.status == 'Aktivna'" class="btn btn-green" v-on:click="setModalType('delete')" data-toggle="modal" data-target="#manifestationModal">
                  <i class="fa fa-trash"></i>
                </button>
                <button v-else-if="m.deleted == true" v-on:click="setModalType('retrieve')" class="btn btn-orange" data-toggle="modal" data-target="#manifestationModal">
                  <i class="fa fa-undo"></i>
                </button>
              </td>
              <div class="modal fade" id="manifestationModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                  <div class="modal-dialog" role="document">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 v-if="modalType == 'approve'" class="modal-title" id="exampleModalLabel">Odobravanje manifestacije</h5>
                        <h5 v-if="modalType == 'decline'" class="modal-title" id="exampleModalLabel">Odbijanje manifestacije</h5>
                        <h5 v-if="modalType == 'delete'" class="modal-title" id="exampleModalLabel">Brisanje manifestacije</h5>
                        <h5 v-if="modalType == 'retrieve'" class="modal-title" id="exampleModalLabel">Povratak manifestacije</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                          <span aria-hidden="true">&times;</span>
                        </button>
                      </div>
                      <div class="modal-body">
                        <p v-if="modalType == 'approve'">
                          Da li ste sigurni da zelite da odobrite manifestaciju?
                        </p>
                        <p v-if="modalType == 'decline'">
                          Da li ste sigurni da zelite da odbijete manifestaciju?
                        </p>
                        <p v-if="modalType == 'delete'">
                          Da li ste sigurni da zelite da obrisete manifestaciju?
                        </p>
                        <p v-if="modalType == 'retrieve'">
                          Da li ste sigurni da zelite da povratite manifestaciju?
                        </p>
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">No</button>
                        <button type="button" class="btn btn-green" data-dismiss="modal" v-on:click="makeActionOnManifestation()">Yes</button>
                      </div>
                    </div>
                  </div>
                </div>
            </tr>
          </tbody>
        </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
module.exports = {
  data () {
    return {
      manifestations: [],
      locations : [],
      manifestation : {},
      url : null,
      role: null,
      manifestationId: '',
      modalType: '',
      sortBy: 'date',
      type: 'all',
      ticketCondition: 'all',
      name: '',
      place: '',
      dateFrom: '',
      dateTo: '',
      priceFrom: 0,
      priceTo: 0,
      sortOrder: 'Asc'
    }
  },
  created () {
    this.getMineManifestations()
    axios
			.get("rest/locations/")
			.then(response => {
				this.locations = response.data;
			});
    axios
      .get("rest/users/role")
      .then(response => {
          this.role = response.data;
      })
  },
  methods: {
    getMineManifestations () {
      axios
      .get('rest/manifestations/list-mine')
      .then(response => {
        this.manifestations = response.data
        console.log(this.manifestations)
      })
    },
    goToManifestation (id) {
      this.$router.push('/manifestation/' + id);
    },
    goToEditManifestation (id) {
      this.$router.push('/edit-manifestation/' + id)
    },
    loadFile(event) {
			var filename = document.getElementById('file').value;
			parts = filename.split("\\");

			var reader = new FileReader();
			let vueComponent = this;

			reader.onload = function(){
				var output = document.getElementById('output');
				output.src = reader.result;
				
				vueComponent.manifestation.image = reader.result;
			}
			reader.readAsDataURL(event.target.files[0]);
		},
    updateManifestation () {
      console.log(this.manifestation)
      if(areInputFieldsEmpty(this.manifestation)) {
			  alert('sva polja su obavezna')
        return;
      }

      if(!isNumberInRange(1, 100000, parseInt(this.manifestation.numberOfSeats))) {
        alert('Pogresan unos broja mesta.')
        return;
      }

      if(!isNumberInRange(1, 100000, parseInt(this.manifestation.ticketPrice))) {
        alert("Pogresan unos cene karte.")
        return;
      }

      if(!validateRange(this.manifestation.startTime, this.manifestation.endTime)) {
        alert("Pogresan datum.")
        return;
      }

      let date =  new Date()
      if(!validateRange(date.toISOString(), this.manifestation.startTime)) {
        alert("Odabran je pogresan datum.")
        return;
      }

      if(!this.locations.find(location => location.id === this.manifestation.locationId)) {
        alert("Lokacija ne postoji.")
        return;
      }

      let manifestationForUpdate = this.manifestation
      if(this.manifestation.type == 'Koncert') {
        manifestationForUpdate.type = 'CONCERT'
      } else if(this.manifestation.type == 'Pozoriste') {
        manifestationForUpdate.type = 'THEATER'
      } else {
        manifestationForUpdate.type = 'FESTIVAL'
      }

      if(this.manifestation.status == 'Aktivna') {
        manifestationForUpdate.status = 'ACTIVE'
      } else {
        manifestationForUpdate.status = 'INACTIVE'
      }

      axios
      .put('rest/manifestations', JSON.stringify(manifestationForUpdate), {
        headers: {"content-type":"application/json"}
      })
      .then(() => {
        this.$router.go()
      })
    },
    setModalType (type) {
      this.modalType = type
    },
    makeActionOnManifestation () {
      if(this.modalType == 'decline') {
        this.declineManifestation()
      } else if(this.modalType == 'approve') {
        this.approveManifestation()
      } else if(this.modalType == 'delete') {
        this.deleteManifestation()
      } else if(this.modalType == 'retrieve') {
        this.retrieveManifestation()
      }
    },
    setManifestationId (id) {
      this.manifestationId = id;
    },
    approveManifestation () {
      axios
        .put('rest/manifestations/approve/' + this.manifestationId)
        .then(() => {
          this.getMineManifestations()
          new Toast({
            message: 'Manifestacija uspesno odobrena!',
            type: 'success'
          });
        })
    },
    declineManifestation () {
      axios
        .put('rest/manifestations/decline/' + this.manifestationId)
        .then(() => {
          this.getMineManifestations()
          new Toast({
            message: 'Manifestacija uspesno odbijena!',
            type: 'success'
          });
        })
    },
    deleteManifestation () {
      axios
        .put('rest/manifestations/delete/' + this.manifestationId)
        .then(() => {
          this.getMineManifestations()
          new Toast({
            message: 'Manifestacija uspesno obrisana!',
            type: 'success'
          });
        })
    },
    retrieveManifestation () {
      axios
        .put('rest/manifestations/retrieve/' + this.manifestationId)
        .then(() => {
          this.getMineManifestations()
          new Toast({
            message: 'Manifestacija uspesno povracena!',
            type: 'success'
          });
        })
    },
    searchManifestations () {
      let dateFrom = ''
      let dateTo = ''
      if(this.dateFrom != '') {
        dateFrom = this.dateFrom + 'T00:00:00'
      }
      if(this.dateTo != '') {
        dateTo = this.dateTo + 'T00:00:00'
      }
      const sortBy = this.sortBy + this.sortOrder
      axios
        .get('rest/manifestations/list-mine', {
          params: {
            name: this.name,
            place: this.place,
            dateFrom: dateFrom,
            dateTo: dateTo,
            priceFrom: this.priceFrom,
            priceTo: this.priceTo,
            sortBy: sortBy,
            type: this.type,
            ticketCondition: this.ticketCondition
          }
        })
        .then(response => {
          this.manifestations = response.data
        })
    },
    manifestationStatus (manifestation) {
      if(manifestation.status == 'Aktivna') return "accepted"
      if(manifestation.status == 'Neaktivna' && manifestation.checked == false) return "waiting"
      if(manifestation.status == 'Neaktivna' && manifestation.checked == true) return "rejected"
    }
  }
}
</script>

<style scoped>
.image-holder-file {
  overflow: hidden;
  position: relative;
  cursor: pointer;
}

.image-holder-file img {
  max-width: 100%;
}

@media screen and (min-width: 1200px) {
  .manifestation .image-holder {
      height: 15em;
  }
}

.manifestation .image-holder {
    overflow: hidden;
    position: relative;
    cursor: pointer;
}

.grey-img {
  filter: grayscale(100%);
}

.rejected-h {
  width: 100%;
  position: absolute;
  z-index: 100;
  margin-top: 5em;
  text-align: center;
  color:white;
  background: rgba(0,0,0,0.3);
  font-weight: normal;
  padding:0.5em;
}

.waiting-h {
  width: 100%;
  position: absolute;
  z-index: 100;
  margin-top: 5em;
  text-align: center;
  color:white;
  background: rgba(0,0,0,0.3);
  font-weight: normal;
  padding:0.5em;
}

.btn-green {
  background-color: #98DDCA;
  border: 1px solid #7cd4bc;
}

.btn-green-invert {
  background-color: #98DDCA;
  color:white;
  border: 1px solid #7cd4bc;
}

</style>