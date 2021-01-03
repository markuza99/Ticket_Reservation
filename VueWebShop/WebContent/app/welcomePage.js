const loginForm = httpVueLoader("./app/components/LoginForm.vue");

Vue.component("welcome-page", {
	data: function() {
		return {
			
		}
	},
	template: `
		<div>
			<login-form></login-form>
			test
		</div>
		
	`,
	methods: {

	},
	components: {
		"login-form" : loginForm
	}
});