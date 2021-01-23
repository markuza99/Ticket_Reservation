
const makeErrorPage = () => {
  document.getElementById("wrapper").innerHTML = "<h1 class=\"text-center\" style=\"padding-top:5em;\">Error 404</h1>";
  document.getElementsByTagName("BODY")[0].style.backgroundColor = "gray";
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