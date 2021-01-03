<template>
 <div class="header-section">
   	<nav class="navbar navbar-light bg-primary justify-content-between fixed-top">
	<a class="navbar-brand text-white">Ulaznice.rs</a>
	<form class="form-inline">
		<button class="btn btn-primary btn-outline-light my-2 my-sm-0" type="submit">Sign In</button>
	</form>
	</nav>
 </div>
</template>

<script>

module.exports = {
	data() {
		return {
			isLoggedIn: false
		}	
	},
	methods: {
		
	}
}
</script>

<style scoped>
	
form.form-inline {
	padding: 0;
	margin:0;

}
</style>