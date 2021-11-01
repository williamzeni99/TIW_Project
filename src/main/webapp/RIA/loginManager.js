(function() { // avoid variables ending up in the global scope

    document.getElementById("loginButton").addEventListener('click', (e) => {
        var form = e.target.closest("form");
        if (form.checkValidity()) {
            makeCall("POST", '../LoginHandlerJS', e.target.closest("form"),
                function(x) {
                    if (x.readyState == XMLHttpRequest.DONE) {
                        var message = x.responseText;
                        switch (x.status) {
                            case 200:
                                sessionStorage.setItem('username', message);
                                window.location.href = "HomepageJS.html";
                                break;
                            case 400: // bad request
                                document.getElementById("errorMsg").textContent = message;
                                break;
                            case 401: // unauthorized
                                document.getElementById("errorMsg").textContent = message;
                                break;
                            case 404: //not found
                                document.getElementById("errorMsg").textContent = message;
                                break;
                            case 500: // server error
                                document.getElementById("errorMsg").textContent = message;
                                break;
                            default: break;
                        }
                    }
                },
                false,
            );
        } else {
            form.reportValidity();
        }
    });

})();
