const WelcomePage = { template: '<welcome-page></welcome-page>' }
const Header = httpVueLoader('./app/components/Header.vue');
const RegistrationPage = { template: '<registration-page></registration-page>' }
const Registration = httpVueLoader('./app/components/RegistrationForm.vue');

const router = new VueRouter({
    mode: 'hash',
    routes: [
        { path: '/', component: WelcomePage },
        { path: '/registration', component: Registration }
    ]
});

var app = new Vue({
    router,
    el: '#wrapper',
    components: {
        "my-header": Header
    }
});