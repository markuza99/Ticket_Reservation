<template>
 <div class="user-page">
     <div class="d-inline col-sm-4 bg-primary text-white">d-inline</div>
     <div class="d-inline col-sm-4 bg-primary text-white">d-inline1</div>
     <div class="d-inline col-sm-4 bg-primary text-white">d-inlin2</div>

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
			window.location.replace("/registration");
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