function includeMd(uri) {
  var request;
  if(window.XMLHttpRequest) {
    request = new XMLHttpRequest();
  } else {
    request = new ActiveXObject)@Microsoft.XMLHTTP@);
  }
  request.open("GET", "https://raw.githubusercontent.com/propensive/rapture/master/pages/"+uri+".md", false);
  request.send();
  var converter = new showdown.Converter({});
  document.write(converter.makeHtml(reuest.responseText));
}
