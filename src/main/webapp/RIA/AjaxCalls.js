/**
 * AJAX call management
 */

function sendFormData(method, url, formElement, cback, reset = true) {
    var req = new XMLHttpRequest(); // visible by closure
    req.onreadystatechange = function() {
        cback(req)
    }; // closure
    req.open(method, url);
    if (formElement == null) {
        req.send();
    } else {
        const formData = new FormData(formElement);
        req.send(formData);
    }
    if (formElement !== null && reset === true) {
        formElement.reset();
    }
}

function sendJsonObject(method, url, obj, cback){
    var req = new XMLHttpRequest();   // new HttpRequest instance
    req.onreadystatechange=function (){
        cback(req);
    };
    req.open("POST", url);
    if(obj==null){
        req.send();
    }
    else{
        const json= JSON.stringify(obj);
        req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        req.send(json)
    }
}