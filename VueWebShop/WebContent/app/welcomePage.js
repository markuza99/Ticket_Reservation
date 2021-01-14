const manifestations = httpVueLoader("./app/components/Manifestations.vue");

Vue.component("welcome-page", {
	data: function() {
		return {
			
		}
	},
	template: `
		<div class="container">
			<h4 class="najskorije-manifestacije">Najskorije manifestacije</h4>
			<hr>
			<manifestations></manifestations>
		</div>
		
	`,
	methods: {

	},
	components: {
		"manifestations" : manifestations
	}
});