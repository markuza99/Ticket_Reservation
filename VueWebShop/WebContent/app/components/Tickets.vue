<template>
  <div class="container-fluid">
    <div class="row mt-5">
      <div class="col-md-2">
        <div>
        <input type="text" v-model="name" class="form-control mb-2" placeholder="name">
        <input type="text" v-model="priceFrom" class="form-control mb-2" placeholder="price from">
        <input type="text" v-model="priceTo" class="form-control mb-2" placeholder="price to">
        <input type="date" v-model="dateFrom" class="form-control mb-2" placeholder="date from">
        <input type="date" v-model="dateTo" class="form-control mb-2" placeholder="date to">
        Tip karte: <select class="form-control mb-2" v-model="ticket_type">
          <option value="all">Sve</option>
          <option value="VIP">Vip</option>
          <option value="REGULAR">Regular</option>
          <option value="FAN_PIT">Fan pit</option>
        </select>
        Status karte: <select class="form-control mb-2" v-model="ticket_status">
          <option value="all">Sve</option>
          <option value="RESERVED">Rezervisano</option>
          <option value="CANCELED">Otkazano</option>
        </select>
        Sortiraj po: <select class="form-control mb-2" v-model="sortBy">
          <option value="price">Cena</option>
          <option value="manifestationName">Naziv manifestacije</option>
          <option value="manifestationDate">Datum manifestacije</option>
        </select>

        <input type="radio" id="one" value="Asc" v-model="sortOrder">
        <label for="one">Rastuce</label>
        <input type="radio" id="two" value="Desc" v-model="sortOrder">
        <label for="two">Opadajuce</label>

        <div class="text-right mt-2">
          <button class="btn btn-green-invert" type="submit" v-on:click="searchTickets()"><i class="fa fa-search"></i></button>
        </div>
      </div>
      </div>
      <div class="col-md-10">
        <table class="table table-hover table-borderless">
        <thead>
          <tr>
            <th scope="col">Id</th>
            <th scope="col">Manifestacija</th>
            <th scope="col">Cena</th>
            <th scope="col">Datum manifestacije</th>
            <th scope="col">Korisnik</th>
            <th scope="col">Status</th>
            <th scope="col">Tip</th>
            <th scope="col">Broj karata</th>
            <th scope="col">#</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="t in tickets" :key="t.id">
            <th scope="row">{{t.id}}</th>
            <td>{{t.manifestationName}}</td>
            <td>{{t.price}}</td>
            <td>{{t.manifestationDate}}</td>
            <td>{{t.user}}</td>
            <td>{{t.status}}</td>
            <td>{{t.type}}</td>
            <td>{{t.numberOfTickets}}</td>
            <td v-on:click="setTicketId(t.id)">
              <button v-if="t.status == 'Rezervisano' && t.manifestationPassed == false && role == 'CUSTOMER'" class="btn text-primary" data-toggle="modal" data-target="#cancelReservationModal">otkazi</button>
              <div class="modal fade" id="cancelReservationModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="exampleModalLabel">Cancel reservation</h5>
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                      </button>
                    </div>
                    <div class="modal-body">
                      Are you sure you want to cancel reservation?
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-dismiss="modal">No</button>
                      <button type="button" class="btn btn-green-invert" data-dismiss="modal" v-on:click="cancelReservation()">Yes</button>
                    </div>
                  </div>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      </div>
    </div>
  </div>
</template>

<script>
module.exports = {
  data () {
    return {
      tickets: [],
      ticket_type: 'all',
      ticket_status: 'all',
      sortBy: 'manifestationDate',
      name: '',
      priceFrom: 0,
      priceTo: 0,
      dateFrom: '',
      dateTo: '',
      sortOrder: 'Asc',
      ticketId: '',
      role: ''
    }
  },
  created () {
    this.getTickets()
    axios
			.get("rest/users/role")
			.then(response => {
				if(!isEmpty(response.data)) {
          this.role = response.data;
        }
			});
  },
  methods: {
    setTicketId (id) {
      this.ticketId = id
    },
    getTickets () {
      axios
      .get('rest/tickets')
      .then(response => {
        this.tickets = response.data
      })
    },
    searchTickets () {
      let dateFrom = ''
      let dateTo = ''
      if(this.dateFrom != '') {
        dateFrom = this.dateFrom + 'T00:00:00'
      }
      if(this.dateTo != '') {
        dateTo = this.dateTo + 'T00:00:00'
      }
      const sortBy = this.sortBy + this.sortOrder
      console.log(sortBy)
      console.log(this.ticket_type, this.ticket_status, this.sortBy, this.name, this.priceFrom, this.priceTo, this.dateFrom, this.dateTo)

      axios
        .get('rest/tickets', {
          params: {
            "manifestationName": this.name,
            "priceFrom": this.priceFrom,
            "priceTo": this.priceTo,
            "dateFrom": dateFrom,
            "dateTo": dateTo,
            "sortBy": sortBy,
            "ticketType": this.ticket_type,
            "ticketStatus": this.ticket_status
          }
        })
        .then(response => {
          this.tickets = response.data
        })
    },
    cancelReservation () {
      axios
        .put('rest/tickets/cancel-reservation/' + this.ticketId)
        .then(response => {
          if(response.data != "") {
            alert('Rezervacija otkazana')
            this.getTickets()
          }
        })
    }
  }
}
</script>