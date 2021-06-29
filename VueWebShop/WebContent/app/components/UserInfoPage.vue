<template>
 <div class="user-page container mt-3">
	 <div class="col-4 mt-5">
		<div class="form-row">
			<div class="form-group col-md-6">
			<label>Ime</label>
			<input type="text" class="form-control" id="firstNameInput" v-model="user.firstName" readonly>
			</div>
			<div class="form-group col-md-6">
			<label>Prezime</label>
			<input type="text" class="form-control" id="lastNameInput" v-model="user.lastName" readonly>
			</div>
		</div>
		<div class="form-group">
			<label >Korisnicko ime</label>
			<input type="text" class="form-control" id="usernameInput" v-model="user.username" readonly>
		</div>
		<div class="form-group">
			<label>Lozinka</label>
			<input type="text" class="form-control" id="password" v-model="user.password" readonly>
		</div>
		<div class="form-group">
			<div class="form-check form-check-inline">
			<input class="form-check-input" type="radio" id="genderMale" v-model="user.gender" value="MALE" disabled>
			<label class="form-check-label">Musko</label>
			</div>
			<div class="form-check form-check-inline">
			<input class="form-check-input" type="radio" id="genderFemale" v-model="user.gender" value="FEMALE" disabled>
			<label class="form-check-label">Zensko</label>
		</div> 
		<div class="form-row">
			<label>Datum rodjenja</label>
			<input type="date" class="form-control" id="dateInput" v-model="user.date" readonly>
		</div>
		<div class="form-row justify-content-between mt-4">
			<button type="submit" class="btn btn-primary" v-on:click="updateProfile()">Potvrdite izmenu</button>
			<button class="btn btn-primary" v-on:click="enableInputs()">Izmenite profil</button>
		</div>
		<p id="message" class="mt-3"></p>
	 </div>
 </div>
</template>

<script>

module.exports = {
	data() {
		return {
			user : null
		}	
	},
	methods: {
		updateProfile() {
			$('#message').html("");
			if(areInputFieldsEmpty(this.user)) {
				$('#message').html("Morate popuniti sva polja.");
				return;
			}
			axios
				.put("rest/users/me", JSON.stringify(this.user), {
					headers: {'content-type':'application/json'}
				})
				.then(() => {
					this.disableInputs()
					$('#message').html("Korisnik uspesno izmenjen.");
				});
		},
		enableInputs () {
			document.getElementById('firstNameInput').readOnly = false;
			document.getElementById('lastNameInput').readOnly = false;
			document.getElementById('usernameInput').readOnly = false;
			document.getElementById('password').readOnly = false;
			document.getElementById('dateInput').readOnly = false;
			document.getElementById('genderMale').disabled = false;
			document.getElementById('genderFemale').disabled = false;
		},
		disableInputs () {
			document.getElementById('firstNameInput').readOnly = true;
			document.getElementById('lastNameInput').readOnly = true;
			document.getElementById('usernameInput').readOnly = true;
			document.getElementById('password').readOnly = true;
			document.getElementById('dateInput').readOnly = true;
			document.getElementById('genderMale').disabled = true;
			document.getElementById('genderFemale').disabled = true;
		}
	},
	mounted() {
		axios
			.get("rest/users/me")
			.then(response => {
				if(response.data == "") {
					this.$router.push('unauthorized')
				}
				this.user = response.data
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