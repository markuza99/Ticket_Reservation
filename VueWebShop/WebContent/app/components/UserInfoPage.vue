<template>
 <div class="user-page container mt-3">
    <form @submit.prevent="updateProfile">
		<div class="form-row">
			<div class="form-group col-md-6">
			<label for="inputFirstName">Ime</label>
			<input type="text" class="form-control" id="inputFirstName" v-model="user.firstName">
			</div>
			<div class="form-group col-md-6">
			<label for="inputLastName">Prezime</label>
			<input type="text" class="form-control" id="inputLastName" v-model="user.lastName">
			</div>
		</div>
		<div class="form-group">
			<label for="inputUsername">Korisnicko ime</label>
			<input type="text" class="form-control" id="inputUsername" v-model="user.username">
		</div>
		<div class="form-group">
			<label for="inputPassword">Lozinka</label>
			<input type="text" class="form-control" id="inputPassword" v-model="user.password">
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
		<div class="form-row">
			<label for="inputDate">Datum rodjenja</label>
			<input type="date" class="form-control" id="inputDate" v-model="user.birthDate"/>
		</div>
		<button type="submit" class="btn btn-primary">Potvrdite izmenu</button>
		<p id="message"></p>
	</form>

 </div>
</template>

<script>

module.exports = {
	data() {
		return {
			isLoggedIn: false,
			oldUsername: "",
			user : {}
		}	
	},
	methods: {
		profileSettings: function() {
			window.location.replace("/registration");
		},
		updateProfile: function() {
			$('#message').html("");
			this.user.isDeleted = "false";
			if(areInputFieldsEmpty(this.user)) {
				$('#message').html("Morate popuniti sva polja.");
				return;
			}
			axios
				.put("rest/users", JSON.stringify(this.user), {
					headers: {'content-type':'application/json'}
				})
				.then(response => {
					if(isEmpty(response.data)) {
						$('#message').html("Korisnicko ime vec postoji.");
						return;
					}
					this.user = response.data;
					this.makeStringDate(this.user.birthDate);
					$('#message').html("Korisnik uspesno izmenjen.");
				});
		},
		makeStringDate(date) {
			if(date.monthValue <= 9) {
				date.monthValue = "0" + date.monthValue;
			}
			if(date.dayOfMonth <= 9) {
				date.dayOfMonth = "0" + date.dayOfMonth;
			}
			this.user.birthDate = date.year + "-" + date.monthValue + "-" + date.dayOfMonth;
		}
	},
	mounted() {
		axios
			.get("rest/users/me")
			.then(response => {
				if(isEmpty(response.data)) {
					// location.redirect
				}
				this.user = response.data;
				console.log(this.user);
				this.oldUsername = this.user.username;
				this.makeStringDate(this.user.birthDate);


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