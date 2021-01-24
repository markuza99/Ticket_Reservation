<template>
 <div class="header-section">
   	<nav v-if="!isLoggedIn" class="navbar navbar-light bg-primary justify-content-between navbar-fixed-top">
	<a class="navbar-brand text-white">Ulaznice.rs</a>
	<form class="form-inline">
		<button class="btn btn-primary btn-outline-light my-2 my-sm-0" type="submit">Sign In</button>
	</form>
	</nav>
	<nav v-if="isLoggedIn" class="navbar navbar-light bg-primary justify-content-between navbar-fixed-top">
	<a class="navbar-brand text-white">Ulaznice.rs</a>
	<a class="navbar-brand text-white" v-on:click="profileSettings()">Moj Profil</a>
	<a class="navbar-brand text-white">Moje ulaznice</a>
	<form class="form-inline">
		<button class="btn btn-primary btn-outline-light my-2 my-sm-0" type="submit" v-on:click="logOut()">Sign Out</button>
	</form>
	</nav>
 </div>
</template>

<script>

module.exports = {
	data() {
		return {
			isLoggedIn: false,
			user: {}
		}	
	},
	methods: {
		logOut: function() {
			axios
			.get("rest/userservice/logout")
			.then(response => {
				
				this.user = response.data;
				this.isLoggedIn = false;
				alert(this.user);
				window.location.reload();

			});
		},
		profileSettings: function() {
			window.location.replace("#/userinfo");
		}
	},
	mounted() {
		axios
			.get("rest/userservice/testlogin")
			.then(response => {
				
				this.user = response.data;
				if(!isEmpty(this.user)) {
						// this.$root.$emit('logged-in-user',response.data);
						this.isLoggedIn = true;
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
</style>