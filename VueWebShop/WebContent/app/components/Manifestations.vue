<template>
	<div class="row">
		<div class="col-lg-3 col-md-4 col-sm-6">

		</div>
	</div>
</template>

<script>
module.exports = {
	data() {
		return {
			manifestations: []
		}
	},
	methods: {

	},
	mounted() {
		axios
			.get("rest/manifestationservice/getall")
			.then(response => {
				this.manifestations = response.data;
			});
	}
}
</script>

<style>
</style>