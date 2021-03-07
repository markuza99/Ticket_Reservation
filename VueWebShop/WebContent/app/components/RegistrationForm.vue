<template>
	<div class="login-form-section">
		
		<div class="container h-100">
			<div class="row justify-content-md-center align-items-center h-100">
				
				<div class="col-lg-4 col-md-6 col-sm-12">
					<div class="login-form mt-5">
						<form @submit.prevent="register">
							<div class="form-group">
								<label class="form-label">Korisnicko ime</label> 
								<input type="text" v-model="user.username" class="form-control" id="username"/>
								<small id="data-error" v-if="username_error" class="form-text">
									Korisnicko ime vec postoji!
								</small>
							</div>
							
							<div class="form-group">
								<label class="form-label">Ime</label> 
								<input type="text" v-model="user.firstName" class="form-control" id="firstName"/>
							</div>
							
							<div class="form-group">
								<label class="form-label">Prezime</label> 
								<input type="text" v-model="user.lastName" class="form-control" id="lastName"/>
							</div>
							
							<div class="form-group">
								<label class="form-label">Lozinka</label>
								<input type="text" v-model="user.password" class="form-control" id="password"/>
							</div>
							
							<div class="form-group">
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" v-model="user.gender" name="inlineRadioOptions" id="inlineRadio1" value="MALE">
									<label class="form-check-label" for="inlineRadio1">Musko</label>
								</div>
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" v-model="user.gender" name="inlineRadioOptions" id="inlineRadio2" value="FEMALE">
									<label class="form-check-label" for="inlineRadio2">Zensko</label>
								</div>    
                        	</div>
							
							<div class="form-group">
								<label class="form-label">Datum rodjenja</label>
								<input type="date" v-model="user.birthDate" class="form-control" id="birthDate" value="2021-01-01" min="1900-01-01" max="2021-12-31"/>
							</div>
							
									
							<button type="submit" class="btn btn-primary mt-2">Registracija</button>
							<a class="btn btn-primary mt-2 float-right" href="#/" role="button">Nazad na login</a>
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
		user:{
			username: "",
			firstName: "",
			lastName: "",
			password: "",
			birthDate: "",
			gender: "",
			isDeleted: "0",
			role: "CUSTOMER"
		},
		username_error: false
      }
	},
    methods: {
      	register() {
			if(areInputFieldsEmpty(this.user)) {
    			document.getElementById("error").innerText = "Morate popuniti sva polja.";
        		return;
			}
			if(forbiddenSignInFields(this.user)) {
                alert("Ne mozete koristiti ; znak");
                return;
            }

			if(this.user.gender == "Musko") {
				this.user.gender = "MALE";
			} else {
				this.user.gender = "FEMALE";
			}
			
			var userJSON = JSON.stringify(this.user);
			console.log(userJSON);
			axios
				.post("rest/users/register", userJSON, {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					if(isEmpty(response.data)) {
						$('#username').addClass("error");
						this.username_error = true;
						
					} else {
						location.replace("#/login");
					}
				});
		},
		redirect : () => {
			location.replace("#/home");
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

#data-error {
	color: red;
}
.login-form-section {
	height: 100vh;
}

</style>
