const manifestations = httpVueLoader("./app/components/Manifestations.vue");
const filteringPanel = httpVueLoader("./app/components/FilteringPanel.vue");

Vue.component("welcome-page", {
	data: function() {
		return {
			
		}
	},
	template: `
		<div class="container">
			<filteringPanel></filteringPanel>
			
			
			<manifestations></manifestations>
		</div>
		
	`,
	methods: {

	},
	components: {
		"manifestations" : manifestations,
		"filteringPanel" : filteringPanel
	}
});