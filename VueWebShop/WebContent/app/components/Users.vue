<template>
    <div class="container-fluid" id="users-panel">
        <form class="form-inline justify-content-between" @submit.prevent="search">
            <div class="form-group">
                <label class="mr-2 text-uppercase">Datum rodjenja:</label>
                <input type="date" v-model="date_from" class="form-control mr-2"/>
                <input type="date" v-model="date_to" class="form-control mr-5"/>
            </div>
            <div class="form-group">
                <button class="btn btn-primary mr-2" data-toggle="modal" data-target="#createUserModal">Dodaj korisnika</button>
                <input type="text" v-model="text" class="form-control mr-2"/>
                <button type="submit" class="btn btn-primary">
                    <i class="fa fa-search"></i>
                </button>
            </div>
        </form>

        <div class="modal fade" id="createUserModal" tabindex="-1" role="dialog" aria-labelledby="createUserModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createUserModalLabel">Dodavanje korisnika</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                            <label>Ime</label>
                            <input type="text" class="form-control" v-model="new_user.first_name" placeholder="Ime">
                            </div>
                            <div class="form-group col-md-6">
                            <label>Prezime</label>
                            <input type="text" class="form-control" v-model="new_user.last_name" placeholder="Prezime">
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                            <label>Korisnicko ime</label>
                            <input type="text" class="form-control" v-model="new_user.username" placeholder="Korisnicko ime">
                            </div>
                            <div class="form-group col-md-6">
                            <label>Lozinka</label>
                            <input type="text" class="form-control" v-model="new_user.password" placeholder="Lozinka">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" v-model="new_user.gender" name="inlineRadioOptions" id="inlineRadio1" value="MALE">
                            <label class="form-check-label" for="inlineRadio1">Musko</label>
                            </div>
                            <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" v-model="new_user.gender" name="inlineRadioOptions" id="inlineRadio2" value="FEMALE">
                            <label class="form-check-label" for="inlineRadio2">Zensko</label>
                        </div>    
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                            <label>Datum rodjenja</label>
                            <input type="date" class="form-control" v-model="new_user.date">
                            </div>
                            <div class="form-group col-md-6">
                            <label>Tip korisnika</label>
                            <select class="form-control" v-model="new_user.role">
                                <option selected>Kupac</option>
                                <option>Prodavac</option>
                            </select>
                            </div>
                        </div>
                        
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazivanje</button>
                    <button type="button" class="btn btn-primary" v-on:click="createUser">Potvrda</button>
                    
                </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-2 col-md-2">
                <div class="mb-2 font-weight-bold">Tip korisnika</div>
                <div class="list-group user_type" id="list-tab" role="tablist">
                    <a class="list-group-item list-group-item-action active" data-toggle="list" id="ALL" v-on:click="setUserType('ALL')">Svi</a>
                    <a class="list-group-item list-group-item-action" data-toggle="list" id="CUSTOMER" v-on:click="setUserType('CUSTOMER')">Kupci</a>
                    <a class="list-group-item list-group-item-action" data-toggle="list" id="SELLER" v-on:click="setUserType('SELLER')">Prodavci</a>
                </div>

                <div class="mb-2 mt-5 font-weight-bold">Status korisnika</div>
                <div class="list-group user_status" id="list-tab" role="tablist">
                    <a class="list-group-item user-status list-group-item-action" data-toggle="list" id="ALL" v-on:click="setUserStatus('ALL')">Svi</a>
                    <a class="list-group-item user-status list-group-item-action active" data-toggle="list" id="ACTIVE" v-on:click="setUserStatus('ACTIVE')">Aktivni</a>
                    <a class="list-group-item user-status list-group-item-action" data-toggle="list" id="DELETED" v-on:click="setUserStatus('DELETED')">Obrisani</a>
                </div>
                <button type="submit" class="btn btn-primary" v-on:click="filter">Filtriraj</button>
            </div>
            <div class="col-lg-10 col-md-10">
                <div class="table-wrapper-scroll">
                    <table class="table table-hover" v-if="users">
                        <thead>
                            <tr>
                            <th scope="col">Username</th>
                            <th scope="col">First Name</th>
                            <th scope="col">Last Name</th>
                            <th scope="col">Password</th>
                            <th scope="col">Gender</th>
                            <th scope="col">Role</th>
                            <th scope="col">Date</th>
                            <th scope="col"><i class="fa fa-trash"></i></th>
                            </tr>
                        </thead>
                        <tbody>
                            
                            <tr v-for="user in users" v-bind:key="user.username" v-bind:class="{ table_warning : user.isDeleted == 1}">
                                <th scope="row">{{user.username}}</th>
                                <td>{{user.firstName}}</td>
                                <td>{{user.lastName}}</td>
                                <td>{{user.password}}</td>
                                <td>{{user.gender}}</td>
                                <td>{{user.role}}</td>
                                <td>{{user.dateString}}</td>
                                <td v-if="user.isDeleted == 0">
                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#deleteUserModal"
                                    v-on:click="setSelectedUser(user.username)">
                                        <i class="fa fa-trash"></i>
                                    </button>
                                </td>
                                <td v-if="user.isDeleted == 1" class="text-danger">
                                    <button type="button" class="btn btn-warning" data-toggle="modal" data-target="#retrieveUserModal"
                                    v-on:click="setSelectedUser(user.username)">
                                        <i class="fa fa-undo"></i>
                                    </button>
                                </td>

                            </tr>
                        </tbody>
                    </table>
                </div>
                
            </div>
        </div>
        
					
        <div class="modal fade" id="deleteUserModal" tabindex="-1" role="dialog" aria-labelledby="deleteUserModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteUserModalLabel">Brisanje korisnika</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    Da li ste sigurni?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazivanje</button>
                    <button type="button" class="btn btn-primary" v-on:click="deleteUser" data-dismiss="modal">Potvrda</button>
                </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="retrieveUserModal" tabindex="-1" role="dialog" aria-labelledby="retrieveUserModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="retrieveUserModalLabel">Vracanje korisnika</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    Da li ste sigurni da zelite da povratite korisnika iz obrisanih korisnika?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazivanje</button>
                    <button type="button" class="btn btn-primary" v-on:click="retrieveUser" data-dismiss="modal">Potvrda</button>
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
            users : [],
            selected_user : "",
            filtered_users : "SVI",
            text : "",
            date_from : "",
            date_to : "",
            user_type : "",
            user_status : "",
            new_user : {
                username : "",
                password : "",
                date : "",
                first_name : "",
                last_name : "",
                gender : "",
                role : "Kupac",
                is_deleted : "0"
            }
        }
    },
    mounted() {
        axios
            .get("rest/userservice/")
            .then(response => {
                this.users = response.data;
                this.users.forEach(user => makeDateString(user));
                console.log(this.users);
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
        setSelectedUser(id) {
            this.selected_user = id;
        },
        deleteUser() {
            axios
                .put("rest/userservice/delete-user/" + this.selected_user)
                .then(response => {
                    this.users = response.data;
                });
        },
        retrieveUser() {
            axios
                .put("rest/userservice/retrieve-user/" + this.selected_user)
                .then(response => {
                    this.users = response.data;
                });
        },
        search() {
            console.log(this.date_from, this.date_to);
            axios
                .get("rest/userservice/search", {
                    params: {
                        "text": this.text,
                        "dateFrom" : this.date_from,
                        "dateTo" : this.date_to
                    }
                })
                .then(response => {
                    this.users = response.data;
                })
        },
        filter() {
            let id_type = $(".user_type .active").attr("id");
            let id_status = $(".user_status .active").attr("id");
            console.log(id_type, id_status);
            this.setUserType(id_type);
            this.setUserStatus(id_status);
            axios
                .get("rest/userservice/filter", {
                    params: {
                        "text": this.text,
                        "dateFrom" : this.date_from,
                        "dateTo" : this.date_to,
                        "role" : this.user_type,
                        "userStatus" : this.user_status
                    }
                })
                .then(response => {
                    this.users = response.data;
                })
        },
        setUserType(type) {
            switch(type) {
                case "ALL" : 
                    this.user_type = "Svi";
                    break;
                case "CUSTOMER" :
                    this.user_type = "CUSTOMER";
                    break;
                case "SELLER" :
                    this.user_type = "SELLER";
                    break;
                default :
                    break;
            }
        },
        setUserStatus(status) {
            switch(status) {
                case "ALL" :
                    this.user_status = "Svi";
                    break;
                case "ACTIVE" : 
                    this.user_status = "0";
                    break;
                case "DELETED" :
                    this.user_status = "1";
                    break;
                default :
                    break;
            }
        },
        createUser() {
            console.log(this.new_user);
            if(areInputFieldsEmpty(this.new_user)) {
                $('#createUserModal input').addClass("error");
                $('#createUserModal input').removeClass("success");
                return;
            }
            $('#createUserModal input').removeClass("error");
            $('#createUserModal input').addClass("success");
            // private String username;
            // private String firstName;
            // private String lastName;
            // private String password;
            // private Gender gender;
            // private LocalDate birthDate;
            // private Role role;
            // private Boolean isDeleted;
            var rola = "";
            switch(this.new_user.role) {
                case "Kupac":
                    // code block
                    rola = "CUSTOMER";
                    break;
                case "Prodavac":
                    // code block
                    rola = "SELLER";
                    break;
                default:
                    // code block
                    break;
            }
            var userNew = {
                username : this.new_user.username.trim(),
                password : this.new_user.password.trim(),
                firstName : this.new_user.first_name.trim(),
                lastName : this.new_user.last_name.trim(),
                gender : this.new_user.gender.trim(),
                birthDate : this.new_user.date.trim(),
                role : rola,
                isDeleted : this.new_user.is_deleted 
            };
            var userJSON = JSON.stringify(userNew);
            console.log(userJSON);
            axios
				.post("rest/userservice/addUser", userJSON, {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					if(response.data != "") {
						alert(userNew.username + " Uspesno dodat!");
                        this.users = response.data;
					} else {
						alert("Korisnik sa tim usernameom vec postoji!!!");
					}
				});
            console.log(this.new_user);
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