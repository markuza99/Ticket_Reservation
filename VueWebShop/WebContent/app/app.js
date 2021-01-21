const WelcomePage = { template: '<welcome-page></welcome-page>' }
const Header = httpVueLoader('./app/components/Header.vue');
const Registration = httpVueLoader('./app/components/RegistrationForm.vue');
const loginForm = httpVueLoader('./app/components/LoginForm.vue');
const Footer = httpVueLoader("./app/components/Footer.vue");
const filteringPanel = httpVueLoader("./app/components/FilteringPanel.vue");
const searchPanel = httpVueLoader("./app/components/SearchPanel.vue");


const router = new VueRouter({
    mode: 'hash',
    routes: [
		{ path: '/', component: WelcomePage },
		{ path: '/login', component: loginForm },
        { path: '/registration', component: Registration }
    ]
});

var app = new Vue({
    router,
    el: '#wrapper',
    components: {
		"my-header": Header,
		"my-footer" : Footer,
		"filtering-panel" : filteringPanel,
		"search-panel" : searchPanel
	}

});