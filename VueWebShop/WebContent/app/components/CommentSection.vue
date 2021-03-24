<template>
  <div class="comment-section pb-4 mt-5 mb-5">
    <h3 v-if="comments.length == 0" class="p-4">Nema komentara</h3>
    <div v-if="correspondsCommentPermision()">
      <div class="d-flex justify-content-between">
        <div class="font-weight-bold pt-4 pl-4 pr-4">Komentarisi</div>
        <div class="rating mt-3 mr-3">
          <span
            class="fa fa-star five-stars"
            v-on:click="clickedStar('five-stars')"
          ></span>
          <span
            class="fa fa-star four-stars"
            v-on:click="clickedStar('four-stars')"
          ></span>
          <span
            class="fa fa-star three-stars"
            v-on:click="clickedStar('three-stars')"
          ></span>
          <span
            class="fa fa-star two-stars"
            v-on:click="clickedStar('two-stars')"
          ></span>
          <span
            class="fa fa-star one-star"
            v-on:click="clickedStar('one-star')"
          ></span>
        </div>
      </div>

      <div class="p-3">
        <textarea class="form-control comment-form" rows="3"></textarea>
      </div>
      <div class="text-right pr-3">
        <button class="btn-pink manifestation-button comment-button">
          Postavi komentar
        </button>
      </div>
    </div>
    <div class="comment m-3" v-for="comment in comments" :key="comment.id">
      <ul class="comment-info d-flex justify-content-between">
        <li class="text-secondary comment-username">{{ comment.user }}</li>
        <li>
          <span
            class="fa fa-star"
            v-bind:class="{ yellow: isCounted(1, comment) }"
          ></span>
          <span
            class="fa fa-star"
            v-bind:class="{ yellow: isCounted(2, comment) }"
          ></span>
          <span
            class="fa fa-star"
            v-bind:class="{ yellow: isCounted(3, comment) }"
          ></span>
          <span
            class="fa fa-star"
            v-bind:class="{ yellow: isCounted(4, comment) }"
          ></span>
          <span
            class="fa fa-star"
            v-bind:class="{ yellow: isCounted(5, comment) }"
          ></span>
        </li>
      </ul>
      <div class="comment-description">
        <p>{{ comment.description }}</p>
      </div>
    </div>

    <!-- <div v-if="commentParams.commentSuccess">
      <div class="comment-success">
        <input
          type="text"
          class="form-control comment-holder"
          placeholder="Vas komentar je uspesno poslat! Ceka se odobrenje."
          aria-label=""
          aria-describedby="basic-addon1"
          readonly
        />
      </div>
    </div> -->
  </div>
</template>

<script>
module.exports = {
  data() {
    return {
      comments: [],
      comment_conditions: [],
      user_rating: 0,
    };
  },
  created() {
    const manifestationId = this.$route.params.id;
    axios
      .get("rest/comments/manifestation/" + manifestationId)
      .then((response) => {
        this.comments = response.data;
        console.log(this.comments);
      });

    axios
      .get(
        "rest/comments/commenting-conditions/manifestation/" + manifestationId
      )
      .then((response) => {
        this.comment_conditions = response.data;
        console.log(this.comment_conditions);
      });
  },
  mounted() {
    this.$root.$on("commenting-conditions", (conditions) => {
      this.comment_conditions = conditions;
      console.log("juhu", this.comment_conditions);
    });
  },
  methods: {
    correspondsCommentPermision() {
      console.log(this.commentParams);
      return (
        (this.comment_conditions.commentApproval == "DENIED" ||
          this.comment_conditions.commentApproval == null) &&
        this.comment_conditions.userAttended
      );
    },
    isCounted(num, comment) {
      return !(num > comment.rating);
    },
    clickedStar(whichstar) {
      this.user_rating = userRating(whichstar);
      console.log(this.user_rating);
    },
  },
};
</script>

<style scoped>
.comment-section {
  border: 1px solid pink;
}

.rating {
  /* position: absolute; */
  /* right:5em; */
  direction: rtl;
  unicode-bidi: bidi-override;
}

.rating > span:hover:before,
.rating > span:hover ~ span:before {
  color: rgb(255, 222, 3);
}

.comment-form:focus {
  outline: none !important;
  /* border: 1px solid rgb(255, 2, 102); */
  border: none;
  box-shadow: 0 0 10px rgb(255, 2, 102);
}

.comment-button {
  border-radius: 0.25rem;
}

.comment {
  border: 1px solid #e6eaed;
  margin-top: 1em;
  border-radius: 10px;
  /* font-family: 'Quicksand', sans-serif; */
}

.comment-info {
  margin: 0;
  padding: 0;
  background-color: #e6eaed;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  /* border-bottom: 1px solid #bebebe; */
  padding: 1em;
}

.comment-date {
  color: #747474;
}

.comment-description {
  padding: 1em;
}

.comment-username {
  margin-right: 1em;
  font-family: "Quicksand", sans-serif;
}

.comment-info li {
  /* display: inline; */
  list-style: none;
}
</style>