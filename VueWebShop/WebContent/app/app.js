const WelcomePage = httpVueLoader('./app/components/Manifestations.vue');
const Header = httpVueLoader('./app/components/Header.vue');
const Registration = httpVueLoader('./app/components/RegistrationForm.vue');
const loginForm = httpVueLoader('./app/components/LoginForm.vue');
const Manifestation = httpVueLoader("./app/components/Manifestation.vue");
const UserProfile = httpVueLoader("./app/components/UserInfoPage.vue");
const editManifestation = httpVueLoader("./app/components/EditManifestation.vue");
const Unauthorized = httpVueLoader("./app/components/Unauthorized.vue");
const Users = httpVueLoader("./app/components/Users.vue");
const Comments = httpVueLoader("./app/components/Comments.vue");
const Manifestations = httpVueLoader('./app/components/UserManifestations.vue')
const Tickets = httpVueLoader('./app/components/Tickets.vue')
const Maps = httpVueLoader('./app/components/Maps.vue')

const router = new VueRouter({
    mode: 'hash',
    routes: [
		{ path: '/', component: WelcomePage },
		{ path: '/login', component: loginForm },
        { path: '/registration', component: Registration },
        { path: '/manifestation/:id', component: Manifestation},
        { path: '/profile', component: UserProfile},
        { path: '/manifestation/edit/:id', component: editManifestation},
        { path: '/unauthorized', component: Unauthorized},
        { path: '/users', component: Users},
        { path: '/comments', component: Comments},
        { path: '/manifestations', component: Manifestations},
        { path: '/create-manifestation', component: Maps},
        { path: '/edit-manifestation/:id', component: Maps},
        { path: '/tickets', component: Tickets}
    ]
});

var app = new Vue({
    router,
    el: '#wrapper',
    components: {
		"my-header": Header
	}

});