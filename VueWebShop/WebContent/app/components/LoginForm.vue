<template>
	<div class="login-form-section">
		
		<div class="container h-100">
			<div class="row justify-content-md-center align-items-center h-100">
				
				<div class="col-lg-4 col-md-6 col-sm-12">
					<div class="login-form mt-5">
						<form @submit.prevent="login">
							<div class="form-group">
								<label class="form-label">Username</label> 
								<input type="text" class="form-control" id="username"/>
							</div>
							
							<div class="form-group">
								<label class="form-label">Password</label>
								<input type="text" class="form-control" id="password"/>
							</div>	
							<button type="submit" class="btn btn-primary mt-2">Sign in</button>
							<a class="btn btn-primary mt-2 float-right" href="#/registration" role="button">Register</a>
							<!-- <button type="submit" class="btn btn-primary mt-2 float-right" v-on:click="redirect">Register</button> -->
							<p id="error"></p>
						</form>
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
		user:{},
		msg : "poruka"
      }
	},
    methods: {
      	login : () => {
			
			var username = $("#username").val();
			var password = $("#password").val();
			user = {username, password};
			if(areInputFieldsEmpty(user)) {
    			document.getElementById("error").innerText = "Morate popuniti sva polja.";
        		return;
    		}
			var userJSON = JSON.stringify(user);
			axios
				.post("rest/users/login", userJSON, {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					this.user = response.data;
					if(!isEmpty(this.user)) {
						// this.$root.$emit('logged-in-user',response.data);
						window.location.replace("#/");
						window.location.reload();
					}
				});
		},
		redirect : () => {
			location.replace("#/");
		}
    },
	
    mounted () {
		axios
			.get("rest/users/test-login")
			.then(response => {
				if(!isEmpty(response.data)) {
					this.redirect();
				}
			});
	}
  }
</script>

<style scoped>

.login-form-section {
	height: 100vh;
}

</style>