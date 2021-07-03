<template>
  <div class="container-fluid">
    <div class="row mt-5">
      <div class="col-md-2">
        <div>
        Naziv manifestacije <input type="text" v-model="name" class="form-control mb-3">
        Cena od<input type="text" v-model="priceFrom" class="form-control mb-3">
        Cena do<input type="text" v-model="priceTo" class="form-control mb-3">
        Datum manifestacije od<input type="date" v-model="dateFrom" class="form-control mb-3">
        Datum manifestacije do<input type="date" v-model="dateTo" class="form-control mb-3">
        Tip karte <select class="form-control mb-3" v-model="ticket_type">
          <option value="all">Sve</option>
          <option value="VIP">Vip</option>
          <option value="REGULAR">Regular</option>
          <option value="FAN_PIT">Fan pit</option>
        </select>
        Status karte <select class="form-control mb-3" v-model="ticket_status">
          <option value="all">Sve</option>
          <option value="RESERVED">Rezervisano</option>
          <option value="CANCELED">Otkazano</option>
        </select>
        Sortiraj po <select class="form-control mb-3" v-model="sortBy">
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
        <table class="table table-hover table-borderless table-striped">
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
            <td v-on:click="goToManifestation(t.manifestationId)" style="cursor:pointer">{{t.manifestationName}}</td>
            <td>{{t.price}}</td>
            <td>{{t.manifestationDate}}</td>
            <td>{{t.user}}</td>
            <td>{{t.status}}</td>
            <td>{{t.type}}</td>
            <td>{{t.numberOfTickets}}</td>
            <td v-on:click="setTicketId(t.id)">
              <button v-if="t.status == 'Rezervisano' && t.manifestationPassed == false && role == 'CUSTOMER'" class="btn btn-green" data-toggle="modal" data-target="#cancelReservationModal">otkazi</button>
              <button v-if="t.deleted == false && role == 'ADMIN'" v-on:click="setModalType('delete')" class="btn btn-green" data-toggle="modal"
               data-target="#ticketOperationsModal">
                <i class="fa fa-trash"></i> 
              </button>
              <button v-if="t.deleted == true && role == 'ADMIN'" v-on:click="setModalType('retrieve')" class="btn btn-pink"
               data-toggle="modal" data-target="#ticketOperationsModal">
                <i class="fa fa-undo"></i>
              </button>
              
              <div class="modal fade" id="cancelReservationModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="exampleModalLabel">Otkazivanje rezervacije</h5>
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                      </button>
                    </div>
                    <div class="modal-body">
                      Da li ste sigurni da zelite da otkazete rezervaciju?
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-dismiss="modal">No</button>
                      <button type="button" class="btn btn-green-invert" data-dismiss="modal" v-on:click="cancelReservation()">Yes</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal fade" id="ticketOperationsModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" v-if="modalType == 'delete'" id="exampleModalLabel">Brisanje karte</h5>
                      <h5 class="modal-title" v-if="modalType == 'retrieve'" id="exampleModalLabel">Povracaj karte</h5>
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                      </button>
                    </div>
                    <div class="modal-body">
                      <p v-if="modalType == 'delete'">
                        Da li ste sigurni da zelite da obrisete kartu?
                      </p>
                      <p v-if="modalType == 'retrieve'">
                        Da li ste sigurni da zelite da povratite kartu?
                      </p>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-dismiss="modal">No</button>
                      <button type="button" class="btn btn-green-invert" data-dismiss="modal" v-on:click="ticketOperation()">Yes</button>
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
      modalType: '',
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
    goToManifestation (id) {
      this.$router.push('/manifestation/' + id);
    },
    setModalType (type) {
      this.modalType = type
    },
    setTicketId (id) {
      this.ticketId = id
    },
    getTickets () {
      axios
      .get('rest/tickets')
      .then(response => {
        this.tickets = response.data
        console.log(this.tickets)
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
            new Toast({
              message: 'Rezervacija uspesno otkazana!',
              type: 'success'
            });
            this.getTickets()
          }
        })
    },
    deleteTicket () {
      axios
        .put('rest/tickets/delete/' + this.ticketId)
        .then(() => {
          this.getTickets()
          new Toast({
            message: 'Karta uspesno obrisana',
            type: 'success'
          });
        })
    },
    retrieveTicket () {
      axios
        .put('rest/tickets/retrieve/' + this.ticketId)
        .then(() => {
          this.getTickets()
          new Toast({
            message: 'Karta uspesno povracena!',
            type: 'success'
          });
        })
    },
    ticketOperation () {
      console.log(this.ticketId)
      if(this.modalType == 'delete') {
        this.deleteTicket()
      } else if(this.modalType == 'retrieve') {
        this.retrieveTicket()
      }
    }
  }
}
</script>