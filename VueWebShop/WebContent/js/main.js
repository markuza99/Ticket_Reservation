

const userRating = (user_rating) => {
  $('.one-star').removeClass("checked");
      $('.two-stars').removeClass("checked");
      $('.three-stars').removeClass("checked");
      $('.four-stars').removeClass("checked");
      $('.five-stars').removeClass("checked");

      switch(whichstar) {
          case "one-star":
              console.log("jedna");
              user_rating = 1;

              $('.one-star').addClass("checked");
              break;
          case "two-stars":
              user_rating = 2;

              $('.one-star').addClass("checked");
              $('.two-stars').addClass("checked");
              break;
          case "three-stars":
              user_rating = 3;

              $('.one-star').addClass("checked");
              $('.two-stars').addClass("checked");
              $('.three-stars').addClass("checked");
              break;
          case "four-stars":
              user_rating = 4;

              $('.one-star').addClass("checked");
              $('.two-stars').addClass("checked");
              $('.three-stars').addClass("checked");
              $('.four-stars').addClass("checked");
              break;
          case "five-stars":
              user_rating = 5;

              $('.one-star').addClass("checked");
              $('.two-stars').addClass("checked");
              $('.three-stars').addClass("checked");
              $('.four-stars').addClass("checked");
              $('.five-stars').addClass("checked");
              break;
          default:
              user_rating = 0;
      }
  return user_rating;
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
 

 const makeDate = (manifestation) => {
  switch(manifestation.date.month) {
				
    case "JANUARY":
      manifestation.formattedMonth = "januar";
      break;
    case "FEBRUARY":
      manifestation.formattedMonth = "februar";
      break;
    case "MARCH":
      manifestation.formattedMonth = "mart";
      break;
    case "APRIL":
      manifestation.formattedMonth = "mart";
      break;
    case "MAY":
      manifestation.formattedMonth = "mart";
      break;
    case "JUNE":
      manifestation.formattedMonth = "mart";
      break;
    case "JULY":
      manifestation.formattedMonth = "mart";
      break;
    case "AUGUST":
      manifestation.formattedMonth = "mart";
      break;
    case "SEPTEMBER":
      manifestation.formattedMonth = "mart";
      break;
    case "OCTOBER":
      manifestation.formattedMonth = "mart";
      break;
    case "NOVEMBER":
      manifestation.formattedMonth = "mart";
      break;
    case "DECEMBER":
      manifestation.formattedMonth = "mart";
      break;
    default:
      console.log("nista");
  }
 }

 function redirect(url) {
  window.location.replace(url);
 }