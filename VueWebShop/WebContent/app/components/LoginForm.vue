<template>
	<div class="login-form-section">
		<div class="container h-100">
			<div class="row justify-content-md-center align-items-center h-100">
				<div class="col-lg-4 col-md-6 col-sm-12">
					<div class="login-form mt-5">
						<form @submit.prevent="login">
							<div class="form-group">
								<label class="form-label">Korisnicko ime</label> 
								<input type="text" class="form-control" v-model="user.username"/>
							</div>
							<div class="form-group">
								<label class="form-label">Lozinka</label>
								<input type="text" class="form-control" v-model="user.password"/>
							</div>	
							
							<a class="btn btn-green mt-2" href="#/registration" role="button">Registracija</a>
							<button type="submit" class="btn btn-green mt-2 float-right">Prijava</button>
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
		user: {
			username: "",
			password: ""
		}
      }
	},
    methods: {
      login () {
			$("#error").html("");
			if(areInputFieldsEmpty(this.user)) {
    			$("#error").html("Morate popuniti sva polja.");
        		return;
    		}
			var userJSON = JSON.stringify(this.user);
			axios
				.post("rest/users/login", userJSON, {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					if(!isEmpty(response.data)) {
						this.$router.push('/')
						this.$router.go()
					} else {
						$("#error").html("Pogresno korisnicko ime/lozinka");
					}
				});
		}
    },
	
    mounted () {
		axios
			.get("rest/users/role")
			.then(response => {
				if(!isEmpty(response.data)) {
					redirect("#/");
				}
			});
	}
  }
</script>

<style scoped>

.login-form-section {
	height: calc(100vh - (56px + 56px));
	width: 100vw;
	overflow: auto;
}

</style>