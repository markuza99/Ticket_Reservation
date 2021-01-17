const WelcomePage = { template: '<welcome-page></welcome-page>' }
const Header = httpVueLoader('./app/components/Header.vue');
const RegistrationPage = { template: '<registration-page></registration-page>' }

const router = new VueRouter({
    mode: 'hash',
    routes: [
        { path: '/', component: WelcomePage },
        { path: '/registration', component: RegistrationPage }
    ]
});

var app = new Vue({
    router,
    el: '#wrapper',
    components: {
        "my-header": Header
    }
});