<template>
  <div v-if="manifestation" id="manifestation">
    <div class="container">
      <div class="row">
        <div class="col-lg-9 col-md-9">
          <div class="image-holder manifestation-image-holder">
            <img
              class="fit-img"
              v-bind:class="{ gray: manifestation_sold }"
              v-bind:src="'images/' + manifestation.image"
            />
          </div>
          <div>
            <div class="row">
              <div class="col mt-3">
                <div class="d-flex">
                  <div class="average_rating" v-if="manifestation_passed">
                    <span
                      class="fa fa-star"
                      v-bind:class="{ yellow: isCountedInAverageRating(1) }"
                    ></span>
                    <span
                      class="fa fa-star"
                      v-bind:class="{ yellow: isCountedInAverageRating(2) }"
                    ></span>
                    <span
                      class="fa fa-star"
                      v-bind:class="{ yellow: isCountedInAverageRating(3) }"
                    ></span>
                    <span
                      class="fa fa-star"
                      v-bind:class="{ yellow: isCountedInAverageRating(4) }"
                    ></span>
                    <span
                      class="fa fa-star"
                      v-bind:class="{ yellow: isCountedInAverageRating(5) }"
                    ></span>
                  </div>
                  <button class="btn-pink manifestation-button">
                    Rezervacija karata
                  </button>
                </div>
              </div>
              <div class="col text-right">
                <h3 class="text-uppercase p-3">{{ manifestation.name }}</h3>
              </div>
            </div>
            <comment-section></comment-section>
          </div>
        </div>
        <div class="col-lg-3 col-md-3">
          <div class="manifestation-details">
            <ul class="shadow">
              <li class="font-weight-bold text-muted">
                Detalji o manifestaciji
              </li>
              <li class="d-flex">
                <i class="fa fa-calendar pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Pocetak</div>
                  <small class="text-uppercase fw-light text-muted">{{
                    manifestation.startTimeString
                  }}</small>
                </div>
              </li>
              <li class="d-flex">
                <i class="fa fa-calendar pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Kraj</div>
                  <small class="text-uppercase fw-light text-muted">{{
                    manifestation.endTimeString
                  }}</small>
                </div>
              </li>
              <li class="d-flex">
                <i class="fa fa-location-arrow pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Lokacija</div>
                  <small class="text-uppercase fw-light text-muted">
                    {{ manifestation.location.street }}
                    {{ manifestation.location.number }},
                    {{ manifestation.location.city }},
                    {{ manifestation.location.state }}
                  </small>
                </div>
              </li>
              <li class="d-flex">
                <i
                  class="fa fa-check pt-3 pb-3 pr-3 pink"
                  v-if="manifestation.status == 'ACTIVE'"
                ></i>
                <i
                  class="fa fa-times pt-3 pb-3 pr-3 pink"
                  v-if="manifestation.status == 'INACTIVE'"
                ></i>
                <div>
                  <div>Status</div>
                  <small class="text-uppercase fw-light text-muted">{{
                    manifestation.status
                  }}</small>
                </div>
              </li>
              <li class="d-flex">
                <i
                  class="fa fa-music pt-3 pb-3 pr-3 pink"
                  v-if="manifestation.type == 'CONCERT'"
                ></i>
                <i
                  class="material-icons pt-3 pb-3 pr-3 pink"
                  v-if="manifestation.type == 'THEATER'"
                  >movie_filter</i
                >
                <i
                  class="material-icons pt-3 pb-3 pr-3 pink"
                  v-if="manifestation.type == 'FESTIVAL'"
                  >wb_sunny</i
                >
                <div>
                  <div>Tip</div>
                  <small class="text-uppercase fw-light text-muted">{{
                    manifestation.formattedType
                  }}</small>
                </div>
              </li>
              <li class="d-flex">
                <i class="fa fa-ticket pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Preostali broj karata</div>
                  <small class="text-uppercase fw-light text-muted">{{
                    manifestation.remainingNumberOfSeats
                  }}</small>
                </div>
              </li>
              <li class="d-flex">
                <i class="fa fa-money pt-3 pb-3 pr-3 pink"></i>
                <div>
                  <div>Cena</div>
                  <small
                    class="text-uppercase fw-light text-muted"
                    v-if="!manifestation_sold"
                    >{{ manifestation.ticketPrice }},00 RSD</small
                  >
                  <small
                    class="text-uppercase fw-light text-muted"
                    v-if="manifestation_sold"
                    >rasprodato</small
                  >
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
module.exports = {
  data() {
    return {
      manifestation: null,
      manifestation_passed: false,
      manifestation_sold: false,
      comment_conditions: [],
    };
  },
  components: {
    "comment-section": httpVueLoader("./CommentSection.vue"),
  },
  created() {
    const manifestationId = this.$route.params.id;
    axios.get("rest/manifestations/" + manifestationId).then((response) => {
      this.manifestation = response.data;
      this.manifestation.startTimeMonth = formatMonth(
        this.manifestation.startTime.month
      );
      this.manifestation.endTimeMonth = formatMonth(
        this.manifestation.endTime.month
      );
      formatType(this.manifestation);
      this.validateAndFormatDate();
    });

    axios
      .get(
        "rest/comments/commenting-conditions/manifestation/" + manifestationId
      )
      .then((response) => {
        this.comment_conditions = response.data;
        this.$root.$emit("commenting-conditions", response.data);
      });
  },
  methods: {
    isCountedInAverageRating(num) {
      return !(num > this.comment_conditions.manifestationRating);
    },
    validateAndFormatDate() {
      let date = new Date();
      let manifestationDate = new Date(
        this.manifestation.endTime.year,
        this.manifestation.endTime.monthValue,
        this.manifestation.endTime.dayOfMonth,
        this.manifestation.endTime.hour,
        this.manifestation.endTime.minute,
        0
      );
      if (!validateRange(date, manifestationDate)) {
        this.manifestation_passed = true;
      }

      this.manifestation_sold =
        this.manifestation.remainingNumberOfSeats > 0 ? false : true;

      this.manifestation.startTimeString =
        this.manifestation.startTime.dayOfMonth +
        " " +
        this.manifestation.startTimeMonth +
        ", " +
        this.manifestation.startTime.year +
        " " +
        ("0" + this.manifestation.startTime.hour).slice(-2) +
        ":" +
        ("0" + this.manifestation.startTime.minute).slice(-2);

      this.manifestation.endTimeString =
        this.manifestation.endTime.dayOfMonth +
        " " +
        this.manifestation.endTimeMonth +
        ", " +
        this.manifestation.endTime.year +
        " " +
        ("0" + this.manifestation.endTime.hour).slice(-2) +
        ":" +
        ("0" + this.manifestation.endTime.minute).slice(-2);
    },
  },
};
</script>

<style scoped>
.manifestation-image-holder {
  height: 30em;
  overflow: hidden;
  border-radius: 10px;
}

.manifestation-details ul {
  list-style-type: none;
  /* border-color: #0d6efd; */
  /* border: 1px solid #0d6efd; */
  padding: 0;
  border-radius: 10px;
}

.yellow {
  color: rgb(255, 222, 3);
}

.yellow-bg {
  background-color: rgb(255, 222, 3);
}

.blue {
  color: rgb(3, 54, 255);
}

.pink {
  color: rgb(255, 2, 102);
}

.manifestation-details li {
  padding: 15px 30px;
  border-top: 1px solid #d7d7d7;
}

.manifestation-details li:first-child {
  border-top: none;
}

#manifestation {
  margin-top: 5em;
}

#manifestation h3 {
  font-weight: 200;
}

#manifestation .average_rating {
  background-color: rgb(255, 2, 102);
  border-top-left-radius: 0.25rem;
  border-bottom-left-radius: 0.25rem;
  padding: 5px 10px 0 10px;
}

#manifestation .manifestation-button {
  border-top-right-radius: 0.25rem;
  border-bottom-right-radius: 0.25rem;

  display: inline-block;
  font-weight: 400;
  line-height: 1.5;
  text-align: center;
  text-decoration: none;
  vertical-align: middle;
  cursor: pointer;
  -webkit-user-select: none;
  -moz-user-select: none;
  user-select: none;
  border: 1px solid transparent;
  border-left: 1px solid rgb(255, 222, 3);
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
}

.btn-pink {
  color: #fff;
  background-color: rgb(255, 2, 102);
  border-color: rgb(255, 2, 102);
}
</style>