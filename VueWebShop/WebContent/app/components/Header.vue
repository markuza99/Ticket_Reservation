<template>
 <div class="header-section">
	<nav class="navbar navbar-light bg-primary justify-content-between navbar-fixed-top">
		<a class="navbar-brand text-white">Ulaznice.rs</a>
		
		<form class="form-inline" v-if="isLoggedIn">
			<a class="text-white p-2" v-on:click="profileSettings()">Profil</a>
			<a class="text-white p-2">Karte</a>
			<a class="text-white p-2" v-if="role != 'CUSTOMER'">Manifestacije</a>
			<a class="text-white p-2" v-if="role != 'CUSTOMER'">Komentari</a>
			<button class="btn btn-primary btn-outline-light my-2 my-sm-0" v-on:click="signOut()">Sign Out</button>
		</form>
		<form class="form-inline"  v-if="!isLoggedIn">
			<button class="btn btn-primary btn-outline-light my-2 my-sm-0" type="submit" v-on:click="signIn">Sign In</button>
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
			.then(function() {
				this.isLoggedIn = false;
				window.location.reload();
			});
		},
		signIn: function() {
			window.location.replace("#/login");
		},
		profileSettings: function() {
			window.location.replace("#/profile");
		}
	},
	mounted() {
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
}
</style>