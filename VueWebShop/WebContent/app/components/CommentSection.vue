<template>
  <div class="comment-section">
    <h3 v-if="comments.length == 0" class="p-4">Nema komentara</h3>
    <div v-if="correspondsCommentPermission()" class="mb-5">
      <div class="d-flex justify-content-between">
        <div class="font-weight-bold pt-4">Komentarisi</div>
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

      <div class="mt-3 mb-3">
        <textarea
          class="form-control comment-form"
          rows="3"
          v-model="description"
        ></textarea>
      </div>
      <div class="d-flex justify-content-between">
        <p id="comment-error" class="ml-4"></p>
        <button class="btn-pink manifestation-button comment-button" v-on:click="comment()">Postavi komentar</button>
      </div>
    </div>
    <ul class="list-group mb-5">
      <li class="list-group-item" v-for="comment in comments" :key="comment.id">
          <div class="d-flex justify-content-between align-items-center mb-2">
              <span class="text-secondary">{{comment.user}}</span>
          </div>
          <div class="d-flex justify-content-between align-items-center">
            {{comment.description}}
            <div class="mb-2">
                <span class="fa fa-star" v-bind:class="{ checked : isCounted(1, comment) }"></span>
                <span class="fa fa-star" v-bind:class="{ checked : isCounted(2, comment) }"></span>
                <span class="fa fa-star" v-bind:class="{ checked : isCounted(3, comment) }"></span>
                <span class="fa fa-star" v-bind:class="{ checked : isCounted(4, comment) }"></span>
                <span class="fa fa-star" v-bind:class="{ checked : isCounted(5, comment) }"></span>
            </div>
          </div>
      </li>
    </ul>
    

    <div v-if="commenting_conditions.commentApproval == 'NOT_CHECKED'">
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
    </div>
  </div>
</template>

<script>
module.exports = {
  data() {
    return {
      comments: [],
      user_rating: 0,
      description: "",
      manifestation_id: "",
      comment_error: false
    };
  },
  props: ["commenting_conditions","manifestation_passed"],
  created() {
    this.manifestation_id = this.$route.params.id;
    axios
      .get("rest/comments/manifestation/" + this.manifestation_id)
      .then((response) => {
        this.comments = response.data;
      });
  },
  mounted() {
    console.log(this.commenting_conditions);
  },
  methods: {
    correspondsCommentPermission() {
      return (
        this.commenting_conditions.userAttended &&
        this.manifestation_passed &&
        this.commenting_conditions.commentApproval != "ACCEPTED" && 
        this.commenting_conditions.commentApproval != "NOT_CHECKED"
      );
    },
    isCounted(num, comment) {
      return !(num > comment.rating);
    },
    clickedStar(whichstar) {
      this.user_rating = userRating(whichstar);
      console.log(this.user_rating);
    },
    comment() {
      console.log(this.user_rating);
      if (this.user_rating == 0 || this.description.trim() == "") {
        $('#comment-error').html('Morate oceniti manifestaciju da biste postavili komentar!');
        return;
      }

      this.description.trim()
      commentDTO = {
        manifestation: this.manifestation_id,
        description: this.description,
        rating: this.user_rating,
      };

      console.log(commentDTO);
      axios
        .post("rest/comments", JSON.stringify(commentDTO), {
          headers: { "content-type": "application/json" },
        })
        .then((response) => {
          $('#comment-error').html('');
          this.commenting_conditions.commentApproval = "NOT_CHECKED";
        });
    },
  },
};
</script>

<style scoped>
.comment-section {
  /* border: 1px solid pink; */
}

.rating {
  /* position: absolute; */
  /* right:5em; */
  direction: rtl;
  unicode-bidi: bidi-override;
}

.rating > span:hover:before,
.rating > span:hover ~ span:before {
  color: #FFD3B4;
}


.comment-button {
  border-radius: 0.25rem;
}

.comment {
  margin-top: 1em;
  border-radius: 10px;
}

.comment-info {
  margin: 0;
  padding: 0;
  background-color: #ffe5f0;
  padding: 1em;
  border-bottom: 1px solid #FFAAA7;
}

.comment-date {
  color: #747474;
}

.comment-description {
  padding: 1em;
}

.comment-username {
  margin-right: 1em;
  /* border-bottom: 2px solid rgb(255, 2, 102); */
}

.comment-info li {
  list-style: none;
}


::-webkit-scrollbar {
  width: 1px;
  background-color: #98DDCA;
}
</style>