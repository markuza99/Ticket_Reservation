<template>
  <div class="container">
    <div class="col-lg-12 col-md-12">
      <div id="manifestations" class="row">
        <div class="col-lg-6 col-xl-4 col-md-6 col-sm-6" v-for="m in manifestations" :key="m.id">
          <div class="card mb-4 box-shadow manifestation" v-on:click="goToManifestation(m.id)">
            <div class="image-holder">
              <img class="card-img-top" v-bind:src="m.image" alt="Card image cap">
            </div>
            <div class="card-body">
              <h5 class="card-title">{{ m.name }}</h5>
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
      manifestations: []
    }
  },
  created () {
    axios
      .get('rest/manifestations/mine')
      .then(response => {
        this.manifestations = response.data
      })
  },
  methods: {
    goToManifestation(id) {
      this.$router.push('/manifestation/' + id);
    }
  }
}
</script>