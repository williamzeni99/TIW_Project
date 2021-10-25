{
    let personalMessage, topicContainer, pageeditor= new pageEditor();

    window.addEventListener("load", () => {
        if (sessionStorage.getItem("username") == null) {
            window.location.href = "LoginJS.html";
        } else {
            pageeditor.start(); // initialize the components
            pageeditor.refresh();
        } // display initial content
    }, false);

    function UserMessage(username, messageContainer){
        this.username=username;
        this.show=function (){
            messageContainer.textContent= this.username
        }
    } //todo edit later for Name Surname better View

    function topicShower(topiccontainerElement){
        this.topicContainer= topiccontainerElement;

        this.show=function (){
            var self= this;
            makeCall("GET", "../DownloadTopicsJS", null, function (req){
                if (req.readyState == 4 && req.status == 200){
                    var topicstoshow=JSON.parse(req.responseText);
                    if(topicstoshow.length === 0){
                        self.topicContainer.textContent="No Topics yet";
                        return;
                    }
                    for(var i=0; i<topicstoshow.length; i++){
                        printer(topicstoshow[i],self.topicContainer);
                    }
                }
            })

            var printer = function (obj, topicContainer) {
                var node = document.createTextNode(obj.id + ". " + obj.name);
                topicContainer.appendChild(node);
                topicContainer.appendChild(document.createElement("br"));
                for (var i = 0; i < obj.subtopics.length; i++) {
                    printer(obj.subtopics[i], topicContainer);
                }
            }
        }
    } //It works fine

    function addTopicForm(formId){

    }

    function pageEditor(){
        this.start=function (){
            let personalMessage = new UserMessage(sessionStorage.getItem("username"), document.getElementById("username"));
            personalMessage.show();

            let topicContainer= new topicShower(document.getElementById("topics"));
            topicContainer.show();

            let addForm=new addTopicForm(document.getElementById("formAdd"));
        }

        this.refresh=function (){

        }

    }

};