<template>
    <div class="container-fluid" id="users-panel">
        <form class="form-inline justify-content-between">
            <div class="form-group">
                <label class="mr-2 text-uppercase">Datum rodjenja:</label>
                <input type="date" class="form-control mr-2">
                <input type="date" class="form-control mr-5">
            </div>
            <div class="form-group">
                <input type="text" class="form-control mr-2">
                <button type="submit" class="btn btn-primary">
                    <i class="fa fa-search"></i>
                </button>
            </div>
        </form>
        <div class="row">
            <div class="col-lg-2 col-md-2">
                <div class="mb-2 font-weight-bold">Tip korisnika</div>
                <div class="list-group" id="list-tab" role="tablist">
                    <a class="list-group-item user-type list-group-item-action active" id="list-home-list" data-toggle="list" href="#list-home" role="tab" aria-controls="home">Svi korisnici</a>
                    <a class="list-group-item user-type list-group-item-action" id="list-profile-list" data-toggle="list" href="#list-profile" role="tab" aria-controls="profile">Kupci</a>
                    <a class="list-group-item user-type list-group-item-action" id="list-messages-list" data-toggle="list" href="#list-messages" role="tab" aria-controls="messages">Prodavci</a>
                </div>

                <div class="mb-2 mt-5 font-weight-bold">Status korisnika</div>
                <div class="list-group" id="list-tab" role="tablist">
                    <a class="list-group-item user-status list-group-item-action" id="list-messages-list" data-toggle="list" href="#list-messages" role="tab" aria-controls="messages">Svi</a>
                    <a class="list-group-item user-status list-group-item-action active" id="list-home-list" data-toggle="list" href="#list-home" role="tab" aria-controls="home">Aktivni</a>
                    <a class="list-group-item user-status list-group-item-action" id="list-profile-list" data-toggle="list" href="#list-profile" role="tab" aria-controls="profile">Obrisani</a>
                </div>
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
            filtered_users : "SVI"
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