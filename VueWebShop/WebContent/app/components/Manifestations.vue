<template>
  <div>
    <div class="search-panel">
      <nav class="navbar navbar-expand-lg navbar-light">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse search-navbar" id="navbarSupportedContent">
          <ul class="nav navbar-nav navbar-center mr-auto">
            <li class="nav-item">
              <input v-model="name" type="text" class="form-control" placeholder="Naziv manifestacije..." id="nazivMan"/>
            </li>
            <li class="nav-item">
              <input v-model="dateFrom" class="form-control" type="date" id="datumOdMan"/>
            </li>
            <li class="nav-item">
              <input v-model="dateTo" class="form-control" type="date" id="datumDoMan"/>
            </li>
            <li class="nav-item">
              <input v-model="priceFrom" class="form-control" type="number" placeholder="Cena od" id="cenaOdMan"/>
            </li>
            <li class="nav-item">
              <input v-model="priceTo" class="form-control" type="number" pattern="[1-9][0-9]*" placeholder="Cena do" id="cenaDoMan"/>
            </li>
            <li class="nav-item">
              <input v-model="place" class="form-control mr-sm-2" type="search" placeholder="Mesto" aria-label="Search" id="mestoMan"/>
            </li>
            <li class = "nav-item">
              <button class="search-button btn-green-invert" type="submit" v-on:click="searchManifestations"><i class="fa fa-search"></i></button>
            </li>
          </ul>
        </div>
      </nav>
    </div>

    <div class="container-fluid">
      <div class="row">
        <div class="col-lg-3 col-md-4">
          <div class="p-2">
            


            <div class="mb-2 mt-5 font-weight-bold">Sortiraj manifestacije</div>
            <select class="form-control my-select" v-model="sortBy">
              <option value="price">Cena</option>
              <option value="name">Naziv manifestacije</option>
              <option value="date">Datum manifestacije</option>
              <option value="location">Lokacija manifestacije</option>
            </select>
            <input type="radio" id="one" value="Asc" v-model="sortOrder" class="mt-3">
            <label for="one">Rastuce</label>
            <input type="radio" id="two" value="Desc" v-model="sortOrder" class="ml-3">
            <label for="two">Opadajuce</label><br>
            <label class="mb-2 mt-3 font-weight-bold">Tip manifestacije</label>
            <select class="form-control" v-model="type">
              <option value="all">Sve</option>
              <option value="CONCERT">Koncert</option>
              <option value="THEATER">Pozoriste</option>
              <option value="FESTIVAL">Festival</option>
            </select>
            <label class="mb-2 mt-3 font-weight-bold">Filtriraj po</label>
            <select class="form-control" v-model="ticketCondition">
              <option value="all">Sve</option>
              <option value="soldOut">Rasprodato</option>
              <option value="notSoldOut">Nerasprodato</option>
            </select>
          </div>
        </div>
        <div class="col-lg-9 col-md-8">
          <div class="text-right pb-2 pt-3">
            <button type="button" class="btn btn-green" v-if="role == 'SELLER'" v-on:click="goToCreateManifestation()">
              Dodaj manifestaciju
            </button>
          </div>
          <div class="scroll-viewer">
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
                        <button type="button" class="btn btn-sm btn-green">{{ m.price }},00 RSD</button>
                      </div>
                      <small class="text-muted ml-2">
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
      </div>
    </div>
  </div>
  
</template>

<script>
module.exports = {
  data() {
    return {
      role: null,
      manifestations: [],
      name: "",
			dateFrom:"",
			dateTo:"",
			place:"",
			priceFrom:"",
			priceTo:"",
      sortBy: 'date',
      type: 'all',
      ticketCondition: 'all',
      sortOrder: 'Desc'
    };
  },
  methods: {
    goToCreateManifestation () {
      this.$router.push('/create-manifestation')
    },
    goToManifestation(id) {
      this.$router.push('/manifestation/' + id)
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
        .get('rest/manifestations/active', {
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
    }
  },
  mounted() {

    axios.get("rest/manifestations/active", {
      params: {
        name: this.name,
        place: this.place,
        dateFrom: null,
        dateTo: null,
        priceFrom: this.priceFrom,
        priceTo: this.priceTo,
        sortBy: "dateDesc",
        type: this.type,
        ticketCondition: this.ticketCondition
      }
    })
    .then((response) => {
      this.manifestations = response.data;
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
  margin-bottom: 20px;
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

.error {
    outline: none !important;
    border:1px solid red;
    box-shadow: 0 0 10px #f40b0b;
}

.search-panel {
	background-color: #FFAAA7;
	padding: 0px;
	margin:0;
	text-align: center;
	padding-bottom: 20px;
}

.navbar-nav.navbar-center {
    margin-left: auto;
	margin-right: auto;
	left: 0;
	right: 0;
}

.search-panel li input {
	background-color: #fff;
	border-radius: 0;
	border:none;
	height:calc(2.5em + .75rem + 2px);
	padding:.375rem .75rem;
	/* box-shadow: 0px 0px 8px #474747; */
}

.search-panel li input:focus {
	outline:0;
}

@media only screen and (max-width: 992px) {
	.search-panel .search-button {
		width: 100%;
		text-align: right;
	}

	#navbarSupportedContent {
		margin-top: 20px;
	}
}

.search-panel .search-button {
	padding: 14px 22px;
	/* border:none; */
	/* box-shadow: 0px 0px 8px #000000; */
}

.search-panel .search-button:hover {
	background-color: #dddddd;
	color:rgb(0, 123, 255);
	transition:color .15s ease-in-out, background-color .15s ease-in-out;
}

.search-panel i {
	border:none;
}

.search-panel .navbar .navbar-toggler{
	background-color: #ffffff;
	
}

.scroll-viewer {
  height: calc(100vh - 200px);
  overflow: auto;
}

::-webkit-scrollbar {
  width: 10px;
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
