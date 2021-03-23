<template>
    <div class="modal fade" id="reservationModal" tabindex="-1" role="dialog" aria-labelledby="reservationModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="reservationModalLabel">Rezervacija karte</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label class="form-label">Broj karata</label> 
                    <input type="number" min="1" max="5" v-model="number_of_tickets" class="form-control" id="username"/>
                    <small id="remaining_number_error" v-if="remaining_number_error" class="form-text">Broj preostalih karata je {{manifestation.remainingNumberOfSeats}}</small>
                </div>
                <div class="form-group">
                    <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" v-model="ticket_type" name="inlineRadioOptions" id="inlineRadio1" value="REGULAR">
                    <label class="form-check-label" for="inlineRadio1">Regular</label>
                    </div>
                    <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" v-model="ticket_type" name="inlineRadioOptions" id="inlineRadio2" value="FAN_PIT">
                    <label class="form-check-label" for="inlineRadio2">Fan pit</label>
                    </div>
                    <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" v-model="ticket_type" name="inlineRadioOptions" id="inlineRadio2" value="VIP">
                    <label class="form-check-label" for="inlineRadio2">Vip</label>
                    </div>
                    <small id="remaining_number_error" class="form-text">Cena Fan Pit karte je dva puta veca od regularne cene, a cena Vip karte je cetiri puta veca od regularne cene.</small>
                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-primary" v-on:click="countPrice">Izracunaj ukupnu cenu</button>
                </div>
                <div class="form-group">
                    <label class="form-label">Ukupna cena</label>
                    <input class="form-control total-price" type="text" placeholder="Readonly input here…" readonly>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Otkazite</button>
                <button type="button" class="btn btn-primary reserve-button" 
                    data-dismiss="modal" data-toggle="modal" data-target="#areYouSureModal">Izvrsite rezervaciju</button>
            </div>
            </div>
        </div>
    </div>
</template>