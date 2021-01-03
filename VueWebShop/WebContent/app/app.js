
const WelcomePage = { template: '<welcome-page></welcome-page>'}
const Header = httpVueLoader('./app/components/Header.vue');
const registrationForm = httpVueLoader('./app/components/RegistrationForm.vue');
const router = new VueRouter({
	  mode: 'hash',
	  routes: [
		{ path: '/', component: WelcomePage},
		{ path: '/registration', component: registrationForm}
	  ]
});

var app = new Vue({
	router,
	el: '#wrapper',
	components: {
		"my-header" : Header
	}
});