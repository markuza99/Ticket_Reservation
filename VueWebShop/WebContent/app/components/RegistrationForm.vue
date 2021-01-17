<template>
<<<<<<< Updated upstream
    <div class="registration-form-section">
		<div class="container h-100">
			<div class="row justify-content-md-end align-items-center h-100">
				
				<div class="col-lg-4 col-md-6 col-sm-12">
					<div class="login-form mt-5">
						<form @submit.prevent="handleSubmit">
=======
	<div class="login-form-section">
		
		<div class="container h-100">
			<div class="row justify-content-md-center align-items-center h-100">
				
				<div class="col-lg-4 col-md-6 col-sm-12">
					<div class="login-form mt-5">
						<form @submit.prevent="register">
							<div class="form-group">
								<label class="form-label">Username</label> 
								<input type="text" class="form-control" id="username"/>
							</div>
							
>>>>>>> Stashed changes
							<div class="form-group">
								<label class="form-label">First name</label> 
								<input type="text" class="form-control" id="firstName"/>
							</div>
<<<<<<< Updated upstream

=======
							
>>>>>>> Stashed changes
							<div class="form-group">
								<label class="form-label">Last name</label> 
								<input type="text" class="form-control" id="lastName"/>
							</div>
<<<<<<< Updated upstream

							<div class="form-group">
								<label class="form-label">Username</label> 
								<input type="text" class="form-control" id="username"/>
							</div>
=======
>>>>>>> Stashed changes
							
							<div class="form-group">
								<label class="form-label">Password</label>
								<input type="text" class="form-control" id="password"/>
<<<<<<< Updated upstream
							</div>	

							<div class="form-group">
								<label class="form-label">Birthday</label>
								<input class="form-control" type="date" id="birthday" name="trip-start">
							</div>
							
							<div class="form-group">
								<div class="form-check form-check-inline">
								<input class="form-check-input" type="radio" name="gender" id="male">
								<label class="form-check-label" for="male">
									Male
								</label>
								</div>
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="gender" id="female" checked>
									<label class="form-check-label" for="female">
										Female
									</label>
								</div>
							</div>
							
							<!-- <a class="btn btn-primary" href="#/" role="button">Link</a> -->
							<button type="submit" class="btn btn-primary mt-2">Sign Up</button>
						
=======
							</div>
							
							<div class="form-group">
								<label class="form-label">Gender</label> 
								<select class="form-control" id="gender">
									<option value="male">male</option>
									<option value="female">female</option>
								</select>
							</div>
							
							<div class="form-group">
								<label class="form-label">Birth date</label>
								<input type="date" class="form-control" id="birthDate" name="trip-start" value="01-01-2021" min="1900-01-01" max="2021-12-31"/>
							</div>
							
									
							<button type="submit" class="btn btn-primary mt-2">Register</button>
							<a class="btn btn-primary mt-2 float-right" href="#/" role="button">Back to Login</a>
							<!-- <button type="submit" class="btn btn-primary mt-2 float-right" v-on:click="redirect">Register</button> -->
>>>>>>> Stashed changes
							<p id="error"></p>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
<<<<<<< Updated upstream
    module.exports = {
        data() {
            return {
                user: {}
            };
        },
        methods: {
            handleSubmit: function(){
                let username = $("#username").val();
                let password = $("#password").val();
                let firstName = $("#firstName").val();
                let lastName = $("#lastName").val();
                let gender = "FEMALE";
				let birthDate = $('#birthday').val();
				let role = "CUSTOMER";
				user = {username, password, firstName, lastName, gender, birthDate, role};
				console.log(user);
				var jsonString = JSON.stringify(user);
				console.log(jsonString);
                axios
                .post("rest/userservice/registration", jsonString, {
                    headers: {'content-type':'application/json'}
                })
                .then(response=>{
                    this.user = response.data
                    if(this.user != "") {
                        location.replace("#/");
                    } 
                });
            }
		}
	}
</script>
=======
  module.exports = {
    data() {
      return {
		user:{},
		msg : "poruka"
      }
	},
    methods: {
      	register : () => {
			
			var username = $("#username").val();
			var password = $("#password").val();
			var firstName = $("#firstName").val();
			var lastName = $("#lastName").val();
			var gender = $( "#gender option:selected" ).text();
			var d = new Date($('#date-input').val());
			var birthDate = document.getElementById("birthDate").value;
			var role = "CUSTOMER";
			user = {username, password, firstName, lastName, gender, birthDate, role};
			
			if(areInputFieldsEmpty(user)) {
    			document.getElementById("error").innerText = "Morate popuniti sva polja.";
        		return;
    		}
			var userJSON = JSON.stringify(user);
			axios
				.post("rest/userservice/register", userJSON, {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					this.user = response.data;
					console.log(this.user);
					if(!isEmpty(this.user)) {
						alert(this.user.firstName);
						location.replace("#/");
					} else {
						alert("Korisnik sa tim usernameom vec postoji!!!");
						location.replace("#/registration");
					}
				});
		},
		redirect : () => {
			location.replace("#/home");
		}
    },
	
    mounted () {
		axios
			.get("rest/userservice/testlogin")
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
>>>>>>> Stashed changes
