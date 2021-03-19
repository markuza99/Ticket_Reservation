<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-lg-3 col-md-4">
        
        <button type="button" class="btn btn-outline-primary mt-5" v-if="role == 'SELLER'" data-toggle="modal" data-target="#createManifestationModal">
          Dodaj manifestaciju
        </button>
        <filtering-panel></filtering-panel>
      </div>
      <div class="col-lg-9 col-md-8">
        <div id="manifestations" class="row">
          <div class="col-lg-6 col-xl-4 col-md-6 col-sm-6" v-for="m in manifestations" :key="m.id">
            <div class="card mb-4 box-shadow manifestation" v-on:click="goToManifestation(m.id)">
              <div class="image-holder">
                <img class="card-img-top" v-bind:src="'images/' + m.image" alt="Card image cap">
              </div>
              <div class="card-body">
                <h5 class="card-title">{{ m.name }}</h5>
                <div class="d-flex justify-content-between align-items-center">
                  <div class="btn-group">
                    <button type="button" class="btn btn-sm btn-outline-secondary">{{ m.formattedType }}</button>
                    <button type="button" class="btn btn-sm btn-danger">{{ m.ticketPrice }},00 RSD</button>
                  </div>
                  <small class="text-muted">
                    {{ m.startTime.dayOfMonth }}
                    {{ m.formattedMonth }}
                    {{ m.startTime.year }}
                  </small>
                </div>
              </div>
                <div class="card-footer text-muted">
                  {{ m.location.street}} {{m.location.number}}, {{m.location.city}}, {{m.location.state}}
                </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <create-manifestation-modal></create-manifestation-modal>
    
  </div>
</template>

<script>
module.exports = {
  data() {
    return {
      role: null,
      manifestations: [],
    };
  },
  components: {
    "create-manifestation-modal": httpVueLoader(
      "./CreateManifestationModal.vue"
    ),
    "filtering-panel":httpVueLoader("./FilterAndSortPanel.vue")
  },
  methods: {
    goToManifestation(id) {
      location.replace("#/manifestation/" + id);
    },

    format() {
      this.manifestations.forEach((manifestation) => formatType(manifestation));
      this.manifestations.forEach((manifestation) => makeDate(manifestation));
    },
  },
  mounted() {
    this.$root.$on("searched-manifestations", (manifestations) => {
      this.manifestations = manifestations;
      this.format();
    });
    this.$root.$on('sorted-filtered-manifestations', (manifestations) => {
      this.manifestations = manifestations;
      this.format();
    });

    this.$root.$on("create-manifestation", (manifestation) => {
      formatType(manifestation);
      makeDate(manifestation);
      this.manifestations.push(manifestation);
    });

    axios.get("rest/manifestations/active").then((response) => {
      this.manifestations = response.data;
      console.log(this.manifestations);
      this.format();
    });

    axios.get("rest/users/role").then((response) => {
      this.role = response.data;
    });
  },
};
</script>

<style>
#error {
  color: red;
}

#manifestations {
  margin-top: 2em;
}

.manifestation {
  border: 1px solid darkgray;
  margin-bottom: 20px;
  box-shadow: 0px 0px 12px #767676;
}

.manifestation .image-holder {
  overflow: hidden;
  position: relative;
  cursor: pointer;
}

.manifestation .image-holder img {
  max-width: 100%;
  -webkit-transition: all 0.4s linear;
  transition: all 0.4s linear;
}

.manifestation .image-holder:hover img {
  -ms-transform: scale(1.2);
  -webkit-transform: scale(1.2);
  transform: scale(1.2);
  transition: all 0.2s;
}

.manifestation .date {
  text-transform: uppercase;
  font-family: "Quicksand", sans-serif;
}

.manifestation .name {
  font-size: 20px;
  text-transform: uppercase;
  color: #767676;
}

.manifestation .description-holder {
  padding: 20px;
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
