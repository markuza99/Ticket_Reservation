const manifestations = httpVueLoader("./app/components/Manifestations.vue");
const searchPanel = httpVueLoader("./app/components/SearchPanel.vue");

Vue.component("welcome-page", {
	data: function() {
		return {
			
		}
	},
	template: `
		<div>
			<search-panel></search-panel>
			
		    <manifestations></manifestations>
		</div>
	`,
	methods: {
		// TO DO -- obrisati ovaj fajl, napraviti Vue komponentu
	},
	components: {
		"manifestations" : manifestations,
		"search-panel":searchPanel
	}
});