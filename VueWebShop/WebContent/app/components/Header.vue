<template>
 <div class="header-section">
	<nav class="navbar navbar-light justify-content-between navbar-fixed-top">
		<a class="navbar-brand text-white" v-on:click="goToWelcomePage()">Ulaznice.rs</a>
		
		<form class="form-inline navbar-links" v-if="isLoggedIn">
			<a class="text-white p-2" v-on:click="profileSettings()">Profil</a>
			<a class="text-white p-2" v-on:click="goToTickets()">Karte</a>
			<a class="text-white p-2" v-on:click="goToManifestations()">Manifestacije</a>
			<a class="text-white p-2" v-if="role != 'CUSTOMER'" v-on:click="goToComments()">Komentari</a>
			<a class="text-white p-2" v-if="role != 'CUSTOMER'" v-on:click="goToUsers()">Korisnici</a>
			<button class="btn btn-outline-light my-2 my-sm-0" v-on:click="signOut()">Odjava</button>
		</form>
		<form class="form-inline"  v-if="!isLoggedIn">
			<button class="btn btn-outline-light my-2 my-sm-0" type="submit" v-on:click="signIn">Prijava</button>
		</form>
	</nav>
 </div>
</template>

<script>

module.exports = {
	data() {
		return {
			isLoggedIn: false,
			role: null
		}	
	},
	methods: {
		signOut: function() {
			axios
			.post("rest/users/logout")
			.then(() => {
				this.isLoggedIn = false;
				this.$router.push('/login')
			});
		},
		signIn: function() {
			this.$router.push('/login')
		},
		profileSettings: function() {
			this.$router.push('/profile')
		},
		goToManifestations () {
			this.$router.push('/manifestations')
		},
		goToWelcomePage () {
			this.$router.push('/')
		},
		goToTickets () {
			this.$router.push('/tickets')
		},
		goToUsers() {
			this.$router.push('/users')
		},
		goToComments() {
			this.$router.push('/comments')
		}
	},
	mounted() {
		console.log(this.$router)
		axios
			.get("rest/users/role")
			.then(response => {
				if(!isEmpty(response.data)) {
						this.isLoggedIn = true;
						this.role = response.data;
					}
			});
	}
}
</script>

<style scoped>
	
form.form-inline {
	padding: 0;
	margin:0;
}

nav {
	height: 56px;
	background: #FFAAA7;
}

.header-section {
	background: #FFAAA7;
}

.navbar-links a {
	cursor: pointer;
	margin: 0 1em;
	text-transform: uppercase;
	text-shadow: 3px 3px 3px #ff827e;
}

.navbar-links a:hover {
	text-decoration: none;
}

.navbar-brand {
	cursor: pointer;
}
</style>