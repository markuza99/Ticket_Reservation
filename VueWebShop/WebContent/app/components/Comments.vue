<template>
    <div class="container-fluid" id="comments-panel">
        <!-- <div class="wrapper-scroll"> -->
            <div class="comment" v-for="comment in comments" :key="comment.id">
                <div class="row">
                    <div class="col-lg-10 col-md-10">
                        <ul class="comment-info bg-light d-flex justify-content-between">
                            <li class="text-secondary comment-username">{{comment.user}}</li>
                            <li>{{comment.manifestation}}</li>
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
                        <button class="btn btn-success mt-5" v-on:click="approve(comment)">
                            Odobri:   <i class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-danger mt-5" v-on:click="dismiss(comment)">
                            <i class="fa fa-times"></i>
                        </button>
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
            comments:[]
        }
    },
    mounted() {

        axios
            .get("rest/commentservice/get-all-comments-for-seller")
            .then(response => {
                this.comments = response.data;
            })
    },
    methods: {
        isCounted(num, comment) {
			return !(num > comment.rating);
        },
        approve(comment) {
            commentDTO = {commentUser: comment.user, commentManifestation: comment.manifestation};
            axios
                .put("rest/commentservice/approve-comment",JSON.stringify(commentDTO), {
                    headers: {"content-type":"application/json"}
                })
                .then(response => {
                    this.comments = response.data;
                    alert("Komentar odobren!");
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
</style>