const manifestations = httpVueLoader("./app/components/Manifestations.vue");


Vue.component("welcome-page", {
	data: function() {
		return {
			
		}
	},
	template: `
	    <div>
		    <manifestations></manifestations>

		</div>
	`,
	methods: {

	},
	components: {
		"manifestations" : manifestations,
	}
});