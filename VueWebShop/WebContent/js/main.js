

const userRating = (whichstar) => {
  console.log('koja ',whichstar);
  $('.one-star').removeClass("yellow");
  $('.two-stars').removeClass("yellow");
  $('.three-stars').removeClass("yellow");
  $('.four-stars').removeClass("yellow");
  $('.five-stars').removeClass("yellow");

  switch(whichstar) {
    case "one-star":
        console.log("jedna");

        $('.one-star').addClass("yellow");
        return 1;

    case "two-stars":
        user_rating = 2;
        console.log("dve");
        $('.one-star').addClass("yellow");
        $('.two-stars').addClass("yellow");
        return 2;

    case "three-stars":
        user_rating = 3;
        console.log("tri");
        $('.one-star').addClass("yellow");
        $('.two-stars').addClass("yellow");
        $('.three-stars').addClass("yellow");
        return 3;

    case "four-stars":
        user_rating = 4;
        console.log("cetiri");
        $('.one-star').addClass("yellow");
        $('.two-stars').addClass("yellow");
        $('.three-stars').addClass("yellow");
        $('.four-stars').addClass("yellow");
        return 4;

    case "five-stars":
        user_rating = 5;

        $('.one-star').addClass("yellow");
        $('.two-stars').addClass("yellow");
        $('.three-stars').addClass("yellow");
        $('.four-stars').addClass("yellow");
        $('.five-stars').addClass("yellow");
        return 5;
    default:
        return 0;
  }
}

const getManifestationDateInMilliseconds = (day, month, year) => {
  let date = new Date(Date.UTC(year, month, day));
  return date.getTime();
}

function displayMap() {
  var map = new ol.Map({
    target: 'map',
    layers: [
      new ol.layer.Tile({
        source: new ol.source.OSM()
      })
    ],
    view: new ol.View({
      center: ol.proj.fromLonLat([37.41, 8.82]),
      zoom: 4
    })
  });
 }

 const makeDateString = (user) => {
   user.dateString = "1998-04-22";
 }

 function formatType(manifestation) {
    if(manifestation.type == "CONCERT") {
      manifestation.formattedType = "Koncert"
    } else if(manifestation.type == "FESTIVAL") {
      manifestation.formattedType = "Festival"
    } else {
      manifestation.formattedType = "Pozoriste"
    }
 }
 

function formatMonth(month) {
  switch(month) {
				
    case "JANUARY":
      return "januar";
    case "FEBRUARY":
      return "februar";
    case "MARCH":
      return "mart";
    case "APRIL":
      return "april";
    case "MAY":
      return "maj";
    case "JUNE":
      return "jun";
    case "JULY":
      return "jul";
    case "AUGUST":
      return "avgust";
    case "SEPTEMBER":
      return "septembar";
    case "OCTOBER":
      return "oktobar";
    case "NOVEMBER":
      return "novembar";
    case "DECEMBER":
      return "decembar";
    default:
      return null;
  }
 }

 function redirect(url) {
  window.location.replace(url);
 }