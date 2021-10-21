{

    window.addEventListener("load", () => {
        if (sessionStorage.getItem("username") == null) {
            window.location.href = "LoginJS.html";
        } else {
            pageEditor().start(); // initialize the components
            pageEditor().refresh();
        } // display initial content
    }, false);

    function UserMessage(username, messageContainer){
        this.username=username;
        this.show=function (){
            messageContainer.textContent= this.username
        }
    } //todo edit later

    function topicShower(topiccontainerElement){
        this.topicContainer= topiccontainerElement;
        this.show=function (){
            var self= this;
            makeCall("GET", "DownloadTopicsJS", null, function (req){
                if (req.readyState == 4 && req.status == 200){
                    var topicstoshow=JSON.parse(req.responseText);
                    if(topicstoshow.lenght === 0){
                        self.topicContainer.textContent="No Topics yet";
                        return;
                    }
                    //todo wip rivedere
                }
                }
            )

        }

    }

    function pageEditor(){
        this.start=function (){
            let personalMessage = new UserMessage(sessionStorage.getItem("username"), document.getElementById("username"));
            personalMessage.show();

            let topicContainer= new topicShower(document.getElementById("topics"));
            topicContainer.show();
        }

        this.refresh=function (){

        }

    }
};