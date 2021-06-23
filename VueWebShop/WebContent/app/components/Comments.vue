<template>
    <div class="container-fluid" id="comments-panel">
        <ul class="list-group m-5">
            <li class="list-group-item" v-for="comment in comments" :key="comment.id">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    Manifestacija: {{comment.manifestation}}
                    <span class="text-secondary">{{comment.user}}</span>
                </div>
                <div class="d-flex justify-content-between align-items-center">
                    {{comment.description}}
                    <div v-if="comment.approval =='NOT_CHECKED' && role == 'SELLER'">
                    <div class="mb-2">
                        <span class="fa fa-star" v-bind:class="{ checked : isCounted(1, comment) }"></span>
                        <span class="fa fa-star" v-bind:class="{ checked : isCounted(2, comment) }"></span>
                        <span class="fa fa-star" v-bind:class="{ checked : isCounted(3, comment) }"></span>
                        <span class="fa fa-star" v-bind:class="{ checked : isCounted(4, comment) }"></span>
                        <span class="fa fa-star" v-bind:class="{ checked : isCounted(5, comment) }"></span>
                    </div>
                    <button class="btn btn-success" v-on:click="approve(comment)">
                        Odobri:   <i class="fa fa-check"></i>
                    </button>
                    <button class="btn btn-danger" v-on:click="decline(comment)">
                        <i class="fa fa-times"></i>
                    </button>
                    </div>
                    <div class="btn btn-outline-danger" v-if="comment.approval == 'DENIED'" disabled>NEODOBREN</div>
                    <div class="btn btn-outline-success" v-if="comment.approval == 'ACCEPTED'" disabled>ODOBREN</div>
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
        axios
            .get("rest/comments")
            .then(response => {
                this.comments = response.data;
            });

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
        approve(comment) {
            axios
                .put("rest/comments/approve",JSON.stringify(comment), {
                    headers: {"content-type":"application/json"}
                })
                .then(response => {
                    this.comments = response.data;
                    alert("Komentar odobren!");
                });

        },
        decline(comment) {
            axios
                .put("rest/comments/decline",JSON.stringify(comment), {
                    headers: {"content-type":"application/json"}
                })
                .then(response => {
                    this.comments = response.data;
                    alert("Komentar neodobren.");
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