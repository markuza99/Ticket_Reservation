<template>
    <div class="container-fluid" id="comments-panel">
        <!-- <div class="wrapper-scroll"> -->
            <div class="comment" v-for="comment in comments" :key="comment.id">
                <div class="row">
                    <div class="col-lg-10 col-md-10">
                        <ul class="comment-info d-flex justify-content-between" v-bind:class="{denied: comment.approval == 'DENIED',
                                                                        accepted: comment.approval == 'ACCEPTED',
                                                                        not_checked: comment.approval == 'NOT_CHECKED' }">
                            <li class="text-secondary comment-username">{{comment.user}}</li>
                            <li>Manifestacija: {{comment.manifestation}}</li>
                            <li>
                                <span class="fa fa-star" v-bind:class="{ checked : isCounted(1, comment) }"></span>
                                <span class="fa fa-star" v-bind:class="{ checked : isCounted(2, comment) }"></span>
                                <span class="fa fa-star" v-bind:class="{ checked : isCounted(3, comment) }"></span>
                                <span class="fa fa-star" v-bind:class="{ checked : isCounted(4, comment) }"></span>
                                <span class="fa fa-star" v-bind:class="{ checked : isCounted(5, comment) }"></span>
                            </li>
                        </ul>
                        <div class="comment-description">
                            <p>{{comment.description}}</p>
                        </div>
                    </div>
                    <div class="col-lg-2 col-md-2">
                        <div class="mt-5" v-if="comment.approval =='NOT_CHECKED' && user.role == 'SELLER'">
                            <button class="btn btn-success" v-on:click="approve(comment)">
                                Odobri:   <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-danger" v-on:click="decline(comment)">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>
                        <div class="mt-5">
                            <div class="btn btn-outline-danger" v-if="comment.approval == 'DENIED'" disabled>NEODOBREN</div>
                            <div class="btn btn-outline-success" v-if="comment.approval == 'ACCEPTED'" disabled>ODOBREN</div>
                        </div>
                    </div>
				</div>



                
            </div>
        <!-- </div> -->
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            comments:[],
            user:{}
        }
    },
    mounted() {

        axios
            .get("rest/commentservice/get-all-comments")
            .then(response => {
                this.comments = response.data;
                console.log(this.comments);
            });

        axios
            .get("rest/userservice/test-login")
            .then(response => {
                this.user = response.data;
            });
    },
    methods: {
        isCounted(num, comment) {
			return !(num > comment.rating);
        },
        approve(comment) {
            //mogu samo poslati ceo komentar
            console.log(comment);
            axios
                .put("rest/commentservice/approve-comment",JSON.stringify(comment), {
                    headers: {"content-type":"application/json"}
                })
                .then(response => {
                    this.comments = response.data;
                    alert("Komentar odobren!");
                });

        },
        decline(comment) {
            axios
                .put("rest/commentservice/decline-comment",JSON.stringify(comment), {
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
.wrapper-scroll {
    height: 30em !important;
    overflow: scroll;
}

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