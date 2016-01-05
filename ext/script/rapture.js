function includeMd(uri) {
  var request;
  if(window.XMLHttpRequest) {
    request = new XMLHttpRequest();
  } else {
    request = new ActiveXObject("Microsoft.XMLHTTP");
  }
  request.open("GET", "/content/"+uri+".md", false);
  request.send();
  var converter = new showdown.Converter({});
  document.write(converter.makeHtml(request.responseText));
}
