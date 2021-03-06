(function() { // avoid variables ending up in the global scope

    if (sessionStorage.getItem("username") != null) {
        window.location.href = "HomepageJS.html";
        return;
    }
    document.getElementById("loginButton").addEventListener('click', (e) => {
        e.preventDefault();
        var form = e.target.closest("form");
        if (form.checkValidity()) {
            sendFormData("POST", '../LoginHandlerJS', e.target.closest("form"),
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
