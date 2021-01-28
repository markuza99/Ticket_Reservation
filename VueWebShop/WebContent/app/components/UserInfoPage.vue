<template>
 <div class="user-page">
    <form>
	<div class="form-row">
		<div class="form-group col-md-6">
		<label for="inputFirstName">First name</label>
		<input type="text" class="form-control" id="inputFirstName" v-model="user.firstName">
		</div>
		<div class="form-group col-md-6">
		<label for="inputLastName">Last name</label>
		<input type="text" class="form-control" id="inputLastName" v-model="user.lastName">
		</div>
	</div>
	<div class="form-group">
		<label for="inputUsername">Username</label>
		<input type="text" class="form-control" id="inputUsername" v-model="user.username">
	</div>
	<div class="form-group">
		<label for="inputPassword">Password</label>
		<input type="text" class="form-control" id="inputPassword" v-model="user.password">
	</div>
	<div class="form-row">
		<div class="form-group col-md-4">
		<label for="inputGender">Gender</label>
		<select v-model="user.gender">
			<option v-for="option in options" v-bind:value="option.value">
				{{ option.text }}
			</option>
		</select>
		</div>
	</div>
	<div class="form-row">
		<label for="inputDate">Birth date</label>
		<input type="date" class="form-control" id="inputDate" v-model="user.birthDate"/>
	</div>
	<button type="submit" class="btn btn-primary" @click="updateProfile()">Sign in</button>
	</form>

 </div>
</template>

<script>

module.exports = {
	data() {
		return {
			isLoggedIn: false,
			oldUsername: "",
			user : {
            },
			options: [
			{ text: 'Muski', value: 'MALE' },
			{ text: 'Zenski', value: 'FEMALE' },
			]
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
		},
		updateProfile: function() {
			console.log(this.user);
			axios
				.post("rest/userservice/updateUser", JSON.stringify({user:this.user, oldUsername:this.oldUsername}), {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					alert("BRAVO");
				});
		}
	},
	mounted() {
		axios
			.get("rest/userservice/test-login")
			.then(response => {
				
				this.user = response.data;
				this.oldUsername = this.user.username;
				if(!isEmpty(this.user)) {
						// this.$root.$emit('logged-in-user',response.data);
						this.isLoggedIn = true;
					}
				alert(this.user.username);


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