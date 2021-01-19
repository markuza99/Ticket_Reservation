const manifestations = httpVueLoader("./app/components/Manifestations.vue");

Vue.component("welcome-page", {
	data: function() {
		return {
			
		}
	},
	template: `
			
			<manifestations></manifestations>
		
	`,
	methods: {

	},
	components: {
		"manifestations" : manifestations,
	}
});