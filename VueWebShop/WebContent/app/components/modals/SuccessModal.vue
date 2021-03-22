<template>
    <div class="modal fade success-modal" id="successModal" tabindex="-1" role="dialog" aria-labelledby="successModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="successModalLabel">{{modal_title}}</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
                {{modal_body}}
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-success" data-dismiss="modal">OK</button>
			</div>
			</div>
		</div>
	</div>
</template>

<script>

module.exports = ({
    data() {
        return {
            modal_title: "",
            modal_body: ""
        }
    },
    mounted() {
        this.$root.$on("modal-called",(modal_title, modal_body) => {
            this.modal_title = modal_title;
            this.modal_body = modal_body;
        })
    }
})
</script>

<style scoped>

.success-modal .modal-content {
    background-color: #d4edda;
    color: #155724;
}

.success-modal .modal-header {
    border-bottom-color: #badbcc;
}

.success-modal .modal-footer {
    border-top-color: #badbcc;
}

</style>