<template>
    <div id="users-panel">
        <div class="container" v-if="role == 'ADMIN'">
            <div class="row">
                <div class="col-2">
                    <label>Uloga</label>
                    <select class="form-control" v-model="roleForFilter">
                    <option value="all">Sve</option>
                    <option value="SELLER">Prodavac</option>
                    <option value="CUSTOMER">Kupac</option>
                    <option value="ADMIN">Admin</option>
                    </select>
                </div>
                <div class="col-2">
                    <label>Tip kupca</label>
                    <select class="form-control" v-model="type">
                    <option value="all">Sve</option>
                    <option value="zlatni">Zlatni</option>
                    <option value="bronzani">Bronzani</option>
                    <option value="srebrni">Srebrni</option>
                    <option value="regularni">Regularni</option>
                    </select>
                </div>
                <div class="col-2">
                    <label>Sortiraj po</label>
                    <select class="form-control" v-model="sortBy">
                    <option value="firstName">Ime</option>
                    <option value="lastName">Prezime</option>
                    <option value="username">Korisnicko ime</option>
                    <option value="points">Broj bodova</option>
                    </select>
                </div>
                <div class="col-4">
                    <input type="radio" id="one" value="Asc" v-model="sortOrder" class="mt-5">
                    <label for="one" class="mr-2">Rastuce</label>
                    <input type="radio" id="two" value="Desc" v-model="sortOrder" class="mt-5">
                    <label for="two">Opadajuce</label>
                </div>
                <div class="col-2 text-right">
                    <button v-if="role == 'ADMIN'" class="btn btn-green" data-toggle="modal" data-target="#createUserModal">Dodaj korisnika</button>
                </div>
                <div class="col-12 mb-3">
                    <div class="d-flex">
                        <input type="text"  v-model="searchQuery" class="mt-3 mr-3 form-control">
                        <button type="submit" class="btn btn-pink mt-2" v-on:click="searchUsers">Potvrdi</button>
                    </div>
                </div>
            </div>
        </div>
        

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
                            <div class="form-group col-md-12">
                            <label>Korisnicko ime</label>
                            <input type="text" class="form-control" v-model="new_user.username" placeholder="Korisnicko ime">
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                            <label>Lozinka</label>
                            <input type="text" class="form-control" v-model="new_user.password" placeholder="Lozinka">
                            </div>
                            <div class="form-group col-md-6">
                            <label>Ponovite lozinku</label>
                            <input type="text" class="form-control" v-model="new_user.repeat_password" placeholder="Ponovite lozinku">
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
                                <option value="CUSTOMER">Kupac</option>
                                <option value="SELLER">Prodavac</option>
                            </select>
                            </div>
                        </div>
                        
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazivanje</button>
                    <button type="button" class="btn btn-green" v-on:click="createUser">Potvrda</button>
                    
                </div>
                </div>
            </div>
        </div>

        <div class="container"> 
            <div class="row">
                <div class="col-12">
                    <table class="table table-hover table-borderless table-striped" v-if="users">
                        <thead>
                            <tr>
                            <th scope="col">Korisnicko ime</th>
                            <th scope="col">Ime</th>
                            <th scope="col">Prezime</th>
                            <th scope="col">Pol</th>
                            <th scope="col">Uloga</th>
                            <th scope="col">Broj bodova</th>
                            <th scope="col">Tip kupca</th>
                            <th scope="col" v-if="role=='ADMIN'"><i class="fa fa-trash"></i></th>
                            </tr>
                        </thead>
                        <tbody>
                            
                            <tr v-for="user in users" v-bind:key="user.username" v-bind:class="{ table_warning : user.deleted == 1}">
                                <th scope="row">{{user.username}}</th>
                                <td>{{user.firstName}}</td>
                                <td>{{user.lastName}}</td>
                                <td>{{user.gender}}</td>
                                <td>{{user.role}}</td>
                                <td>{{user.points}}</td>
                                <td>{{user.type}}</td>
                                <td v-if="user.deleted == 0 && role=='ADMIN'">
                                    <button type="button" class="btn btn-green" data-toggle="modal" data-target="#deleteUserModal"
                                    v-on:click="setSelectedUser(user.username)">
                                        <i class="fa fa-trash"></i>
                                    </button>
                                </td>
                                <td v-if="user.deleted == 1 && role=='ADMIN'" class="text-danger">
                                    <button type="button" class="btn btn-pink-invert" data-toggle="modal" data-target="#retrieveUserModal"
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
                    <button type="button" class="btn btn-green" v-on:click="deleteUser" data-dismiss="modal">Potvrda</button>
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
                    <button type="button" class="btn btn-green" v-on:click="retrieveUser" data-dismiss="modal">Potvrda</button>
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
            searchQuery : "",
            type : "all",
            roleForFilter : "all",
            new_user : {
                username : "",
                password : "",
                date : "",
                first_name : "",
                last_name : "",
                gender : "",
                role : "SELLER",
                is_deleted : "0"
            },
            role: null,
            sortOrder: 'Asc',
            sortBy: 'username'
        }
    },
    mounted() {
        this.getUsers()
        axios
            .get("rest/users/me")
            .then(response => {
                this.role = response.data.role;
            });
        
    },
    methods : {
        getUsers () {
            axios
            .get("rest/users")
            .then(response => {
                this.users = response.data
            })
        },
        setSelectedUser(id) {
            this.selected_user = id;
        },
        deleteUser() {
            axios
                .put("rest/users/delete/" + this.selected_user)
                .then(() => {
                    this.getUsers()
                    new Toast({
                        message: 'Korisnik uspesno obrisan!',
                        type: 'success'
                    });
                });
        },
        retrieveUser() {
            axios
                .put("rest/users/retrieve/" + this.selected_user)
                .then(() => {
                    this.getUsers()
                    new Toast({
                        message: 'Korisnik uspesno povracen!',
                        type: 'success'
                    });
                });
        },
        searchUsers() {
            const sortBy = this.sortBy + this.sortOrder
            console.log(sortBy)
            axios
                .get("rest/users", {
                    params: {
                        "searchQuery": this.searchQuery,
                        "role" : this.roleForFilter,
                        "type" : this.type,
                        "sortBy": sortBy
                    }
                })
                .then(response => {
                    this.users = response.data;
                })
        },
        createUser() {
            if(areInputFieldsEmpty(this.new_user)) {
                alert("Morate popuniti sva polja");
                return;
            }
            if(forbiddenSignInFields(this.new_user)) {
                alert("Ne mozete koristiti ; znak");
                return;
            }
            if(this.new_user.password !== this.new_user.repeat_password) {
                alert("Lozinke se ne poklapaju");
                return;
            }
            
            var userNew = {
                username : this.new_user.username.trim(),
                password : this.new_user.password.trim(),
                firstName : this.new_user.first_name.trim(),
                lastName : this.new_user.last_name.trim(),
                gender : this.new_user.gender.trim(),
                birthDate : this.new_user.date.trim(),
                role : this.new_user.role,
                isDeleted : this.new_user.is_deleted 
            };

            var userJSON = JSON.stringify(userNew);
            axios
				.post("rest/users/register", userJSON, {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					if(response.data != "") {
						new Toast({
                            message: 'Korisnik uspesno kreiran!',
                            type: 'success'
                        });
                        this.getUsers()
					} else {
						new Toast({
                            message: 'Korisnik sa tim korisnickim imenom vec postoji!',
                            type: 'danger'
                        });
					}
				});
        }
    }
}
</script>

<style scoped>

#users-panel {
    padding-top: 3em;
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

</style>