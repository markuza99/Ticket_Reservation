const manifestations = httpVueLoader("./app/components/Manifestations.vue");
const filteringPanel = httpVueLoader("./app/components/FilteringPanel.vue");
const searchPanel = httpVueLoader("./app/components/SearchPanel.vue");

Vue.component("welcome-page", {
	data: function() {
		return {
			
		}
	},
	template: `
		<div>
			<search-panel></search-panel>
			<div class="container">
				<filtering-panel></filtering-panel>
			</div>
		    <manifestations></manifestations>
		</div>
	`,
	methods: {

	},
	components: {
		"manifestations" : manifestations,
		"filtering-panel":filteringPanel,
		"search-panel":searchPanel
	}
});