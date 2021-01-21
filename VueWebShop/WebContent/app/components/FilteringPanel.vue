<template>
<div>
	<h4 class="latest-manifestations mr-5 mt-3">Najskorije manifestacije</h4>
	<hr>
	<ul class="filtering-panel d-flex justify-content-between">
				<li>
					
					<select class="form-select pl-3 pr-3 pt-2 pb-2 mr-3">
					<option v-bind:key="v.id" v-for="v in manifestation_type">
						{{v}}
					</option>
					</select>

					<select class="pl-3 pr-3 pt-2 pb-2 mr-3">
					<option>
						Najskorije
					</option>
					<option>
						Najkasnije
					</option>
					</select>

					<span class="mr-1">Nerasprodato: </span>
					<input type="checkbox" id="" name="" value="Boat">

					
				</li>
				
				<li>
					<button type="button" class="btn btn-primary" v-on:click="sendToManifestation">Filtriraj</button>
				</li>
				
				

	</ul>
	</div>
</template>

 <script>
module.exports = {
	data() {
		return {
			manifestation_type: ["TIP MANIFESTACIJE","SVE","THEATER"]
		}
	},
	methods: {
		sendToManifestation : function() {
			this.$root.$emit('messageFromFilteringToManifestations','Hello Manifestations from filtering');
		}
	}
 }
 </script>

 <style>

.latest-manifestations {
	font-family: 'Open Sans Condensed', sans-serif;
	font-family: 'Quicksand', sans-serif;
	/* margin-top: 20px; */
	font-size: 25px;
}

.filtering-panel {
	margin-top: 20px;
	list-style: none;
}

.filtering-panel select {
	border: 1px solid rgb(0, 123, 255);
	border-radius: 5px;
}

 </style>