<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-lg-3 col-md-12">
        <filtering-panel></filtering-panel>
      </div>
      <div class="col-lg-9 col-md-12">
        <div id="manifestations" class="row">
          <div class="col-lg-4 col-md-4 col-sm-6" v-for="m in manifestations" :key="m.id">
            <div class="manifestation" v-bind:id="m.id" v-on:click="goToManifestation(m.id)">
              <div class="image-holder">
                <img class="fit-img" v-bind:src="'images/' + m.image" />
              </div>
              <div class="description-holder">
                <h3 class="name">{{ m.name }}</h3>
                <span class="text-info">{{ m.location.street }}, </span>
                <span class="text-info">{{ m.location.city }}</span><br />
              </div>
              <div class="price" style="text-align: right">
                <span class="p-2 bg-danger" style="color: white; position: relative; bottom: 6px">{{ m.ticketPrice }},00 RSD</span>
              </div>
              <div class="date" style="text-align: right; white-space: nowrap">
                <div class="bg-warning" style="padding: 0 7px">
                  {{ m.date.dayOfMonth }}
                  {{ m.formattedMonth }}
                  {{ m.date.year }}
                </div>
              </div>

              <div class="text-center w-100 pt-1 pb-1 manifestation-type">
                {{ m.formattedType }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    

    <!-- <button
      type="button"
      class="btn btn-primary"
      v-if="role == 'SELLER'"
      data-toggle="modal"
      data-target="#createManifestationModal"
    >
      Dodaj manifestaciju
    </button> -->
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
      console.log(this.manifestation);
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
