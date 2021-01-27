const WelcomePage = { template: '<welcome-page></welcome-page>' }
const Header = httpVueLoader('./app/components/Header.vue');
const Registration = httpVueLoader('./app/components/RegistrationForm.vue');
const loginForm = httpVueLoader('./app/components/LoginForm.vue');
const Footer = httpVueLoader("./app/components/Footer.vue");
const Manifestation = httpVueLoader("./app/components/Manifestation.vue");
const editManifestation = httpVueLoader("./app/components/EditManifestation.vue");
const Unauthorized = httpVueLoader("./app/components/Unauthorized.vue");
const Users = httpVueLoader("./app/components/Users.vue");

const router = new VueRouter({
    mode: 'hash',
    routes: [
		{ path: '/', component: WelcomePage },
		{ path: '/login', component: loginForm },
        { path: '/registration', component: Registration },
        { path: '/manifestation/:id', component: Manifestation},
        { path: '/manifestation/edit/:id', component: editManifestation},
        { path: '/unauthorized', component: Unauthorized},
        { path: '/users', component: Users}
    ]
});

var app = new Vue({
    router,
    el: '#wrapper',
    components: {
		"my-header": Header,
		"my-footer" : Footer
	}

});