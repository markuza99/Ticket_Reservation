
const WelcomePage = { template: '<welcome-page></welcome-page>'}
const Header = httpVueLoader('./app/components/Header.vue');
<<<<<<< Updated upstream
const registrationForm = httpVueLoader('./app/components/RegistrationForm.vue');
=======
const RegistrationPage = { template: '<registration-page></registration-page>'}

>>>>>>> Stashed changes
const router = new VueRouter({
	  mode: 'hash',
	  routes: [
		{ path: '/', component: WelcomePage},
<<<<<<< Updated upstream
		{ path: '/registration', component: registrationForm}
=======
		{ path: '/registration', component: RegistrationPage}
>>>>>>> Stashed changes
	  ]
});

var app = new Vue({
	router,
	el: '#wrapper',
	components: {
		"my-header" : Header
	}
});