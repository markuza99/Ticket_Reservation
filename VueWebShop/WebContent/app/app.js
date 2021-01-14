
const WelcomePage = { template: '<welcome-page></welcome-page>'}
const Header = httpVueLoader('./app/components/Header.vue');
const loginForm = httpVueLoader('./app/components/LoginForm.vue');
const searchPanel = httpVueLoader("./app/components/SearchPanel.vue");

const router = new VueRouter({
	  mode: 'hash',
	  routes: [
		{ path: '/', component: WelcomePage},
		{ path: '/login', component: loginForm}
	  ]
});

var app = new Vue({
	router,
	el: '#wrapper',
	components: {
		"my-header" : Header,
		"search-panel": searchPanel
	}
});