<template>
    <div class="container-fluid" id="manifs-panel">
        <form class="form-inline justify-content-between" @submit.prevent="search">
            <div class="form-group">
                <label class="mr-1 text-uppercase">Datum manifestacije:</label>
                <input type="date" v-model="dateFrom" class="form-control mr-1"/>
                <input type="date" v-model="dateTo" class="form-control mr-1"/>
            </div>
            <div class="form-group">
                <label class="mr-1 text-uppercase">Cena manifestacije:</label>
                <input type="text" v-model="priceFrom" class="form-control mr-1"/>
                <input type="text" v-model="priceTo" class="form-control mr-1"/>
            </div>
            <div class="form-group">
                <label class="mr-1 text-uppercase">Naziv manifestacije:</label>
                <input type="text" v-model="text" class="form-control mr-1"/>
                <button type="submit" class="btn btn-primary">
                    <i class="fa fa-search"></i>
                </button>
            </div>
        </form>
        <div class="row"> 
            <div class="col-lg-2 col-md-2">
                <div class="mb-2 font-weight-bold">Tip Karte</div>
                <div class="list-group user_type" id="list-tab" role="tablist">
                    <a class="list-group-item list-group-item-action active" data-toggle="list" id="ALL" v-on:click="setTicketType('ALL')">Svi</a>
                    <a class="list-group-item list-group-item-action" data-toggle="list" id="VIP" v-on:click="setTicketType('VIP')">VIP</a>
                    <a class="list-group-item list-group-item-action" data-toggle="list" id="FAN_PIT" v-on:click="setTicketType('FAN_PIT')">FAN PIT</a>
                    <a class="list-group-item list-group-item-action" data-toggle="list" id="REGULAR">REGULAR</a>
                </div>

                <div class="mb-2 mt-5 font-weight-bold">Status karte</div>
                <div class="list-group user_status" id="list-tab" role="tablist">
                    <a class="list-group-item user-status list-group-item-action active" data-toggle="list" id="ALL" v-on:click="setTicketStatus('ALL')">Svi</a>
                    <a class="list-group-item user-status list-group-item-action" data-toggle="list" id="RESERVED" v-on:click="setTicketStatus('RESERVED')">Rezervisano</a>
                    <a class="list-group-item user-status list-group-item-action" data-toggle="list" id="CANCELED" v-on:click="setTicketStatus('CANCELED')">Otkaano</a>
                </div>
                <button type="submit" class="btn btn-primary" @click="filter()">Filtriraj</button>
            </div>
            <div class="col-lg-10 col-md-10">
                <div class="table-wrapper-scroll">
                    <table class="table table-hover" v-if="tickets">
                        <thead>
                            <tr>
                            <th scope="col">Id ticket</th>
                            <th scope="col">Id manifestation</th>
                            <th scope="col">Date</th>
                            <th scope="col">Price</th>
                            <th scope="col">Status</th>
                            <th scope="col">Type</th>
                            </tr>
                        </thead>
                        <tbody>
                            
                            <tr v-for="ticket in tickets" v-bind:key="ticket.id">
                                <th scope="row">{{ticket.id}}</th>
                                <td>{{findManifName(ticket.manifestationId)}}</td>
                                <td>{{toStringDateTime(ticket.dateTime)}}</td>
                                <td>{{ticket.price}}</td>
                                <td>{{ticket.ticketStatus}}</td>
                                <td>{{ticket.ticketType}}</td>
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
            .get("rest/ticketservice/")
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
        // { "year": 2021, "monthValue": 1, "dayOfMonth": 25, "hour": 17, "minute": 38, "second": 23, "nano": 0, "month": "JANUARY", "dayOfWeek": "MONDAY", "dayOfYear": 25, "chronology": { "id": "ISO", "calendarType": "iso8601" } }
            toStringDateTime(date) {
                return date.year+"-"+date.monthValue+"-"+date.dayOfMonth+" " +date.hour+":"+date.minute+":"+date.second;
            },
            findManifName(id) {
                for (var i = 0; i < this.manifestations.length; i++) {
                    if(this.manifestations[i].id == id) {
                        return this.manifestations[i].name;
                    }
                }
            },
            setTicketType(type) {
                switch(type) {
                    case "ALL" : 
                        this.ticket_type = "Svi";
                        break;
                    case "VIP" :
                        this.ticket_type = "VIP";
                        break;
                    case "FAN_PIT" :
                        this.ticket_type = "FAN_PIT";
                        break;
                    case "REGULAR" :
                        this.ticket_type = "REGULAR";
                        break;
                    default :
                        break;
                }
            },
            setTicketStatus(status) {
                switch(status) {
                    case "ALL" :
                        this.ticket_status = "Svi";
                        break;
                    case "RESERVED" : 
                        this.ticket_status = "RESERVED";
                        break;
                    case "CANCELED" :
                        this.ticket_status = "CANCELED";
                        break;
                    default :
                        break;
                }
            },
            filter() {
                let id_type = $(".user_type .active").attr("id");
                let id_status = $(".user_status .active").attr("id");
                console.log(id_type, id_status);
                this.setTicketType(id_type);
                this.setTicketStatus(id_status);
                axios
                    .get("rest/ticketservice/filter", {
                        params: {
                            "dateFrom":this.dateFrom,
                            "dateTo":this.dateTo,
                            "priceFrom":this.priceFrom,
                            "priceTo":this.priceTo,
                            "text":this.text,
                            "ticket_type":this.ticket_type,
                            "ticket_status":this.ticket_status

                        }
                    })
                    .then(response => {
                        alert("RERESSSSI");
                        this.tickets = response.data;
                    })
            },
            search: function() {
			if(!(validatePrice(this.priceFrom, this.priceTo) && validateRange(this.dateFrom, this.dateTo))) {
				alert("GRESKA U KORACIMA");
			}
			
			axios
			.get("rest/ticketservice/search", {
				params: {
					"dateFrom":this.dateFrom,
					"dateTo":this.dateTo,
					"priceFrom":this.priceFrom,
					"priceTo":this.priceTo,
                    "text":this.text
					// "izborTipa":this.izborTipa
				}
			})
			.then(response => {
                alert("VUOLA");
				this.tickets = response.data;
			});

		}
        // setSelectedUser(id) {
        //     this.selected_user = id;
        // },
        // deleteUser() {
        //     axios
        //         .put("rest/userservice/delete-user/" + this.selected_user)
        //         .then(response => {
        //             this.users = response.data;
        //         });
        // },
        // retrieveUser() {
        //     axios
        //         .put("rest/userservice/retrieve-user/" + this.selected_user)
        //         .then(response => {
        //             this.users = response.data;
        //         });
        // },
        // search() {
        //     console.log(this.dateFrom, this.dateTo);
        //     axios
        //         .get("rest/userservice/search", {
        //             params: {
        //                 "text": this.text,
        //                 "dateFrom" : this.dateFrom,
        //                 "dateTo" : this.dateTo
        //             }
        //         })
        //         .then(response => {
        //             this.users = response.data;
        //         })
        // },
        // filter() {
        //     let id_type = $(".user_type .active").attr("id");
        //     let id_status = $(".user_status .active").attr("id");
        //     console.log(id_type, id_status);
        //     this.setUserType(id_type);
        //     this.setUserStatus(id_status);
        //     axios
        //         .get("rest/userservice/filter", {
        //             params: {
        //                 "text": this.text,
        //                 "dateFrom" : this.dateFrom,
        //                 "dateTo" : this.dateTo,
        //                 "role" : this.user_type,
        //                 "userStatus" : this.user_status
        //             }
        //         })
        //         .then(response => {
        //             this.users = response.data;
        //         })
        // },
        // setUserType(type) {
        //     switch(type) {
        //         case "ALL" : 
        //             this.user_type = "Svi";
        //             break;
        //         case "CUSTOMER" :
        //             this.user_type = "CUSTOMER";
        //             break;
        //         case "SELLER" :
        //             this.user_type = "SELLER";
        //             break;
        //         default :
        //             break;
        //     }
        // },
        // setUserStatus(status) {
        //     switch(status) {
        //         case "ALL" :
        //             this.user_status = "Svi";
        //             break;
        //         case "ACTIVE" : 
        //             this.user_status = "0";
        //             break;
        //         case "DELETED" :
        //             this.user_status = "1";
        //             break;
        //         default :
        //             break;
        //     }
        // },
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