<template>
    <div class="modal fade" id="areYouSureModal" tabindex="-1" role="dialog" aria-labelledby="areYouSureModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="areYouSureModalLabel">Potvrda rezervacije</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                Da li ste sigurni?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazite</button>
                <button type="button" class="btn btn-primary" v-on:click="reserve" data-dismiss="modal">Potvrdite rezervaciju</button>
            </div>
            </div>
        </div>
    </div>
</template>