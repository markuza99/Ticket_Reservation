<template>
    <div class="container edit-manifestation-form" v-if="manifestation">
            <form @submit.prevent="save">
                <div class="form-row">
                    <div class="form-group col-md-6">
                    <label for="inputNumOfSeats">Broj mesta</label>
                    <input type="number" class="form-control" id="inputNumOfSeats" v-model="manifestation.numberOfSeats" readonly>
                    <small id="number-of-seats-error" v-if="number_of_seats_error" class="form-text data-error">Nevalidan unos preostalih mesta!</small>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="inputRemainingNumOfSeats">Preostali broj mesta</label>
                        <input type="number" class="form-control" id="inputRemainingNumOfSeats" v-model="manifestation.remainingNumberOfSeats" readonly>
                        <small id="remaining-number-error" v-if="remaining_number_error" class="form-text data-error">Nevalidan unos preostalih mesta!</small>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputName">Naziv manifestacije</label>
                    <input type="text" class="form-control" id="inputName" v-model="manifestation.name" readonly>
                    </div>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="type">Tip manifestacije</label>
                        <select v-model="manifestation.type" class="form-control" id="type" disabled>
                        
                        <option v-for="type in types" :key="type.id" >{{ type }}</option>
                        </select>
                    </div>
                    <div class="form-group col-md-6">
                    <label for="status">Status manifestacije</label>
                    <select v-model="manifestation.status" class="form-control" id="status" disabled>
                        
                        <option v-for="option in options" :key="option.id" >{{ option }}</option>

                    </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="maintainanceDate">Datum odrzavanja</label>
					<input type="datetime-local" readonly class="form-control" id="maintainanceDate" v-model="manifestation.dateString" min="1900-01-01" max="2021-12-31"/>
					<small id="data-error" v-if="date_error" class="form-text">Postoji vec manifestacija na istoj lokaciji u isto vreme</small>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-4">
                        <label for="inputPrice">Cena karte</label>
                        <input type="number" readonly class="form-control" id="inputPrice" v-model="manifestation.ticketPrice">
                        <small id="data-error" v-if="price_error" class="form-text">Nevalidan unos!</small>
                
                    </div>
                    <div class="form-group col-md-4">
                    <label for="inputState">Lokacija</label>
                    <select id="inputState" v-model="manifestation.location" class="form-control" disabled>
                        
                        <option v-for="location in locations" :key="location.id" >{{ location }}</option>
                    </select>
                    </div>
                    <div class="form-group col-md-4">
                    <label for="file">Poster manifestacije</label>
                    <input type="file" id="file" ref="file" v-on:change="handleFileUpload" class="form-control-file" disabled>
                    <button v-on:click="submitFile">Submit file</button>
                    </div>
                    <div id="preview">
                        <img v-if="url" :src="url" />
                    </div>
                </div>
                <div class="d-flex justify-content-between  mt-2">
                    <button type="button" class="btn btn-primary" v-on:click="edit">Izmeni</button>
                    <button type="button" id="cancel" class="btn btn-primary" v-on:click="cancel" disabled>Otkazi</button>
                    <button type="submit" id="save_changes" class="btn btn-primary" disabled>Sacuvaj izmene</button>
                </div>
                
            </form>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            manifestation : null,
            updatedManifestation : {},
            locations : ["001","002"],
            options : ["ACTIVE", "NONACTIVE"],
            types : ["CONCERT","FESTIVAL","THEATER"],
            url : null,
            date_error : false,
            remaining_number_error : false,
            number_of_seats_error : false,
            price_error : false,
            file : ""
        }
    },
    methods: {
        onFileChange(e) {
            const file = e.target.files[0];
            this.url = URL.createObjectURL(file);
        },
        cancel() {
            $('#save_changes').attr("disabled", true);
            $(":input").attr("readonly", true);
            $("#inputState").attr("disabled", true);
            $("#type").attr("disabled", true);
            $("#status").attr("disabled", true);
            $('#file').attr("disabled", true);
            // $('#cancel').attr("disabled", true);
            $('#inputRemainingNumOfSeats').removeClass("error");
            $('#inputNumOfSeats').removeClass("error");
            $('#inputPrice').removeClass("error");
            this.remaining_number_error = false;
            this.number_of_seats_error = false;
            this.price_error = false;
            this.date_error = false;
        },
        save() {
            this.remaining_number_error = false;
            this.number_of_seats_error = false;
            this.price_error = false;
            $('#inputRemainingNumOfSeats').removeClass("error");
            $('#inputNumOfSeats').removeClass("error");
            $('#inputPrice').removeClass("error");

            $('#save_changes').attr("disabled", true);
            $(":input").attr("readonly", true);
            $("#inputState").attr("disabled", true);
            $("#type").attr("disabled", true);
            $("#status").attr("disabled", true);
            $('#file').attr("disabled", true);
            $('#cancel').attr("disabled", true);

            let numberOfSeats = $('#inputNumOfSeats').val();
            let remainingNumberOfSeats = $('#inputRemainingNumOfSeats').val();
            let name = $('#inputName').val();
            let type = this.manifestation.type;
            let status = this.manifestation.status;
            let date = $('#maintainanceDate').val();
            let price = $('#inputPrice').val();
            let location = this.manifestation.location;
            // let image = $('#inputImage').val();

            console.log(numberOfSeats);
            console.log(remainingNumberOfSeats);
            console.log(price);

            if(!validateRange(parseInt(remainingNumberOfSeats), parseInt(numberOfSeats))) {
                this.remaining_number_error = true;
                $('#inputRemainingNumOfSeats').addClass("error");
                return;
            }

            if(!validateNumberRange(0,100000, parseInt(remainingNumberOfSeats))) {
                this.remaining_number_error = true;
                $('#inputRemainingNumOfSeats').addClass("error");
                return;
            }

            if(!validateNumberRange(0, 100000, parseInt(numberOfSeats))) {
                this.number_of_seats_error = true;
                $('#inputNumOfSeats').addClass("error");
                return;
            }

            if(!validateNumberRange(100, 100000, parseInt(price))) {
                this.price_error = true;
                $('#inputPrice').addClass("error");
                return;
            }
            
            dateparams = date.split("T");
            dateparams[1] = dateparams[1] + ":00";
            date = dateparams.join(" ");
            
            // if(this.url == null) {
            //     url = this.manifestation.image;
            // } else {
            //     console.log("eo me");
            //     url = this.url.toString();
            //     for(var i = 0; i < 5; i++) {
            //         url = url.substring(1);
            //     }
            // }
            updatedManifestation = {
                id : this.manifestation.id,
                name : name,
                type : type,
                numberOfSeats : parseInt(numberOfSeats),
                remainingNumberOfSeats : parseInt(remainingNumberOfSeats),
                date : date,
                ticketPrice : price,
                status : status,
                location : location,
                image : this.manifestation.image
            }
            
            console.log(updatedManifestation);
            
            axios
                .put("rest/manifestations/", JSON.stringify(updatedManifestation), {
					headers: {'content-type':'application/json'}
				})
                .then(response => {
                    if(response.data) {
                        console.log("bla");
                        window.location.replace("#/");
                    } else {
                        $('#maintainanceDate').addClass("error");
                        console.log("red");
                        this.date_error = true;
                    }
                });
        },
        edit() {
            $('#inputRemainingNumOfSeats').removeClass("error");
            $('#inputNumOfSeats').removeClass("error");
            $('#inputPrice').removeClass("error");
            this.remaining_number_error = false;
            this.number_of_seats_error = false;
            this.price_error = false;
            $(":input").attr("readonly", false);
            $("#inputState").attr("disabled", false);
            $("#type").attr("disabled", false);
            $("#status").attr("disabled", false);
            $('#file').attr("disabled", false);
            $('#save_changes').attr("disabled", false);
            $('#cancel').attr("disabled", false);
            console.log("edit");
        },
        makeDateValue(monthValue, dayOfMonth, hour, minute) {
            if(monthValue <= 9) {
                this.manifestation.monthValueString = "0" + this.manifestation.date.monthValue;
            } else {
                this.manifestation.monthValueString = this.manifestation.date.monthValue;
            }
            if(dayOfMonth <= 9) {
                this.manifestation.dayValueString = "0" + this.manifestation.date.dayOfMonth;
            } else {
                this.manifestation.dayValueString = this.manifestation.date.dayOfMonth;
            }
            if(hour <= 9) {
                this.manifestation.hourString = "0" + this.manifestation.date.hour;
            } else {
                this.manifestation.hourString = this.manifestation.date.hour;
            }
            if(minute <= 9) {
                this.manifestation.minuteString = "0" + this.manifestation.date.minute;
            } else {
                this.manifestation.minuteString = this.manifestation.date.minute;
            }
        },
        makeStringDate(date) {
            this.makeDateValue(this.manifestation.date.monthValue, this.manifestation.date.dayOfMonth,
                          this.manifestation.date.hour, this.manifestation.date.minute);
            this.manifestation.dateString = date.year + "-" 
            + this.manifestation.monthValueString + "-" + this.manifestation.dayValueString + "T"
            + this.manifestation.hourString + ":" + this.manifestation.minuteString;
            console.log(this.manifestation.dateString);
        },
        handleFileUpload() {
            this.file = this.$refs.file.files[0];
            console.log("fajl ",this.file);
        },
        submitFile() {
            let formData = new FormData();
            formData.append("file",this.file);
            console.log("form data: ", formData);
            axios
                .post("rest/manifestations/upload", formData, {
                    headers: {
                        "Content-Type":"multipart/form-data"
                    }
                })
                .then(response => {
                    console.log("cooool");
                })

        }
    },
    mounted() {
        let path = window.location.href;
		let pathparams = path.split("//")[1];
		let manifestationId = pathparams.split("/")[5];
        console.log(manifestationId);
		axios
			.get("rest/manifestations/" + manifestationId)
			.then(response => {
                this.manifestation = response.data;
                console.log(this.manifestation);
                // this.makeMonthValue(this.manifestation.date.monthValue);
				this.makeStringDate(this.manifestation.date);
            });
        
    }
}
</script>

<style scoped>

.edit-manifestation-form  {
    padding:4em;
}

#data-error {
    color:red;
}
</style>