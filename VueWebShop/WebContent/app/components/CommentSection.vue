<template>
    <div class="comment-section">
        <h3 v-if="comments.length > 0">Komentari</h3>
        <h3 v-if="comments.length == 0">Nema komentara</h3>
        <div class="comment" v-for="comment in comments" :key="comment.id">
            <ul class="comment-info bg-light d-flex justify-content-between">
                <li class="text-secondary comment-username">{{comment.user}}</li>
                <!-- <li class="comment-date">20 Oktobar 2020</li> -->
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
        <div class="logged-user-comment" v-if="correspondsCommentPermision()">
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <button class="btn btn-outline-secondary" type="button" v-on:click="comment">Komentarisi</button>
                </div>
                <input type="text" class="form-control comment-holder" placeholder="" aria-label="" aria-describedby="basic-addon1">
                <div class="rating">
                <span class="fa fa-star five-stars" v-on:click="clickedStar('five-stars')"></span>
                <span class="fa fa-star four-stars" v-on:click="clickedStar('four-stars')"></span>
                <span class="fa fa-star three-stars" v-on:click="clickedStar('three-stars')"></span>
                <span class="fa fa-star two-stars" v-on:click="clickedStar('two-stars')"></span>
                <span class="fa fa-star one-star" v-on:click="clickedStar('one-star')"></span>
                </div>
            </div>
            
        </div>

        <div v-if="commentParams.commentSuccess">
            <div class="comment-success">
                <input type="text" class="form-control comment-holder " placeholder="Vas komentar je uspesno poslat! Ceka se odobrenje."
                aria-label="" aria-describedby="basic-addon1" readonly>
            </div>
            
        </div>
            
    </div>
</template>