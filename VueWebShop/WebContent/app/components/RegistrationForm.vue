<template>
    <div class="registration-form-section">
		<div class="container h-100">
			<div class="row justify-content-md-end align-items-center h-100">
				
				<div class="col-lg-4 col-md-6 col-sm-12">
					<div class="login-form mt-5">
						<form @submit.prevent="handleSubmit">
							<div class="form-group">
								<label class="form-label">First name</label> 
								<input type="text" class="form-control" id="firstName"/>
							</div>

							<div class="form-group">
								<label class="form-label">Last name</label> 
								<input type="text" class="form-control" id="lastName"/>
							</div>

							<div class="form-group">
								<label class="form-label">Username</label> 
								<input type="text" class="form-control" id="username"/>
							</div>
							
							<div class="form-group">
								<label class="form-label">Password</label>
								<input type="text" class="form-control" id="password"/>
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