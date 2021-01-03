
const WelcomePage = { template: '<welcome-page></welcome-page>'}
const Header = httpVueLoader('./app/components/Header.vue');

const router = new VueRouter({
	  mode: 'hash',
	  routes: [
		{ path: '/', component: WelcomePage}
	  ]
});

var app = new Vue({
	router,
	el: '#wrapper',
	components: {
		"my-header" : Header
	}
});