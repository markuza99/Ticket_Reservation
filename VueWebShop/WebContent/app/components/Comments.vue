<template>
    <div class="container-fluid" id="comments-panel">
        <ul class="list-group m-5">
            <li class="list-group-item" v-for="comment in comments" :key="comment.id">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    Manifestacija: {{comment.manifestation}}
                    <span class="text-secondary">{{comment.user}}
                        <div class="mb-2">
                            <span class="fa fa-star" v-bind:class="{ checked : isCounted(1, comment) }"></span>
                            <span class="fa fa-star" v-bind:class="{ checked : isCounted(2, comment) }"></span>
                            <span class="fa fa-star" v-bind:class="{ checked : isCounted(3, comment) }"></span>
                            <span class="fa fa-star" v-bind:class="{ checked : isCounted(4, comment) }"></span>
                            <span class="fa fa-star" v-bind:class="{ checked : isCounted(5, comment) }"></span>
                        </div>
                    </span>
                </div>
                <div class="d-flex justify-content-between align-items-center">
                    {{comment.description}}
                    <div v-if="comment.approval =='NOT_CHECKED' && role == 'SELLER'">
                        <button class="btn btn-green" v-on:click="approve(comment.id)">
                            Odobri:   <i class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-pink-invert" v-on:click="decline(comment.id)">
                            <i class="fa fa-times"></i>
                        </button>
                    </div>
                    <div v-else>
                        <div class="text-pink" v-if="comment.approval == 'DENIED'" disabled>NEODOBREN</div>
                        <div class="text-green" v-if="comment.approval == 'ACCEPTED'" disabled>ODOBREN</div>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            comments:[],
            role:null
        }
    },
    mounted() {
        this.getComments()
        axios
            .get("rest/users/role")
            .then(response => {
                this.role = response.data;
            });
    },
    methods: {
        isCounted(num, comment) {
			return !(num > comment.rating);
        },
        approve(id) {
            axios
                .put("rest/comments/approve/" + id)
                .then(() => {
                    new Toast({
                        message: 'Komentar je uspesno odobren!',
                        type: 'success'
                    });
                    this.getComments()
                });

        },
        decline(id) {
            axios
                .put("rest/comments/decline/" + id)
                .then(() => {
                    new Toast({
                        message: 'Komentar je uspesno odbijen',
                        type: 'success'
                    });
                    this.getComments()
                });
        },
        getComments () {
            axios
            .get("rest/comments")
            .then(response => {
                this.comments = response.data;
            });
        }
    }
}
</script>

<style scoped>
.denied {
    background-color: #f5c6cb;
}

.accepted {
    background-color: #c3e6cb;
}

.not_checked {
    background-color: #bee5eb;
}
</style>