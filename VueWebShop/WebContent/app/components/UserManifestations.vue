<template>
  <div class="container">
    <div class="col-lg-12 col-md-12">
      <div id="manifestations" class="row mt-5">
        <div class="col-lg-6 col-xl-4 col-md-6 col-sm-6" v-for="m in manifestations" :key="m.id">
          <div class="card mb-4 box-shadow manifestation">
            <div class="image-holder" v-on:click="goToManifestation(m.id)">
              <img class="card-img-top" v-bind:src="m.image" alt="Card image cap">
            </div>
            <div class="card-body">
              <div class="d-flex justify-content-between">
                <h5 class="card-title" v-on:click="goToManifestation(m.id)">{{ m.name }}</h5>
                <button class="btn btn-primary" data-toggle="modal" data-target="#updateManifestationModal"
                 v-on:click="getManifestation(m.id)"
                 v-if="!m.manifestationPassed">izmeni</button>
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
                            <option value="CONCERT">Koncert</option>
                            <option value="THEATER">Pozoriste</option>
                            <option value="FESTIVAL">Festival</option>
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
                          <input list="locations" class="form-control" v-model="manifestation.location">
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
                  <button type="button" class="btn btn-sm btn-danger">{{ m.price }},00 RSD</button>
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
      url : null
    }
  },
  created () {
    axios
      .get('rest/manifestations/mine')
      .then(response => {
        this.manifestations = response.data
      })
    axios
			.get("rest/locations/")
			.then(response => {
				this.locations = response.data;
			});
  },
  methods: {
    goToManifestation (id) {
      this.$router.push('/manifestation/' + id);
    },
    getManifestation (id) {
      axios
      .get('rest/manifestations/' + id)
      .then(response => {
        this.manifestation = response.data
      })
    },
    loadFile(event) {
			var filename = document.getElementById('file').value;
			parts = filename.split("\\");

			var reader = new FileReader();
			let vueComponent = this;

			reader.onload = function(){
				var output = document.getElementById('output');
				output.src = reader.result;
				
				vueComponent.manifestation.image64base = reader.result;
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

      if(!this.locations.find(location => location.id === this.manifestation.location)) {
        alert("Lokacija ne postoji.")
        return;
      }

      axios
      .put('rest/manifestations', JSON.stringify(this.manifestation), {
        headers: {"content-type":"application/json"}
      })
      .then(response => {
       // this.manifestation = response.data
        document.getElementById('closeEditManifestationButton').click()
      })
    }
  }
}
</script>