<template>
    <div class="container-fluid" id="manifs-panel">
            <div>
                <div class="table-wrapper-scroll">
                    <table class="table table-hover" v-if="tickets">
                        <thead>
                            <tr>
                            <th scope="col">Id karte</th>
                            <th scope="col">Id manifestacije</th>
                            <th scope="col">Naziv manifestacije</th>
                            <th scope="col">Datum manifestacije</th>
                            <th scope="col">Cena</th>
                            <th scope="col">Status</th>
                            <th scope="col">Tip</th>
                            <th scope="col">Korisnik</th>
                            </tr>
                        </thead>
                        <tbody>
                            
                            <tr v-for="ticket in tickets" v-bind:key="ticket.id">
                                <th scope="row">{{ticket.id}}</th>
                                <td>{{ticket.manifestationId}}</td>
                                <td>{{findManifName(ticket.manifestationId)}}</td>
                                <td>{{toStringDateTime(ticket.dateTime)}}</td>
                                <td>{{ticket.price}}</td>
                                <td>{{ticket.ticketStatus}}</td>
                                <td>{{ticket.ticketType}}</td>
                                <td>{{ticket.user}}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
            </div>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            tickets : [],
            manifestations: [],
            selected_ticket : "",
            filtered_tickets : "SVI",
            text : "",
            dateFrom : "",
            dateTo : "",
            priceTo: "",
            priceFrom: "",
            ticket_type : "",
            ticket_status : "",

        }
    },
    mounted() {
        axios
            .get("rest/ticketservice/getall")
            .then(response => {
                this.tickets = response.data;
                console.log(this.tickets);
                // this.tickets.forEach(element => {
                //     alert(this.toStringDateTime(element.dateTime));
                // });
            });
            axios
			.get("rest/manifestationservice/getall")
			.then(response => {
				
				this.manifestations = response.data;
				console.log(this.manifestations);
				
				// this.manifestations.forEach(manifestation => this.makeDate(manifestation));
			});
        // axios
        //     .get("rest/userservice/test-login")
        //     .then(response => {
        //         if(response.data.role != "ADMIN") {
        //             location.replace("#/unauthorized");
        //         }
        //     });
    },
    methods : {
            toStringDateTime(date) {
                return date.year+"-"+date.monthValue+"-"+date.dayOfMonth+" " +date.hour+":"+date.minute+":"+date.second;
            },
            findManifName(id) {
                for (var i = 0; i < this.manifestations.length; i++) {
                    if(this.manifestations[i].id == id) {
                        return this.manifestations[i].name;
                    }
                }
            }
    }
}
</script>

<style scoped>

#users-panel {
    padding-top: 3em;
}

.table_warning, .table_warning>td, .table_warning>th {
    background-color: #ffeeba;
}

.list-group-item.active {
    z-index: 2;
    color: #343a40;
    background-color: #bee5eb;
    border-color: #bee5eb;
}

.form-inline {
    margin-bottom:2em;
    padding: 0px 3em;
}

.table-wrapper-scroll {
    height: 30em !important;
    overflow: scroll;
}

</style>