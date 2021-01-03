<template>
	<div class="row">
		<div class="manifestation" v-bind:key="m.id" v-for="m in manifestations">
			<div class="col-lg-3 col-md-4 col-sm-6">
				
				<!--<img v-bind:src = "contextPath + 'WebContent\\images\\' + m.image">-->
				<!-- <img v-bind:src = "'..\\..\\images\\' + m.image"> -->
				<img v-bind:src="'../../images/' + m.image">
				<h2>{{m.name}}</h2>
				<span>{{m.date.dayOfMonth}}</span>
				<span>{{m.date.month}}</span>
				<span>{{m.date.year}}</span>
				<span>{{m.location.street}}</span>
				<span>{{m.location.city}}</span>
			</div>
		</div>
	</div>
</template>

<script>
module.exports = {
	data() {
		return {
			manifestations: [],
			contextPath: ""
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
		axios
			.get("rest/manifestationservice/getcontextpath")
			.then(response => {
				this.contextPath = response.data;
			});
	}
}
</script>

<style>
</style>