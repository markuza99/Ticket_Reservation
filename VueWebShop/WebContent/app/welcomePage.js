const manifestations = httpVueLoader("./app/components/Manifestations.vue");
const manifestationPage = httpVueLoader("./app/components/Manifestation.vue");

Vue.component("welcome-page", {
	data: function() {
		return {
			
		}
	},
	template: `
	    <div>
		    <manifestations></manifestations>
			<manifestationPage></manifestationPage>
		</div>
	`,
	methods: {

	},
	components: {
		"manifestations" : manifestations,
		"manifestationPage" : manifestationPage
	}
});