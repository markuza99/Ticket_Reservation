const registrationForm = httpVueLoader("./app/components/RegistrationForm.vue");

Vue.component("registration-page", {
	data: function() {
		return {
			
		}
	},
	template: `
		<div>
			<registration-form></registration-form>
			test
		</div>
		
	`,
	methods: {

	},
	components: {
		"registration-form" : registrationForm
	}
});